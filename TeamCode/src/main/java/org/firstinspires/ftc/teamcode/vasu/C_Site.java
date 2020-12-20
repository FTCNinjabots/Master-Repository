package org.firstinspires.ftc.teamcode.vasu;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;



@Autonomous(name="CS", group="CS")

public class C_Site extends LinearOpMode {

    private DcMotor bl = null;
    private DcMotor br = null;
    private DcMotor fl = null;
    private DcMotor fr = null;

    @Override

    public void runOpMode() {

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



            waitForStart();
            while (opModeIsActive()) {
                bl.setTargetPosition(14400);
                br.setTargetPosition(14400);
                fl.setTargetPosition(14400);
                fr.setTargetPosition(14400);
                bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                bl.setPower(0.5);
                br.setPower(0.5);
                fl.setPower(0.5);
                fr.setPower(0.5);
                float current_encoder = bl.getCurrentPosition();
                telemetry.addData("Encoder Position: ", current_encoder);
                telemetry.update();
            }
            /*
            sleep(700);

            bl.setTargetPosition(-1620);
            br.setTargetPosition(-1620);
            fl.setTargetPosition(-1620);
            fr.setTargetPosition(-1620);
            bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            br.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bl.setPower(-0.5);
            br.setPower(-0.5);
            fl.setPower(-0.5);
            fr.setPower(-0.5);*/
    }
}