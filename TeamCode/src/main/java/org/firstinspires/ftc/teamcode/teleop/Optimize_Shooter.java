package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;



// 0.8
/// 0.8

@TeleOp(name = "Find Shooter Power")
public class Optimize_Shooter extends LinearOpMode {
    private DcMotor shooter = null;
    private DcMotor flicker = null;

    @Override
    public void runOpMode() throws InterruptedException {
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        flicker = hardwareMap.get(DcMotor.class, "flicker");

        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        double power = -0.05;
        waitForStart();
        while (opModeIsActive()){
            shooter.setPower(power);
            if (gamepad2.a){
                power -= 0.0001;
            }
            else if (gamepad2.b){
                power += 0.0001;
            }

            if (gamepad2.left_trigger > 0 /*&& current_value > -250*/) {
                flicker.setPower(-0.55);
            } else if (gamepad2.right_trigger > 0 /*&& current_value < 0*/) {
                flicker.setPower(0.55);


            } else {
                flicker.setPower(0);
            }

            telemetry.addData("Current Power: ", power);
            telemetry.update();


        }






    }

}
