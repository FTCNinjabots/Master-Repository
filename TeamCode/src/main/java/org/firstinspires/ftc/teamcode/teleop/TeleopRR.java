package org.firstinspires.ftc.teamcode.teleop;

import android.sax.TextElementListener;

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
import org.firstinspires.ftc.teamcode.common.PoseStorage;
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
        STATE_AT_POWER_SHOOT,
        STATE_SHOOTING
    }

    private NinjaBot ninjabot;
    private ElapsedTime timer;
    private FtcDashboard dashboard;
    private TelemetryPacket packet;
    private SampleMecanumDrive driveTrain;
    private TeleopState state;
    private VisionNavigator vn;
    private boolean isPowerShot;
    private boolean shooting;
    private double g2sense = 0.4;

    private double xdelta;
    private double ydelta;
    private double hdelta;

    // High Tower shooting parameters
    private double highTowerPower = 0.80;
    private double highTowerShootPosX = -6;
    private double highTowerShootPosY = 19.5;
    private double highTowerShootDegree = 13;

    // Power shot shooting parameters
    private double powerShotPower = 0.75;
    private double powerShotShootPosX = -3;
    private double powerShotShootPosY = 29;
    private double powerShotShootDegree = 0;
    private double powerShotShootDegree2 = -0.5;
    private double powerShotShootDegree3 = -1;
    private int powerShotNum = 0;

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
        this.isPowerShot = false;
        this.shooting = false;

        // Store the pose to where we ended up in autonomous
        this.driveTrain.setPoseEstimate(PoseStorage.currentPose);
        state = TeleopState.STATE_DRIVER_CONTROL;

        this.telemetry.addData("x", PoseStorage.currentPose.getX());
        this.telemetry.addData("y", PoseStorage.currentPose.getY());
        this.telemetry.addData("heading", PoseStorage.currentPose.getHeading());
        this.telemetry.update();
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

        /* Read pose */
        Pose2d poseEstimate = this.driveTrain.getPoseEstimate();

        switch (state)
        {
            case STATE_DRIVER_CONTROL:

                ydelta = -gamepad1.left_stick_y + (-gamepad2.left_stick_y * g2sense);
                xdelta =  -gamepad1.left_stick_x +  (-gamepad2.left_stick_x * g2sense);
                hdelta = -gamepad1.right_stick_x + (-gamepad2.right_stick_x * g2sense);

                if (!gamepad1.a && !gamepad1.y && !shooting) {
                    this.driveTrain.setWeightedDrivePower(
                            new Pose2d(ydelta, xdelta, hdelta)
                    );
                }
                else {
                    //this.driveTrain.setWeightedDrivePower(
                    //        new Pose2d(
                    //                -gamepad1.left_stick_y,
                    //                -gamepad1.left_stick_x,
                    //                -gamepad1.right_stick_x
                    //        )
                    // );

                    poseEstimate = this.driveTrain.getPoseEstimate();

                    /* Go to auto shoot position */
                    if (gamepad1.a || shooting) {
                        if (!this.driveTrain.isBusy()) {
                            Trajectory shootPosition;

                            if (poseEstimate.getHeading() == highTowerShootDegree) {
                                shootPosition = driveTrain.trajectoryBuilder(poseEstimate)
                                        .splineToConstantHeading(new Vector2d(highTowerShootPosX, highTowerShootPosY), Math.toRadians(highTowerShootDegree))
                                        .build();
                            } else {
                                shootPosition = driveTrain.trajectoryBuilder(poseEstimate)
                                        .splineTo(new Vector2d(highTowerShootPosX, highTowerShootPosY), Math.toRadians(highTowerShootDegree))
                                        .build();
                            }

                            driveTrain.followTrajectoryAsync(shootPosition);
                            state = TeleopState.STATE_DRIVING_TO_SHOOT;
                            this.timer.reset();
                            this.ninjabot.shooter.start(highTowerPower);

                            /* Raise the elevator */
                            this.ninjabot.elevator.lift();

                            // set shooting to false
                            shooting = false;
                        } else {
                            shooting = true;
                        }
                    }

                    /* Go to power shoot position */
                    if (gamepad1.y || shooting) {

                        if (!this.driveTrain.isBusy()) {
                            Trajectory shootPosition;

                            if (poseEstimate.getHeading() == powerShotShootDegree) {
                                shootPosition = driveTrain.trajectoryBuilder(poseEstimate)
                                        .splineToConstantHeading(new Vector2d(powerShotShootPosX, powerShotShootPosY), Math.toRadians(powerShotShootDegree))
                                        .build();
                            } else {
                                shootPosition = driveTrain.trajectoryBuilder(poseEstimate)
                                        .splineTo(new Vector2d(powerShotShootPosX, powerShotShootPosY), Math.toRadians(powerShotShootDegree))
                                        .build();
                            }

                            driveTrain.followTrajectoryAsync(shootPosition);
                            state = TeleopState.STATE_DRIVING_TO_SHOOT;
                            this.timer.reset();
                            this.ninjabot.shooter.start(powerShotPower);
                            this.isPowerShot = true;
                            this.powerShotNum = 1;

                            /* Raise the elevator */
                            this.ninjabot.elevator.lift();

                            // set shooting to false
                            shooting = false;
                        } else {
                            shooting = true;
                        }
                    }
                }
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
                    if (this.isPowerShot)
                    {
                        this.state = TeleopState.STATE_AT_POWER_SHOOT;
                    }
                    else {
                        this.state = TeleopState.STATE_AT_SHOOT;
                    }
                }
            break;

            case STATE_AT_SHOOT:
                /* Stop intake */
                this.ninjabot.intake.stop();

                // TODO: We should make sure shooter has spun up here
                /* Start flicking */
               // this.ninjabot.flicker.flick(4);
               // this.state = TeleopState.STATE_SHOOTING;
                this.state = TeleopState.STATE_DRIVER_CONTROL;
            break;

            case STATE_AT_POWER_SHOOT:
                /* Stop intake */
                this.ninjabot.intake.stop();

                // TODO: We should make sure shooter has spun ip here
                // Flick the ring - do it once only once for shot 1 and 2
                switch (this.powerShotNum)
                {
                    case 1:
                    case 2:
                      //  this.ninjabot.flicker.flick(1);
                    break;

                    case 3:
                      //  this.ninjabot.flicker.flick(2);
                    break;
                }
                //this.state = TeleopState.STATE_SHOOTING;
                this.state = TeleopState.STATE_DRIVER_CONTROL;
            break;

            case STATE_SHOOTING:
                /* Can resume driver control if flicker is done shooting */
                if (this.ninjabot.flicker.maxFlicks == 0)
                {
                    if (!isPowerShot) {
                        this.ninjabot.shooter.stop();
                        this.state = TeleopState.STATE_DRIVER_CONTROL;
                    }
                    else
                    {
                        Trajectory shootPosition;

                        switch (powerShotNum)
                        {
                            case 1:
                                // First power shot done - adjust heading for second shot
                                shootPosition = driveTrain.trajectoryBuilder(poseEstimate)
                                        .splineTo(new Vector2d(powerShotShootPosX, powerShotShootPosY), Math.toRadians(powerShotShootDegree2))
                                        .build();
                                driveTrain.followTrajectoryAsync(shootPosition);
                                state = TeleopState.STATE_DRIVING_TO_SHOOT;
                                this.timer.reset();
                                powerShotNum = 2;
                            break;
                            case 2:
                                // Second power shot done - adjust heading for third shot
                                shootPosition = driveTrain.trajectoryBuilder(poseEstimate)
                                        .splineTo(new Vector2d(powerShotShootPosX, powerShotShootPosY), Math.toRadians(powerShotShootDegree3))
                                        .build();
                                driveTrain.followTrajectoryAsync(shootPosition);
                                state = TeleopState.STATE_DRIVING_TO_SHOOT;
                                this.timer.reset();
                                powerShotNum = 2;
                            break;
                            case 3:
                                // Third power shot done - switch back to driver control
                                isPowerShot = false;
                                powerShotNum = 0;
                                this.ninjabot.shooter.stop();
                                this.state = TeleopState.STATE_DRIVER_CONTROL;
                            break;
                        }
                    }
                }
            break;
        }


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

        if (gamepad1.dpad_left)
        {
            this.ninjabot.wobbleMotor.down();
        }

        if (gamepad1.dpad_right)
        {
            this.ninjabot.wobbleMotor.raise();
        }

        if (gamepad1.dpad_down)
        {
            this.ninjabot.elevator.swivelMore();
        }

        if (gamepad1.dpad_up)
        {
            this.ninjabot.elevator.swivelReset();
        }

        /***************************************************************************************
         *                         G A M E   P A D   2   C O N T R O L S                        *
         *                                                                                      *
         *                                                                                      *
         *                                                                                      *
         ****************************************************************************************/

        if (gamepad2.dpad_up)
        {  // Start intake
            this.ninjabot.intake.start();
            this.ninjabot.elevator.drop();
        }

        if (gamepad2.dpad_down)
        {  // Stop intake
            this.ninjabot.intake.stop();
        }

        if (gamepad2.dpad_left)
        { // Start shooter
            this.ninjabot.shooter.start(highTowerPower);
        }

        if (gamepad2.dpad_right)
        { // Stop shooter
            this.ninjabot.shooter.stop();
        }

        if (gamepad2.x)
        { // Lift elevator
            this.ninjabot.elevator.lift();
        }

        if (gamepad2.b)
        { // Drop elevator
            this.ninjabot.elevator.drop();
        }

        if (gamepad2.a)
        {
            this.ninjabot.flicker.flick(4);
        }

        // Rescue flick
        if (gamepad2.y)
        {
            this.ninjabot.elevator.rescue();
        }

        if (gamepad2.right_bumper)
        {
            this.ninjabot.flicker.nodrop();
            this.ninjabot.flicker.flick(1);
        }

        // Get out of jail button
        if (gamepad2.left_bumper)
        {
            isPowerShot = false;
            powerShotNum = 0;
            this.state = TeleopState.STATE_DRIVER_CONTROL;
        }

        // Print pose to telemetry
        this.telemetry.addData("x", poseEstimate.getX());
        this.telemetry.addData("y", poseEstimate.getY());
        this.telemetry.addData("heading", poseEstimate.getHeading());
        this.telemetry.addData("Shooter Power", ninjabot.shooter.currentPower);
        this.telemetry.addData("State: ", state);
        this.telemetry.addData("RR Flicks: ", this.ninjabot.flicker.maxFlicks);
        this.telemetry.addData("Powershot: " , this.isPowerShot + "Shot Num: " + powerShotNum);
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

