package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Elevator {
    public enum State
    {
        STATE_ELEVATOR_TOP,
        STATE_ELEVATOR_BOTTOM,
        STATE_ELEVATOR_LIFTING,
        STATE_ELEVATOR_DROPPING,
        STATE_ELEVATOR_IDLE
    }

    public State eleState = State.STATE_ELEVATOR_IDLE;
    private CRServo elevatorServoLeft;
    private CRServo elevatorServoRight;

    private Telemetry telemetry;
    private ElapsedTime timer;
    private int elevTime = 1500;
    private int dropTime = 600;
    private double elevPower = 1.0;
    private double dropPower = -1.0;

    private void stopElevator()
    {
        this.elevatorServoLeft.setPower(0);
        this.elevatorServoRight.setPower(0);
    }

    private void startElevator()
    {
        switch (eleState)
        {
            case STATE_ELEVATOR_LIFTING:
                elevatorServoLeft.setPower(elevPower);
                elevatorServoRight.setPower(elevPower);
                break;

            case STATE_ELEVATOR_DROPPING:
                elevatorServoLeft.setPower(dropPower);
                elevatorServoRight.setPower(dropPower);
                break;
        }
    }

    public Elevator(HardwareMap hardwareMap, Telemetry telemetry){
        elevatorServoLeft = hardwareMap.get(CRServo.class, "elevServoLeft");
        elevatorServoRight = hardwareMap.get(CRServo.class, "elevServoRight");
        elevatorServoLeft.setDirection(CRServo.Direction.REVERSE);

        this.telemetry = telemetry;
        this.timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    public void lift(){
        // Lift elevator for desired amount of time
        this.timer.reset();
        eleState = State.STATE_ELEVATOR_LIFTING;
        this.startElevator();
    }

    public void drop(){
        //drop elevator for desired amount of time
        this.timer.reset();
        eleState = State.STATE_ELEVATOR_DROPPING;
        this.startElevator();
    }

    public void update(){
        //updates state based on idleness
        switch (eleState){
            case STATE_ELEVATOR_TOP:
                this.stopElevator();
            break;
            case STATE_ELEVATOR_BOTTOM:
                this.stopElevator();
            break;
            case STATE_ELEVATOR_LIFTING:
                if (timer.milliseconds() >= elevTime){
                    eleState = State.STATE_ELEVATOR_TOP;
                }
            break;
            case STATE_ELEVATOR_DROPPING:
                if (timer.milliseconds() >= dropTime){
                    eleState = State.STATE_ELEVATOR_BOTTOM;
                }
            break;
        }
    }
}
