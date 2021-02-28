package org.firstinspires.ftc.teamcode.anay;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
@Disabled
@TeleOp(name = "Run Dc Motor", group = "DcMotor")
public class Dcmotor extends LinearOpMode {


    public DcMotor Motor = null;
    public double power = 0.64 ;
    @Override
    public void runOpMode() throws InterruptedException {
        Motor = hardwareMap.get(DcMotor.class, "shooter");//This needs to be changed to the name of whatever port the motor is on
        Motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        while (opModeIsActive()){
            Motor.setPower(power);

        }
    }
}
