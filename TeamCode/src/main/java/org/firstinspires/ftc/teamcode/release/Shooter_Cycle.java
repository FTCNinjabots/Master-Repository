package org.firstinspires.ftc.teamcode.release;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "Run Shooter Cycle")
public class Shooter_Cycle extends LinearOpMode {
    private DcMotor intake = null;
    private DcMotor flicker = null;
    private DcMotor shooter = null;

    @Override
    public void runOpMode() throws InterruptedException {
        intake = hardwareMap.get(DcMotor.class, "intake_spin");
        flicker = hardwareMap.get(DcMotor.class, "flicker");
        shooter = hardwareMap.get(DcMotor.class, "shooter");

        // Reset Encoders and prime motors
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flicker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flicker.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();


        int encoder_value = 200;
        int target_position = flicker.getCurrentPosition();
        while (target_position < encoder_value){
            flicker.setPower(0.5);
            target_position = flicker.getCurrentPosition();

        }
        while(opModeIsActive()){
            shooter.setPower(1.0);

        }







    }
}
