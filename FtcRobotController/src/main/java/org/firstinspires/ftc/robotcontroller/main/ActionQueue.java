package org.firstinspires.ftc.robotcontroller.main;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class ActionQueue extends OpMode {
    MotorMovement movement = new MotorMovement();
    @Override
    public void init() {
        movement.Init(hardwareMap);
    }

    @Override
    public void loop() {
        movement.RawMove(0, 0.3, 0);
    }
}
