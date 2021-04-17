package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
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

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.common.Elevator;
import org.firstinspires.ftc.teamcode.common.NinjaBot;
import org.firstinspires.ftc.teamcode.common.PoseStorage;
import org.firstinspires.ftc.teamcode.common.WobbleGate;
import org.firstinspires.ftc.teamcode.common.WobbleMotor;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.util.DashboardUtil;
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
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="AAutonomousRR")
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
    Trajectory    ring0Drive2PickupWobble;
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
    Trajectory    ring1Drive2PickupWobble; // Drive to pickup wobble goal
    Trajectory    ring1PickupWobble; // Pickup wobble
    Trajectory    ring1DropWobble2; // Drop 2nd wobble goal
    Trajectory    ring1Term;  // Traverse to center line

    public static double ring1shooterPower = 0.72; // Shooter power

    // *************************** RING4 TRAJECTORIES & CONSTANTS *********************************
    // Trajectories for ring 4
    Trajectory    ring4D2S;    // Drive to shoot
    Trajectory    ring4Intake3; // Ring4 intake 3
    Trajectory    ring4Intake1; // Ring4 intake 1
    Trajectory    ring4DropWobble; // Drop off wobble goal
    Trajectory    ring4Term;     // Traverse to center line
    Trajectory    ring4Drive2PickupWobble;
    Trajectory    ring4PickupWobble;
    Trajectory    ring4DropWobble2;

    public static double ring4shooterPower = 0.8; // Shooter power
    public static double ring4shooterPowerSecondary = 0.78;

    // Common state
    public AutonomousRR.SkyStoneDeterminationPipeline pipeline;
    public AutonomousRR.SkyStoneDeterminationPipeline.RingPosition numRings = SkyStoneDeterminationPipeline.RingPosition.UNKNOWN;
    OpenCvWebcam webcam;
    public static final String VUFORIA_LICENSE_KEY = "AQe+9fb/////AAABmROBsZ/F0UMyud7D/EVc5vsgUyoOerSwc/ezXcf5K5BhgPP734+wZNvMD5dLeQrmgUt4Mb74CpYc2RcEbZSTiFgoShurU9gGxvfsb29fLq0rCEd2YKGjaO3qqyF0VxTwPXzLTJoWTV5s4XqleSO7dNajCel9TVI7EoxU8dE0w8pHK9r7inc94Xvnpf/GDanpwqf/0Z7gu0qTutmWryJfj5p9I4BhvWmgNUGtz0cZGUXl9JzSk7zHdCQk7US9f4A5/SK0tC/1spxAqtYyTgbQY0kupMdmzW/zsusE20UVt6hf625rOzmkNomsHdApnXeN2YjhatIIHdM4QNs1yzqUZBFfBGk59Q5KGvff1QLEgMON";


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
        FtcDashboard dashboard;
        dashboard = FtcDashboard.getInstance();
        dashboard.setTelemetryTransmissionInterval(25);
        //VuforiaLocalizer.Parameters vuforiaParams = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        //vuforiaParams.vuforiaLicenseKey = VUFORIA_LICENSE_KEY;
        //VuforiaLocalizer vuforia = ClassFactory.getInstance().createVuforia(vuforiaParams);


        // Detect ring for autonomous
        this.detect();
        numRings = AutonomousRR.SkyStoneDeterminationPipeline.position;
        lastRing = numRings;

       // Build trajectories
        buildRing0Trajectory();
        buildRing1Trajectory();
        buildRing4Trajectory();

        // Raise wobble goal and close wobble gate first
        ninjabot.wobbleGate.close();

        // Detect rings - must get 3 seconds of same ring
        this.ninjaSleep(1000);
        this.numRings = AutonomousRR.SkyStoneDeterminationPipeline.position;
        while ((numRings != lastRing) || (detectCount < 3) ||
               (numRings == AutonomousRR.SkyStoneDeterminationPipeline.RingPosition.UNKNOWN) ||
                (pipeline.getAnalysis() == 0))
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

            if ((this.numRings != AutonomousRR.SkyStoneDeterminationPipeline.RingPosition.UNKNOWN) &&
                (this.numRings == lastRing) &&
                (this.numRings == AutonomousRR.SkyStoneDeterminationPipeline.position) &&
                (AutonomousRR.SkyStoneDeterminationPipeline.position != AutonomousRR.SkyStoneDeterminationPipeline.RingPosition.UNKNOWN) &&
                (pipeline.getAnalysis() != 0))
            {
                detectCount++;
            }
            else
            {
                lastRing = this.numRings;
                this.numRings = AutonomousRR.SkyStoneDeterminationPipeline.position;
                detectCount = 0;
            }

            telemetry.addData("Detect Count: ", detectCount);
            telemetry.addData("Analysis: ", pipeline.getAnalysis());
            telemetry.addData("Rings: ", AutonomousRR.SkyStoneDeterminationPipeline.position);
            telemetry.update();
            this.ninjaSleep(1000);
            this.ninjabot.update();
        }
        dashboard.startCameraStream(webcam, 45);

        // Wait for start to be pressed
        waitForStart();

        if (isStopRequested()) return;


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

        // Store current pose
        PoseStorage.currentPose = drive.getPoseEstimate();

        this.telemetry.addData("x", PoseStorage.currentPose.getX());
        this.telemetry.addData("y", PoseStorage.currentPose.getY());
        this.telemetry.addData("heading", PoseStorage.currentPose.getHeading());
        this.telemetry.update();
    }

    // Ring 0 program
    private void runRing0Program()
    {
        // Traverse to shoot 3 rings in robot
        drive.followTrajectory(ring0Shoot);

        // Shoot the 3 rings to high tower goal
        this.ninjaSleep(1000);
        this.flickRing(4);

        // Traverse to drop off the wobble goal
        drive.followTrajectory(ring0DropWobble);

        // Drop the wobble motor and wait for it to go idle
        dropWobble();

        // Wait for wobble gate to open
        openWobbleGate();

        // Raise the wobble motor
        ninjabot.wobbleMotor.raise();

        // Drive towards the wobble goal
        drive.followTrajectory(ring0Drive2PickupWobble);

        // Drop down the wobble goal
        dropWobble();

        // Open the wobble gate
        openWobbleGate();

        // Drive to the wobble goal itself
        drive.followTrajectory(ring0PickupWobble);

        // Close wobble gate
        closeWobblegate();

        // Drive to drop off wobble goal in square - raise the wobble goal
        raiseWobble();
        drive.followTrajectory(ring0DropWobble2);

        // Drop the wobble goal on the square
        dropWobble();

        // Open the wobble gate
        openWobbleGate();

        // Drive to center line
        drive.followTrajectory(ring0Term);
    }

    public void runRing1Program()
    {
        // Traverse to just before the ring
        drive.followTrajectory(ring1D2S);

        // Shoot the 3 rings to high tower goal
        this.ninjaSleep(1000);
        this.flickRing(4);

        // Drop the elevator
        this.ninjabot.elevator.drop();
        while (this.ninjabot.elevator.eleState != Elevator.State.STATE_ELEVATOR_BOTTOM)
        {
            this.ninjabot.update();
        }

        // Start the intake
        ninjabot.intake.start();

        // Traverse picking up rings - shooter is still on
        drive.followTrajectory(ring1Intake);

        // Wait for 1000 msec for rings to complete intake
        this.ninjaSleep(1000);

        // Turn off intake and start shooting
        ninjabot.intake.stop();

        // Flick the rings
        this.flickRing(2);

        // Traverse to drop the wobble goal
        drive.followTrajectory(ring1DropWobbleWPt1);

        // Drop the wobble motor
        dropWobble();

        // Open the wobble gate
        openWobbleGate();

        // Raise the wobble motor
        ninjabot.wobbleMotor.raise();

        // Drive towards the wobble goal
        drive.followTrajectory(ring1Drive2PickupWobble);

        // Drop the wobble motor
        dropWobble();

        // Open the wobble gate
        openWobbleGate();

        // Drive to pickup second wobble goal
        drive.followTrajectory(ring1PickupWobble);

        // Close the wobble gate
        closeWobblegate();

        // Drive to drop off wobble goal in square - raise the wobble goal
        raiseWobble();
        drive.followTrajectory(ring1DropWobble2);

        // Drop the wobble motor
        dropWobble();;

        // open the wobble gate
        openWobbleGate();

        // Traverse to right two inche - we are on center line
        drive.followTrajectory(ring1Term);
    }

    public void runRing4Program()
    {
        // Traverse to just before the ring
        drive.followTrajectory(ring4D2S);

        // Shoot the 3 rings to high tower goal
        this.ninjaSleep(1000);
        this.flickRing(4);
        this.ninjabot.shooter.stop();

        // Drop off the wobble goal
        drive.followTrajectory(ring4DropWobble);

        // Drop the wobble goal
        dropWobble();

        // Wait for wobble gate to open
        openWobbleGate();

        // Raise the wobble motor
        ninjabot.wobbleMotor.raise();

        // Drive towards the second wobble goal
        drive.followTrajectory(ring4Drive2PickupWobble);

        // Drop down the wobble goal
        dropWobble();

        // Open the wobble gate
        openWobbleGate();

        // Drive to the wobble goal itself
        drive.followTrajectory(ring4PickupWobble);

        // Close wobble gate
        closeWobblegate();

        // Drive to drop off the wobble goal in square
        raiseWobble();
        drive.followTrajectory(ring4DropWobble2);

        // Drop the wobble goal into the square
        dropWobble();

        // OPen the wobble gate
        openWobbleGate();

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
            this.ninjabot.update();
        }
    }

    private void buildRing0Trajectory()
    {
        // ----------------> Ring 0 Trajectories
        // Trajectory to drive to shoot
        ring0Shoot = drive.trajectoryBuilder(this.startPose)
                .splineTo(new Vector2d(-5, 32),  0.06981317007977318)
                .addTemporalMarker(0.25, () ->
                {
                    // Wait for wobble gate to close. Should be closed by now
                   while (!ninjabot.wobbleGate.isClosed())
                   {
                       ninjabot.wobbleGate.update();
                    }

                    // Turn on the shooter before getting ready to flick
                    ninjabot.shooter.start(AutonomousRR.ring0shooterPower);
                })
                .build();

        // Trajectory to drop off wobble goal
        ring0DropWobble = drive.trajectoryBuilder(ring0Shoot.end())
                .splineTo(new Vector2d(16, 37), 4.71238898038469)
                .addTemporalMarker(1, () ->
                {
                    // Turn off the shooter
                    ninjabot.shooter.stop();
                    // Elevator drop will trigger here
                    ninjabot.update();
                })
                .build();

        // Trajectory to drive to pick up wobble goal 2
        ring0Drive2PickupWobble = drive.trajectoryBuilder(ring0DropWobble.end())
                .splineTo(new Vector2d(-8,  16), 0)
                .addTemporalMarker(1, () ->
                {
                    // Stop the wobble motor as it was up
                    ninjabot.wobbleMotor.stop();
                })
                .build();

        // Trajectory to pick up wobble goal 2
        ring0PickupWobble = drive.trajectoryBuilder(ring0Drive2PickupWobble.end())
                .back(21, new MinVelocityConstraint(
                                Arrays.asList(
                                        new AngularVelocityConstraint(DriveConstants.MAX_ANG_VEL),
                                        new MecanumVelocityConstraint(20, DriveConstants.TRACK_WIDTH)
                                )
                        ),
                        new ProfileAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();


        // Trajectory to drop off wobble goal 2
        ring0DropWobble2 = drive.trajectoryBuilder(ring0PickupWobble.end())
                .splineTo(new Vector2d(9, 29), 4.71238898038469)
                .build();

        ring0Term = drive.trajectoryBuilder(ring0DropWobble2.end())
                .splineToConstantHeading(new Vector2d(9, 25), 0)
                .build();

    }

    private void buildRing1Trajectory()
    {
        // -------------------------> Ring 1 trajectories
        ring1D2S = drive.trajectoryBuilder(this.startPose)
                .splineTo(new Vector2d(-40, 36), 0.008726646259971648)
                .addTemporalMarker(0.15, () ->
                {
                    // Turn on the shooter before getting ready to flick
                    ninjabot.shooter.start(AutonomousRR.ring1shooterPower);
                })
                .build();

        // Intake rings so go at a slower speed
        ring1Intake = drive.trajectoryBuilder(ring1D2S.end())
                .forward(26, new MinVelocityConstraint(
                                Arrays.asList(
                                        new AngularVelocityConstraint(DriveConstants.MAX_ANG_VEL),
                                        new MecanumVelocityConstraint(20, DriveConstants.TRACK_WIDTH)
                                )
                        ),
                        new ProfileAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        // Drop off wobble goal in square
        ring1DropWobbleWPt1 = drive.trajectoryBuilder(ring1Intake.end())
                .splineTo(new Vector2d(26, 45),  3.141592653589793)
                .build();

        // Drive to pickup wobble goal 2
        ring1Drive2PickupWobble = drive.trajectoryBuilder(ring1DropWobbleWPt1.end())
                .splineTo(new Vector2d(-8, 14.25), 0)
                .addTemporalMarker(0.5, () ->
                {
                    // Turn off wobble motor
                    ninjabot.wobbleMotor.stop();
                })
                .build();

        // Pick up wobble goal 2
        ring1PickupWobble = drive.trajectoryBuilder(ring1Drive2PickupWobble.end())
                .back(29, new MinVelocityConstraint(
                                Arrays.asList(
                                        new AngularVelocityConstraint(DriveConstants.MAX_ANG_VEL),
                                        new MecanumVelocityConstraint(20, DriveConstants.TRACK_WIDTH)
                                )
                        ),
                        new ProfileAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        // Drop off wobble goal 2
        ring1DropWobble2 = drive.trajectoryBuilder((ring1PickupWobble.end()))
                .splineTo(new Vector2d(24, 39), 3.141592653589793)
                .build();

        // Drive to center line
        ring1Term = drive.trajectoryBuilder(ring1DropWobble2.end(), true)
                .splineToConstantHeading(new Vector2d(12, 39), 0)
                .build();
    }

    private void buildRing4Trajectory()
    {
        // -------------------------> Ring 4 trajectories
        // Same as ring1D2S
        ring4D2S = drive.trajectoryBuilder(this.startPose)
                .splineTo(new Vector2d(0, 50),  -0.17453292519943295)
                .addTemporalMarker(1.45, () ->
                {
                    // Turn on the shooter before getting ready to flick
                    ninjabot.shooter.start(AutonomousRR.ring4shooterPower);
                })
                .build();

        // Drop off wobble goal
        ring4DropWobble = drive.trajectoryBuilder(ring4D2S.end())
                .splineTo(new Vector2d(54, 45), -2.0943951023931953)
                .build();

        // Drive to pickup wobble goal
        ring4Drive2PickupWobble = drive.trajectoryBuilder(ring4DropWobble.end())
                .splineTo(new Vector2d(-24, 7), -0.3490658503988659)
                .addTemporalMarker(0.6, () ->
                {
                    // Turn off wobble motor
                    ninjabot.wobbleMotor.stop();
                })
                .build();

        // Pickup wobble goal 2
        ring4PickupWobble = drive.trajectoryBuilder(ring4Drive2PickupWobble.end())
                .back(15.5, new MinVelocityConstraint(
                                Arrays.asList(
                                        new AngularVelocityConstraint(DriveConstants.MAX_ANG_VEL),
                                        new MecanumVelocityConstraint(20, DriveConstants.TRACK_WIDTH)
                                )
                        ),
                        new ProfileAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        // Drop off wobble goal 2
        ring4DropWobble2 = drive.trajectoryBuilder((ring4PickupWobble.end()))
                .splineTo(new Vector2d(45, 40), -2.0943951023931953)
                .build();

        // Traverse to center line
        ring4Term = drive.trajectoryBuilder(ring4DropWobble2.end())
                .splineTo(new Vector2d(12, 24), -2.0943951023931953)
                .build();
    }

    private void dropWobble()
    {
        // Drop down wobble goal
        ninjabot.wobbleMotor.down();

        // Wait for the wobble motor to close
        while (ninjabot.wobbleMotor.state != WobbleMotor.State.STATE_WOBBLE_IDLE)
        {
            ninjabot.update();
        }

        this.ninjaSleep(300);
    }

    private void raiseWobble()
    {
        // Drop down wobble goal
        ninjabot.wobbleMotor.raise();

        // Wait for the wobble motor to close
        while (ninjabot.wobbleMotor.state != WobbleMotor.State.STATE_WOBBLE_IDLE)
        {
            ninjabot.update();
        }
    }

    private void closeWobblegate()
    {
        // Pick up wobble goal
        ninjabot.wobbleGate.close();

        // Wait for the wobble gate to close
        while (!ninjabot.wobbleGate.isClosed())
        {
            ninjabot.update();
        }
    }

    private void openWobbleGate()
    {
        ninjabot.wobbleGate.open();

        // Wait for gate to open
        while (!ninjabot.wobbleGate.isOpen())
        {
            ninjabot.update();
        }
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
        static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(115, 75);
        //static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(181, 98);

        static final int REGION_WIDTH = 50;
        static final int REGION_HEIGHT = 50;

        final int FOUR_RING_THRESHOLD = 138;
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
