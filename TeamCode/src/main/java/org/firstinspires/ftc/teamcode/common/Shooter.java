package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Shooter {

    private DcMotor shooter;
    private double currentPower;

    public Shooter(HardwareMap hardwareMap)
    {
        this.shooter = hardwareMap.get(DcMotor.class, "shooter");
        this.shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.currentPower = 0.0;
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
