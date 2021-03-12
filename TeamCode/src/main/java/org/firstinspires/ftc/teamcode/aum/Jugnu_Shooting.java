package org.firstinspires.ftc.teamcode.aum;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.myblock;


@TeleOp (name = "Shivanni Shooting")
@Disabled
public class Jugnu_Shooting extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        DcMotor shooter = hardwareMap.get(DcMotor.class, "shooter");
        DcMotor flicker = hardwareMap.get(DcMotor.class, "flicker");

        int num_flicks = 0;
        double motorpower = 1.0;

        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flicker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flicker.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        waitForStart();
        shooter.setPower(1.0);
        while (opModeIsActive()) {


            if (gamepad2.b)
            {
                while (num_flicks < 4)
                {
                    flicker.setPower(1.0);
                    sleep(250);//180, 135, 100
                    flicker.setPower(0);
                    sleep(250);//30
                    flicker.setPower(-0.85);
                    sleep(250);//300, 225
                    flicker.setPower(0);
                    sleep(250);//30
                    num_flicks += 1;
                }
                num_flicks = 0;
            }
            else if (gamepad2.a)
            {
                flicker.setPower(0.0);
            }
        }
    }
}
