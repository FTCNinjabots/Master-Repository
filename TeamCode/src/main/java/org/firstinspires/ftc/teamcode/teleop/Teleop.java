package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.Flicker;
import org.firstinspires.ftc.teamcode.common.NinjaBot;
import org.firstinspires.ftc.teamcode.common.WobbleGate;

import static java.lang.Thread.sleep;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

@Config
@TeleOp(name="TeleOp4Real")
public class Teleop extends OpMode {

    private NinjaBot ninjabot;
    private ElapsedTime timer;
    public static int strafeIncrement = 100;
    public static int driveIncrement = 100;
    public static double shooterControl = 0.6;
    public static double powerShotPower = 0.75;
    private boolean strafing = false;
    private FtcDashboard dashboard;
    private TelemetryPacket packet;

    public Teleop()
    {

    }

    @Override
    public void init()
    {
       // this.dashboard = FtcDashboard.getInstance();
      //  this.dashboard.setTelemetryTransmissionInterval(25);
      //  this.packet = new TelemetryPacket();
       // telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        ninjabot = new NinjaBot(hardwareMap, telemetry);
        timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        ElapsedTime currentTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
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
        if ((gamepad1.left_trigger > 0.5) || (gamepad2.left_trigger > 0.5))
        {  // Strafe left
            if (gamepad2.left_trigger > 0.5)
            {
                ninjabot.driveTrain.strafeLeft(strafeIncrement, this.shooterControl * gamepad2.left_trigger);
            }
            else
            {
                ninjabot.driveTrain.strafeLeft(strafeIncrement, gamepad1.left_trigger);
            }
            this.strafing = true;
        }
        else if ((gamepad1.right_trigger > 0.5)  || (gamepad2.right_trigger > 0.5))
        {
            // Strafe right
            if (gamepad2.right_trigger > 0.5) {
                ninjabot.driveTrain.strafeRight(strafeIncrement, this.shooterControl * gamepad2.right_trigger);
            }
            else
            {
                ninjabot.driveTrain.strafeRight(strafeIncrement, gamepad1.right_trigger);
            }

            this.strafing = true;
        }

        if ((gamepad2.left_stick_y != 0) || gamepad2.right_stick_y != 0)
        {  // Moving with left/right joy stick
            if (strafing)
            {
                ninjabot.driveTrain.stop();
                ninjabot.driveTrain.setPosition(ninjabot.driveTrain.getFLPostion(),
                        ninjabot.driveTrain.getFRPosition(),
                        ninjabot.driveTrain.getBRPosition(),
                        ninjabot.driveTrain.getBLPosition());
            }

            ninjabot.driveTrain.updatePosition((int) Math.round(-1 * this.shooterControl * gamepad2.left_stick_y * driveIncrement),
                    (int) Math.round(-1 * this.shooterControl * gamepad2.right_stick_y * driveIncrement),
                    (int) Math.round(-1 * this.shooterControl * gamepad2.right_stick_y * driveIncrement),
                    (int) Math.round(-1 * this.shooterControl * gamepad2.left_stick_y * driveIncrement));
            ninjabot.driveTrain.setPower(-1 * gamepad2.left_stick_y,
                    -1 * gamepad2.right_stick_y);

            // No longer strafing
            this.strafing = false;
        }
        else if ((gamepad1.left_stick_y != 0) || gamepad1.right_stick_y != 0)
        {  // Moving with left/right joy stick
            if (strafing)
            { // Previously strafing so stop the robot and load the current position before updating
                ninjabot.driveTrain.stop();
                ninjabot.driveTrain.setPosition(ninjabot.driveTrain.getFLPostion(),
                        ninjabot.driveTrain.getFRPosition(),
                        ninjabot.driveTrain.getBRPosition(),
                        ninjabot.driveTrain.getBLPosition());
            }

            ninjabot.driveTrain.updatePosition(Math.round(-1 * gamepad1.left_stick_y * driveIncrement),
                    Math.round(-1 * gamepad1.right_stick_y * driveIncrement),
                    Math.round(-1 * gamepad1.right_stick_y * driveIncrement),
                    Math.round(-1 * gamepad1.left_stick_y * driveIncrement));
            ninjabot.driveTrain.setPower(-1 * gamepad1.left_stick_y,
                    -1 * gamepad1.right_stick_y);

            // No longer strafing
            this.strafing = false;
        }

        if ((gamepad1.left_trigger < 0.5) && (gamepad2.left_trigger < 0.5) &&
            (gamepad1.right_trigger < 0.5) && (gamepad2.right_trigger < 0.5) &&
            (gamepad2.left_stick_y == 0) && (gamepad2.right_stick_y == 0) &&
            (gamepad1.left_stick_y == 0) && (gamepad1.right_stick_y == 0))
        {
            // Stop the robot and set the current position to where we are at
            ninjabot.driveTrain.stop();
            ninjabot.driveTrain.setPosition(ninjabot.driveTrain.getFLPostion(),
                                            ninjabot.driveTrain.getFRPosition(),
                                            ninjabot.driveTrain.getBRPosition(),
                                            ninjabot.driveTrain.getBLPosition());

            // No longer strafing
            this.strafing = false;
        }

        // Wobble motor
        if (gamepad1.left_bumper)
        {
            // Don't allow raising the wobble motor till wobble gate is closed
            if (ninjabot.wobbleGate.state == WobbleGate.State.STATE_GATE_CLOSED)
            {
                ninjabot.wobbleMotor.raise();
            }
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
        if (gamepad1.dpad_down)
        {
            ninjabot.intakeGate.lower();
        }
        else if (gamepad1.dpad_up)
        {
            ninjabot.intakeGate.raise();
        }

        // Counter clockwise turn
        if (gamepad1.dpad_right)
        {
            ninjabot.driveTrain.clockwiseTurn(500, 1);
        }
        else if (gamepad1.dpad_left)
        {
            ninjabot.driveTrain.counterClockWiseTurn(500, 1);
        }

        /*
        // Intake - disable turning on intake as it is causing confusion. Shooter off turns
        // intake on (gamepad2.dpad_down)
        if (gamepad2.a)
        {
            // Start intake and move flicker back (flicker stop moves it to stop)
            ninjabot.intake.start();
            ninjabot.flicker.stop();
        }
        else if (gamepad2.y)
        {
            ninjabot.intake.stop();
        }
        */

        // Only allow turning off intake
        if (gamepad2.y)
        {
            ninjabot.intake.stop();
        }

        // Flick partial
        if (gamepad2.right_bumper)
        {
           ninjabot.flicker.push_partial();
        }

        // Flicker
        if (gamepad2.x)
        {
            ninjabot.intake.stop();
            ninjabot.flicker.flick(4);
        }
        else if (gamepad2.b)
        {  // Emergency stop for flicker
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
        else if (gamepad2.dpad_left)
        { // Shooter for power shot
            this.powerShot();
        }
        else if (gamepad2.dpad_down)
        { // When the shooter is stopped then the intake is turned on and flicker is pushed back
            ninjabot.shooter.stop();
            ninjabot.intake.start();
            ninjabot.flicker.stop();
        }

        // Update ninjabot
        ninjabot.update();
        this.telemetry.update();
    }

    @Override
    public void stop()
    {
        // Stop the bot
        ninjabot.stop();
    }

    private void powerShot()
    {
       try {
           // ninjabot.flicker.stop();
            ninjabot.shooter.setPower(this.powerShotPower);
           // while (ninjabot.flicker.flickerState != Flicker.State.STATE_FLICKER_STOPPED)
           // {
            //    ninjabot.flicker.update();
           // }
            this.ninjaSleep(3000);
            ninjabot.flicker.flick(1);
            while (ninjabot.flicker.flickerState != Flicker.State.STATE_FLICKER_STOPPED) {
                ninjabot.flicker.update();
            }

            this.timer.reset();
            ninjabot.driveTrain.clockwiseTurn(230, 1);
            if (!gamepad2.b) {
                while (ninjabot.driveTrain.moving && (this.timer.milliseconds() < 200)){
                    ninjabot.driveTrain.update();
                }
                ninjabot.flicker.flick(1);
                while (ninjabot.flicker.flickerState != Flicker.State.STATE_FLICKER_STOPPED) {
                    ninjabot.flicker.update();
                }
                this.timer.reset();
                ninjabot.shooter.setPower(this.powerShotPower + 0.15);
                ninjabot.driveTrain.clockwiseTurn(105, 1);
                if (!gamepad2.b) {
                   /* while (ninjabot.driveTrain.moving && (this.timer.milliseconds() < 200)){
                        ninjabot.driveTrain.update();
                    }
                    */
                    sleep(200);
                    ninjabot.flicker.flick(2);
                }
            }
            ninjabot.shooter.stop();
        }
       catch (InterruptedException e){}
    }

    private void ninjaSleep(long msec)
    {
        try {
            sleep(msec);
        }
        catch (InterruptedException e){}
    }
}