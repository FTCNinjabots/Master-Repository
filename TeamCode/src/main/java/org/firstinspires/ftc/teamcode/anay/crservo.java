package org.firstinspires.ftc.teamcode.anay;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
@Autonomous
public class crservo extends LinearOpMode {
    private CRServo main = null;

    @Override
    public void runOpMode() throws InterruptedException {
        main = hardwareMap.get(CRServo.class, "crtest");

        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.right_trigger != 0) {
                if (main.getPower() == 1.0){
                    main.setPower(0);
                }
                else{
                    main.setPower(1.0);
                }


            } else if (gamepad1.left_trigger != 0) {
                if (main.getPower() == -1.0){
                    main.setPower(0);
                }
                else{
                    main.setPower(-1.0);
                }

            }


        }
    }
}