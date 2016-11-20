package com.pieisnotpi.engine.rendering.window;

import com.pieisnotpi.engine.PiEngine;

import static org.lwjgl.glfw.GLFW.*;

public interface HintInitializer
{
    int TRUE    = GLFW_TRUE,
        FALSE   = GLFW_FALSE;

    int FOCUSED         = GLFW_FOCUSED,
        RESIZABLE       = GLFW_RESIZABLE,
        VISIBLE         = GLFW_VISIBLE,
        DECORATED       = GLFW_DECORATED,
        AUTO_ICONIFY    = GLFW_AUTO_ICONIFY,
        FLOATING        = GLFW_FLOATING,
        MAXIMIZED       = GLFW_MAXIMIZED,
        SAMPLES         = GLFW_SAMPLES,
        REFRESH_RATE    = GLFW_REFRESH_RATE;

    default void init()
    {
        defaultHints();
        hint(VISIBLE, FALSE);
        hint(RESIZABLE, TRUE);
        hint(AUTO_ICONIFY, FALSE);
        hint(SAMPLES, 8);
        hint(GLFW_CONTEXT_VERSION_MAJOR, PiEngine.glMajor);
        hint(GLFW_CONTEXT_VERSION_MINOR, PiEngine.glMinor);
        hint(GLFW_OPENGL_FORWARD_COMPAT, TRUE);
        hint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    }

    default void hint(int hint, int value)
    {
        glfwWindowHint(hint, value);
    }

    default void defaultHints()
    {
        glfwDefaultWindowHints();
    }
}
