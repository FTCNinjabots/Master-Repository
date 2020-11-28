package org.firstinspires.ftc.teamcode.anay.servo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Servo Check", group = "Servo")
public class servocheck extends LinearOpMode {
    Servo s1_test = null;

    @Override
    public void runOpMode() throws InterruptedException {
        s1_test = hardwareMap.get(Servo.class, "s1");
        waitForStart();
        while(opModeIsActive()){
            double pos = s1_test.getPosition();
            telemetry.addData("Servo Position", pos);
            telemetry.update();
        }
    }
}
