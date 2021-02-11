package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

@TeleOp(name = "Find Shooter Power")
public class Optimize_Shooter extends LinearOpMode {
    private DcMotor shooter = null;
    private CRServo flicker = null;

    @Override
    public void runOpMode() throws InterruptedException {
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        flicker = hardwareMap.get(CRServo.class, "flicker");

        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double power = -0.7;
        waitForStart();
        while (opModeIsActive()){
            shooter.setPower(power);
            if (gamepad1.a){
                power -= 0.001;
            }
            else if (gamepad1.b){
                power += 0.001;
            }

            if (power > 0){
                power = 0;
            }
            if (gamepad1.left_trigger > 0 /*&& current_value > -250*/) {
                flicker.setPower(-1.0);
            } else if (gamepad1.right_trigger > 0 /*&& current_value < 0*/) {
                flicker.setPower(1.0);


            } else {
                flicker.setPower(0);
            }

            telemetry.addData("Current Power: ", power);
            telemetry.update();


        }






    }

}
