package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

public class Flicker {

    public enum State
    {
        STATE_FLICKER_STOPPED,
        STATE_FLICKER_GOING_BACK,
        STATE_FLICKER_BACK,
        STATE_FLICKER_PARTIAL,
        STATE_FLICKER_GOING_FORWARD,
        STATE_FLICKER_FORWARD
    }

    public State flickerState;

    private DcMotor flicker;
    private Shooter shooter;
    private double backPower = 0.5; // 1.0
    private double fwdPower = -0.80; // -0.85
    private double partialPower = -0.5;
    private double backDuration = 175; // 300 Duration to wait in msec going back (GOING_BACK)
    private double fwdDuration = 125; // 145 Duration to wait in msec going forward (GOING_FWD)
    private double fwdPartial = 50; // Move forward partial
    private double idleDuration = 300; // Duration to wait for flywheel to expand
    private double shooterEncoderRotations = Shooter.countPerRotation * 70;
    private double currentPower;
    private int shooterEncoderPosition;
    private int curFlicks;
    private int maxFlicks;
    private ElapsedTime timer;
    private Telemetry telemetry;

    public Flicker(HardwareMap hardwareMap, Shooter robotShooter, Telemetry tele)
    {
        this.flicker = hardwareMap.get(DcMotor.class, "flicker");
        this.flicker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.flicker.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.shooter = robotShooter;
        this.maxFlicks = 0;
        this.curFlicks = 0;
        this.timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        this.setPower(0.0);
        this.flickerState = State.STATE_FLICKER_GOING_BACK;
        this.telemetry = tele;
    }

    public void flick(int times)
    {
        this.maxFlicks = times;
        this.curFlicks = 0;
        this.timer.reset();
        if (this.flickerState == State.STATE_FLICKER_STOPPED)
        {
            this.flickerState = State.STATE_FLICKER_GOING_FORWARD;
            this.setPower(this.fwdPower);
            this.curFlicks = 1;
        }
        else if (this.flickerState != State.STATE_FLICKER_GOING_BACK)
        {
            this.flickerState = State.STATE_FLICKER_GOING_BACK;
            this.setPower(this.backPower);
        }
    }

    public void stop()
    {
        // Stopping the flicker always makes it go to the back position first
        if (this.flickerState != State.STATE_FLICKER_STOPPED) {
            this.flickerState = State.STATE_FLICKER_GOING_BACK;
            this.maxFlicks = 0;
            this.curFlicks = 0;
            this.setPower(this.backPower);
            this.timer.reset();
            this.shooterEncoderPosition = this.shooter.getCurrentPosition();
        }
    }

    public boolean isStopped()
    {
        /* Shooter is stopped if the following conditions are met:
         *
         * (1) Max flicks is set to 0 (set in stop or when curFlocks == maxFlicks)
         * (2) Flicker has moved to the STOPPED state (all the way back)
         */
        return ((this.maxFlicks == 0) && (this.flickerState == State.STATE_FLICKER_STOPPED));
    }

    // Push first ring partial
    public void push_partial()
    {
       if (this.flickerState == State.STATE_FLICKER_STOPPED)
        {
            this.flickerState = State.STATE_FLICKER_PARTIAL;
            this.timer.reset();
            this.shooterEncoderPosition = this.shooter.getCurrentPosition();
            this.setPower(this.partialPower);
        }
    }

    public void update()
    {
        int shooterPosition;

        // Depending on current flicker state move to next proposed state
        switch(this.flickerState)
        {
            case STATE_FLICKER_STOPPED:
                // Flicker is stopped so nothing to do
                this.telemetry.addData("Flicker: ", "Stopped: ");
                break;
            case STATE_FLICKER_GOING_BACK:
                if (this.timer.milliseconds() >= this.backDuration)
                { // Reached back
                    this.setPower(0.0);
                    this.timer.reset();
                    this.flickerState = State.STATE_FLICKER_BACK;
                }
                else
                {
                    // Still waiting for the flicker to be going back
                    this.telemetry.addData("Flicker: ", "Going Back: " + this.curFlicks +
                                        "/" + this.maxFlicks);
                }
                break;
            case STATE_FLICKER_BACK:
                // If currently flicking see if sufficient time has passed to start going fwd
                shooterPosition = this.shooter.getCurrentPosition();

                if ((shooterPosition - this.shooterEncoderPosition) > this.shooterEncoderRotations)
                {
                    if (this.maxFlicks != 0) {
                        // Start moving flicker forward as sufficient time has passed
                        this.timer.reset();
                        this.setPower(this.fwdPower);
                        this.flickerState = State.STATE_FLICKER_GOING_FORWARD;

                        // Bump up flick count
                        this.curFlicks = this.curFlicks + 1;
                    }
                    else
                    {
                        this.timer.reset();
                        this.setPower(0.0);
                        this.flickerState = State.STATE_FLICKER_STOPPED;
                    }
                }
                else
                {
                    // Keep waiting till flywheel has expanded (idleDuration)
                    this.telemetry.addData("Flicker: ", "Back: " + this.curFlicks +
                            "/" + this.maxFlicks);
                }
                break;
            case STATE_FLICKER_PARTIAL:
                if (this.timer.milliseconds() >= this.fwdPartial)
                {  // Reached partial so move to STOPPED
                    this.timer.reset();
                    this.setPower(0.0);
                    this.flickerState = State.STATE_FLICKER_STOPPED;
                }
                break;
            case STATE_FLICKER_GOING_FORWARD:
                // See if sufficient has passed to start going forward
                if (this.timer.milliseconds() >= this.fwdDuration)
                {
                    // Forward position reached - should have flicked a ring
                    this.setPower(0.0);
                    this.timer.reset();
                    this.flickerState = State.STATE_FLICKER_FORWARD;
                }
                else
                {
                    // Keep waiting till flicker has reached forward position
                    this.telemetry.addData("Flicker: ", "Going Forward: " + this.curFlicks +
                            "/" + this.maxFlicks);
                }
                break;
            case STATE_FLICKER_FORWARD:
                // If curFlicks == maxFlicks then we are done else continue shooting and move
                // flicker backwards
                if (this.curFlicks >= this.maxFlicks)
                {
                    this.stop();
                }
                else
                {
                    this.timer.reset();
                    this.setPower(this.backPower);
                    this.flickerState = State.STATE_FLICKER_GOING_BACK;
                    this.shooterEncoderPosition = this.shooter.getCurrentPosition();
                }
                break;
        }
    }

    private void setPower(double power)
    {
        this.currentPower = power;
        this.flicker.setPower(this.currentPower);
    }
}
