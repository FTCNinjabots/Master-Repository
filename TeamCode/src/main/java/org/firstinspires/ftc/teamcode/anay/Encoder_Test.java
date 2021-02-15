package org.firstinspires.ftc.teamcode.anay;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
@Disabled
@TeleOp(name = "Move Tank", group = "Move Tank Group")
public class Encoder_Test extends LinearOpMode {
    private DcMotor bl = null;
    private DcMotor br = null;
    private DcMotor fl = null;
    private DcMotor fr = null;



    @Override
    public void runOpMode() throws InterruptedException {
        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        telemetry.addData("Status: ", "Initialized");
        telemetry.update();
        waitForStart();
        bl.setPower(0.0);
        fl.setPower(0.0);
        br.setPower(0.0);
        fr.setPower(0.0);
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

        while (opModeIsActive()) {
            bl.setPower(0.5);
            fl.setPower(0.5);
            br.setPower(0.5);
            fr.setPower(0.5);
            float bl_position = bl.getCurrentPosition();
            float br_position = br.getCurrentPosition();
            float fl_position = fl.getCurrentPosition();
            float fr_position = fr.getCurrentPosition();

            telemetry.addData("bl_position;", bl_position);
            telemetry.addData("br_position;", br_position);
            telemetry.addData("fl_position;", fl_position);
            telemetry.addData("fr_position;", fr_position);
            telemetry.addData("mode: ", fl.getMode());
            telemetry.update();

        }
        float bl_position = bl.getCurrentPosition();
        float br_position = br.getCurrentPosition();
        float fl_position = fl.getCurrentPosition();
        float fr_position = fr.getCurrentPosition();
        telemetry.addData("bl_position;", bl_position);
        telemetry.addData("br_position;", br_position);
        telemetry.addData("fl_position;", fl_position);
        telemetry.addData("fr_position;", fr_position);
        telemetry.update();
    }
}
