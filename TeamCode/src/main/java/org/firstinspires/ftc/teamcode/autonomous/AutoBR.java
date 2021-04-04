package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "AutoBR")
public class AutoBR extends LinearOpMode {
    private DcMotor br;
    @Override
    public void runOpMode() throws InterruptedException {
        br = hardwareMap.get(DcMotor.class, "br");
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();
        while (opModeIsActive()){
            if (gamepad1.left_stick_y > 0.2){
                br.setPower(1.0);
            }
            else if (gamepad1.left_stick_y < -0.2){
                br.setPower(-1.0);
            }
            else{
                br.setPower(0.0);
            }
            telemetry.addData("Br Encoder: ", br.getCurrentPosition());
            telemetry.update();
        }
    }
}
