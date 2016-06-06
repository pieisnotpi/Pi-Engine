package com.pieisnotpi.engine.input.joysticks;

/**
 * Controller axis/button maps for the Dualshock 4 controller.
 * Unfortunately, touchpad input cannot be retrieved, aside from the button functionality of the touchpad.
 */

public final class DS4
{
    public static final int

            AXIS_LSTICK_X = 0,
            AXIS_LSTICK_Y = 1,
            AXIS_RSTICK_X = 2,
            AXIS_L2 = 3,
            AXIS_R2 = 4,
            AXIS_RSTICK_Y = 5;

    public static final int

            BUTTON_SQUARE = 0,
            BUTTON_X = 1,
            BUTTON_CIRCLE = 2,
            BUTTON_TRIANGLE = 3,
            BUTTON_L1 = 4,
            BUTTON_R1 = 5,
            BUTTON_LTRIGGER = 6,
            BUTTON_RTRIGGER = 7,
            BUTTON_SHARE = 8,
            BUTTON_OPTIONS = 9,
            BUTTON_L2 = 10,
            BUTTON_R2 = 11,
            BUTTON_PS = 12,
            BUTTON_TOUCHPAD = 13,
            BUTTON_DPAD_RIGHT = 14,
            BUTTON_DPAD_UP = 15,
            BUTTON_DPAD_DOWN = 16,
            BUTTON_DPAD_LEFT = 17;
}
