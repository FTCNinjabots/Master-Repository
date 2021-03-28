package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class FlickerServo {


    public enum State
    {
        STATE_FLICKER_STOPPED,
        STATE_FLICKER_FLICKING,
        STATE_FLICKER_RESET
    }

    public State flickerState = State.STATE_FLICKER_STOPPED;

    private double servo_pos;

    /*
    private double backPower = 0.75; // 1.0
    private double fwdPower = -0.85; // -0.85
    private double partialPower = -0.5;
    private double backDuration = 200; // 300 Duration to wait in msec going back (GOING_BACK)
    private double fwdDuration = 140; // 145 Duration to wait in msec going forward (GOING_FWD)
    private double fwdPartial = 50; // Move forward partial
    private double idleDuration = 300; // Duration to wait for flywheel to expand
    private double shooterEncoderRotations = Shooter.countPerRotation * 70;
    private double currentPower;
    private int shooterEncoderPosition;
    */

    private int maxFlicks;
    private ElapsedTime timer;
    private Telemetry telemetry;
    private Servo flicker;

    public FlickerServo(HardwareMap hardwareMap, Telemetry tele)
    {
        flicker = hardwareMap.get(Servo.class, "flicker");
        this.telemetry = tele;
    }



    public void flick(int times)
    {
        int curFlicks = 0;

        // Convert max flicks to degrees, current flicks to 0

        if (flickerState != State.STATE_FLICKER_STOPPED){
            return;
        }

        switch (times){
            case 1:
                servo_pos = 0.66;
            case 2:
                servo_pos = 0.33;
            default:
                servo_pos = 0.0;

        }
        flicker.setPosition(servo_pos);
        flickerState = State.STATE_FLICKER_FLICKING;



    }

    public void stop()
    {

    }

    public boolean isStopped()
    {
        /* Shooter is stopped if the following conditions are met:
         *
         * (1) Max flicks is set to 0 (set in stop or when curFlocks == maxFlicks)
         * (2) Flicker has moved to the STOPPED state (all the way back)
         */


        return flickerState == State.STATE_FLICKER_STOPPED;
    }

    // Push first ring partial


    public void update()
    {
        switch(flickerState){
            case STATE_FLICKER_FLICKING:
                if (flicker.getPosition() == servo_pos)
                {
                    // Stop the flicker
                    servo_pos = 1.0;
                    flicker.setPosition(servo_pos);
                    flickerState = State.STATE_FLICKER_RESET;
                }
            case STATE_FLICKER_RESET:
                if (flicker.getPosition() == servo_pos){
                    flickerState = State.STATE_FLICKER_STOPPED;
                }
        }
        this.telemetry.addData("Flicker State: ", flickerState);
    }

    private void setPower(double power)
    {
    }
}
