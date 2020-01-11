package com.pieisnotpi.engine.rendering.window;

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
        CENTER_CURSOR   = GLFW_CENTER_CURSOR,
        //TRANSPARENT     = GLFW_TRANSPARENT,
        SAMPLES         = GLFW_SAMPLES,
        REFRESH_RATE    = GLFW_REFRESH_RATE;

    default void init()
    {
        defaultHints();
        //hint(TRANSPARENT, TRUE);
        hint(VISIBLE, FALSE);
        hint(RESIZABLE, TRUE);
        hint(AUTO_ICONIFY, FALSE);
        hint(SAMPLES, 8);
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
