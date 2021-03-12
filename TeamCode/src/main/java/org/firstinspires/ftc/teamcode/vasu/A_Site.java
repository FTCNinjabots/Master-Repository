package org.firstinspires.ftc.teamcode.vasu;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.myblock;

@Autonomous(name="AS", group="AS")
@Disabled
public class A_Site extends myblock {
    DcMotor bl;
    DcMotor fr;
    DcMotor fl;
    DcMotor br;


    /*public void MoveTank(int target_position, double motorPower) throws InterruptedException {
        float current_position;
        boolean has_stopped = false;

        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");

        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        br.setDirection(DcMotor.Direction.REVERSE);
        fr.setDirection(DcMotor.Direction.REVERSE);

        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        bl.setPower(motorPower);
        br.setPower(motorPower);
        fl.setPower(motorPower);
        fr.setPower(motorPower);

        while (!has_stopped) {
            current_position = br.getCurrentPosition();
            if (current_position >= target_position) {
                bl.setPower(0.0);
                br.setPower(0.0);
                fl.setPower(0.0);
                fr.setPower(0.0);
                has_stopped = true;

            }
            telemetry.addData("Current Position: ", current_position);
            telemetry.update();
        }


    }*/


    public void runOpMode() throws InterruptedException {

        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");

        //bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        br.setDirection(DcMotor.Direction.REVERSE);
        fr.setDirection(DcMotor.Direction.REVERSE);

        //bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        while (opModeIsActive()) {
            MoveTank(6400, 0.5);
            telemetry.addData("BR:", br.getCurrentPosition());
            telemetry.addData("BL:", bl.getCurrentPosition());
            telemetry.addData("FL:", fl.getCurrentPosition());
            telemetry.addData("FR:", fr.getCurrentPosition());



            Strafe(1500, 0.25);


            MoveTank(6400, 0.5);

            //Drop Wobble Goal Here

        }
    }

}