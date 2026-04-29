package org.firstinspires.ftc.robotcontroller.main;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.List;
import java.util.ArrayList;

@TeleOp
public class ActionQueue extends OpMode {
    MotorMovement movement = new MotorMovement();
    List<Action> queue = new ArrayList<>();
    int queuePos;
    Action currentAction;
    DistanceUnit distanceUnit = DistanceUnit.CM;

    @Override
    public void init() {
        //Add actions here!
        queue.add(DeclareAction(100, ActionType.ROTATE, 0.2));
        //Don't add actions beyond here
        currentAction = queue.get(0);
        queuePos = 0;

        movement.start(hardwareMap);
        movement.Reset();
    }

    @Override
    public void loop() {
        //data
        telemetry.addLine("Testing");
        telemetry.addData("Runtime", getRuntime());
        telemetry.addData("Motors", movement.GetMotors());
        telemetry.addData("Dead wheels", movement.HasDeadWheels());
        telemetry.addData("Queue Pos", queuePos);
        telemetry.addData("Queue Length", queue.toArray().length);
        //action
        if (!currentAction.started) {
            telemetry.addLine("Started action " + currentAction.action.toString());
            telemetry.addData("Dist", currentAction.distance);
            telemetry.addData("Speed", currentAction.speed);
            currentAction.started = true;
            movement.Reset();
            if (currentAction.action==ActionType.MOVE_STRAIGHT) {
                movement.RawMove(currentAction.speed*Math.signum(currentAction.distance), 0, 0);
            }
            else if (currentAction.action==ActionType.MOVE_LATERAL) {
                movement.RawMove(0,  currentAction.speed*Math.signum(currentAction.distance), 0);
            }
            else if (currentAction.action==ActionType.ROTATE) {
                movement.RawMove(0,  0, currentAction.speed*Math.signum(currentAction.distance));
            }
        }
        else if (currentAction.completed) {
            telemetry.addLine("Completed action " + currentAction.action.toString());
            queuePos+=1;
            if (queuePos>=queue.toArray().length) {
                terminateOpModeNow();
                return;
            }
            currentAction = queue.get(queuePos);
            currentAction.started=false;
            currentAction.completed=false;
        }
        else {
            telemetry.addData("Performing action", currentAction.action.toString());
            if (currentAction.action==ActionType.MOVE_STRAIGHT) {
                movement.Update();
                telemetry.addData("pos", movement.pose2D.getX(distanceUnit));
                if (Math.abs( movement.pose2D.getX(distanceUnit))>=Math.abs(currentAction.distance)) {
                    currentAction.Complete();
                    movement.StopMove();
                    movement.Reset();
                }
            }
            else if (currentAction.action==ActionType.MOVE_LATERAL) {
                movement.Update();
                telemetry.addData("pos", movement.pose2D.getY(distanceUnit));
                if (Math.abs(movement.pose2D.getY(distanceUnit))>=Math.abs(currentAction.distance)) {
                    currentAction.Complete();
                    movement.StopMove();
                    movement.Reset();
                }
            }
            else if (currentAction.action==ActionType.ROTATE) {
                movement.Update();
                telemetry.addData("posX", movement.pose2D.getX(distanceUnit));
                telemetry.addData("posY", movement.pose2D.getY(distanceUnit));
            }
            else if (currentAction.action==ActionType.WAIT) {
                movement.Update();
                telemetry.addData("posX", movement.pose2D.getX(distanceUnit));
                telemetry.addData("posY", movement.pose2D.getY(distanceUnit));
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
