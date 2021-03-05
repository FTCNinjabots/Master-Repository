package org.firstinspires.ftc.teamcode.autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.common.myblock;
import org.firstinspires.ftc.teamcode.common.myblock.SkystoneDeterminationPipeline;
@Autonomous(name="Autonomous Ninjabots")

public class Autonomous_Ninjabots extends myblock {


    //7743  shoooooter


    @Override
    public void runOpMode() {
        SkystoneDeterminationPipeline.RingPosition ringposition;
        DcMotor bl = hardwareMap.get(DcMotor.class, "bl");
        DcMotor br = hardwareMap.get(DcMotor.class, "br");
        DcMotor fl = hardwareMap.get(DcMotor.class, "fl");
        DcMotor fr = hardwareMap.get(DcMotor.class, "fr");
        DcMotor wobble = hardwareMap.get(DcMotor.class, "wobble");
        DcMotor shooter = hardwareMap.get(DcMotor.class, "shooter");
        DcMotor flicker = hardwareMap.get(DcMotor.class, "flicker");
        CRServo wobble_gate = hardwareMap.get(CRServo.class, "wobble_gate");
        DcMotor intake = hardwareMap.get(DcMotor.class, "intake");
        CRServo intake_servo = hardwareMap.get(CRServo.class, "intake_servo");


        //shooter power 0.82


        //vasu is smelling anay's poopies

        //anay has done the POOPIES

        //SHARVA FARTED

        //MAURYA IS VERY SAD

        //AUM IS CALLING GIRLFRIEND'S MOTHER

        //Adit is sad


        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wobble.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flicker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wobble.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flicker.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        br.setDirection(DcMotor.Direction.REVERSE);
        fr.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();
        while (opModeIsActive()) {

            ringposition = detect();
            int target_encoder = -500;
            int wobble_encoder = wobble.getCurrentPosition();
            double wobble_power = -0.45;
            boolean hundred_passed = false;
            boolean twohundred_passed = false;
            boolean threehundred_passed = false;
            boolean fourhundred_passed = false;
            wobble_gate.setPower(1.0);
            sleep(1000);


            MoveTank(300, 0.5);

            while (wobble_encoder >= target_encoder) {
                wobble.setPower(wobble_power);
                wobble_encoder = wobble.getCurrentPosition();

                if (wobble_encoder < -480 && !fourhundred_passed) {
                    wobble_power += 0.15;
                    fourhundred_passed = true;
                } else if (wobble_encoder < -420 && !threehundred_passed) {
                    wobble_power += 0.05;
                    threehundred_passed = true;
                } /*else if (wobble_encoder < -250 && !twohundred_passed) {
                    wobble_power += 0.05;
                    twohundred_passed = true;
                } else if (wobble_encoder < -100 && !hundred_passed) {
                    wobble_power += 0.08;
                    hundred_passed = true;
                }*/


                telemetry.addData("Motor encoder: ", wobble_encoder);
                telemetry.update();
                }
                wobble.setPower(0.0);


                if (ringposition == SkystoneDeterminationPipeline.RingPosition.FOUR && opModeIsActive()) {

                    int num_flicks = 0;
                    double motorpower = 1.0;

                    shooter.setPower(0.76);

                    telemetry.addData("Into four loop:", "True");
                    telemetry.update();

                    //MoveTank(9500, 0.5);
                    MoveTank(1500, 0.5);


                    //wobble goes here
                    flicker.setPower(motorpower);
                    MoveTank(300, 0.5);
                    Turn(15, 0.5);



                    while (num_flicks < 4) {
                        flicker.setPower(-0.85);
                        sleep(225);//180, 135, 100
                        flicker.setPower(0);
                        flicker.setPower(motorpower);
                        sleep(300);//300, 225
                        flicker.setPower(0);

                        //sleep(300);//30
                        num_flicks += 1;

                        if (num_flicks == 2) {
                            intake_servo.setPower(1.0);

                        }
                        //if (num_flicks == 4){
                        //    flicker.setPower(1.0);
                        //}
                    }
                    flicker.setPower(1.00);
                    intake_servo.setPower(0.0);
                    shooter.setPower(0);
                    flicker.setPower(0.0);

                    Turn(29, 0.5); // We need to change degrees from 47 - 55


                    intake.setPower(1.0);


                    int q=0;
                    while(q < 10){
                        intake_servo.setPower(-0.07);
                        MoveTank(200, 0.075);
                        intake_servo.setPower(0.0);
                        MoveTank(200, 0.075);
                        q++;
                    }


                    Turn(-44, -0.5);


                    // shooter shoots at power shots


                    shooter.setPower(0.85);
                    num_flicks = 0;
                    while (num_flicks < 4) {
                        flicker.setPower(-0.85);
                        sleep(225);//180, 135, 100
                        flicker.setPower(0);
                        flicker.setPower(motorpower);
                        sleep(300);//300, 225
                        flicker.setPower(0);
                        num_flicks += 1;
                    }
                    Turn(44, 0.5);
                    MoveTank(1000, 0.15);

                    shooter.setPower(0.83);
                    Turn(-42, -0.5);
                    num_flicks = 0;
                    motorpower = 1.0;
                    while (num_flicks < 4) {
                        flicker.setPower(-0.85);
                        sleep(225);//180, 135, 100
                        flicker.setPower(0);
                        flicker.setPower(motorpower);
                        sleep(300);//300, 225
                        flicker.setPower(0);
                        num_flicks += 1;
                    }

                    intake.setPower(0.0);
                    shooter.setPower(0.0);
                    MoveTank(4800, 0.5);
                    Turn(150, 0.4);
                    MoveTank(-2200, -0.3);
                    sleep(1000);
                    Turn(20, 0.15);
                    wobble_gate.setPower(-1.0);

                    //wobble_gate.setPower(0.0);
                    MoveTank(5600, 0.5);


                    sleep(2000);


                    telemetry.addData("Status:", "Completed");
                    telemetry.update();

                    sleep(100000);


                    //wobble goes here
                } else if (ringposition == SkystoneDeterminationPipeline.RingPosition.ONE && opModeIsActive()) {
                    shooter.setPower(0.723);


                    telemetry.update();
                    //MoveTank(9500, 0.5);
                    MoveTank(1700, 0.5);

                    //wobble goes here
                    int num_flicks = 0;
                    double motorpower = 1.0;
                    while (num_flicks < 3) {
                        flicker.setPower(motorpower);
                        sleep(100);//180, 135, 100
                        flicker.setPower(0);
                        sleep(1000);//30
                        flicker.setPower(-motorpower + 0.4);
                        sleep(225);//300, 225
                        flicker.setPower(0);
                        sleep(1000);//30
                        num_flicks += 1;
                    }
                    flicker.setPower(0.0);
                    shooter.setPower(0.67);
                    sleep(500);
                    Turn(41, 0.5); // We need to change degrees  anay has officially poopied his pants
                    sleep(300);
                    intake.setPower(1.0);
                    sleep(100);
                    MoveTank(3300, 0.5);
                    sleep(100);
                    Turn(-48, -0.5);

                    sleep(1000);
                    num_flicks = 0;
                    motorpower = 1.0;
                    while (num_flicks < 2) {
                        flicker.setPower(motorpower);
                        sleep(100);//180, 135, 100
                        flicker.setPower(0);
                        sleep(1000);//30
                        flicker.setPower(-motorpower + 0.4);
                        sleep(225);//300, 225
                        flicker.setPower(0);
                        sleep(1000);//30
                        num_flicks += 1;
                    }
                    flicker.setPower(0.0);
                    shooter.setPower(0.0);
                    intake.setPower(0.0);
                    //Turn(-3, -0.5);
                    MoveTank(4000, 0.5);
                    ;
                    Turn(180, 0.5);
                    wobble_gate.setPower(-1.0);
                    sleep(500);
                    Turn(-8, -0.1);
                    MoveTank(6900, 0.5);
                    sleep(200);
                    Strafe(200, 0.25);
                    //MoveTank(2300, 0.5);
                    //Turn(190, 1.0);


                    sleep(10000000);


                    //wobble goes here
                } else if (ringposition == SkystoneDeterminationPipeline.RingPosition.NONE && opModeIsActive()) {
                    shooter.setPower(0.723);


                    telemetry.update();
                    //MoveTank(9500, 0.5);
                    MoveTank(1700, 0.5);

                    //wobble goes here
                    int num_flicks = 0;
                    double motorpower = 1.0;
                    while (num_flicks < 3) {
                        flicker.setPower(motorpower);
                        sleep(100);//180, 135, 100
                        flicker.setPower(0);
                        sleep(1000);//30
                        flicker.setPower(-motorpower + 0.4);
                        sleep(225);//300, 225
                        flicker.setPower(0);
                        sleep(1000);//30
                        num_flicks += 1;
                    }
                    flicker.setPower(0.0);
                    shooter.setPower(0.0);
                    sleep(500);
                    MoveTank(4700, 0.5);
                    sleep(200);

                    Turn(185, 0.5);
                    sleep(200);
                    wobble_gate.setPower(-1.0);
                    sleep(500);
                    MoveTank(1000, 0.5);
                    sleep(200);
                    Turn(-55, -0.3);
                    MoveTank(4220, 0.75);
                    sleep(1000);
                    Strafe(2100, 0.25);
                    sleep(1000);
                    wobble_gate.setPower(1.0);
                    sleep(100000);
// work on rings


                    //wobble goes here
                }



        }
    }
}
