package com.pieisnotpi.engine.rendering.shaders;

import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.Window;

import java.util.Scanner;

import static org.lwjgl.opengl.GL20.*;

public class ShaderFile
{
    public int shaderID;
    public String path;

    public ShaderFile(String path, int shaderType)
    {
        this.path = path.replaceAll("\\\\", "/");

        Scanner scanner = new Scanner(Window.class.getResourceAsStream(path));

        String code = "";

        while(scanner.hasNextLine()) code += scanner.nextLine() + "\n";
        scanner.close();

        shaderID = glCreateShader(shaderType);

        glShaderSource(shaderID, code);
        glCompileShader(shaderID);

        int status = glGetShaderi(shaderID, GL_COMPILE_STATUS);

        if(status == 0)
        {
            String log = glGetShaderInfoLog(shaderID).replaceAll("\n", "\n\t");
            Logger.SHADER_COMPILER.err("Failed shader '" + path.substring(path.lastIndexOf('/') + 1) + "'\n\t" + log);
        }
        else Logger.SHADER_COMPILER.debug("Compiled shader '" + path.substring(path.lastIndexOf('/') + 1) + '\'');
    }

    public void attach(int program)
    {
        glAttachShader(program, shaderID);
    }

    public String toString()
    {
        return path;
    }

    public void finalize() throws Throwable
    {
        super.finalize();

        glDeleteShader(shaderID);
    }
}
