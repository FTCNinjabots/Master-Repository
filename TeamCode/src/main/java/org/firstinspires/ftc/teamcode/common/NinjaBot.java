package org.firstinspires.ftc.teamcode.common;

import android.provider.BlockedNumberContract;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class NinjaBot {
    public DriveTrain driveTrain;
    public WobbleMotor wobbleMotor;
    public WobbleGate wobbleGate;
    public Intake intake;
    public IntakeGate intakeGate;
    public Shooter shooter;
    public FlickerServo flicker;
    public Elevator elevator;
    private Telemetry telemetry;

    public  NinjaBot(HardwareMap hardwareMap, Telemetry tele, boolean haveDriveTrain)
    {
        if (haveDriveTrain)
            driveTrain = new DriveTrain(hardwareMap, tele);
        else
            driveTrain = null;
        wobbleMotor = new WobbleMotor(hardwareMap, tele);
        wobbleGate = new WobbleGate(hardwareMap, tele);
        intake = new Intake(hardwareMap, tele);
        intakeGate = new IntakeGate(hardwareMap, tele);
        shooter = new Shooter(hardwareMap, tele);
        elevator = new Elevator(hardwareMap, tele);
        flicker = new FlickerServo(hardwareMap, elevator, tele);

        this.telemetry = tele;
    }

    public void init()
    {
        // Initialize the drive train incase it's moved during init
        if (driveTrain != null) {
            driveTrain.resetEncoders(DcMotor.RunMode.RUN_TO_POSITION);
        }

        this.intakeGate.lower();
    }

    public void stop()
    {
        // Stop all motors
        if (driveTrain != null) {
            driveTrain.stop();
        }
        intake.stop();
        shooter.stop();
        flicker.stop();
    }

    public void update()
    {
        // Update all robot components
        if (driveTrain != null) {
            driveTrain.update();
        }
        wobbleMotor.update();
        wobbleGate.update();
        intakeGate.update();
        flicker.update();
        elevator.update();
    }
}
