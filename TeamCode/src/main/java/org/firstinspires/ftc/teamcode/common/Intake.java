package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {

    private final DcMotor intake;
    private final Telemetry telemetry;
    private final double intakePower = 1.0;

    public Intake(HardwareMap hardwareMap, Telemetry tele)
    {
        // Note: We are not running the intake with encoder
        this.intake = hardwareMap.get(DcMotor.class, "intake");
        this.telemetry = tele;
        //this.intake.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void start()
    {
        this.intake.setPower(intakePower);
    }

    public void stop()
    {
        this.intake.setPower(0.0);
    }
}
