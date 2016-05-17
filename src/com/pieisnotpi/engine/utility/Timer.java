package com.pieisnotpi.engine.utility;

public class Timer
{
    private int time;
    private long startTime;
    private boolean started = false, shouldStop = true, shouldForceFinish = false;

    /**
     * A blank timer intended to be used for dynamic time amounts
     * @param shouldStop Determines if the timer should stop when it finishes
     */

    public Timer(boolean shouldStop) { this.shouldStop = shouldStop; }

    /**
     * A timer with a predetermined time, intended to be used in case time will not change
     * @param shouldStop Determines if the timer should stop when it finishes
     * @param timeBeforeFinish Amount of time, in milliseconds, before timer will complete
     */

    public Timer(boolean shouldStop, int timeBeforeFinish) { time = timeBeforeFinish; this.shouldStop = shouldStop; }

    /**
     * Starts the timer without assigning a new time, will throw exception if time is invalid
     */

    public void start()
    {
        assert time > 0;

        if(started) return;

        started = true;
        startTime = System.currentTimeMillis();
    }

    /**
     * Starts timer with a new time, will throw exception if time is invalid
     * @param timeBeforeFinish Amount of time, in milliseconds, before timer will complete
     */

    public void start(int timeBeforeFinish)
    {
        assert timeBeforeFinish > 0;

        if(started) return;

        started = true;
        time = timeBeforeFinish;
        startTime = System.currentTimeMillis();
    }

    /**
     * Used to stop/reset the timer
     */

    public void stop()
    {
        if(!started) return;

        started = false;
        startTime = 0;
    }

    /**
     * @return Returns true if timer has exceeded specified time
     */

    public boolean isFinished()
    {
        if(shouldForceFinish)
        {
            shouldForceFinish = false;
            return true;
        }

        if(!started) return false;

        if(System.currentTimeMillis() - startTime > time)
        {
            stop();
            if(!shouldStop) start();
            return true;
        }

        return false;
    }

    /**
     * @return The specified time interval for the timer
     */

    public int getTime()
    {
        return time;
    }

    /**
     * @return Whether or not the timer has been started
     */

    public boolean isStarted()
    {
        return started;
    }

    /**
     * Force the timer to return true on the next isFinished() call
     */

    public void forceFinish()
    {
        shouldForceFinish = true;
    }
}
