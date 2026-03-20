
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

        frontLeft = hardwareMap.get(DcMotorEx.class, "lF");
        backLeft = hardwareMap.get(DcMotorEx.class, "lB");
        frontRight = hardwareMap.get(DcMotorEx.class, "rF");
        backRight = hardwareMap.get(DcMotorEx.class, "rB");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            double max;

            double axial = 0.3;
            double lateral =  0;
            double yaw  =  0;

            double frontLeftPower  = axial + lateral + yaw;
            double frontRightPower = axial - lateral - yaw;
            double backLeftPower   = axial - lateral + yaw;
            double backRightPower  = axial + lateral - yaw;

            max = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(frontRightPower), Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))));

            backLeftPower = ClampWheels(backLeftPower, max);
            frontLeftPower = ClampWheels(frontLeftPower, max);
            backRightPower = ClampWheels(backRightPower, max);
            frontRightPower = ClampWheels(frontRightPower, max);

            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", frontLeftPower, frontRightPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backLeftPower, backRightPower);
            telemetry.update();
        }
    }
    public double ClampWheels(double power, double max) {
        if (max > 1.0) {
            power /= max;
        }
        return power;
    }
}

