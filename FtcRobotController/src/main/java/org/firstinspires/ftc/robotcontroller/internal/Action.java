package org.firstinspires.ftc.robotcontroller.internal;

public class Action {
    public ActionType action;
    public double distance;
    public double speed;
    public boolean started = false;
    public boolean completed = false;

    public void Complete() {
        completed = true;
        //other stuff will be here later... maybe
    }

    public void Reset() {
        completed = false;
        started = false;
    }
}
