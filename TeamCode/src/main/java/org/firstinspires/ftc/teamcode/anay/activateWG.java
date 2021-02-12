package org.firstinspires.ftc.teamcode.anay;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
@TeleOp
public class activateWG extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor wobble = hardwareMap.get(DcMotor.class, "wobble");
        Servo wobble_gate = hardwareMap.get(Servo.class, "wobble_gate");

        waitForStart();
        while (opModeIsActive()) {


            int wobble_target = -300;
            int current_wobble = wobble.getCurrentPosition();
            while (current_wobble > wobble_target) {
                wobble.setPower(-0.35);
                wobble_gate.setPosition(0.0);
                current_wobble = wobble.getCurrentPosition();
            }
            wobble.setPower(0.0);
            sleep(1000000);
        }
    }
}
