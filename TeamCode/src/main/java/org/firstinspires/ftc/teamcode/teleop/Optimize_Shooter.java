package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


// 0.8
/// 0.8






//narkar            12 high tower       4 mid tower            1 low tower     46.02 sec                              1 min 10 sec


//anay               15  high tower       6  mid tower         0 low tower        54.36 sec                            1 min 19 sec


//Maurya            22  high tower          1  mid tower           low tower         57.29 sec                          53 sec


//McShrpie                25 high tower           3 mid tower                     1 min 10 sec                            1 min 3 sec


//Vasu                                                                           40 sec

//adit                                                                            1 min  9 sec                            1 min 3 sec






@TeleOp(name = "Find Shooter Power")
public class Optimize_Shooter extends LinearOpMode {
    private DcMotor br = null;
    private DcMotor fr = null;
    private DcMotor fl = null;
    private DcMotor bl = null;

    int strafe_value = 0;


    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor shooter = hardwareMap.get(DcMotor.class, "shooter");
        DcMotor flicker = hardwareMap.get(DcMotor.class, "flicker");
        DcMotor intake = hardwareMap.get(DcMotor.class, "intake");
        DcMotor br = hardwareMap.get(DcMotor.class, "br");
        DcMotor bl = hardwareMap.get(DcMotor.class, "bl");
        DcMotor fl = hardwareMap.get(DcMotor.class, "fl");
        DcMotor fr = hardwareMap.get(DcMotor.class, "fr");




        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        flicker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flicker.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        br.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);


        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        double power = 1.0;

        waitForStart();
        shooter.setPower(power);

        while (opModeIsActive()){
            if (gamepad2.a){
                power -= 0.0001;
            }
            else if (gamepad2.b){
                power += 0.0001;
            }

            int num_flicks = 0;
            double motorpower = 1.0;


            if (gamepad2.dpad_up)
            {
                while (num_flicks < 4)
                {
                    flicker.setPower(1.0);
                    sleep(250);//180, 135, 100
                    flicker.setPower(0);
                    sleep(250);//30
                    flicker.setPower(-0.85);
                    sleep(250);//300, 225
                    flicker.setPower(0);
                    sleep(250);//30
                    num_flicks += 1;
                }
                num_flicks = 0;
            }
            else if (gamepad2.dpad_down)
            {
                flicker.setPower(0.0);
            }

            if(gamepad2.x){
                intake.setPower(1.0);
                flicker.setPower(1.0);
                sleep(250);
                flicker.setPower(0.0 );
            }
            else if(gamepad2.y){
                intake.setPower(0.0);
            }

            //if (gamepad2.dpad_down){
             //   power = 0.0;
             //   shooter.setPower(0.0);
           // }

            int strafe_value2 = 0;

            if (gamepad2.left_bumper) {
                strafe_value2 = 1;
            } else if (gamepad2.right_bumper) {
                strafe_value2 = 2;
            } else if (gamepad2.left_stick_y != 0 || gamepad2.right_stick_y != 0) {
                strafe_value2 = 0;
            } else {
                strafe_value2 = 4;
            }

            if (strafe_value2 == 1) {
                //strafe left - LT
                bl.setPower(0.75);
                br.setPower(-0.75);
                fl.setPower(-0.75);
                fr.setPower(0.75);

            } else if (strafe_value2 == 2) {
                //strafe right - RT
                bl.setPower(-0.75);
                br.setPower(0.75);
                fl.setPower(0.75);
                fr.setPower(-0.75);
            } else if (strafe_value2 == 0) {
                //Moving
                br.setPower(-gamepad2.right_stick_y);
                fl.setPower(-gamepad2.left_stick_y);
                fr.setPower(-gamepad2.right_stick_y);
                bl.setPower(-gamepad2.left_stick_y);
            } else if (strafe_value2 == 4) {
                br.setPower(0.0);
                fl.setPower(0.0);
                fr.setPower(0.0);
                bl.setPower(0.0);

            }

            if (gamepad2.dpad_down){
                shooter.setPower(0.0);
            }

            if (gamepad2.dpad_left) {
                shooter.setPower(1.0);
            }

            telemetry.addData("Current Power: ", power);
            telemetry.update();


        }






    }

}
