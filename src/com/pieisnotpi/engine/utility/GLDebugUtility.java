package com.pieisnotpi.engine.utility;

import com.pieisnotpi.engine.output.Logger;
import org.lwjgl.opengl.GLDebugMessageCallbackI;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL43.*;

public class GLDebugUtility
{
    public static GLDebugMessageCallbackI createCallback()
    {
        return (source, type, id, severity, length, message, userParam) ->
                Logger.OPENGL.log(String.format("OpenGL Debug Output\n\tSource: %s\n\tType: %s\n\tID: %d\n\tSeverity: %s\n\tMessage: %s",
                    getDebugSource(source),
                    getDebugType(type),
                    id,
                    getDebugSeverity(severity),
                    getMessage(message)));
    }

    public static String getDebugSource(int source)
    {
        switch(source)
        {
            case GL_DEBUG_SOURCE_API: return "API";
            case GL_DEBUG_SOURCE_APPLICATION: return "Application";
            case GL_DEBUG_SOURCE_OTHER: return "Other";
            case GL_DEBUG_SOURCE_SHADER_COMPILER: return "Shader Compiler";
            case GL_DEBUG_SOURCE_THIRD_PARTY: return "Third Party";
            case GL_DEBUG_SOURCE_WINDOW_SYSTEM: return "Window System";
        }

        return "Unknown";
    }

    public static String getDebugType(int type)
    {
        switch(type)
        {
            case GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR: return "Deprecated Behavior";
            case GL_DEBUG_TYPE_ERROR: return "Error";
            case GL_DEBUG_TYPE_MARKER: return "Marker";
            case GL_DEBUG_TYPE_OTHER: return "Other";
            case GL_DEBUG_TYPE_PERFORMANCE: return "Performance";
            case GL_DEBUG_TYPE_POP_GROUP: return "Pop Group";
            case GL_DEBUG_TYPE_PORTABILITY: return "Portability";
            case GL_DEBUG_TYPE_PUSH_GROUP: return "Push Group";
            case GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR: return "Undefined";
        }

        return "Unknown";
    }

    public static String getDebugSeverity(int severity)
    {
        switch(severity)
        {
            case GL_DEBUG_SEVERITY_HIGH: return "High";
            case GL_DEBUG_SEVERITY_MEDIUM: return "Medium";
            case GL_DEBUG_SEVERITY_LOW: return "Low";
            case GL_DEBUG_SEVERITY_NOTIFICATION: return "Notification";
        }

        return "Unknown";
    }

    public static String getMessage(long message)
    {
        return MemoryUtil.memUTF8(message);
    }
}
