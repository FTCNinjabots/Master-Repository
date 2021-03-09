package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeGate {

    private enum State
    {
        STATE_GATE_UNKNOWN,
        STATE_GATE_LOWER,
        STATE_GATE_RAISE,
        STATE_GATE_MID
    };

    private CRServo intakeGate;
    private ElapsedTime timer;
    private boolean moving;
    private double moveDuration = 5000;  // Time taken to lower/raise intake in milliseconds
    private double lowerGatePower = 1.0;
    private double raiseGatePower = -1.0;
    private double midGatePower = 0.0;
    private Telemetry telemetry;
    private State state;

    public IntakeGate(HardwareMap hardwareMap, Telemetry tele)
    {
        this.intakeGate = hardwareMap.get(CRServo.class, "intake_servo");
        this.timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        this.moving = false;
        this.telemetry = tele;
        this.state = State.STATE_GATE_UNKNOWN;
    }

    public void lower()
    {
        this.intakeGate.setPower(this.lowerGatePower);
        this.timer.reset();
        this.moving = true;
        this.state = State.STATE_GATE_LOWER;
    }

    public void raise()
    {
        this.intakeGate.setPower(this.raiseGatePower);
        this.timer.reset();
        this.moving = true;
        this.state = State.STATE_GATE_RAISE;
    }

    public void mid()
    {
        this.intakeGate.setPower(this.midGatePower);
        this.timer.reset();
        this.moving = true;
        this.state = State.STATE_GATE_MID;
    }

    public void update()
    {
        if (this.moving)
        {
            if (this.timer.milliseconds() >= this.moveDuration)
            {
                this.moving = false;
            }
        }

        this.telemetry.addData("Intake_Gate:", this.state.toString());
    }
}
