package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.NinjaBot;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

@Config
@TeleOp(name="TeleOp4Real")
public class Teleop extends OpMode {

    private NinjaBot ninjabot;
    private ElapsedTime timer;
    private int strafeIncrement = 100;
    private int driveIncrement = 100;

    public Teleop()
    {
        ninjabot = new NinjaBot(hardwareMap);
        timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        ElapsedTime currentTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    @Override
    public void init()
    {
        // Init ninjabot
        ninjabot.init();
    }

    @Override
    public void init_loop()
    {
        // Reset drivetrain incase it is moved during init
        ninjabot.driveTrain.resetEncoders(DcMotor.RunMode.RUN_TO_POSITION);
    }

    @Override
    public void start()
    {

    }

    @Override
    public void loop() {
        // Strafing is controlled by gamedpad 1 left trigger
        if (gamepad1.left_trigger > 0.5) {
            // Strafe left
            ninjabot.driveTrain.strafeLeft(strafeIncrement, gamepad1.left_trigger);
        } else if (gamepad1.right_trigger > 0.5) {
            // Strafe right
            ninjabot.driveTrain.strafeRight(strafeIncrement, gamepad1.right_trigger);
        } else if ((gamepad1.left_stick_y != 0) || gamepad1.right_stick_y != 0) {
            // Moving?
            ninjabot.driveTrain.updatePosition(Math.round(-1 * gamepad1.left_stick_y * driveIncrement),
                    Math.round(-1 * gamepad1.left_stick_x * driveIncrement),
                    Math.round(-1 * gamepad1.left_stick_x * driveIncrement),
                    Math.round(-1 * gamepad1.left_stick_y * driveIncrement));
            ninjabot.driveTrain.setPower(-1 * gamepad1.left_stick_y,
                    -1 * gamepad1.left_stick_x);
        } else {
            ninjabot.driveTrain.stop();
        }

        // Wobble motor
        if (gamepad1.left_bumper)
        {
            ninjabot.wobbleMotor.raise();
        }
        else if (gamepad1.right_bumper)
        {
            ninjabot.wobbleMotor.down();
        }

        // Wobble gate
        if (gamepad1.x)
        {
            ninjabot.wobbleGate.close();
        }
        else if (gamepad1.b)
        {
            ninjabot.wobbleGate.open();
        }

        // Intake servo
        if (gamepad1.dpad_left)
        {
            ninjabot.intakeGate.lower();
        }
        else
        {
            ninjabot.intakeGate.raise();
        }

        // Intake
        if (gamepad2.a)
        {
            ninjabot.intake.start();
        }
        else if (gamepad2.y)
        {
            ninjabot.intake.stop();
        }

        // Flicker
        if (gamepad2.x)
        {
            ninjabot.flicker.flick(4);
        }
        else if (gamepad2.b)
        {
            ninjabot.flicker.stop();
        }

        // Flicker manual
        if (gamepad2.left_bumper)
        {
            ninjabot.flicker.flick(1);
        }

        // Shooter
        if (gamepad2.dpad_up)
        {
            ninjabot.shooter.start();
        }
        else if (gamepad2.dpad_down)
        {
            ninjabot.shooter.stop();
        }

        // Update ninjabot
        ninjabot.update();
    }

    @Override
    public void stop()
    {
        // Stop the bot
        ninjabot.stop();
    }
}
