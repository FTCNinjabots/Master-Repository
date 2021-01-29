package org.firstinspires.ftc.teamcode.teleop;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

//Control 0 - br
//Control 1 - bl
//Control 2 - fl
//Control 3 - fr
//Expansion 0 - intake - no encoder
//Expansion 1 - wobble
//Expansion 2 - flicker
//Expansion 3 - shooter - no encoder
//Control Servo 0 - wobble_gate
//Control Servo 1 - rack_pinion

@TeleOp(name = "TeleOp Release")
public class TeleOp_Ninjabots extends LinearOpMode {
    //all motors + servos
    private DcMotor br = null;
    private DcMotor bl = null;
    private DcMotor fl = null;
    private DcMotor fr = null;
    private DcMotor intake = null;
    private DcMotor wobble = null;
    private DcMotor flicker = null;
    private DcMotor shooter = null;

    private Servo wobble_gate = null;
    private CRServo rack_pinion = null;

    @Override
    public void runOpMode() throws InterruptedException {


        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        intake = hardwareMap.get(DcMotor.class, "intake");
        wobble = hardwareMap.get(DcMotor.class, "wobble");
        flicker = hardwareMap.get(DcMotor.class, "flicker");
        shooter = hardwareMap.get(DcMotor.class, "shooter");

        wobble_gate = hardwareMap.get(Servo.class, "wobble_gate");
        rack_pinion = hardwareMap.get(CRServo.class, "rack_pinion");

        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wobble.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flicker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wobble.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flicker.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        br.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        int strafe_value = 0;

        waitForStart();
        while(opModeIsActive()){


            if (gamepad1.left_trigger > 0.5){
                strafe_value = 1;
            }
            else if (gamepad1.right_trigger > 0.5){
                strafe_value = 2;
            }
            else if (gamepad1.left_stick_y != 0 || gamepad1.right_stick_y != 0){
                strafe_value = 0;
            }
            else{
                strafe_value = 4;
            }

            if (strafe_value == 1) {
                //strafe left - LT
                bl.setPower(gamepad1.left_trigger);
                br.setPower(-gamepad1.left_trigger);
                fl.setPower(-gamepad1.left_trigger);
                fr.setPower(gamepad1.left_trigger);

            }
            else if (strafe_value == 2) {
                //strafe right - RT
                bl.setPower(-gamepad1.right_trigger);
                br.setPower(gamepad1.right_trigger);
                fl.setPower(gamepad1.right_trigger);
                fr.setPower(-gamepad1.right_trigger);
            }
            else if (strafe_value == 0){
                //Moving
                br.setPower(-gamepad1.right_stick_y);
                fl.setPower(-gamepad1.left_stick_y);
                fr.setPower(-gamepad1.right_stick_y);
                bl.setPower(-gamepad1.left_stick_y);

            }
            else if (strafe_value == 4){
                br.setPower(0.0);
                fl.setPower(0.0);
                fr.setPower(0.0);
                bl.setPower(0.0);

            }


            //Wobble Goal - LB to go inside, RB to go outside, a to stop motor power
            if (gamepad1.left_bumper) {
                wobble.setPower(0.4);
            } else if (gamepad1.right_bumper) {
                wobble.setPower(-0.4);
            }

            if (gamepad1.a) {
                wobble.setPower(0.0);
            }

            //Wobble Goal Gate - DPAD Down to open, DPAD Up to Close

            if (gamepad1.x){
                wobble_gate.setPosition(1.0);
            }
            else if (gamepad1.b){
                wobble_gate.setPosition(0.0);
            }




            ///CONTROLLER 2

            //Intake - A to start, Y to stop
            if (gamepad2.a){
                //intake.setPower(1.0);
                intake.setPower(1.0);
        }
            else if (gamepad2.y){
                intake.setPower(0.0);
            }

            //Rack + Pinion - LB and RB, b to stop
            // continues until stopped
            if (gamepad2.left_bumper){
                rack_pinion.setPower(1.0);
            }
            else if (gamepad2.right_bumper){
                rack_pinion.setPower(-1.0);
            }

            if (gamepad2.b){ // come back to this later if we want to use joystick
                rack_pinion.setPower(0.0);
            }

            //FLicker - LT + RT
            if (gamepad2.left_trigger > 0 /*&& current_value > -250*/) {
                flicker.setPower(-0.2);
            }
            else if (gamepad2.right_trigger > 0 /*&& current_value < 0*/){
                flicker.setPower(0.2);

            }
            else{
                flicker.setPower(0);
            }

            //Shooter - DPAD UP TO START, DPAD DOWN TO STOP
            if (gamepad2.dpad_up){
                shooter.setPower(-1.0);
            }
            else if (gamepad2.dpad_down){
                shooter.setPower(0.0);
            }


        }

    }
}

