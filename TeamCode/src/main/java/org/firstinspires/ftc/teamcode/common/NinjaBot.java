package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class NinjaBot {
    public DriveTrain driveTrain;
    public WobbleMotor wobbleMotor;
    public WobbleGate wobbleGate;
    public Intake intake;
    public IntakeGate intakeGate;
    public Shooter shooter;
    public org.firstinspires.ftc.teamcode.common.Flicker flicker;

    public  NinjaBot(HardwareMap hardwareMap)
    {
        driveTrain = new DriveTrain(hardwareMap);
        wobbleMotor = new WobbleMotor(hardwareMap);
        wobbleGate = new WobbleGate(hardwareMap);
        intake = new Intake(hardwareMap);
        intakeGate = new IntakeGate(hardwareMap);
        shooter = new Shooter(hardwareMap);
        flicker = new org.firstinspires.ftc.teamcode.common.Flicker(hardwareMap, shooter);
    }

    public void init()
    {
        // Initialize the drive train incase it's moved during init
        driveTrain.resetEncoders(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void stop()
    {
        // Stop all motors
        driveTrain.stop();
        intake.stop();
        shooter.stop();
        flicker.stop();
    }

    public void update()
    {
        // Update all robot components
        driveTrain.update();
        wobbleMotor.update();
        wobbleGate.update();
        intakeGate.update();
        flicker.update();
    }
}
