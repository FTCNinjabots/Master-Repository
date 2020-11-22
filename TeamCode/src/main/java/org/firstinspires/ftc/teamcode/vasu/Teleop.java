package org.firstinspires.ftc.teamcode.vasu;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


//first identify what type of code is this, teleop or auto

@TeleOp
public class Teleop extends OpMode {

    //second identify the configured motors of the robot
    //in our case we had four motors

    DcMotor fl;
    DcMotor br;
    DcMotor fr;
    DcMotor bl;

    public void init() {

        //third we right down hardware map
        //hardware map gives the robot the ability to be interactive
        fl = hardwareMap.dcMotor.get("fl");
        br = hardwareMap.dcMotor.get("br");
        fr = hardwareMap.dcMotor.get("fr");
        bl = hardwareMap.dcMotor.get("bl");

    }
    public void loop() {

        //last but not least, we give each motor a joystick pattern
        br.setPower(gamepad1.right_stick_y);
        fl.setPower(-gamepad1.left_stick_y);
        fr.setPower(gamepad1.right_stick_y);
        bl.setPower(-gamepad1.left_stick_y);

    }
}
