package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

public class WobbleMotor {

    public enum State
    {
        STATE_WOBBLE_IDLE,
        STATE_WOBBLE_GOING_DOWN,
        STATE_WOBBLE_GOING_DOWN_PHASE1,
        STATE_WOBBLE_GOING_DOWN_PHASE2,
        STATE_WOBBLE_GOING_UP,
        STATE_WOBBLE_GOING_UP_PHASE1
    };
    public State state;

    private DcMotor wobble;
    private double curPos;
    private double wobblePowerDown = -0.45;
    private double wobblePowerRaise = 0.8;
    private double targetDownPhase1 = -420;
    private double targetDownPhase2 = -480;
    private double targetDown = -500;
    private double targetUpPhase1 = 100;
    private double targetUp = 10;
    private double currentPower;

    public WobbleMotor(HardwareMap hardwareMap)
    {
        this.wobble = hardwareMap.get(DcMotor.class, "wobble");
        this.wobble.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.wobble.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.curPos = wobble.getCurrentPosition();
        this.state = State.STATE_WOBBLE_IDLE;
    }

    private void setPower(double power)
    {
        this.currentPower = power;
        this.wobble.setPower(this.currentPower);
    }

    public void down()
    {
        this.state = State.STATE_WOBBLE_GOING_DOWN;
        this.setPower(this.wobblePowerDown);
    }

    public void raise()
    {
        this.state = State.STATE_WOBBLE_GOING_UP;
        this.wobble.setPower(this.wobblePowerRaise);
    }

    public void update()
    {
        int currentPosition;

        // TODO: Check if wobble goal is moving down/up and has reached target. Also perform
        // non-linear motion to reduce speed on wobble goal towards the end.
        switch(this.state) {
            case STATE_WOBBLE_GOING_DOWN:
                currentPosition = this.wobble.getCurrentPosition();
                telemetry.addData("WobbleMotor: ", "Going Down: " + currentPosition);
                telemetry.update();
                if (currentPosition <= this.targetDownPhase1) {
                    this.state = State.STATE_WOBBLE_GOING_DOWN_PHASE1;
                    this.setPower(this.currentPower + 0.05);
                }
                break;
            case STATE_WOBBLE_GOING_DOWN_PHASE1:
                currentPosition = this.wobble.getCurrentPosition();
                telemetry.addData("WobbleMotor: ", "Down Phase1: " + currentPosition);
                telemetry.update();
                if (currentPosition <= this.targetDownPhase2) {
                    this.state = State.STATE_WOBBLE_GOING_DOWN_PHASE2;
                    this.setPower(this.currentPower + 0.15);
                }
                break;
            case STATE_WOBBLE_GOING_DOWN_PHASE2:
                currentPosition = this.wobble.getCurrentPosition();
                telemetry.addData("WobbleMotor: ", "Down Phase2: " + currentPosition);
                telemetry.update();
                if (currentPosition <= this.targetDown) {
                    this.state = State.STATE_WOBBLE_IDLE;
                }
                break;
            case STATE_WOBBLE_GOING_UP:
                currentPosition = this.wobble.getCurrentPosition();
                telemetry.addData("WobbleMotor: ", "Going Up: " + currentPosition);
                telemetry.update();
                if (currentPosition >= this.targetUpPhase1) {
                    this.state = State.STATE_WOBBLE_GOING_UP_PHASE1;
                    this.setPower(this.currentPower - 0.15);
                }
                break;
            case STATE_WOBBLE_GOING_UP_PHASE1:
                currentPosition = this.wobble.getCurrentPosition();
                telemetry.addData("WobbleMotor: ", "Up Phase1: " + currentPosition);
                telemetry.update();
                if (currentPosition >= this.targetUp) {
                    this.state = State.STATE_WOBBLE_IDLE;
                }
                break;
            case STATE_WOBBLE_IDLE:
                currentPosition = this.wobble.getCurrentPosition();
                telemetry.addData("WobbleMotor: ", "Idle: " + currentPosition);
                telemetry.update();
                this.setPower(0.0);
                break;
        }
    }
}
