package org.firstinspires.ftc.teamcode.anay;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;

@Autonomous
public class crservo extends LinearOpMode{
   private CRServo main = null;

    @Override
    public void runOpMode() throws InterruptedException {
        main = hardwareMap.get(CRServo.class, "crtest");

        waitForStart();
        while(opModeIsActive()){
            main.setPower(0.1);
        }
    }
}
