package com.pieisnotpi.engine.rendering.shaders;

import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.utility.FileUtility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static com.pieisnotpi.engine.PiEngine.glInstance;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_CONTROL_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_EVALUATION_SHADER;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;

public class ShaderFile
{
    public static String defaultPath = "/assets/shaders/";
    public static final int

            TYPE_VERT           = GL_VERTEX_SHADER,             // OpenGL 2.0
            TYPE_FRAG           = GL_FRAGMENT_SHADER,           // OpenGL 2.0
            TYPE_GEOMETRY       = GL_GEOMETRY_SHADER,           // OpenGL 3.2
            TYPE_TESS_CONTROL   = GL_TESS_CONTROL_SHADER,       // OpenGL 4.0
            TYPE_TESS_EVAL      = GL_TESS_EVALUATION_SHADER,    // OpenGL 4.0
            TYPE_COMPUTE        = GL_COMPUTE_SHADER;            // OpenGL 4.3

    public int handle;
    private String name;
    private ShaderProgram program;

    private boolean initialized = false;

    private ShaderFile(String path, int shaderType)
    {
        handle = glCreateShader(shaderType);

        String name = path.replaceAll("\\\\", "/");
        if(name.startsWith(defaultPath)) name = name.substring(defaultPath.length());

        try { setFile(FileUtility.findStream(path), name); }
        catch(IOException e)
        {
            Logger.OPENGL.err("Shader file error\n\t");
            e.printStackTrace();
        }
    }

    public void attach(ShaderProgram program)
    {
        this.program = program;
        glAttachShader(program.handle, handle);
    }

    public void setFile(File file, String name) throws IOException
    {
        if(file == null)
        {
            Logger.OPENGL.err("Shader file not found: " + name);
            initialized = false;
            return;
        }

        Scanner scanner = new Scanner(file);
        String code = scanner.useDelimiter("\\A").next();
        scanner.close();

        String old = glGetShaderSource(handle);
        boolean compiled = compile(code);

        if(!compiled)
        {
            String log = glGetShaderInfoLog(handle).replaceAll("\n", "\n\t");
            Logger.OPENGL.err("Failed shader '" + name + "'\n\t" + log);
            initialized = false;

            if(old.length() > 0 && compile(old)) Logger.OPENGL.debug("Successfully reverted shader '" + name + "'");
            else System.exit(-1);
        }
        else
        {
            Logger.OPENGL.debug("Compiled shader '" + name + '\'');
            initialized = true;
        }

        this.name = name;
    }
    
    public void setFile(InputStream file, String name) throws IOException
    {
        if(file == null)
        {
            Logger.OPENGL.err("Shader file not found: " + name);
            initialized = false;
            return;
        }
        
        Scanner scanner = new Scanner(file);
        String code = scanner.useDelimiter("\\A").next();
        scanner.close();
        
        String old = glGetShaderSource(handle);
        boolean compiled = compile(code);
        
        if(!compiled)
        {
            String log = glGetShaderInfoLog(handle).replaceAll("\n", "\n\t");
            Logger.OPENGL.err("Failed shader '" + name + "'\n\t" + log);
            initialized = false;
            
            if(old.length() > 0 && compile(old)) Logger.OPENGL.debug("Successfully reverted shader '" + name + "'");
            else System.exit(-1);
        }
        else
        {
            Logger.OPENGL.debug("Compiled shader '" + name + '\'');
            initialized = true;
        }
        
        this.name = name;
    }

    public void reload(File file)
    {
        try
        {
            setFile(file, name);
            if(program != null) program.reload();
        }
        catch(IOException e) {}
    }

    public boolean isInitialized()
    {
        return initialized;
    }

    @Override
    public String toString()
    {
        return name;
    }

    private boolean compile(String code)
    {
        glShaderSource(handle, code);
        glCompileShader(handle);

        int status = glGetShaderi(handle, GL_COMPILE_STATUS);

        return status != 0;
    }

    public static ShaderFile getShaderFile(String name, int type)
    {
        String path;
        if(name.charAt(0) != '\n') path = defaultPath + name;
        else path = name = name.substring(1);

        ShaderFile f = glInstance.getShaderFile(path);

        if(f == null) f = new ShaderFile(path, type);
        if(f.isInitialized()) glInstance.registerShaderFile(name, f);
        return f;
    }
}
