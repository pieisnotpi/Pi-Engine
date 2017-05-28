package com.pieisnotpi.engine.updates;

import com.pieisnotpi.engine.output.Logger;

public class GameUpdate
{
    /**
     * @value updates: The amount of updates this GameUpdate has done
     * @value period: The time between each update of this GameUpdate, in milliseconds
     * @value frequency: The amount of times this GameUpdate should update per second
     * @value abbreviation: The abbreviation of this GameUpdate's name, used for debug
     * @value action: The action this GameUpdate runs
     */

    public String name = "GAME";
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

    public GameUpdate setName(String name)
    {
        this.name = name;
        return this;
    }

    public GameUpdate setFrequency(int frequency)
    {
        this.frequency = frequency;
        period = 1000/frequency;

        return this;
    }

    public void update(long time) throws Exception
    {
        if(!shouldUpdate(time)) return;

        updateAction.runAction((time - lastUpdateTime)/1000f);
        lastUpdateTime = time;
        totalTimeTaken += lastTimeTaken = System.currentTimeMillis() - time;
        if(lastTimeTaken > frequency) Logger.SYSTEM.debugErr(String.format("Game update '%s' took %dms to run", name, lastTimeTaken));
        updates++;
    }

    public void runPerSecondAction() throws Exception
    {
        if(perSecondAction != null) perSecondAction.runAction(1);
        totalTimeTaken = 0;
    }

    public boolean shouldUpdate(long time)
    {
        return updates < frequency && lastUpdateTime + period <= time;
    }
}
