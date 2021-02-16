package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class TeleOp_WobbleGoalMotor extends LinearOpMode {
    private DcMotor wobble_goal_motor = null;
    private Servo main = null;
    @Override
    public void runOpMode() throws InterruptedException {
        wobble_goal_motor = hardwareMap.get(DcMotor.class, "wobble");
        main = hardwareMap.get(Servo.class, "wobble_gate");

        wobble_goal_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wobble_goal_motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.left_bumper) {
                wobble_goal_motor.setPower(0.35);
                main.setPosition(0.0);
            } else if (gamepad1.right_bumper) {
                wobble_goal_motor.setPower(-0.35);
                main.setPosition(0.0);
            }
            else{
                wobble_goal_motor.setPower(0.0);
            }


            if (gamepad1.a) {
                main.setPosition(0.0);
            }
            if(gamepad1.b){
                main.setPosition(1.0);
            }



            }
        }
        /*
        if (gamepad1.y){
            boolean has_pressed_y = false;
            int target_pos = wobble_goal_motor.getCurrentPosition();
            while(! has_pressed_y){
                int current_pos = wobble_goal_motor.getCurrentPosition();
                int going_forward = 1;
                if (current_pos < target_pos){//motor is less that

                }



                if (gamepad1.y)
            }
        }
        */




}
