package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter {

    public static int countPerRotation = 28; // From Gobilda yellow jacket website

    private final DcMotor shooter;
    public double currentPower;
    private final double defaultPower = 1.0;
    private final Telemetry telemetry;

    public Shooter(HardwareMap hardwareMap, Telemetry tele)
    {
        this.shooter = hardwareMap.get(DcMotor.class, "shooter");
        this.shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       // this.shooter.setDirection(DcMotorSimple.Direction.REVERSE);
        this.currentPower = 0.0;
        this.telemetry = tele;
    }

    public void setShooterPower(double power)
    {
        this.currentPower = power;
        this.shooter.setPower(this.currentPower);
    }

    public void stop()
    {
        this.setShooterPower(0);
    }

    public void start(double power)
    {
        this.setShooterPower(power);
    }

    // Start with default power
    public void start()
    {
        this.start(this.defaultPower);
    }

    public int getCurrentPosition()
    {
        return this.shooter.getCurrentPosition();
    }
}
