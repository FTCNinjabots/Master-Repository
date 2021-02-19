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
@Autonomous(name="Sharva IQ")

public class ring_test extends myblock {


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
            intake.setPower(1.0);
            //sleep(225);
            //flicker.setPower(0.0);
            //sleep(2000);
            //Turn(47, 0.5); // We need to change degrees  anay has officially poopied his pants
            sleep(1000);
            //intake.setPower(-0.75);
            //sleep(1000);
            MoveTank(2000, 0.15);
            sleep(1000);
            //MoveTank(-1000, -0.5);
            //intake.setPower(1.0);
            sleep(1000);
            intake.setPower(0.0);
            //MoveTank(1500, 0.35);



            telemetry.addData("Status:", "Completed");
            telemetry.update();

            sleep(100000);



        }
    }
}
