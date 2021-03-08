package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {

    private DcMotor intake; //hardwareMap.get(DcMotor.class, "intake");

    public Intake(HardwareMap hardwareMap)
    {
        this.intake = hardwareMap.get(DcMotor.class, "intake");
        // Note: We are not running the intake with encoder
    }

    public void start()
    {
        this.intake.setPower(1.0);
    }

    public void stop()
    {
        this.intake.setPower(0.0);
    }
}
