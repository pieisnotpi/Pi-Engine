package com.pieisnotpi.engine.rendering.shaders;

import com.pieisnotpi.engine.output.Logger;
import com.pieisnotpi.engine.rendering.Window;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;
import java.util.Scanner;

import static org.lwjgl.opengl.GL20.*;

public class ShaderFile
{
    public int shaderID;
    public String path;

    public ShaderFile(String path, int shaderType)
    {
        this.path = path;

        Scanner scanner = new Scanner(Window.class.getResourceAsStream(path));

        String code = "";

        while(scanner.hasNextLine()) code += scanner.nextLine() + "\n";
        scanner.close();

        shaderID = glCreateShader(shaderType);

        glShaderSource(shaderID, code);
        glCompileShader(shaderID);

        IntBuffer status = BufferUtils.createIntBuffer(1);

        glGetShaderiv(shaderID, GL_COMPILE_STATUS, status);

        if(status.get() == 0) Logger.SHADER_COMPILER.debugErr("Failed shader\t'" + path.substring(path.lastIndexOf('/') + 1) + '\'');
        else Logger.SHADER_COMPILER.debug("Compiled shader\t'" + path.substring(path.lastIndexOf('/') + 1) + '\'');
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
