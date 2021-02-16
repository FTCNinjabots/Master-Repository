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



        //Tmr Do wobble goal

        //DISSABLE ALL ENCODERS FOR MOVEMENT
        //DISSABLE ALL ENCODERS FOR MOVEMENT
        //DISSABLE ALL ENCODERS FOR MOVEMENT
        //DISSABLE ALL ENCODERS FOR MOVEMENT
        //DISSABLE ALL ENCODERS FOR MOVEMENT
        //DISSABLE ALL ENCODERS FOR MOVEMENT
        //DISSABLE ALL ENCODERS FOR MOVEMENT
        //DISSABLE ALL ENCODERS FOR MOVEMENT
        //DISSABLE ALL ENCODERS FOR MOVEMENT
        //DISSABLE ALL ENCODERS FOR MOVEMENT
        //DISSABLE ALL ENCODERS FOR MOVEMENT
        //DISSABLE ALL ENCODERS FOR MOVEMENT
        //DISSABLE ALL ENCODERS FOR MOVEMENT
        //DISSABLE ALL ENCODERS FOR MOVEMENT//DISSABLE ALL ENCODERS FOR MOVEMENT


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
            sleep(2000);
            int wobble_target = -500;
            int current_wobble = wobble.getCurrentPosition();
            while(current_wobble > wobble_target){
                wobble.setPower(-0.35);
                wobble_gate.setPosition(0.0);
                current_wobble = wobble.getCurrentPosition();
            }
            telemetry.addData("Position: ", ringposition);
            telemetry.update();
            sleep(2000);



            if (ringposition == SkystoneDeterminationPipeline.RingPosition.FOUR) {
                telemetry.addData("Position: ", "Four Rings"); // C Site
                telemetry.addData("Detected: ", ringposition);
                shooter.setPower(0.7);


                telemetry.update();
                //MoveTank(9500, 0.5);
                MoveTank(1700, 0.5);

                //wobble goes here
            } else if (ringposition == SkystoneDeterminationPipeline.RingPosition.ONE) {
                telemetry.addData("Position: ", "One Ring"); // B Site
                telemetry.addData("Detected: ", ringposition);
                shooter.setPower(0.7);


                telemetry.update();

                //MoveTank(6400, 0.5);
                //Strafe(2300, 0.25);
                //MoveTank(-2000, -0.5);
                MoveTank(1700, 0.5);

                //wobble goes here
            } else if (ringposition == SkystoneDeterminationPipeline.RingPosition.NONE) {
                telemetry.addData("Position: ", "No Rings"); // A Site
                telemetry.addData("Detected: ", ringposition);
                shooter.setPower(0.7);


                telemetry.update();
                //MoveTank(6400, 0.5);
                //Strafe(1500, 0.25);
                MoveTank(1700, 0.5);

                //wobble goes here
            }


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

            //flicker.setPower(-motorpower + 0.5);

            //sleep(225);
            flicker.setPower(0.0);

            telemetry.addData("Status:", "Completed");
            telemetry.update();

            sleep(100000);



        }
    }
}
