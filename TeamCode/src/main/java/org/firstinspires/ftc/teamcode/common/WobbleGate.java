package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class WobbleGate {

    public enum State
    {
      STATE_GATE_CLOSING,
      STATE_GATE_OPENING,
      STATE_GATE_IDLE
    };
    public State state;

    private CRServo wobbleGate;
    private ElapsedTime timer;
    private double closePower = 1.0;
    private double openPower = 0;
    private double idlePower = 0.5;
    private double closeDuration = 1200; // Duration in msec to close gate
    private double openDuration = 1200; // Duration in msec to open gate

    public WobbleGate(HardwareMap hardwareMap)
    {
        this.wobbleGate = hardwareMap.get(CRServo.class, "wobble_gate");
        this.timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        this.state = State.STATE_GATE_IDLE;
    }

    public void close()
    {
        this.wobbleGate.setPower(this.closePower);
        this.state = State.STATE_GATE_CLOSING;
        this.timer.reset();
    }

    public void open()
    {
        this.wobbleGate.setPower(this.openPower);
        this.state = State.STATE_GATE_OPENING;
        this.timer.reset();
    }

    public void update()
    {
        switch (this.state)
        {
            case STATE_GATE_CLOSING:
                if (this.timer.milliseconds() >= this.closeDuration)
                {
                    this.state = State.STATE_GATE_IDLE;
                }
                break;
            case STATE_GATE_OPENING:
                if (this.timer.milliseconds() >= this.openDuration)
                {
                    this.state = State.STATE_GATE_IDLE;
                }
                break;
            case STATE_GATE_IDLE:
                this.wobbleGate.setPower(this.idlePower);
                break;
        }
    }
}
