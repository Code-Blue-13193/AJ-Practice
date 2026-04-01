package org.firstinspires.ftc.robotcontroller.main;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.internal.ActionType;
import org.firstinspires.ftc.robotcontroller.internal.Action;

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
            telemetry.addLine("Started action");
            telemetry.update();
            currentAction.started = true;
            if (currentAction.action==ActionType.MOVE_STRAIGHT) {
                movement.RawMove(currentAction.speed, 0, 0);
            }
            else if (currentAction.action==ActionType.MOVE_LATERAL) {
                movement.RawMove(0, currentAction.speed, 0);
            }
        }
        else if (currentAction.completed) {
            telemetry.addLine("Completed action");
            telemetry.update();
            queuePos++;
        }
        else {
            if (currentAction.action==ActionType.MOVE_STRAIGHT) {
                if (movement.HighestEncoder()>= currentAction.distance) {
                    currentAction.Complete();
                }
            }
        }
    }

    public Action DeclareAction(double distance, ActionType action, double speed) {
        Action newAction = new Action();
        newAction.distance = distance;
        newAction.action = action;
        newAction.speed = speed;
        return newAction;
    }
}
