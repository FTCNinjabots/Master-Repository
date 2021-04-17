package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class FlickerServo {


    public enum State
    {
        STATE_FLICKER_STOPPED,
        STATE_FLICKER_ELEVATOR,
        STATE_FLICKER_FWD,
        STATE_FLICKER_BACK
    }

    public State flickerState;
    public int maxFlicks;
    private int curFlicks;
    private ElapsedTime timer;
    private Telemetry telemetry;
    private Servo flicker;
    private boolean flicking;
    private boolean nodrop;

    //degrees for going foward starts at 1.0
    private double flickPosition = 0.65; //0.75;
    private double basePosition = 1.0; //1.0;
    private double startPosition = 0.75;

    //time between each flick
    private double fwdTime = 100; // 120
    private double backTime = 450; // 135
    private Elevator elevator;

    public FlickerServo(HardwareMap hardwareMap, Elevator elev, Telemetry tele)
    {
        flicker = hardwareMap.get(Servo.class, "flicker");
        this.telemetry = tele;
        this.flicker.setPosition(this.startPosition);
        this.flickerState = State.STATE_FLICKER_STOPPED;
        this.maxFlicks = 0;
        this.timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        this.elevator = elev;
        this.flicking = false;
        this.nodrop = false;
    }

    public void flick(int times)
    {
        if (this.flickerState != State.STATE_FLICKER_STOPPED)
        {
            return;
        }

        /* Set maxflicks and initialize current flicks to 0 */
        this.maxFlicks = times;
        this.curFlicks = 0;

        /* Lift the elevator */
        this.elevator.lift();
        this.flickerState = State.STATE_FLICKER_ELEVATOR;
        this.flicking = true;
    }

    public void forceFlick(int times)
    {
        /* Set maxflicks and initialize current flicks to 0 */
        this.maxFlicks = times;
        this.curFlicks = 0;
        this.flicking = true;
        this.flickerState = State.STATE_FLICKER_BACK;
        this.flicker.setPosition(this.basePosition);
        this.timer.reset();
    }

    public void stop()
    {
        /* Set the position to 0 and update maxflicks and curflicks to 0 */
        //position backwards
        this.flicker.setPosition(this.basePosition);
        this.maxFlicks = 0;
        this.curFlicks = 0;
        this.flickerState = State.STATE_FLICKER_BACK;
        this.timer.reset();
        this.flicking = false;
    }

    public boolean isStopped()
    {
        return flickerState == State.STATE_FLICKER_STOPPED;
    }

    public void nodrop()
    {
        nodrop = true;
    }

    public void update()
    {
        switch(flickerState){
            case STATE_FLICKER_ELEVATOR:
                if (this.elevator.eleState == Elevator.State.STATE_ELEVATOR_SWIVELED) {
                    /* Set flicker state to FWD and set position */
                    this.flickerState = State.STATE_FLICKER_BACK;
                    this.flicker.setPosition(this.basePosition);
                    this.timer.reset();
                }
            break;

            case STATE_FLICKER_FWD:
                if (this.timer.milliseconds() >= this.fwdTime)
                {
                    if (this.curFlicks != this.maxFlicks)
                    {
                        /* Still have flicks to do - start going forward */
                        this.flicker.setPosition(this.basePosition);
                        this.flickerState = State.STATE_FLICKER_BACK;
                        this.timer.reset();
                    }
                    else
                    {
                        this.maxFlicks = 0;
                        this.curFlicks = 0;
                        this.flickerState = State.STATE_FLICKER_STOPPED;
                        this.flicker.setPosition(this.startPosition);
                    }
                }
            break;

            case STATE_FLICKER_BACK:
                if (this.timer.milliseconds() >= this.backTime)
                {
                    /* Increment current flick count */
                    this.curFlicks = this.curFlicks + 1;

                    /* Move flicker back */
                    this.flickerState = State.STATE_FLICKER_FWD;
                    this.flicker.setPosition(this.flickPosition);
                    this.timer.reset();
                }
            break;

            case STATE_FLICKER_STOPPED:
                /* Flicker is stopped - nothing to do. Move the elevator down */
                if ((this.elevator.eleState == Elevator.State.STATE_ELEVATOR_SWIVELED) && flicking)
                {
                    if (!nodrop) {
                        this.elevator.drop();
                        this.flicking = false;
                    }
                    nodrop = true;
                }
            break;
        }
        this.telemetry.addData("Flicker: ", "State " + flickerState + "Flick " + this.curFlicks + "/" + this.maxFlicks);
    }

}
