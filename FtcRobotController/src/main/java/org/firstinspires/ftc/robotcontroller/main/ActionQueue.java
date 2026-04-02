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
        movement.Init(hardwareMap);
        //Add actions here!
        queue.add(DeclareAction(2, ActionType.MOVE_STRAIGHT, 0.3));
        //Don't add actions beyond here
        currentAction = queue.get(0);
    }

    @Override
    public void loop() {
        if (!currentAction.started) {
            telemetry.addLine("Started action " + currentAction.action.toString());
            telemetry.addData("Dist", currentAction.distance);
            telemetry.addData("Speed", currentAction.speed);
            telemetry.update();
            currentAction.started = true;
            if (currentAction.action==ActionType.MOVE_STRAIGHT) {
                movement.EncoderMove(1, 0, 0, currentAction.distance, currentAction.speed);
            }
            else if (currentAction.action==ActionType.MOVE_LATERAL) {
                movement.EncoderMove(0, 1, 0, currentAction.distance, currentAction.speed);
            }
        }
        else if (currentAction.completed) {
            telemetry.addLine("Completed action");
            telemetry.update();
            queuePos++;
            if (queuePos==queue.toArray().length) {
                terminateOpModeNow();
                return;
            }
            currentAction = queue.get(queuePos);
        }
        else {
            if (currentAction.action==ActionType.MOVE_STRAIGHT) {
                if (movement.HighestEncoder()>= currentAction.distance) {
                    currentAction.Complete();
                }
            }
        }
    }

    public Action DeclareAction(int distance, ActionType action, double speed) {
        Action newAction = new Action();
        newAction.distance = distance;
        newAction.action = action;
        newAction.speed = speed;
        return newAction;
    }
}
