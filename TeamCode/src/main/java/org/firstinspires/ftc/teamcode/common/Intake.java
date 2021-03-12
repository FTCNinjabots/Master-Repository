package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {

    private DcMotor intake;
    private Telemetry telemetry;

    public Intake(HardwareMap hardwareMap, Telemetry tele)
    {
        // Note: We are not running the intake with encoder
        this.intake = hardwareMap.get(DcMotor.class, "intake");
        this.telemetry = tele;
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