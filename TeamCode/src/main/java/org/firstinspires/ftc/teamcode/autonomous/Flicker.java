package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
@TeleOp(name = "Run 2 Flicker")
public class Flicker extends LinearOpMode {
    @Override
    public void runOpMode() {

        DcMotor flicker = hardwareMap.get(DcMotor.class, "flicker");
        DcMotor shooter = hardwareMap.get(DcMotor.class, "shooter");

        flicker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flicker.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();
        shooter.setPower(1.0);
        while (opModeIsActive()) {


            int num_flicks = 0;
            double motorpower = 1.0;
            while (num_flicks < 4) {
                flicker.setPower(motorpower);
                sleep(100);//180, 135, 100
                flicker.setPower(0);
                sleep(100);//30
                flicker.setPower(-motorpower + 0.4);
                sleep(100);//300, 225
                flicker.setPower(0);
                sleep(100);//30
                num_flicks += 1;
            }

            //flicker.setPower(-motorpower + 0.5);

            //sleep(225);
            flicker.setPower(0.0);

            sleep(1000000);
        }
    }
}
