package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static java.lang.Thread.sleep;
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
    }

    public State state;

    private final DcMotor wobble;
    private final double curPos;
    private final double wobblePowerDown = -0.3;
    private final double wobblePowerRaise = 0.5;
    private final ElapsedTime timer;
    private double currentPower;
    private final Telemetry telemetry;
    private final int UpTime = 500;
    private final int DownTime = 300;

    public WobbleMotor(HardwareMap hardwareMap, Telemetry tele)
    {
        this.wobble = hardwareMap.get(DcMotor.class, "wobble");
        this.wobble.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.wobble.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.curPos = wobble.getCurrentPosition();
        this.state = State.STATE_WOBBLE_IDLE;
        this.telemetry = tele;
        this.wobble.setDirection(DcMotorSimple.Direction.REVERSE);
        this.timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    private void setPower(double power)
    {
        this.wobble.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.wobble.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.currentPower = power;
        this.wobble.setPower(this.currentPower);
        this.timer.reset();
    }

    public void stop()
    {
        this.setPower(0);
        this.state = State.STATE_WOBBLE_IDLE;
    }

    public void down()
    {
        if (this.state != State.STATE_WOBBLE_IDLE)
            return;
        this.timer.reset();
        this.state = State.STATE_WOBBLE_GOING_DOWN;
        this.setPower(this.wobblePowerDown);
    }

    public void raise()
    {
        if (this.state != State.STATE_WOBBLE_IDLE)
            return;
        this.timer.reset();
        this.state = State.STATE_WOBBLE_GOING_UP;
        this.setPower(this.wobblePowerRaise);
    }

    public void update()
    {

        switch (this.state){
            case STATE_WOBBLE_GOING_UP:
                if (this.timer.milliseconds() > UpTime){
                    stop();                }
                break;

            case STATE_WOBBLE_GOING_DOWN:
                if (this.timer.milliseconds() > DownTime){
                    stop();

                }
                break;
        }
        this.telemetry.addData("Timer: ", timer.milliseconds());
        this.telemetry.update();


        //Old Code

        /*

        int currentPosition;

        // TODO: Check if wobble goal is moving down/up and has reached target. Also perform
        // non-linear motion to reduce speed on wobble goal towards the end.
        switch(this.state) {
            case STATE_WOBBLE_GOING_DOWN:
                currentPosition = this.wobble.getCurrentPosition();
                this.telemetry.addData("WobbleMotor: ", "Going Down: " + currentPosition);
                if (this.timer.milliseconds() > 300)
                {
                    this.state = State.STATE_WOBBLE_GOING_DOWN_PHASE1;
                    this.setPower(this.currentPower + 0.15);
                }
                break;
            case STATE_WOBBLE_GOING_DOWN_PHASE1:
                currentPosition = this.wobble.getCurrentPosition();
                this.telemetry.addData("WobbleMotor: ", "Down Phase1: " + currentPosition);
                if (this.timer.milliseconds() > 200)
                {
                    this.state = State.STATE_WOBBLE_GOING_DOWN_PHASE2;
                    this.setPower(this.currentPower + 0.15);
                }
                break;
            case STATE_WOBBLE_GOING_DOWN_PHASE2:
                currentPosition = this.wobble.getCurrentPosition();
                this.telemetry.addData("WobbleMotor: ", "Down Phase2: " + currentPosition);
                if (this.timer.milliseconds() > 50)
                {
                    this.state = State.STATE_WOBBLE_IDLE;
                }
                break;
            case STATE_WOBBLE_GOING_UP:
                currentPosition = this.wobble.getCurrentPosition();
                this.telemetry.addData("WobbleMotor: ", "Going Up: " + currentPosition);
                if (this.timer.milliseconds() >= 200)
                {
                    this.state = State.STATE_WOBBLE_GOING_UP_PHASE1;
                    this.setPower(this.currentPower - 0.15);
                }
                break;
            case STATE_WOBBLE_GOING_UP_PHASE1:
                currentPosition = this.wobble.getCurrentPosition();
                this.telemetry.addData("WobbleMotor: ", "Up Phase1: " + currentPosition);
                if (this.timer.milliseconds() >= 200)
                {
                    this.state = State.STATE_WOBBLE_IDLE;
                }
                break;
            case STATE_WOBBLE_IDLE:
                currentPosition = this.wobble.getCurrentPosition();
                this.telemetry.addData("WobbleMotor: ", "Idle: " + currentPosition);
                this.setPower(0.0);
                break;
        }
        */
    }
}
