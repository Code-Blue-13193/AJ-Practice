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

    double deltaTime;
    double prevRuntime = 0;

    @Override
    public void init() {
        //Add actions here!
        queue.add(DeclareAction(90, ActionType.ROTATE, 0.3));
        //Don't add actions beyond here
        currentAction = queue.get(0);
        queuePos = 0;

        movement.start(hardwareMap);
        movement.Reset();
    }

    @Override
    public void loop() {

        deltaTime = getRuntime() - prevRuntime;

        //data
        telemetry.addData("Runtime", getRuntime());
        telemetry.addData("Delta time", deltaTime);
        telemetry.addLine("Queue " + queuePos + "/" + queue.toArray().length);

        movement.Update();

        //action
        if (!currentAction.started) {
            telemetry.addLine("Starting action " + currentAction.action.toString());
            telemetry.addLine("Time until start: " + currentAction.startCount);
            telemetry.addData("Dist", currentAction.distance);
            telemetry.addData("Speed", currentAction.speed);
            movement.Reset();
            if (currentAction.startCount>0) {
                currentAction.startCount -= deltaTime;
            }
            else {
                currentAction.started = true;
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
                if (Math.abs( movement.pose2D.getX(distanceUnit))>=Math.abs(currentAction.distance)) {
                    currentAction.Complete();
                    movement.StopMove();
                    movement.Reset();
                }
            }
            else if (currentAction.action==ActionType.MOVE_LATERAL) {
                movement.CorrectDriftY(currentAction.distance);
                if (Math.abs(movement.pose2D.getY(distanceUnit))>=Math.abs(currentAction.distance)) {
                    currentAction.Complete();
                    movement.StopMove();
                    movement.Reset();
                }
            }
            else if (currentAction.action==ActionType.ROTATE) {
                double rotation = Math.atan2(movement.pose2D.getX(distanceUnit),movement.pose2D.getY(distanceUnit));
                rotation = (rotation + 2 * Math.PI) % (2 * Math.PI);
                rotation = Math.toDegrees(rotation);
                telemetry.addData("Rotation", rotation);
                if (Math.abs(rotation- currentAction.distance)<0.3) {
                    currentAction.Complete();
                    movement.StopMove();
                    movement.Reset();
                }
            }
            else if (currentAction.action==ActionType.WAIT) {
               //
            }
            LogPos();
        }
        telemetry.update();
        prevRuntime = getRuntime();
    }

    public Action DeclareAction(int distance, ActionType action, double speed) {
        Action newAction = new Action();
        newAction.distance = distance;
        newAction.action = action;
        newAction.speed = speed;
        return newAction;
    }

    public void LogPos() {
        telemetry.addData("posX", movement.pose2D.getX(distanceUnit));
        telemetry.addData("posY", movement.pose2D.getY(distanceUnit));
    }

}
