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
@Autonomous(name="Arsh Turning")

    public class Arsh_Turning extends myblock {


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
        Servo wobble_gate = hardwareMap.get(Servo.class, "wobble_gate");
        DcMotor intake = hardwareMap.get(DcMotor.class, "intake");



        //vasu is smelling anay's poopies

        //anay has done the POOPIES

        //SHARVA FARTED

        //MAURYA IS VERY SAD

        //AUM IS CALLING GIRLFRIEND'S MOTHER

        //ADIT HAS BEEN KICKED OUT OF ninjabots, no longer needed, waste





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

            MoveTank(300, 0.5);
            sleep(1);
            int wobble_target = -400;
            int current_wobble = wobble.getCurrentPosition();
            while(current_wobble > wobble_target){
                wobble.setPower(-0.35);
                wobble_gate.setPosition(0.0);
                current_wobble = wobble.getCurrentPosition();
            }
            telemetry.addData("Position: ", ringposition);
            telemetry.update();
            sleep(1);
            wobble.setPower(0.0);



            if (ringposition == SkystoneDeterminationPipeline.RingPosition.FOUR) {

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
                sleep(500);
                MoveTank(4700, 0.5);
                sleep(500);

                Turn(150, 0.5);
                sleep(500);
                wobble_gate.setPosition(1.0);
                sleep(1000);
                MoveTank(5000, 0.5);
                sleep(500);
                Turn(45, 0.3);
                sleep(100000);


                //wobble goes here
            } else if (ringposition == SkystoneDeterminationPipeline.RingPosition.ONE) {
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
                Turn(47, 0.5); // We need to change degrees  anay has officially poopied his pants
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
                MoveTank(4000, 0.5);;
                Turn(180, 0.5);
                wobble_gate.setPosition(1.0);
                sleep(500);
                Turn(-8, -0.1);
                MoveTank(6900, 0.5);
                sleep(200);
                Strafe(200, 0.5);
                //MoveTank(2300, 0.5);
                //Turn(190, 1.0);




                sleep(10000000);


                //wobble goes here
            } else if (ringposition == SkystoneDeterminationPipeline.RingPosition.NONE) {
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
                wobble_gate.setPosition(1.0);
                sleep(500);
                MoveTank(1000, 0.5);
                sleep(200);
                Turn(-55, -0.3);
                MoveTank(4220, 0.75);
                sleep(1000);
                Strafe(2100, 0.25);
                sleep(1000);
                wobble_gate.setPosition(0.0);
                sleep(100000);


                //wobble goes here
            }



            /*(sleep(2000);
            Turn(47, 0.5); // We need to change degrees  anay has officially poopied his pants
            sleep(1000);
            intake.setPower(-0.75);
            sleep(1000);
            MoveTank(2000, 0.3);
            sleep(1000);
            MoveTank(-1000, -0.5);
            intake.setPower(1.0);
            sleep(1000);
            MoveTank(1500, 0.35);
            */


            telemetry.addData("Status:", "Completed");
            telemetry.update();

            sleep(100000);



        }
    }
}
