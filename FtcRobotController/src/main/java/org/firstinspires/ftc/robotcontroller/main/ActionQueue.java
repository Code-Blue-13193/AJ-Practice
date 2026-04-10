package org.firstinspires.ftc.robotcontroller.main;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;
import java.util.ArrayList;

@TeleOp
public class ActionQueue extends OpMode {
    MotorMovement movement = new MotorMovement();
    List<Action> queue = new ArrayList<>();
    int queuePos = 0;
    Action currentAction;

    @Override
    public void init() {
        //Add actions here!
        queue.add(DeclareAction(10, ActionType.MOVE_LATERAL, 0.02));
        //Don't add actions beyond here
        currentAction = queue.get(0);
    }

    @Override
    public void loop() {
        //data
        telemetry.addData("Runtime", getRuntime());
        telemetry.addData("Dead wheels", movement.HasDeadwheels());
        //start action
        if (!currentAction.started) {
            telemetry.addLine("Started action " + currentAction.action.toString());
            telemetry.addData("Dist", currentAction.distance);
            telemetry.addData("Speed", currentAction.speed);
            currentAction.started = true;
            if (currentAction.action==ActionType.MOVE_STRAIGHT) {
                movement.RawMove(currentAction.distance, 0, 0);
            }
            else if (currentAction.action==ActionType.MOVE_LATERAL) {
                movement.RawMove(0, 0, currentAction.speed);
            }
        }
        //complete action
        else if (currentAction.completed) {
            telemetry.addLine("Completed action " + currentAction.action.toString());
            queuePos++;
            if (queuePos==queue.toArray().length) {
                terminateOpModeNow();
                return;
            }
            currentAction = queue.get(queuePos);
        }
        //mid action
        else {
            if (currentAction.action==ActionType.MOVE_STRAIGHT) {
                telemetry.addData("Performing action", currentAction.action.toString());
                if (!movement.AllMotorsBusy()) {
                    telemetry.addLine("Attempting to complete action " + currentAction.action.toString());
                    currentAction.Complete();
                    movement.StopMove();
                }
            }
        }
        telemetry.update();
    }

    public Action DeclareAction(int distance, ActionType action, double speed) {
        Action newAction = new Action();
        newAction.distance = distance;
        newAction.action = action;
        newAction.speed = speed;
        return newAction;
    }
}
