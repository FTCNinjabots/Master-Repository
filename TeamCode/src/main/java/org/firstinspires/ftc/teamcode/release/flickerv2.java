package org.firstinspires.ftc.teamcode.release;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
@Disabled
@Autonomous
public class flickerv2 extends LinearOpMode {
    private DcMotor flicker = null;
    private DcMotor shooter = null;
    Gamepad shoot_gamepad = null;

    @Override
    public void runOpMode() throws InterruptedException {
        flicker = hardwareMap.get(DcMotor.class, "flicker");
        flicker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flicker.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooter = hardwareMap.get(DcMotor.class, "shooter");


        waitForStart();
        shooter.setPower(1.0);


        while (opModeIsActive()) {
            sleep(2000);

            int encoder_value = -250;
            int target_position = flicker.getCurrentPosition();
            flicker.setPower(-0.2);
            while (target_position >= encoder_value) {


                target_position = flicker.getCurrentPosition();
                telemetry.addData("Current Position: ", target_position);
                telemetry.update();

                if (target_position < encoder_value) {
                    flicker.setPower(0);
                }

            }
            telemetry.addData("position right now", target_position);
            telemetry.update();

            encoder_value = 0;
            target_position = flicker.getCurrentPosition();
            flicker.setPower(0.2);
            while (target_position <= encoder_value) {


                target_position = flicker.getCurrentPosition();
                telemetry.addData("Current Position: ", target_position);
                telemetry.update();

                if (target_position > encoder_value) {
                    flicker.setPower(0);
                }

            }


        }
    }
}
