package org.firstinspires.ftc.robotcontroller.main;

//import op mode
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//import motors
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

public class hello_world extends OpMode {

    private DcMotorEx leftFront, leftBack, rightFront, rightBack;
    private IMU imu;

    public void init(HardwareMap hardwareMap){
        leftFront=hardwareMap.get(DcMotorEx.class, "lF");
        leftBack=hardwareMap.get(DcMotorEx.class, "lB");
        rightFront=hardwareMap.get(DcMotorEx.class, "rF");
        rightBack=hardwareMap.get(DcMotorEx.class, "rB");

        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //----------------IMU--------------
        imu= hardwareMap.get(IMU.class,"imu");

        RevHubOrientationOnRobot revHubOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        );
        imu.initialize(new IMU.Parameters(revHubOrientation));

    }
    public void drive (double forward, double strafe, double rotate){
        double leftFrontPower = forward + strafe + rotate;
        double leftBackPower = forward - strafe + rotate;
        double rightFrontPower = forward - strafe - rotate;
        double rightBackPower = forward + strafe - rotate;

        double maxPower = 1;
        double maxSpeed = 1;

        maxPower = Math.max(maxPower, Math.abs(leftFrontPower));
        maxPower = Math.max(maxPower, Math.abs(leftBackPower));
        maxPower = Math.max(maxPower, Math.abs(rightFrontPower));
        maxPower = Math.max(maxPower, Math.abs(rightBackPower));

        leftFront.setPower(maxSpeed * (leftFrontPower / maxPower));
        leftBack.setPower(maxSpeed * (leftBackPower / maxPower));
        rightFront.setPower(maxSpeed * (rightFrontPower / maxPower));
        rightBack.setPower(maxSpeed * (rightBackPower / maxPower));

    }

    @Override
    void loop() {

    }
}
