package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name = "TeleOp Continuous")
public class TeleOp_ContinuousServo extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        CRServo cr = hardwareMap.get(CRServo.class, "crservo");
        waitForStart();
        while(opModeIsActive()){
            if(gamepad2.right_trigger != 0){
                cr.setPower(1.0);
            }
            else if (gamepad2.left_trigger != 0){
                cr.setPower(-1.0);
            }
            else{
                cr.setPower(0.0);
            }
        }
    }
}
