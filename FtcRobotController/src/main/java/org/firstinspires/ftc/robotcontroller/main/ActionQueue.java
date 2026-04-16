package org.firstinspires.ftc.robotcontroller.main;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

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
        queue.add(DeclareAction(2, ActionType.MOVE_LATERAL, 0.5));
        queue.add(DeclareAction(5, ActionType.MOVE_STRAIGHT, 0.5));
        queue.add(DeclareAction(-2, ActionType.MOVE_LATERAL, 0.5));
        queue.add(DeclareAction(-5, ActionType.MOVE_STRAIGHT, 0.5));
        //Don't add actions beyond here
        currentAction = queue.get(0);

        movement.init(hardwareMap);
        movement.Reset();
    }

    @Override
    public void loop() {
        //data
        telemetry.addData("Runtime", getRuntime());
        telemetry.addData("Motors", movement.GetMotors());
        telemetry.addData("Dead wheels", movement.HasDeadWheels());
        telemetry.addData("Queue Remaining", queue.toArray().length-queuePos+1);
        //start action
        if (!currentAction.started) {
            telemetry.addLine("Started action " + currentAction.action.toString());
            telemetry.addData("Dist", currentAction.distance);
            telemetry.addData("Speed", currentAction.speed);
            currentAction.started = true;
            if (currentAction.action==ActionType.MOVE_STRAIGHT) {
                movement.RawMove(currentAction.speed, 0, 0);
            }
            else if (currentAction.action==ActionType.MOVE_LATERAL) {
                movement.RawMove(0,  currentAction.speed, 0);
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
            currentAction.completed=false;
        }
        //mid action
        else {
            telemetry.addData("Performing action", currentAction.action.toString());
            if (currentAction.action==ActionType.MOVE_STRAIGHT) {
                movement.Update();
                telemetry.addData("pos", movement.GetY());
                if (movement.GetY()>=Math.abs(currentAction.distance)) {
                    currentAction.Complete();
                    movement.StopMove();
                    movement.Reset();
                }
            }
            else if (currentAction.action==ActionType.MOVE_LATERAL) {
                movement.Update();
                telemetry.addData("pos", movement.GetX());
                if (movement.GetX()>=Math.abs(currentAction.distance)) {
                    currentAction.Complete();
                    movement.StopMove();
                    movement.Reset();
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
