package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "TeleOp Servo")
public class TeleOp_Servo extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Servo mag_servo = hardwareMap.get(Servo.class, "mag_servo");
        waitForStart();
        double mag_pos = 0.0;
        while(opModeIsActive()){
            mag_servo.setPosition(mag_pos);
            if(gamepad2.a){
                mag_pos = 0.25;
            }

            else if (gamepad2.b){
                mag_pos = 0.5;
            }

            else if (gamepad2.y){
                mag_pos = 0.75;
            }

            else if (gamepad2.x){
                mag_pos = 1.0;
            }

            if (gamepad2.dpad_left){
                mag_pos -= 0.0001;
            }

            else if (gamepad2.dpad_right){
                mag_pos += 0.0001;
            }

            telemetry.addData("Servo Pos: ", mag_pos);
            telemetry.update();
        }
    }
}
