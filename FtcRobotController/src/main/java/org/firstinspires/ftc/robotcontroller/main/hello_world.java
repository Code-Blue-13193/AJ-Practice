
package org.firstinspires.ftc.robotcontroller.main;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class hello_world extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotorEx frontLeft = null;
    private DcMotorEx backLeft = null;
    private DcMotorEx frontRight = null;
    private DcMotorEx backRight = null;

    @Override
    public void runOpMode() {

        frontLeft = hardwareMap.get(DcMotorEx.class, "lf");
        backLeft = hardwareMap.get(DcMotorEx.class, "lb");
        frontRight = hardwareMap.get(DcMotorEx.class, "rf");
        backRight = hardwareMap.get(DcMotorEx.class, "rb");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            RawMove(0.3, 0, 0);
            if (getRuntime()>=1) {
                RawMove(0.3, 0.3, 0);
            }
        }
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

        //debug
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Front left/Right", "%4.2f, %4.2f", frontLeftPower, frontRightPower);
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backLeftPower, backRightPower);
        telemetry.update();
    }

    public void StopMove() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
}

