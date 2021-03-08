package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

public class Flicker {

    private enum State
    {
        STATE_FLICKER_STOPPED,
        STATE_FLICKER_GOING_BACK,
        STATE_FLICKER_BACK,
        STATE_FLICKER_GOING_FORWARD,
        STATE_FLICKER_FORWARD
    };

    private DcMotor flicker;
    private Shooter shooter;
    private double backPower = 1.0;
    private double fwdPower = -0.85;
    private double backDuration = 300; // Duration to wait in msec going back (GOING_BACK)
    private double fwdDuration = 200; // Duration to wait in msec going forward (GOING_FWD)
    private double idleDuration = 300; // Duration to wait for flywheel to expand
    private double currentPower;
    private int maxFlicks;
    private int curFlicks;
    private State flickerState;
    private ElapsedTime timer;

    public Flicker(HardwareMap hardwareMap, Shooter robotShooter)
    {
        this.flicker = hardwareMap.get(DcMotor.class, "flicker");
        this.flicker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.flicker.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.shooter = robotShooter;
        this.maxFlicks = 0;
        this.curFlicks = 0;
        this.timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        this.setPower(0.0);
        this.flickerState = State.STATE_FLICKER_STOPPED;
    }

    public void flick(int times)
    {
        this.maxFlicks = times;
        this.curFlicks = 0;
        this.flickerState = State.STATE_FLICKER_GOING_BACK;
        this.timer.reset();
        this.setPower(this.backPower);
    }

    public void stop()
    {
        // Stopping the flicker always makes it go to the back position first
        this.flickerState = State.STATE_FLICKER_GOING_BACK;
        this.maxFlicks = 0;
        this.curFlicks = 0;
        this.setPower(this.backPower);
        this.timer.reset();
    }

    public void update()
    {
        // Depending on current flicker state move to next proposed state
        switch(this.flickerState)
        {
            case STATE_FLICKER_STOPPED:
                // Flicker is stopped so nothing to do
                telemetry.addData("Flicker: ", "Stopped: ");
                telemetry.update();
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
                    telemetry.addData("Flicker: ", "Going Back: " + this.curFlicks +
                                        "/" + this.maxFlicks);
                    telemetry.update();
                }
                break;
            case STATE_FLICKER_BACK:
                // If currently flicking see if sufficient time has passed to start going fwd
                // TODO: Read shooter encoder speed to flick forward again
                if (this.timer.milliseconds() >= this.idleDuration)
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
                    telemetry.addData("Flicker: ", "Back: " + this.curFlicks +
                            "/" + this.maxFlicks);
                    telemetry.update();
                }
                break;
            case STATE_FLICKER_GOING_FORWARD:
                // See if sufficient has passed to start going forward
                if (this.timer.milliseconds() >= this.fwdDuration)
                {
                    // Foward position reached - should have flicked a ring
                    this.setPower(0.0);
                    this.timer.reset();
                    this.flickerState = State.STATE_FLICKER_FORWARD;
                }
                else
                {
                    // Keep waiting till flicker has reached forward position
                    telemetry.addData("Flicker: ", "Going Forward: " + this.curFlicks +
                            "/" + this.maxFlicks);
                    telemetry.update();
                }
                break;
            case STATE_FLICKER_FORWARD:
                // If curFlicks == maxFlicks then we are done else continue shooting and move
                // flicker backwards
                if (this.curFlicks == this.maxFlicks)
                {
                    this.stop();
                }
                else
                {
                    this.timer.reset();
                    this.setPower(this.backPower);
                    this.flickerState = State.STATE_FLICKER_GOING_BACK;
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
