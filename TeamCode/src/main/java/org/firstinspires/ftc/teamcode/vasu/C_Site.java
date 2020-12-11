package org.firstinspires.ftc.teamcode.vasu;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Disabled

@Autonomous(name="CS", group="CS")

public class C_Site extends LinearOpMode {

    private DcMotor bl = null;
    private DcMotor br = null;
    private DcMotor fl = null;
    private DcMotor fr = null;

    @Override

    public void runOpMode() {

        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");

        waitForStart();

        br.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);

        while (opModeIsActive()) {

            bl.setTargetPosition(1620);
            br.setTargetPosition(1620);
            fl.setTargetPosition(1620);
            fr.setTargetPosition(1620);

            sleep(700);

            bl.setTargetPosition(-1620);
            br.setTargetPosition(-1620);
            fl.setTargetPosition(-1620);
            fr.setTargetPosition(-1620);
        }
    }
}