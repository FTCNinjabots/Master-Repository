package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.VisionNavigator;
import org.firstinspires.ftc.teamcode.common.FlickerServo;
import org.firstinspires.ftc.teamcode.common.NinjaBot;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.List;

import static java.lang.Thread.sleep;

import static java.lang.Thread.sleep;

@Config
@TeleOp(name="TeleOpRR")
public class TeleopRR extends OpMode{

    private enum TeleopState
    {
        STATE_DRIVER_CONTROL,
        STATE_DRIVING_TO_SHOOT,
        STATE_AT_SHOOT,
        STATE_SHOOTING
    }

    private NinjaBot ninjabot;
    private ElapsedTime timer;
    private FtcDashboard dashboard;
    private TelemetryPacket packet;
    private SampleMecanumDrive driveTrain;
    private TeleopState state;
    private VisionNavigator vn;

    public TeleopRR()
    {

    }

    @Override
    public void init()
    {
        this.dashboard = FtcDashboard.getInstance();
        this.dashboard.setTelemetryTransmissionInterval(25);
        this.packet = new TelemetryPacket();
        this.ninjabot = new NinjaBot(hardwareMap, telemetry, false);
        this.timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        this.driveTrain = new SampleMecanumDrive(hardwareMap);
        this.driveTrain.setPoseEstimate(new Pose2d(-60, 50, 0));
        state = TeleopState.STATE_DRIVER_CONTROL;
    }

    @Override
    public void init_loop()
    {

    }

    @Override
    public void start()
    {
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }
    }

    @Override
    public void loop() {

        switch (state)
        {
            case STATE_DRIVER_CONTROL:
                this.driveTrain.setWeightedDrivePower(
                        new Pose2d(
                                -gamepad1.left_stick_y,
                                -gamepad1.left_stick_x,
                                -gamepad1.right_stick_x
                        )
                );
            break;

            case STATE_DRIVING_TO_SHOOT:
                /* If sufficient time has stopped driving to shoot can stop intake */
                if (this.timer.milliseconds() >= 1000)
                {
                    this.ninjabot.intake.stop();
                }

                /* If reached target to shoot then transition to shoot */
                if (!this.driveTrain.isBusy())
                {
                    this.state = TeleopState.STATE_AT_SHOOT;
                }
            break;

            case STATE_AT_SHOOT:
                /* Stop intake */
                this.ninjabot.intake.stop();

                /* Start flicking */
                this.ninjabot.flicker.flick(4);
                this.state = TeleopState.STATE_SHOOTING;
            break;

            case STATE_SHOOTING:
                /* Can resume driver control if flicker is done shooting */
                if (this.ninjabot.flicker.flickerState == FlickerServo.State.STATE_FLICKER_STOPPED)
                {
                    this.state = TeleopState.STATE_DRIVER_CONTROL;
                }
            break;
        }

        /* Read pose */
        Pose2d poseEstimate = this.driveTrain.getPoseEstimate();

        /***************************************************************************************
         *                         G A M E   P A D   1  C O N T R O L S                        *
         *                                                                                      *
         *  X - Close wobble gate                                                               *
         *  B - Open wobble gate                                                                *
         *                                                                                      *
         ****************************************************************************************/

        /* Raise/lower wobble gate */
        if (gamepad1.x)
        {
            ninjabot.wobbleGate.close();
        }
        else if (gamepad1.b)
        {
            ninjabot.wobbleGate.open();
        }

        /* Go to auto shoot position */
        if (gamepad1.a)
        {
            Trajectory shootPosition = driveTrain.trajectoryBuilder(poseEstimate)
                    .splineTo(new Vector2d(0, 20), Math.toRadians(20))
                    .build();

            driveTrain.followTrajectoryAsync(shootPosition);
            state = TeleopState.STATE_DRIVING_TO_SHOOT;
            this.timer.reset();

            /* Raise the elevator */
            this.ninjabot.elevator.lift();
        }

        /***************************************************************************************
         *                         G A M E   P A D   2   C O N T R O L S                        *
         *                                                                                      *
         *                                                                                      *
         *                                                                                      *
         ****************************************************************************************/




        // Print pose to telemetry
        this.telemetry.addData("x", poseEstimate.getX());
        this.telemetry.addData("y", poseEstimate.getY());
        this.telemetry.addData("heading", poseEstimate.getHeading());

        // Update ninjabot, drivetrain and telemetry
        this.ninjabot.update();
        this.driveTrain.update();
        this.telemetry.update();
    }

    @Override
    public void stop()
    {
        // Stop the bot
        ninjabot.stop();
    }

    private void ninjaSleep(long msec)
    {
        try {
            sleep(msec);
        }
        catch (InterruptedException e)
        {
            telemetry.addData("Sleep exception: " , e);
        }
    }
}

