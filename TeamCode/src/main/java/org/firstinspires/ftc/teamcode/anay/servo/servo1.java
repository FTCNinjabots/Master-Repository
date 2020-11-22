package org.firstinspires.ftc.teamcode.anay.servo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Servo Test", group = "Servo Test")
public class servo1 extends LinearOpMode {

    public Servo s1 = null;

    @Override
    public void runOpMode() {
        s1 = hardwareMap.get(Servo.class, "s1");
        waitForStart();
        while (opModeIsActive()){
            s1.setPosition(1.0);
        }
    }
}
