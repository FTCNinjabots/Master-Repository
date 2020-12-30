package org.firstinspires.ftc.teamcode.anay;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Run Dc Motor", group = "DcMotor")
public class Dcmotor extends LinearOpMode {

    public DcMotor Motor = null;
    public double power = -1.0;
    @Override
    public void runOpMode() throws InterruptedException {
        Motor = hardwareMap.get(DcMotor.class, "test");//This needs to be changed to the name of whatever port the motor is on
        waitForStart();
        while (opModeIsActive()){
            Motor.setPower(power);

        }
    }
}
