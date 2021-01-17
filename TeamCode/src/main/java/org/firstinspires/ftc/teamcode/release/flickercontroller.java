package org.firstinspires.ftc.teamcode.release;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
@Disabled
@Autonomous
public class flickercontroller extends LinearOpMode {
    private DcMotor flicker = null;
    private DcMotor shooter = null;


    @Override
    public void runOpMode() throws InterruptedException {
        flicker = hardwareMap.get(DcMotor.class, "flicker");
        flicker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flicker.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooter = hardwareMap.get(DcMotor.class, "shooter");


        waitForStart();
        int encoder_value = 0;
        int current_value = flicker.getCurrentPosition();
        shooter.setPower(1.0);
        while (opModeIsActive()) {
            current_value = flicker.getCurrentPosition();
            if (gamepad1.left_trigger > 0 /*&& current_value > -250*/) {
                flicker.setPower(-0.2);
            }
            else if (gamepad1.right_trigger > 0 /*&& current_value < 0*/){
                flicker.setPower(0.2);

            }
            else{
                flicker.setPower(0);
            }


            if (gamepad1.a){
                shooter.setPower(0);
                }
            else if (gamepad1.b){
                shooter.setPower(1.0);
            }
        }
    }

}