package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter {

    private DcMotor shooter;
    private double currentPower;
    private Telemetry telemetry;

    public Shooter(HardwareMap hardwareMap, Telemetry tele)
    {
        this.shooter = hardwareMap.get(DcMotor.class, "shooter");
        this.shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.currentPower = 0.0;
        this.telemetry = tele;
    }

    public void setPower(double power)
    {
        this.currentPower = power;
        this.shooter.setPower(this.currentPower);
    }

    public void stop()
    {
        this.setPower(0);
    }

    public void start()
    {
        this.setPower(1.0);
    }
}
