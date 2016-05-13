package com.pieisnotpi.engine.input.joysticks;

/**
 * Controller axis/button maps for the Xbox 360/One controllers.
 * Unfortunately, the Xbox button's inputs cannot be retrieved, meaning it cannot be used within this engine.
 */

public class Xbox
{
    public static final int

            AXIS_LSTICK_X = 0,
            AXIS_LSTICK_Y = 1,
            AXIS_RSTICK_X = 2,
            AXIS_RSTICK_Y = 3,
            AXIS_LTRIGGER = 4,
            AXIS_RTRIGGER = 5;

    public static final int

            BUTTON_A = 0,
            BUTTON_B = 1,
            BUTTON_X = 2,
            BUTTON_Y = 3,
            BUTTON_LBUMPER = 4,
            BUTTON_RBUMPER = 5,
            BUTTON_BACK = 6,
            BUTTON_START = 7,
            BUTTON_LSTICK = 8,
            BUTTON_RSTICK = 9,
            BUTTON_DPAD_UP = 10,
            BUTTON_DPAD_RIGHT = 11,
            BUTTON_DPAD_DOWN = 12,
            BUTTON_DPAD_LEFT = 13;
}
