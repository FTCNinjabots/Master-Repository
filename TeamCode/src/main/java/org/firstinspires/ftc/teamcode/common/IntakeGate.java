package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeGate {

    private CRServo intakeGate;
    private ElapsedTime timer;
    private boolean moving;
    private double moveDuration = 1200;  // Time taken to lower/raise intake in milliseconds
    private Telemetry telemetry;

    public IntakeGate(HardwareMap hardwareMap, Telemetry tele)
    {
        this.intakeGate = hardwareMap.get(CRServo.class, "intake_servo");
        this.timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        this.moving = false;
        this.telemetry = tele;
    }

    public void lower()
    {
        this.intakeGate.setPower(1.0);
        this.timer.reset();
        this.moving = true;
    }

    public void raise()
    {
        this.intakeGate.setPower(-1.0);
        this.timer.reset();
        this.moving = true;
    }

    public void update()
    {
        if (this.moving)
        {
            if (this.timer.milliseconds() > this.moveDuration)
            {
                this.intakeGate.setPower(0.0);
                this.moving = false;
            }
        }
    }
}
