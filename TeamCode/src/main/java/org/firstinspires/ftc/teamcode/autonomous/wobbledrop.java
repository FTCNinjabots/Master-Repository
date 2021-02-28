package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
@Autonomous(name = "Wobble Drop")
public class wobbledrop extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor wobble = hardwareMap.get(DcMotor.class, "wobble");
        CRServo wobble_gate = hardwareMap.get(CRServo.class, "wobble_gate");

        wobble.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wobble.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();
        int target_encoder = -420;
        int wobble_encoder = wobble.getCurrentPosition();
        double wobble_power = -0.3;
        boolean hundred_passed = false;
        boolean twohundred_passed = false;
        boolean threehundred_passed = false;

        while(opModeIsActive()){

            wobble_gate.setPower(1.0);
            sleep(1000);
            while(wobble_encoder >= target_encoder) {
                wobble.setPower(wobble_power);
                wobble_encoder = wobble.getCurrentPosition();

                if(wobble_encoder < -350 && !threehundred_passed){
                    wobble_power += 0.05;
                    threehundred_passed = true;
                }
                else if(wobble_encoder < -250 && !twohundred_passed){
                    wobble_power += 0.07;
                    twohundred_passed = true;
                }
                else if(wobble_encoder < -100 && !hundred_passed){
                    wobble_power += 0.1;
                    hundred_passed = true;
                }



            }
            wobble.setPower(0.0);
            wobble_gate.setPower(1.0);
            sleep(3000);
            wobble_gate.setPower(1.0);
            sleep(1000000);


        }

    }
}
