
package org.firstinspires.ftc.robotcontroller.main;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
@TeleOp
public class MotorMovement {

    // Declare OpMode members for each of the 4 motors.
    public DcMotorEx frontLeft;
    public DcMotorEx backLeft;
    public DcMotorEx frontRight;
    public DcMotorEx backRight;

    public GoBildaPinpointDriver pinpoint = null;

    DistanceUnit distanceUnit = DistanceUnit.CM;
    public void init(HardwareMap hardwareMap) {
        frontLeft = hardwareMap.get(DcMotorEx.class, "lf");
        backLeft = hardwareMap.get(DcMotorEx.class, "lb");
        frontRight = hardwareMap.get(DcMotorEx.class, "rf");
        backRight = hardwareMap.get(DcMotorEx.class, "rb");

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        pinpoint.setOffsets(0, 0, distanceUnit);
        pinpoint.resetPosAndIMU();
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
        Reset();
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

    public void StopMove() {
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }

    public boolean AllMotorsBusy() {
        return !(!frontLeft.isBusy()||!backLeft.isBusy()||!frontRight.isBusy()||!backRight.isBusy());
    }

    public String GetMotorModes() {
        return "fl:" + frontLeft.getMode().toString()+", bl:" + backLeft.getMode().toString()+ ", fr:" + frontRight.getMode().toString()+ ", br:" + backRight.getMode().toString();
    }

    public String GetMotorVelocities() {
        return "fl:" + frontLeft.getPower()+", bl:" + backLeft.getPower()+ ", fr:" + frontRight.getPower()+ ", br:" + backRight.getPower();
    }

    public String GetMotors() {
        return "fl:" + Boolean.toString(frontLeft!=null) + ", bl:" + Boolean.toString(backLeft!=null)+ ", fr:" + Boolean.toString(frontRight!=null)+ ", br:" + Boolean.toString(backRight!=null);
    }

    public String GetDeadWheelPositions() {
        return pinpoint.getPosition().getX(distanceUnit) + ", " + pinpoint.getPosition().getX(distanceUnit);
    }

    public Boolean HasDeadWheels() {
        return (pinpoint!=null);
    }

    public void Update() {
        pinpoint.update();
    }

    public void Reset() {
        pinpoint.resetPosAndIMU();
    }

    public double GetX() {
        return Math.abs(pinpoint.getPosX(distanceUnit));
    }

    public double GetY() {
        return Math.abs(pinpoint.getPosY(distanceUnit));
    }

}

