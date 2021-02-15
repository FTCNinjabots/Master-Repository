package org.firstinspires.ftc.teamcode.anay;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled
@TeleOp(name="Run with Intake")
public class Run_With_Intake_On extends LinearOpMode {
    private DcMotor bl = null;
    private DcMotor br = null;
    private DcMotor fl = null;
    private DcMotor fr = null;
    private DcMotor intake_spin = null;

    @Override
    public void runOpMode() throws InterruptedException {
        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        intake_spin = hardwareMap.get(DcMotor.class, "intake_spin");

        br.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        intake_spin.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake_spin.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

           // bl.setPower(0.5);
           // br.setPower(0.5);
           // fl.setPower(0.5);
          //  fr.setPower(0.5);
        /*
        boolean has_stopped = false;
        int encoder_value = 500;

        int target_position = intake_spin.getCurrentPosition();

        intake_spin.setPower(0.1);
        while (target_position < encoder_value){

            target_position = intake_spin.getCurrentPosition();
            if (target_position > encoder_value){
                intake_spin.setPower(0);
            }
        }
        */


        int target_value = 300;
        int current_pos = intake_spin.getCurrentPosition();
        while (opModeIsActive()){
            if (current_pos < target_value){
               intake_spin.setPower(0.24);
           }

           else if (current_pos > target_value){
               intake_spin.setPower(-0.24);
           }
           else{
               intake_spin.setPower(0.0);
           }
           current_pos = intake_spin.getCurrentPosition();

        }




    }
}
