package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.common.NinjaBot;
import org.firstinspires.ftc.teamcode.common.WobbleGate;
import org.firstinspires.ftc.teamcode.common.WobbleMotor;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.vasu.Auto;
import org.jetbrains.annotations.NotNull;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.Arrays;
import java.util.Vector;

@Config
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AutonomousRR")
public class AutonomousRR extends LinearOpMode
{
    private NinjaBot ninjabot;
    private SampleMecanumDrive drive;
    private Pose2d startPose;
    private ElapsedTime timer;

    // ****************************** RING0 TRAJECTORIES & CONSTANTS ******************************
    // Trajectories for ring 0
    Trajectory    ring0Shoot;  // 5.54 seconds
    Trajectory    ring0DropWobble; // 3.12
    Trajectory    ring0PickupWobble; // 3.26
    Trajectory    ring0DropWobble2; //
    Trajectory    ring0Term;
    Trajectory     tempTraj;

    public static double ring0shooterPower = 1.0; // Shooter power

    // *************************** RING1 TRAJECTORIES & CONSTANTS *********************************
    // Trajectories for ring 1
    Trajectory    ring1D2S;    // Drive to shoot
    Trajectory    ring1Intake; // Intake 1 ring from field
    Trajectory    ring1DropWobbleWPt1; // Waypoint for drop wobble
    Trajectory    ring1DropWobbleWPt2; // Drop off the wobble
    Trajectory    ring1PickupWobble; // Pickup wobble
    Trajectory    ring1DropWobble2; // Drop 2nd wobble goal
    Trajectory    ring1Term;  // Traverse to center line

    public static double ring1shooterPower = 0.78; // Shooter power

    // *************************** RING4 TRAJECTORIES & CONSTANTS *********************************
    // Trajectories for ring 4
    Trajectory    ring4D2S;    // Drive to shoot
    Trajectory    ring4Intake3; // Ring4 intake 3
    Trajectory    ring4Intake1; // Ring4 intake 1
    Trajectory    ring4DropWobble; // Drop off wobble goal
    Trajectory    ring4Term;     // Traverse to center line

    public static double ring4shooterPower = 0.79; // Shooter power
    public static double ring4shooterPowerSecondary = 0.78;

    // Common state
    public AutonomousRR.SkyStoneDeterminationPipeline pipeline;
    public AutonomousRR.SkyStoneDeterminationPipeline.RingPosition numRings = SkyStoneDeterminationPipeline.RingPosition.UNKNOWN;
    OpenCvWebcam webcam;

    @Override
    public void runOpMode() throws InterruptedException
    {
        AutonomousRR.SkyStoneDeterminationPipeline.RingPosition lastRing;
        int detectCount = 0;

        drive = new SampleMecanumDrive(hardwareMap);
        // Ninjabot does not have a drivetrain as we use the SameMecanumDrive train from RR
        ninjabot = new NinjaBot(hardwareMap, telemetry, false);
        timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        startPose = new Pose2d(-60, 50, 0);
        drive.setPoseEstimate(startPose);

        // Detect ring for autonomous
        this.detect();
        numRings = AutonomousRR.SkyStoneDeterminationPipeline.position;
        lastRing = numRings;

       // Build trajectories
        buildRing0Trajectory();
        buildRing1Trajectory();
        buildRing4Trajectory();

        // Detect rings - must get 3 seconds of same ring
        this.ninjaSleep(1000);
        this.numRings = AutonomousRR.SkyStoneDeterminationPipeline.position;
        while ((numRings != lastRing) || (detectCount < 3) ||
               (numRings == SkyStoneDeterminationPipeline.RingPosition.UNKNOWN))
        {
            switch (numRings) {
                case NONE:
                    telemetry.addData("Rings Detected: ", "0");
                    break;

                case ONE:
                    telemetry.addData("Rings Detected: ", "1");
                    break;

                case FOUR:
                    telemetry.addData("Rings Detected: ", "4");
                    break;

                case UNKNOWN:
                    telemetry.addData("Rings Detected: ", "UNKNOWN");
                    break;
            }
            if ((numRings == lastRing) && (numRings != SkyStoneDeterminationPipeline.RingPosition.UNKNOWN))
            {
                detectCount++;
            }
            else
            {
                lastRing = numRings;
                this.numRings = AutonomousRR.SkyStoneDeterminationPipeline.position;
                detectCount = 0;
            }

            telemetry.addData("Detect Count: ", detectCount);
            telemetry.addData("Analysis: ", pipeline.getAnalysis());
            telemetry.addData("Rings: ", Autonomous.SkystoneDeterminationPipeline.position);
            telemetry.update();
            this.ninjaSleep(1000);
        }

        // Wait for start to be pressed
        waitForStart();

        if (isStopRequested()) return;

        // Close wobble gate first
        ninjabot.wobbleGate.close();

        switch (this.numRings)
        {
            case NONE:
                runRing0Program();
            break;

            case ONE:
                runRing1Program();
            break;

            case FOUR:
                runRing4Program();
            break;
        }
    }

    // Ring 0 program
    private void runRing0Program()
    {
        // Traverse to shoot 3 rings in robot
        drive.followTrajectory(ring0Shoot);

        // Make sure wobble motor is stopped
        ninjabot.wobbleMotor.stop();

        // Shoot the 3 rings to high tower goal
        this.flickRing(4);

        // Traverse to drop off the wobble goal
        drive.followTrajectory(ring0DropWobble);

        // Open the wobble gate
        ninjabot.wobbleGate.open();

        // Wait for gate to open
        while (!ninjabot.wobbleGate.isOpen()) {
            ninjabot.wobbleGate.update();
        }

        // Raise the wobble motor
        ninjabot.wobbleMotor.raise();

        // Wait for motor to go up
        this.ninjaSleep(100);

        // Drive to pick up wobble goal
        drive.followTrajectory(ring0PickupWobble);

        // Drop down wobble goal
        ninjabot.wobbleMotor.down();

        // Wait for the wobble motor to close
        this.ninjaSleep(400);
        ninjabot.wobbleMotor.stop();

        // Pick up wobble goal
        ninjabot.wobbleGate.close();

        // Wait for the wobble gate to close
        // TODO: can have a autofunction that does a wobblegate.open/close followed by isOpen/Closed
        while (!ninjabot.wobbleGate.isClosed()) {
            ninjabot.wobbleGate.update();
        }

        // Drive to drop off wobble goal in square
        // TODO: Could also raise the wobble goal if its dragging
        drive.followTrajectory(ring0DropWobble2);

        // Open wobble gate
        ninjabot.wobbleGate.open();

        // Wait for gate to open
        while (!ninjabot.wobbleGate.isOpen()) {
            ninjabot.wobbleGate.update();
        }

        // Raise wobble mote
        ninjabot.wobbleMotor.raise();

        // Wait for motor to go up
        this.ninjaSleep(100);

        // Drive to center line
        drive.followTrajectory(ring0Term);
    }

    public void runRing1Program()
    {
        // Traverse to just before the ring
        drive.followTrajectory(ring1D2S);

        // Make sure wobble motor is stopped
        ninjabot.wobbleMotor.stop();

        // Shoot the 3 rings to high tower goal
        this.flickRing(4);

        // Start the intake
        ninjabot.intake.start();

        // Traverse picking up rings - shooter is still on
        drive.followTrajectory(ring1Intake);

        // Wait for 1.2 second for rings to complete intake
        this.ninjaSleep(1200);

        // Turn off intake and start shooting
        ninjabot.intake.stop();

        // Flick the rings
        this.flickRing(2);

        // Traverse to drop the wobble goal
        drive.followTrajectory(ring1DropWobbleWPt1);

        // Open the wobble gate
        ninjabot.wobbleGate.open();

        // Wait for gate to open
        while (!ninjabot.wobbleGate.isOpen())
        {
            ninjabot.wobbleGate.update();
        }

        // Raise the wobble motor
        ninjabot.wobbleMotor.raise();

        // Wait 50 msec before moving
        this.ninjaSleep(50);

        // Drive to pickup second wobble goal
        drive.followTrajectory(ring1PickupWobble);

        // Drop the wobble gate
        ninjabot.wobbleMotor.stop();
        ninjabot.wobbleMotor.down();

        // Wait for 400 msec
        this.ninjaSleep(800);

        // Stop wobblemotor
        ninjabot.wobbleMotor.stop();

        this.ninjaSleep(100);

        // Pick up wobble goal
        ninjabot.wobbleGate.close();

        // Wait for gate to close
        while (!ninjabot.wobbleGate.isClosed())
        {
            ninjabot.wobbleGate.update();
        }

        // Traverse to drop off wobble goal
        drive.followTrajectory(ring1DropWobble2);

        // Drop the wobble motor and open gate
        ninjabot.wobbleMotor.down();

        // Stop the wobble motor and open gate
        this.ninjaSleep(350);
        ninjabot.wobbleMotor.stop();

        // Open the wobble gate
        ninjabot.wobbleGate.open();

        // Wait for gate to open
        while (!ninjabot.wobbleGate.isOpen())
        {
            ninjabot.wobbleGate.update();
        }

        // Raise wobble goal motor
        ninjabot.wobbleMotor.raise();

        // Traverse to center line
        drive.followTrajectory(ring1Term);

        // Stop wobblemotor
        ninjabot.wobbleMotor.stop();
    }

    public void runRing4Program()
    {
        // Traverse to just before the ring
        drive.followTrajectory(ring4D2S);

        // Make sure wobble motor is stopped
        ninjabot.wobbleMotor.stop();

        // Shoot the 3 rings to high tower goal
        this.flickRing(4);

        // Start the intake
        ninjabot.intake.start();

        // Move forward to collect 3 rings
        drive.followTrajectory(ring4Intake3);

        // Wait for 1 second to collect rings
        this.ninjaSleep(1200);

        // Flick the collected rings
        this.flickRing(3);

        // Move forward to collect 1 ring
        drive.followTrajectory(ring4Intake1);

        // Wait to intake ring
        this.ninjaSleep(700);

        // Flick ring (2 times)
        this.flickRing(2);

        // Drop off the wobble goal
        drive.followTrajectory(ring4DropWobble);

        // Open the wobble gate
        ninjabot.wobbleGate.open();

        // Wait for wobble gate to open
        while (!ninjabot.wobbleGate.isOpen())
        {
            ninjabot.wobbleGate.update();
        }

        // Raise the wobble motor
        ninjabot.wobbleMotor.raise();

        // Go to center line
        drive.followTrajectory(ring4Term);
    }

    private void ninjaSleep(int delay)
    {
        this.timer.reset();
        while (this.timer.milliseconds() < delay){}
    }

    private void flickRing(int numFlicks)
    {
        ninjabot.flicker.flick(numFlicks);

        while (!ninjabot.flicker.isStopped())
        {
            ninjabot.flicker.update();
        }
    }

    private void buildRing0Trajectory()
    {
        // ----------------> Ring 0 Trajectories
        // Trajectory to drive to shoot
        ring0Shoot = drive.trajectoryBuilder(this.startPose)
                .splineTo(new Vector2d(0, 30), 0.017453292519943295)
                .addTemporalMarker(1.75, () ->
                {
                    // Wait for wobble gate to close. Should be closed by now
                    while (!ninjabot.wobbleGate.isClosed()) {
                        ninjabot.wobbleGate.update();
                    }

                    // Turn on the shooter before getting ready to flick
                    ninjabot.shooter.start(AutonomousRR.ring0shooterPower);
                    ninjabot.wobbleMotor.down();
                })
                .addTemporalMarker(2.2, ()->
                {   // Turn off wobble motor
                    ninjabot.wobbleMotor.stop();
                })
                .build();

        // Trajectory to drop off wobble goal
        ring0DropWobble = drive.trajectoryBuilder(ring0Shoot.end())
                .splineTo(new Vector2d(10, 50), 3.3161255787892263)
                .addTemporalMarker(1, () ->
                {
                    // Turn off the shooter
                    ninjabot.shooter.stop();
                })
                .build();

        // Trajectory to pick up wobble goal 2
        ring0PickupWobble = drive.trajectoryBuilder(ring0DropWobble.end())
                .splineTo(new Vector2d(-45, 12.5), 3.3161255787892263)
                .addTemporalMarker(1, () ->
                {
                    // Stop the wobble motor as it was up
                    ninjabot.wobbleMotor.stop();
                })
                .build();

        // Trajectory to drop off wobble goal 2
        ring0DropWobble2 = drive.trajectoryBuilder(ring0PickupWobble.end())
                .splineTo(new Vector2d(15, 48), 3.3161255787892263)
                .addTemporalMarker(1, () ->
                {
                    // Ensure wobble gate is closed
                    ninjabot.wobbleGate.update();
                })
                .build();

        ring0Term = drive.trajectoryBuilder(ring0DropWobble2.end())
                .splineTo(new Vector2d(10, 20), 0)
                .addTemporalMarker(1, () ->
                {
                    // Stop the wobble motor as it was up
                    ninjabot.wobbleMotor.stop();
                })
                .build();

    }

    private void buildRing1Trajectory()
    {
        // -------------------------> Ring 1 trajectories
        ring1D2S = drive.trajectoryBuilder(this.startPose)
                .splineTo(new Vector2d(-40, 37), -0.07592182246175333)
                .addTemporalMarker(1.45, () ->
                {
                    // Wait for wobble gate to close. Should be closed by now
                    while (!ninjabot.wobbleGate.isClosed()) {
                        ninjabot.wobbleGate.update();
                    }

                    // Turn on the shooter before getting ready to flick
                    ninjabot.shooter.start(AutonomousRR.ring1shooterPower);
                    ninjabot.wobbleMotor.down();
                })
                .addTemporalMarker(1.9, ()->
                {   // Turn off wobble motor
                    ninjabot.wobbleMotor.stop();
                })
                .build();

        // Intake rings so go at a slower speed
        ring1Intake = drive.trajectoryBuilder(ring1D2S.end())
                .forward(16, new MinVelocityConstraint(
                                Arrays.asList(
                                        new AngularVelocityConstraint(DriveConstants.MAX_ANG_VEL),
                                        new MecanumVelocityConstraint(20, DriveConstants.TRACK_WIDTH)
                                )
                        ),
                        new ProfileAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        // Drop off wobble goal in square
        ring1DropWobbleWPt1 = drive.trajectoryBuilder(ring1Intake.end())
                .splineTo(new Vector2d(47, 42),  0)
                .build();

        // Pick up wobble goal 2
        ring1PickupWobble = drive.trajectoryBuilder(ring1DropWobbleWPt1.end(), true)
                .splineTo(new Vector2d(-51, 37.75), 3.14159265358979)
                .build();

        // Drop off wobble goal 2
        ring1DropWobble2 = drive.trajectoryBuilder((ring1PickupWobble.end()))
                .splineTo(new Vector2d(44, 54), 0)
                .addTemporalMarker(0.5, () ->
                {
                    // TUrn off wobble motor
                    ninjabot.wobbleMotor.stop();
                })
                .build();

        // Drive to center line
        ring1Term = drive.trajectoryBuilder(ring1DropWobble2.end(), true)
                .splineTo(new Vector2d(10, 57), 3.141592653589793)
                .build();
    }

    private void buildRing4Trajectory()
    {
        // -------------------------> Ring 4 trajectories
        // Same as ring1D2S
        ring4D2S = drive.trajectoryBuilder(this.startPose)
                .splineTo(new Vector2d(-40, 37),  -0.061086523819801536)
                .addTemporalMarker(1.45, () ->
                {
                    // Wait for wobble gate to close. Should be closed by now
                    while (!ninjabot.wobbleGate.isClosed()) {
                        ninjabot.wobbleGate.update();
                    }

                    // Turn on the shooter before getting ready to flick
                    ninjabot.shooter.start(AutonomousRR.ring4shooterPower);
                    ninjabot.wobbleMotor.down();
                    ninjabot.intakeGate.lower();
                })
                .addTemporalMarker(1.9, ()->
                {   // Turn off wobble motor
                    ninjabot.wobbleMotor.stop();
                })
                .build();

        // Intake 3 rings so go at a slower speed
        ring4Intake3 = drive.trajectoryBuilder(ring4D2S.end())
                .forward(16, new MinVelocityConstraint(
                                Arrays.asList(
                                        new AngularVelocityConstraint(DriveConstants.MAX_ANG_VEL),
                                        new MecanumVelocityConstraint(3.5, DriveConstants.TRACK_WIDTH)
                                )
                        ),
                        new ProfileAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addTemporalMarker(0.5, () ->
                {
                    // Reduce power before shooting
                    ninjabot.shooter.start(AutonomousRR.ring4shooterPowerSecondary);
                })
                .build();

        // Intake 1 ring so can go faster
        ring4Intake1 = drive.trajectoryBuilder(ring4Intake3.end())
                .forward(14, new MinVelocityConstraint(
                                Arrays.asList(
                                        new AngularVelocityConstraint(DriveConstants.MAX_ANG_VEL),
                                        new MecanumVelocityConstraint(30, DriveConstants.TRACK_WIDTH)
                                )
                        ),
                        new ProfileAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        // Drop off wobble goal
        ring4DropWobble = drive.trajectoryBuilder(ring4Intake1.end())
                .splineTo(new Vector2d(50, 55), 1.5707963267948966)
                .build();

        // Traverse to center line
        ring4Term = drive.trajectoryBuilder(ring4DropWobble.end())
                .splineTo(new Vector2d(10, 53), 1.5707963267948966)
                .addTemporalMarker(1, () ->
                {
                    // shudown wobblemotor
                    ninjabot.wobbleMotor.stop();
                })
                .build();
    }

    private AutonomousRR.SkyStoneDeterminationPipeline.RingPosition detect() {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "webcam"), cameraMonitorViewId);

        pipeline = new AutonomousRR.SkyStoneDeterminationPipeline();

        webcam.setPipeline(pipeline);


        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
        // out when the RC activity is in portrait. We do our actual image processing assuming
        // landscape orientation, though
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240);
            }
        });

        pipeline.getAnalysis();

        //dashboard.startCameraStream();
        return AutonomousRR.SkyStoneDeterminationPipeline.position;
    }

    public static class SkyStoneDeterminationPipeline extends OpenCvPipeline {

        /*
         * An enum to define the skystone position
         */
        public enum RingPosition {
            FOUR,
            ONE,
            NONE,
            UNKNOWN
        }

        /*
         * Some color constants
         */
        static final Scalar BLUE = new Scalar(0, 0, 255);
        static final Scalar GREEN = new Scalar(0, 255, 0);

        /*
         * The core values which define the location and size of the sample regions
         */
        static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(130, 40);
        //static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(181, 98);

        static final int REGION_WIDTH = 50;
        static final int REGION_HEIGHT = 50;

        final int FOUR_RING_THRESHOLD = 133;
        final int ONE_RING_THRESHOLD = 129;

        Point region1_pointA = new Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x,
                REGION1_TOPLEFT_ANCHOR_POINT.y);
        Point region1_pointB = new Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

        /*
         * Working variables
         */
        Mat region1_Cb;
        Mat YCrCb = new Mat();
        Mat Cb = new Mat();
        int avg1;

        // Volatile since accessed by OpMode thread w/o synchronization
        public static volatile AutonomousRR.SkyStoneDeterminationPipeline.RingPosition position = RingPosition.UNKNOWN;

        /*
         * This function takes the RGB frame, converts to YCrCb,
         * and extracts the Cb channel to the 'Cb' variable
         */
        void inputToCb(Mat input) {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cb, 1);
        }

        @Override
        public void init(Mat firstFrame) {
            inputToCb(firstFrame);

            region1_Cb = Cb.submat(new Rect(region1_pointA, region1_pointB));
        }

        @Override
        public Mat processFrame(Mat input) {
            inputToCb(input);

            avg1 = (int) Core.mean(region1_Cb).val[0];

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    BLUE, // The color the rectangle is drawn in
                    2); // Thickness of the rectangle lines

            position = RingPosition.FOUR; // Record our analysis
            if (avg1 > FOUR_RING_THRESHOLD) {
                position = RingPosition.FOUR;
            } else if (avg1 > ONE_RING_THRESHOLD) {
                position = RingPosition.ONE;
            } else {
                position = RingPosition.NONE;
            }

            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill

            return input;
        }

        public int getAnalysis() {
            return avg1;
        }
    }
}
