package com.pieisnotpi.engine.updates;

public interface GameUpdateAction
{
    void runAction(float timeStep) throws Exception;
}
