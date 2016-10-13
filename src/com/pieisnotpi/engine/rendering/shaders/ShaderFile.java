package com.pieisnotpi.engine.rendering.shaders;

import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.window.Window;

import java.util.Scanner;

import static org.lwjgl.opengl.GL20.*;

public class ShaderFile
{
    public int handle;

    public ShaderFile(String path, int shaderType)
    {
        String name = path.replaceAll("\\\\", "/").substring(path.lastIndexOf('/') + 1);

        Scanner scanner = new Scanner(Window.class.getResourceAsStream(path));
        String code = scanner.useDelimiter("\\A").next();
        scanner.close();

        handle = glCreateShader(shaderType);

        glShaderSource(handle, code);
        glCompileShader(handle);

        int status = glGetShaderi(handle, GL_COMPILE_STATUS);

        if(status == 0)
        {
            String log = glGetShaderInfoLog(handle).replaceAll("\n", "\n\t");
            Logger.SHADER_COMPILER.err("Failed shader '" + name + "'\n\t" + log);
        }
        else Logger.SHADER_COMPILER.debug("Compiled shader '" + name + '\'');
    }

    public void attach(int program)
    {
        glAttachShader(program, handle);
    }

    public void finalize() throws Throwable
    {
        super.finalize();

        glDeleteShader(handle);
    }
}
