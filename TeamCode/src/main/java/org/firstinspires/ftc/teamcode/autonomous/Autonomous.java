package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

//import org.firstinspires.ftc.teamcode.anay.vision.OpenCVVision;
//import org.firstinspires.ftc.teamcode.anay.vision.OpenCVVision;
//import org.firstinspires.ftc.teamcode.anay.vision.OpenCvIndependent;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.common.Flicker;
import org.firstinspires.ftc.teamcode.common.NinjaBot;
import org.firstinspires.ftc.teamcode.common.Path;
import org.firstinspires.ftc.teamcode.common.PathSeg;
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

import static java.lang.Thread.sleep;


@Config
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Autonomous4Real")
public class Autonomous extends OpMode {

    private enum State
    {
        // Common States
        STATE_DETECT_RINGS,        // Detect ring state
        STATE_DRIVE_STACK,         // Drive to ring stack (ring 1/4) to shoot
        STATE_WAIT_FOR_FLICKER,    // Wait for rings to be flicked (Ring 0, 1 & 4)
        STATE_AUTONOMOUS_TERMINATE,  // Autonomous program terminate
        STATE_AUTONOMOUS_STOPPED,   // Ninjabot has stopped

        // Ring 0 states
        STATE_RING0_DRIVE2SHOOT,   // Ring 0 drive to shoot line to shoot stored rings
        STATE_RING0_WOBBLE_DROPOFF, // Ring 0 drop wobble goal into square

        // Ring 1 state
        STATE_RING1_COLLECT,       // Collect 1 ring from the field
        STATE_RING1_WOBBLE_DROPOFF, // Ring 1 drop wobble goal into square

        // Ring 4 states
        STATE_RING4_COLLECT3,      // Collect 3 rings from field
        STATE_RING4_COLLECT4,      // Collect 4th ring from field
        STATE_RING4_WOBBLE_DROPOFF, // Ring 4 drop wobble goal into square
    }

    // ******************************** CONSTANTS FOR COMMON STATE *********************************
    // Path for ninjabot from start line to shooting stack
    final PathSeg[] driverStackSeg =
    {
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_DRIVE, 17, 17, 1),
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_STRAFE_RIGHT, 0, 1450, 1),
    };
    final Path driverStackPath = new Path(driverStackSeg);

    // Strafe diagonal to stacks
    final PathSeg[] driverStackDiagSeg =
    {
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_STRAFE_NE, 0, 3000, 1),
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_TURN_CLOCKWISE, 0, 160, 1),
    };
    final Path driverStackDiagPath = new Path(driverStackDiagSeg);

    public static double wobbleDownMsec = 1000; // Time in msec to when shooter should turn on
    public static long   intakeDelay    = 1500; // Intake delay in msec. Wait to shoot for this long

    // ******************************** CONSTANTS FOR RING0 STATE MACHINE **************************
    // Path for ninjabot from start line to shooting line
    final PathSeg[] ring0ShootSeg =
    {
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_DRIVE, 30, 30, 1),
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_TURN_CLOCKWISE, 0, 340, 1),
    };
    final Path ring0ShootPath = new Path(ring0ShootSeg);

    // Path to square to drop off wobble goal after shooting
    final PathSeg[] ring0WobbleDropSeg =
    {
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_DRIVE, 23, 23, 1),
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_TURN_CLOCKWISE, 0, 3600, 1),
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_DRIVE, -19, -19, 1),
    };
    final Path ring0WobbleDropPath = new Path(ring0WobbleDropSeg);

    // Path from drop of square to center line (after dropping of wobble goal)
    final PathSeg[] ring0TermSeg =
    {
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_DRIVE, 4, 4, 1),
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_STRAFE_LEFT, 1200, 0, 1),
    };
    final Path ring0TermPath = new Path(ring0TermSeg);

    public static double ring0shooterPower = 0.78; // Shooter power

    // *************************** CONSTANTS FOR RING1 STATE MACHINE ***********************
    // Path for ninjabot to collect 1 ring from the field
    final PathSeg[] ring1CollectSeg =
    {
        //new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_TURN_COUNTER_CLOCKWISE, 45, 0, 1),
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_DRIVE, 10, 10, 1)
    };
    final Path ring1CollectPath = new Path(ring1CollectSeg);

    // Drive to drop off wobble goal in square
    final PathSeg[] ring1WobbleDropSeg =
    {
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_DRIVE, 43, 43, 1),
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_DRIVE, 0, 39, 1),
        //new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_STRAFE_LEFT, 3000, 0, 1),
    };
    final Path ring1WobbleDropPath = new Path(ring1WobbleDropSeg);

    // Path from drop of square to center line (after dropping of wobble goal)
    final PathSeg[] ring1TermSeg =
    {
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_STRAFE_LEFT, 2000, 0, 1),
    };
    final Path ring1TermPath = new Path(ring1TermSeg);

    private boolean ring1collect = false;

    // *************************** CONSTANTS FOR RING4 STATE MACHINE ***********************
    // Drive to collect 3 rings from the field with intake gate
    final PathSeg[] ring4CollectSeg =
    {
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_TURN_COUNTER_CLOCKWISE, 50, 0, 1),
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_DRIVE, 17, 17, 0.17),
    };
    final Path ring4CollectPath = new Path(ring4CollectSeg);

    // Drive to collect 4th ring from the field with intake gate
    final PathSeg[] ring4CollectSeg1 =
    {
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_DRIVE, 13, 13, 1),
    };
    final Path ring4CollectPath1 = new Path(ring4CollectSeg1);

    // Drive to drop off wobble goal in square
    final PathSeg[] ring4WobbleDropSeg =
    {
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_DRIVE, 5, 5, 1),
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_DRIVE, 37, 100, 1),
    };
    final Path ring4WobbleDropPath = new Path(ring4WobbleDropSeg);

    // Path from drop of square to center line (after dropping of wobble goal)
    final PathSeg[] ring4TermSeg =
    {
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_DRIVE, 4, 30, 1),
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_DRIVE, 10, 10, 1),
    };
    final Path ring4TermPath = new Path(ring4TermSeg);

    public static double ring4shooterPowerInitial = 0.78; // Shooter power for initial 3 rings - Changed from 0.79
    private boolean ring4collect3 = false;
    private boolean ring4collect4 = false;
    public double ring4shooterPowerSecondary = 0.77; // Shooter power for 3 field rings
    public double ring4shooterPowerTertiary = 0.78; // Shooter power for last field ring

    // *********************************** AUTONOMOUS STATE **************************************
    private NinjaBot ninjabot;
    private ElapsedTime timer;
    private ElapsedTime pathTimer;
    private ElapsedTime currentTime;
    private State state;
    private Path currentPath;
    private boolean wobbleDown = false;
    private final boolean detected = false;
    private FtcDashboard dashboard;
    private TelemetryPacket packet;

    public SkystoneDeterminationPipeline pipeline;
    public SkystoneDeterminationPipeline.RingPosition numRings = SkystoneDeterminationPipeline.RingPosition.UNKNOWN;
    OpenCvWebcam webcam;

    @Override
    public void init()
    {
        // Ninjabot has a drive train
        this.ninjabot = new NinjaBot(hardwareMap, telemetry, true);
        this.timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        this.pathTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        this.currentTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        this.currentPath = null;
        this.dashboard = FtcDashboard.getInstance();
        this.dashboard.setTelemetryTransmissionInterval(25);
        this.packet = new TelemetryPacket();
        this.detect();
    }

    @Override
    public void init_loop()
    {
        // Reset drivetrain incase it is moved during init
        ninjabot.driveTrain.resetEncoders(DcMotor.RunMode.RUN_TO_POSITION);
        this.numRings = SkystoneDeterminationPipeline.position;

        switch (numRings)
        {
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

        telemetry.addData("Analysis: ", pipeline.getAnalysis());
        telemetry.addData("Rings: ", SkystoneDeterminationPipeline.position);
        telemetry.update();
    }

    @Override
    public void start()
    {
        // Start autonomous timer
        this.timer.reset();

        // Init the drive train and go to initial state
        ninjabot.driveTrain.resetEncoders(DcMotor.RunMode.RUN_TO_POSITION);

        // Close the wobble gate
        ninjabot.wobbleGate.close();

        // Start with initial state to detect rings
        this.newState(State.STATE_DETECT_RINGS);
    }

    @Override
    public void loop()
    {
        switch (state)
        {
            // Detect number of rings
            case STATE_DETECT_RINGS:
            if (this.numRings == SkystoneDeterminationPipeline.RingPosition.UNKNOWN)
            {
                // Detect rings
                numRings = SkystoneDeterminationPipeline.position;
                break;
            }
            if (this.numRings == SkystoneDeterminationPipeline.RingPosition.FOUR)
            {
                // Four ring detected
               this.loadPath(this.driverStackDiagPath);
               this.newState(State.STATE_DRIVE_STACK);
            }
            else if (this.numRings == SkystoneDeterminationPipeline.RingPosition.ONE)
            {
                // One ring detected - same path as ring 4 initially
                this.loadPath(this.driverStackDiagPath);
                this.newState(State.STATE_DRIVE_STACK);
            }
            else
            {
                // No ring detected. TODO: assert that no ring was detected
                // Drive to shooting line and shoot rings held in robot. While driving do the
                // following actions:
                // 1. Lower the wobble goal
                //
                // Note: We don't lower the intake servo as it is not needed

                // Load path to shooting line
                this.loadPath(ring0ShootPath);

                // Move state to RING0_DRIVE2SHOOT
                this.newState(State.STATE_RING0_DRIVE2SHOOT);
            }
            break;

            // Drive to shoot for ring 0
            case STATE_RING0_DRIVE2SHOOT:
                // Wait till ninjabot raches the shooting line and has turned
                if (this.isPathComplete())
                {
                    // Shooter can start shooting the rings now - shoot 4 times
                    ninjabot.flicker.flick(4);

                    // Wait for the shooter to complete shooting
                    this.newState(State.STATE_WAIT_FOR_FLICKER);

                    telemetry.addData("Ring 0:", "Starting2Shoot");
                }
                else
                {  // Robot is still moving to position - if robot has been in the current path
                    //for atleast wobbleDownMsec then drop down wobble goal and turn on the shooter
                    //so we are ready to shoot when we get there
                    if (!this.wobbleDown && (this.pathTimer.milliseconds() > Autonomous.wobbleDownMsec))
                    {
                        dropWobbleAndPowerShooter(Autonomous.ring0shooterPower);
                    }

                    telemetry.addData("Ring 0:", "Drive2Shoot: " +
                                      this.currentPath.getSegmentIndex());
                }
            break;

            case STATE_WAIT_FOR_FLICKER:
                // Ninjabot is currently flicking - wait for all flicks to be complete before
                // stopping the shooter and moving to the next state
               // if (ninjabot.flicker.flickerState == Flicker.State.STATE_FLICKER_STOPPED)
                if (ninjabot.flicker.isStopped())
                { // Flicker has stopped
                    if (this.numRings == SkystoneDeterminationPipeline.RingPosition.FOUR)
                    {
                        if (!this.ring4collect3) {
                            // Four rings - move forward to collect 3 rings with the intake gate
                            this.loadPath(this.ring4CollectPath);
                            this.newState(State.STATE_RING4_COLLECT3);
                            this.ring4collect3 = true;
                            // Turn down shooter power
                            ninjabot.shooter.setPower(this.ring4shooterPowerSecondary);

                            // Start the intake
                            ninjabot.intake.start();
                        }
                        else if (!this.ring4collect4)
                        {   // Finished flicking of 3 rings. Load path to pick up 4th ring
                            this.loadPath(this.ring4CollectPath1);
                            this.newState(State.STATE_RING4_COLLECT4);
                            this.ring4collect4 = true;
                            // Turn down shooter power for 4th ring
                            ninjabot.shooter.setPower(this.ring4shooterPowerTertiary);
                        }
                        else
                        {
                            // Load path to drop off wobble goal
                            this.loadPath(this.ring4WobbleDropPath);
                            this.newState(State.STATE_RING4_WOBBLE_DROPOFF);

                            // Stop the shooter
                            ninjabot.shooter.stop();

                            // Raise the intake gate
                            ninjabot.intakeGate.raise();
                        }
                    }
                    else if (this.numRings == SkystoneDeterminationPipeline.RingPosition.ONE)
                    {
                        if (!this.ring1collect) {
                            // Move move forward and collect the ring.
                            this.loadPath(this.ring1CollectPath);
                            this.newState(State.STATE_RING1_COLLECT);
                            ninjabot.intake.start();
                            ninjabot.shooter.setPower(Autonomous.ring4shooterPowerInitial - 0.01);
                            this.ring1collect = true;
                        }
                        else
                        { // Load path to drop off wobble goal
                          this.loadPath(this.ring1WobbleDropPath);
                          this.newState(State.STATE_RING1_WOBBLE_DROPOFF);

                          // Stop the shooter
                          ninjabot.shooter.stop();
                        }
                    }
                    else
                    { // Rotate into square to drop wobble goal
                      this.loadPath(this.ring0WobbleDropPath);
                      this.newState(State.STATE_RING0_WOBBLE_DROPOFF);
                    }
                }
            break;
            case STATE_RING0_WOBBLE_DROPOFF:
                // Wait for ninjabot to move into square to drop off wobble goal
                if (this.isPathComplete())
                { // Open wobble gate and load path to move to center shooting line
                  ninjabot.wobbleGate.open();

                  // Sleep for 100 msec to open the wobbleGate before moving to shooting line
                  this.ninjaSleep(100);

                  // Move to terminate path
                  this.loadPath(this.ring0TermPath);

                  this.newState(State.STATE_AUTONOMOUS_TERMINATE);
                }
                else
                {
                    telemetry.addData("Ring 0:", "Terminate: " +
                            this.currentPath.getSegmentIndex());
                }
            break;

            case STATE_DRIVE_STACK:
                // Wait till we finish reaching the stack to start shooting. On the way up drop the
                // wobble goal as well as start the shooter
                if (this.isPathComplete())
                {
                    // Ninjabot can start shooting the rings now - shoot 4 times
                    ninjabot.flicker.flick(4);

                    // Wait for the shooter to complete shooting
                    this.newState(State.STATE_WAIT_FOR_FLICKER);



                    if (!this.wobbleDown)
                    {
                        // Drop wobble goal down as we have already reached position
                        dropWobbleAndPowerShooter(Autonomous.ring4shooterPowerInitial);

                        // For 4 rings also need to drop down the intake gate so we can push the
                        // rings

                        if (this.numRings == SkystoneDeterminationPipeline.RingPosition.FOUR) {
                            ninjabot.intakeGate.lower();
                        }

                        // Anay + Vasu : Strafe a bit because the intake is not aligned

                        if (numRings != SkystoneDeterminationPipeline.RingPosition.NONE){
                            this.ninjabot.driveTrain.strafeRight(700, 1.0);
                        }
                    }

                    telemetry.addData("Drive2Stack:", "Initial Shots");
                }
                else
                {   // Robot is still moving to stack - if robot has been in the current path
                    // for atleast wobbleDownMsec then drop down wobble goal and turn on the shooter
                    //  so we are ready to shoot when we get there
                    if (!this.wobbleDown && (this.pathTimer.milliseconds() > Autonomous.wobbleDownMsec))
                    {
                        dropWobbleAndPowerShooter(Autonomous.ring4shooterPowerInitial);

                        // For 4 rings also need to drop down the intake gate so we can push the
                        // rings
                        if (this.numRings == SkystoneDeterminationPipeline.RingPosition.FOUR) {
                            ninjabot.intakeGate.lower();
                        }
                    }

                    telemetry.addData("Driving2Stack", "Segment: " +
                            this.currentPath.getSegmentIndex());
                }
            break;

            case STATE_RING1_COLLECT:
                // Moving forward to collect one ring from the field
                if (this.isPathComplete())
                {
                    // Sleep for 1200 msec for rings to intake
                    this.ninjaSleep(Autonomous.intakeDelay + 1000);

                    // Ninjabot can start shooting the fourth ring - shoot 2 times
                    ninjabot.flicker.flick(2);

                    // Wait for the shooter to complete shooting
                    this.newState(State.STATE_WAIT_FOR_FLICKER);

                    // Turn off the intake
                    ninjabot.intake.stop();

                    telemetry.addData("Ring 1:", "Field Ring Flick");
                }
                else
                {
                    telemetry.addData("Ring 1:", "Collecting field ring");
                }
            break;

            case STATE_RING1_WOBBLE_DROPOFF:
                // Driving to drop off wobble goal
                if (this.isPathComplete())
                {
                    // Open wobble gate and load path to move to center shooting line
                    ninjabot.wobbleGate.open();

                    // Sleep for 100 msec to open the wobbleGate before moving to shooting line
                    this.ninjaSleep(100);

                    // Move the wobble goal up
                    ninjabot.wobbleMotor.raise();
                    this.ninjaSleep(30);
                    ninjabot.wobbleGate.close();

                    // Move to terminate path
                    this.loadPath(this.ring1TermPath);
                    this.newState(State.STATE_AUTONOMOUS_TERMINATE);
                }
                else
                {
                    telemetry.addData("Ring 1:", "Dropping off wobble goal");
                }
            break;

            case STATE_RING4_COLLECT3:
                // Moving forward to collect 3 rings from the field
                if (this.isPathComplete())
                {
                    // Wait for 500 msec for rings to intake
                    this.ninjaSleep(700);

                    // Ninjabot can start shooting the rings now - shoot 3 times
                    ninjabot.flicker.flick(3);

                    // Wait for the shooter to complete shooting
                    this.newState(State.STATE_WAIT_FOR_FLICKER);

                    telemetry.addData("Ring 4:", "Field 3 Ring Flick");
                }
                else
                {
                    if (0 == (Math.round(this.pathTimer.milliseconds() / 10) % 5))
                        ninjabot.intakeGate.mid();
                    else if (0 == (Math.round(this.pathTimer.milliseconds() / 10) % 10))
                        ninjabot.intakeGate.lower();

                    telemetry.addData("Ring 4:", "Collecting 3 field rings");
                }
            break;

            case STATE_RING4_COLLECT4:
                // Moving forward to collect 4th ring from the field
                if (this.isPathComplete())
                {
                    // Sleep for 1200 msec for rings to intake
                    this.ninjaSleep(1000);

                    // Ninjabot can start shooting the fourth ring - shoot 2 times
                    ninjabot.flicker.flick(2);

                    // Wait for the shooter to complete shooting
                    this.newState(State.STATE_WAIT_FOR_FLICKER);

                    // Turn off the intake
                    ninjabot.intake.stop();

                    telemetry.addData("Ring 4:", "Field 4th Ring Flick");
                }
                else
                {
                    telemetry.addData("Ring 4:", "Collecting last field ring");
                }
            break;

            case STATE_RING4_WOBBLE_DROPOFF:
                // Driving to drop off wobble goal
                if (this.isPathComplete())
                {
                    // Open wobble gate and load path to move to center shooting line
                    ninjabot.wobbleGate.open();

                    // Sleep for 100 msec to open the wobbleGate before moving to shooting line
                    this.ninjaSleep(100);

                    // Move the wobble goal up
                    ninjabot.wobbleMotor.raise();
                    this.ninjaSleep(30);
                    ninjabot.wobbleGate.close();

                    // Move to terminate path
                    this.loadPath(this.ring4TermPath);

                    this.newState(State.STATE_AUTONOMOUS_TERMINATE);
                }
                else
                {
                    telemetry.addData("Ring 4:", "Dropping off wobble goal");
                }
            break;

            case STATE_AUTONOMOUS_TERMINATE:
                if (this.isPathComplete())
                { // Reached the end of autonomous program. Terminate
                  ninjabot.stop();

                  this.newState(State.STATE_AUTONOMOUS_STOPPED);
                }
                else
                {
                    telemetry.addData("Terminating: ", "Segment:  " +
                            this.currentPath.getSegmentIndex());
                }
            break;
            case STATE_AUTONOMOUS_STOPPED:
                // Ninjabot is stopped
            break;
        }

        telemetry.addData("State: ", this.state);

        // Update ninjabot state machine and telemetry
        ninjabot.update();
        telemetry.update();
        dashboard.sendTelemetryPacket(packet);
    }

    @Override
    public void stop()
    {
        // Stop the bot
        ninjabot.stop();
    }

    // Load path to navigate
    private void loadPath(Path path)
    {
        this.currentPath = path;
        this.currentPath.resetPath();
        this.pathTimer.reset();
        this.loadPathSegment();
    }

    private void loadPathSegment()
    {
        if ((this.currentPath != null) && !this.currentPath.isPathComplete())
        {
            PathSeg seg = this.currentPath.getNextSegment();

            switch(seg.type)
            {
                case PATH_SEG_TYPE_DRIVE:
                    ninjabot.driveTrain.drive(seg.lpos, seg.rpos, seg.power);
                break;

                case PATH_SEG_TYPE_TURN_CLOCKWISE:
                    ninjabot.driveTrain.clockwiseTurn((int) seg.rpos, seg.power);
                break;

                case PATH_SEG_TYPE_TURN_COUNTER_CLOCKWISE:
                    ninjabot.driveTrain.counterClockWiseTurn((int) seg.lpos, seg.power);
                break;

                case PATH_SEG_TYPE_STRAFE_LEFT:
                    ninjabot.driveTrain.strafeLeft((int) seg.lpos, seg.power);
                break;

                case PATH_SEG_TYPE_STRAFE_RIGHT:
                    ninjabot.driveTrain.strafeRight((int) seg.rpos, seg.power);
                break;

                case PATH_SEG_TYPE_STRAFE_NE:
                    ninjabot.driveTrain.strafeNE((int) seg.rpos, seg.power);
                break;

                case PATH_SEG_TYPE_STRAFE_SE:
                    ninjabot.driveTrain.strafeSE((int) seg.rpos, seg.power);
                break;

                case PATH_SEG_TYPE_STRAFE_SW:
                    ninjabot.driveTrain.strafeSW((int) seg.lpos, seg.power);
                break;

                case PATH_SEG_TYPE_STRAFE_NW:
                    ninjabot.driveTrain.strafeNW((int) seg.lpos, seg.power);
                break;
            }
        }
    }

    private boolean isPathComplete()
    {
        if (this.currentPath == null)
        { // No path so say complete
            return true;
        }

        if (!ninjabot.driveTrain.isMoving())
        {
            // If any more path segments then load them else we are done
            if (!this.currentPath.isPathComplete())
            {
                this.loadPathSegment();
            }
            else
            {   // Path is complete
                this.currentPath = null;
                return true;
            }
        }

        // Drive train is moving (or we have additional path segments to navigate
        return false;
    }

    private void newState(State newState)
    {
        this.state = newState;
    }

    private void ninjaSleep(long msec)
    {
        try {
            sleep(msec);
        }
        catch (InterruptedException e)
        {
            telemetry.addData("NinjaSleep Exception: ", e);
        }
    }

    // Drop wobble goal and power shooter on way to shooting
    private void dropWobbleAndPowerShooter(double power)
    {
        // Lower wobble goal
        ninjabot.wobbleMotor.down();
        // Start shooter
        ninjabot.shooter.setPower(power);
        // Move flicker back
        ninjabot.flicker.stop();

        this.wobbleDown = true;
    }

    private SkystoneDeterminationPipeline.RingPosition detect() {

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "webcam"), cameraMonitorViewId);

        pipeline = new SkystoneDeterminationPipeline();

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
        return SkystoneDeterminationPipeline.position;
    }

    public static class SkystoneDeterminationPipeline extends OpenCvPipeline {

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
        public static volatile RingPosition position = RingPosition.UNKNOWN;

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
