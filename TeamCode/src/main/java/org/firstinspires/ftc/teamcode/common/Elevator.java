package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Elevator {

    /* Class constants */
    public static int elevTime = 1200;
    public static int dropTime = 750;
    public static int swivelTime = 200;
    public static int unswivelTime = swivelTime + 50;
    public static double elevPowerLeft = 0.66;
    public static double elevPowerRight = elevPowerLeft;
    public static double swivelPosition = 0.79;
    public static double swivelBasePosition = 0.8;
    public static double rescuePosition = 0.6;

    public enum State
    {
        STATE_ELEVATOR_TOP,
        STATE_ELEVATOR_BOTTOM,
        STATE_ELEVATOR_LIFTING,
        STATE_ELEVATOR_DROPPING,
        STATE_ELEVATOR_SWIVELING,
        STATE_ELEVATOR_SWIVELED,
        STATE_ELEVATOR_UNSWIVELED,
        STATE_ELEVATOR_IDLE
    }

    public State eleState = State.STATE_ELEVATOR_IDLE;
    private CRServo elevatorServoLeft;
    private CRServo elevatorServoRight;
    private Servo elevSwivel;
    private Telemetry telemetry;
    private ElapsedTime timer;
    private TouchSensor touch;
    private boolean rescue;

    public Elevator(HardwareMap hardwareMap, Telemetry telemetry){
        elevatorServoLeft = hardwareMap.get(CRServo.class, "elevServoLeft");
        elevatorServoRight = hardwareMap.get(CRServo.class, "elevServoRight");
        elevSwivel = hardwareMap.get(Servo.class, "elevSwivel");
        touch = hardwareMap.get(TouchSensor.class, "elevTouch");

        elevatorServoLeft.setDirection(CRServo.Direction.REVERSE);
        this.stopElevator();
        //this.elevSwivel.setPosition(Elevator.swivelBasePosition);

        this.telemetry = telemetry;
        this.timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        this.rescue = false;
    }

    private void stopElevator()
    {
        this.elevatorServoLeft.setPower(0);
        this.elevatorServoRight.setPower(0);
    }

    private void startElevator()
    {
        this.timer.reset();
        switch (eleState)
        {
            case STATE_ELEVATOR_LIFTING:
                elevatorServoLeft.setPower(-1 * Elevator.elevPowerLeft);
                elevatorServoRight.setPower(-1 * Elevator.elevPowerRight);
            break;

            case STATE_ELEVATOR_DROPPING:
                elevatorServoLeft.setPower(Elevator.elevPowerLeft);
                elevatorServoRight.setPower(Elevator.elevPowerRight);
            break;
        }
    }

    public void lift(){
        // Lift elevator for desired amount of time
        eleState = State.STATE_ELEVATOR_LIFTING;
       // this.elevSwivel.setPosition(Elevator.swivelBasePosition);
        this.startElevator();
    }

    public void rescue()
    {
        // Move half way up and swivel all the way
        this.rescue = true;
        this.timer.reset();
        this.lift();
    }

    public void drop(){

        //drop elevator for desired amount of time
        switch(eleState)
        {
            case STATE_ELEVATOR_SWIVELED:
                eleState = State.STATE_ELEVATOR_UNSWIVELED;
              //  this.elevSwivel.setPosition(Elevator.swivelBasePosition);
                this.timer.reset();
            break;
            default:
                eleState = State.STATE_ELEVATOR_DROPPING;
                //this.elevSwivel.setPosition(Elevator.swivelBasePosition);
                this.startElevator();
        }
    }

    public void swivelMore()
    {
        double curPosition = this.elevSwivel.getPosition();

        //this.elevSwivel.setPosition(curPosition - 0.05);
    }

    public void swivelReset()
    {
        //this.elevSwivel.setPosition(Elevator.swivelPosition);
    }

    public void update(){
        //updates state based on idleness
        switch (eleState){
            case STATE_ELEVATOR_TOP:
                eleState = State.STATE_ELEVATOR_SWIVELING;
              //  this.elevSwivel.setPosition(Elevator.swivelPosition);
                this.timer.reset();
            break;
            case STATE_ELEVATOR_BOTTOM:
                this.telemetry.addData("Elevator is stopped", "Time: " + this.timer.milliseconds());
            break;
            case STATE_ELEVATOR_LIFTING:
                /* if (timer.milliseconds() >= Elevator.elevTime) */
                if (this.touch.isPressed())
                {
                    eleState = State.STATE_ELEVATOR_TOP;
                    this.stopElevator();
                }

                if (this.rescue && this.timer.milliseconds() >= 600)
                {
                   // this.elevSwivel.setPosition(Elevator.rescuePosition);
                    eleState = State.STATE_ELEVATOR_IDLE;
                    this.rescue = false;
                }
            break;
            case STATE_ELEVATOR_DROPPING:
                if (timer.milliseconds() >= Elevator.dropTime){
                    eleState = State.STATE_ELEVATOR_BOTTOM;
                    this.stopElevator();
                }
            break;
            case STATE_ELEVATOR_SWIVELING:
                /* Wait for Elevator to complete swiveling */
                if (this.timer.milliseconds() >= Elevator.swivelTime)
                {
                    eleState = State.STATE_ELEVATOR_SWIVELED;
                }
            break;
            case STATE_ELEVATOR_SWIVELED:
                /* On top and swiveled - ready to shoot */
                this.telemetry.addData("Elevator swiveled", "Ready to shoot. Time: " + this.timer.milliseconds());
                this.stopElevator();
            break;
            case STATE_ELEVATOR_UNSWIVELED:
                /* Wait to unswivel and then start dropping */
                if (this.timer.milliseconds() >= Elevator.unswivelTime)
                {
                    eleState = State.STATE_ELEVATOR_DROPPING;
                    this.startElevator();
                }
            break;
        }

        this.telemetry.addData("Elevator State", eleState + "Button: " + this.touch.isPressed());
    }
}
