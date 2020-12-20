package org.firstinspires.ftc.teamcode.vasu;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@Autonomous(name="Auto1", group="Auto1")
//@Disabled
public class Auto extends LinearOpMode {
    DcMotor fr;
    DcMotor fl;
    DcMotor br;
    DcMotor bl;


    @Override
    public void runOpMode() throws InterruptedException {
        fr = hardwareMap.dcMotor.get("fr");
        fl = hardwareMap.dcMotor.get("fl");
        br = hardwareMap.dcMotor.get("br");
        bl = hardwareMap.dcMotor.get("bl");

        br.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Mode", "waiting");
        telemetry.update();

        waitForStart();

        telemetry.addData("Mode", "running");
        telemetry.update();

        sleep(500);

        bl.setPower(0.25);
        br.setPower(0.25);
        fl.setPower(-0.25);
        fr.setPower(-0.25);

        sleep(2000);

            fr.setPower(0.25);
            fl.setPower(0.25);
            br.setPower(0.25);
            bl.setPower(0.25);


            sleep(10000);

    }
}
