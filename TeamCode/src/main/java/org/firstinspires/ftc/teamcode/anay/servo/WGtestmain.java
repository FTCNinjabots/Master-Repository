package org.firstinspires.ftc.teamcode.anay.servo;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
@Disabled
@TeleOp(name="Wobble Goal Test")
public class WGtestmain extends LinearOpMode {


    private CRServo rack_servo = null;
    private Servo swivel = null;

    @Override
    public void runOpMode(){
        rack_servo = hardwareMap.get(CRServo.class, "rack_servo");
        swivel = hardwareMap.get(Servo.class, "swivel");

        waitForStart();

        while (opModeIsActive()){
            rack_servo.setPower(0.5);
            sleep(1000);
            swivel.setPosition(0.7);
            rack_servo.setPower(-0.5);
            sleep(1000);

        }

    }
}
