package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.Flicker;
import org.firstinspires.ftc.teamcode.common.NinjaBot;
import org.firstinspires.ftc.teamcode.common.WobbleGate;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import static java.lang.Thread.sleep;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import static java.lang.Thread.sleep;

@Config
@TeleOp(name="TeleOpRR")
public class TeleopRR extends OpMode{

    private enum TeleopState
    {
        STATE_DRIVER_CONTROL,
        STATE_DRIVING_TO_SHOOT,
        STATE_AT_SHOOT
    }

    private NinjaBot ninjabot;
    private ElapsedTime timer;
    private FtcDashboard dashboard;
    private TelemetryPacket packet;
    private SampleMecanumDrive driveTrain;
    private TeleopState state;

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
                if (!this.driveTrain.isBusy())
                {
                    this.state = TeleopState.STATE_AT_SHOOT;
                }
            break;

            case STATE_AT_SHOOT:
                this.state = TeleopState.STATE_DRIVER_CONTROL;
            break;
        }

        /* Read pose */
        Pose2d poseEstimate = this.driveTrain.getPoseEstimate();

        // Do other stuff like gamepad2.b

        if (gamepad1.x)
        {
            Trajectory shootPosition = driveTrain.trajectoryBuilder(poseEstimate)
                    .splineTo(new Vector2d(0, 20), Math.toRadians(20))
                    .build();

            driveTrain.followTrajectoryAsync(shootPosition);
            state = TeleopState.STATE_DRIVING_TO_SHOOT;
        }

        // Print pose to telemetry
        this.telemetry.addData("x", poseEstimate.getX());
        this.telemetry.addData("y", poseEstimate.getY());
        this.telemetry.addData("heading", poseEstimate.getHeading());

        // Update ninjabot
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

