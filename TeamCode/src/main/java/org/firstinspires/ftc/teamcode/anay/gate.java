package org.firstinspires.ftc.teamcode.anay;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
@Disabled
@TeleOp
public class gate extends LinearOpMode {
    private Servo main = null;

    @Override
    public void runOpMode() throws InterruptedException {
        main = hardwareMap.get(Servo.class, "gate");

        waitForStart();
        while(opModeIsActive()){
            main.setPosition(0.0);
            sleep(2000);
            main.setPosition(1.0);
            sleep(2000);
        }
    }
}

