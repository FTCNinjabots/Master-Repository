package org.firstinspires.ftc.teamcode.anay.servo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

//@Disabled
@Autonomous(name = "Servo Check", group = "Servo")
public class servocheck extends LinearOpMode {
    Servo s2_test = null;

    @Override
    public void runOpMode() throws InterruptedException {
        s2_test = hardwareMap.get(Servo.class, "s2");
        waitForStart();
        while(opModeIsActive()){
            double pos = s2_test.getPosition();
            telemetry.addData("Servo Position", pos);
            telemetry.update();
        }
    }
}
