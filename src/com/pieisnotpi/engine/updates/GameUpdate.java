package com.pieisnotpi.engine.updates;

public class GameUpdate
{
    /**
     * @value updates: The amount of updates this GameUpdate has done
     * @value period: The time between each update of this GameUpdate, in milliseconds
     * @value frequency: The amount of times this GameUpdate should update per second
     * @value abbreviation: The abbreviation of this GameUpdate's name, used for debug
     * @value action: The action this GameUpdate runs
     */
    public long lastUpdateTime = 0, lastTimeTaken = 0, totalTimeTaken = 0;
    public int updates, period, frequency;
    private GameUpdateAction updateAction;
    private GameUpdateAction perSecondAction;

    public GameUpdate(int frequency, GameUpdateAction action)
    {
        this.frequency = frequency;
        this.updateAction = action;
        this.perSecondAction = null;

        updates = 0;
        period = 1000/frequency;
    }

    public GameUpdate(int frequency, GameUpdateAction action, GameUpdateAction perSecond)
    {
        this.frequency = frequency;
        this.updateAction = action;
        this.perSecondAction = perSecond;

        updates = 0;
        period = 1000/frequency;
    }

    public void setFrequency(int frequency)
    {
        this.frequency = frequency;
        period = 1000/frequency;
    }

    public void update(long time)
    {
        if(!shouldUpdate(time)) return;

        lastUpdateTime = time;
        updateAction.runAction();
        totalTimeTaken += lastTimeTaken = System.currentTimeMillis() - time;
        updates++;
    }

    public void runPerSecondAction()
    {
        if(perSecondAction != null) perSecondAction.runAction();
        totalTimeTaken = 0;
    }

    public boolean shouldUpdate(long time)
    {
        return updates < frequency && lastUpdateTime + period <= time;
    }
}
