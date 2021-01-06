package org.firstinspires.ftc.teamcode.release;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class testFlicker extends LinearOpMode {
    private DcMotor flicker = null;

    @Override
    public void runOpMode() throws InterruptedException {
        flicker = hardwareMap.get(DcMotor.class, "flicker");
        flicker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flicker.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();
        int encoder_value = 200;
        int target_position = flicker.getCurrentPosition();
        while (target_position < encoder_value){
            flicker.setPower(0.5);
            target_position = flicker.getCurrentPosition();

        }


    }
}
