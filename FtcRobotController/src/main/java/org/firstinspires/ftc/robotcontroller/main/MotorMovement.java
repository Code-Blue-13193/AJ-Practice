
package org.firstinspires.ftc.robotcontroller.main;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MotorMovement {

    // Declare OpMode members for each of the 4 motors.
    private DcMotorEx frontLeft = null;
    private DcMotorEx backLeft = null;
    private DcMotorEx frontRight = null;
    private DcMotorEx backRight = null;

    public void Init(HardwareMap hardwareMap) {
        frontLeft = hardwareMap.get(DcMotorEx.class, "lf");
        backLeft = hardwareMap.get(DcMotorEx.class, "lb");
        frontRight = hardwareMap.get(DcMotorEx.class, "rf");
        backRight = hardwareMap.get(DcMotorEx.class, "rb");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public double ClampWheel(double power, double max) {
        if (max > 1.0) {
            power /= max;
        }
        return power;
    }

    public void RawMove(double axial, double lateral, double yaw) {
        //axial is total movement power
        //lateral is horizontal movement power (from the robots pov)
        //yaw rotates the robot

        //misc var declaration
        double max;

        //calc motor power
        double frontLeftPower  = axial + lateral + yaw;
        double frontRightPower = axial - lateral - yaw;
        double backLeftPower   = axial - lateral + yaw;
        double backRightPower  = axial + lateral - yaw;

        //limit motor powers
        max = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(frontRightPower), Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))));

        backLeftPower = ClampWheel(backLeftPower, max);
        frontLeftPower = ClampWheel(frontLeftPower, max);
        backRightPower = ClampWheel(backRightPower, max);
        frontRightPower = ClampWheel(frontRightPower, max);

        //apply motor powers
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);
    }

    public void EncoderMove(double axial, double lateral, double yaw, int distance, double speed) {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setVelocity(speed);
        backLeft.setVelocity(speed);
        frontRight.setVelocity(speed);
        backRight.setVelocity(speed);

        frontLeft.setTargetPosition(distance);
        backLeft.setTargetPosition(distance);
        frontRight.setTargetPosition(distance);
        backRight.setTargetPosition(distance);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void StopMove() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public int HighestEncoder() {
        return Math.max(frontLeft.getCurrentPosition(),Math.max(backLeft.getCurrentPosition(), Math.max(frontRight.getCurrentPosition(), backRight.getCurrentPosition())));
    }
}

