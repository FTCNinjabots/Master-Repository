package org.firstinspires.ftc.teamcode.anay;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="Run with Intake")
public class Run_With_Intake_On extends LinearOpMode {
    private DcMotor bl = null;
    private DcMotor br = null;
    private DcMotor fl = null;
    private DcMotor fr = null;
    private DcMotor intake_spin = null;

    @Override
    public void runOpMode() throws InterruptedException {
        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        intake_spin = hardwareMap.get(DcMotor.class, "intake_spin");

        br.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        while(opModeIsActive()){
           // bl.setPower(0.5);
           // br.setPower(0.5);
           // fl.setPower(0.5);
          //  fr.setPower(0.5);
            intake_spin.setPower(1.0);
            telemetry.addData("Running: ", "true");
            telemetry.update();

        }
    }
}
