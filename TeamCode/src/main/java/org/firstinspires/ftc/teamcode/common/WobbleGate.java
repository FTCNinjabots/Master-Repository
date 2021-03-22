package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class WobbleGate {

    public enum State
    {
      STATE_GATE_CLOSING,
      STATE_GATE_CLOSED,
      STATE_GATE_OPENING,
      STATE_GATE_IDLE
    }

    public State state;

    private final Servo wobbleGate;
    private final ElapsedTime timer;
    private final double closeDuration = 150; // Duration in msec to close gate
    private final double openDuration = 150; // Duration in msec to open gate
    private final Telemetry telemetry;

    public WobbleGate(HardwareMap hardwareMap, Telemetry tele)
    {
        this.wobbleGate = hardwareMap.get(Servo.class, "wobble_gate");
        this.timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        this.state = State.STATE_GATE_IDLE;
        this.telemetry = tele;
    }

    public void close()
    {
        this.wobbleGate.setPosition(Servo.MAX_POSITION);
        this.state = State.STATE_GATE_CLOSING;
        this.timer.reset();
    }

    public void open()
    {
        this.wobbleGate.setPosition(Servo.MIN_POSITION);
        this.state = State.STATE_GATE_OPENING;
        this.timer.reset();
    }

    public boolean isOpen()
    {
        return this.state == State.STATE_GATE_IDLE;
    }

    public boolean isClosed()
    {
        return this.state == State.STATE_GATE_CLOSED;
    }

    public void update()
    {
        switch (this.state)
        {
            case STATE_GATE_CLOSING:
                if (this.timer.milliseconds() >= this.closeDuration)
                {
                    this.state = State.STATE_GATE_CLOSED;
                }
                break;
            case STATE_GATE_CLOSED:
                // Nothing to do - keep position to closed
                break;
            case STATE_GATE_OPENING:
                if (this.timer.milliseconds() >= this.openDuration)
                {
                    this.state = State.STATE_GATE_IDLE;
                }
                break;
            case STATE_GATE_IDLE:
                // Opened - keep to min position
                this.wobbleGate.setPosition(Servo.MIN_POSITION);
                break;
        }
    }
}
