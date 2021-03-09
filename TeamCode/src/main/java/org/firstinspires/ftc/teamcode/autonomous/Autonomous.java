package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.ElapsedTime;

//import org.firstinspires.ftc.teamcode.anay.vision.OpenCVVision;
//import org.firstinspires.ftc.teamcode.anay.vision.OpenCVVision;
//import org.firstinspires.ftc.teamcode.anay.vision.OpenCvIndependent;
import org.firstinspires.ftc.teamcode.anay.vision.OpenCvIndependent;
import org.firstinspires.ftc.teamcode.common.DriveTrain;
import org.firstinspires.ftc.teamcode.common.Flicker;
import org.firstinspires.ftc.teamcode.common.Intake;
import org.firstinspires.ftc.teamcode.common.IntakeGate;
import org.firstinspires.ftc.teamcode.common.NinjaBot;
import org.firstinspires.ftc.teamcode.common.Path;
import org.firstinspires.ftc.teamcode.common.PathSeg;
import org.firstinspires.ftc.teamcode.common.Shooter;
import org.firstinspires.ftc.teamcode.common.WobbleGate;
import org.firstinspires.ftc.teamcode.common.WobbleMotor;
import org.firstinspires.ftc.teamcode.common.myblock;
import org.firstinspires.ftc.teamcode.common.myblock.SkystoneDeterminationPipeline;

import static java.lang.Thread.sleep;


@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Autonomous4Real")
public class Autonomous extends OpMode {

    private enum State
    {
        STATE_DETECT_RINGS,        // Detect ring state
        STATE_RING0_DRIVE2SHOOT,   // Ring 0 drive to shoot line to shoot stored rings
        STATE_WAIT_FOR_FLICKER,    // Wait for rings to be flicked (Ring 0, 1 & 4)
        STATE_RING0_WOBBLE_DROPOFF, // Ring 0 drop wobble goal into square
        STATE_RING0_TERMINATE,      // Ring 0 terminate program state
    };

    // ******************************** CONSTANTS FOR RING0 STATE MACHINE **************************
    // Path for ninjabot from start line to shooting line
    final PathSeg[] ring0ShootSeg =
    {
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_DRIVE, 54, 54, 1),
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_TURN_CLOCKWISE, 100, 0, 1),
    };
    final Path ring0ShootPath = new Path(ring0ShootSeg);

    // Path to square to drop off wobble goal after shooting
    final PathSeg[] ring0WobbleDropSeg =
    {
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_TURN_CLOCKWISE, 0, 5000, 1),
    };
    final Path ring0WobbleDropPath = new Path(ring0WobbleDropSeg);

    // Path from drop of square to center line (after dropping of wobble goal)
    final PathSeg[] ring0TermSeg =
    {
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_STRAFE_LEFT, 500, 0, 1),
        new PathSeg(PathSeg.PathSegType.PATH_SEG_TYPE_DRIVE, 500, 500, 1),
    };
    final Path ring0TermPath = new Path(ring0TermSeg);

    private double ring0shooterOn = 3000; // Time in msec to when shooter should turn on
    private double ring0shooterPower = 1.0; // Shooter power

    private NinjaBot ninjabot;
    private ElapsedTime timer;
    private ElapsedTime pathTimer;
    private ElapsedTime currentTime;
    private State state;
    private Path currentPath;

    private myblock.SkystoneDeterminationPipeline.RingPosition numRings;

    @Override
    public void init()
    {
        this.ninjabot = new NinjaBot(hardwareMap, telemetry);
        this.timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        this.pathTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        this.currentTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        this.currentPath = null;
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
        // Start autonomous timer
        this.timer.reset();

        // Init the drive train and go to initial state
        ninjabot.driveTrain.resetEncoders(DcMotor.RunMode.RUN_TO_POSITION);

        // Close the wobble gate
        ninjabot.wobbleGate.close();

        // TODO: Call detect() to detect rings....

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
            if (numRings == myblock.SkystoneDeterminationPipeline.RingPosition.FOUR)
            {
                // Four ring detected
            }
            else if (numRings == myblock.SkystoneDeterminationPipeline.RingPosition.ONE)
            {
                // One ring detected
            }
            else
            {
                // No ring detected. TODO: assert that no ring was detected
                // Drive to shooting line and shoot rings held in robot. While driving do the
                // following actions:
                // 1. Lower the wobble goal
                //
                // Note: We don't lower the intake servo as it is not needed

                // Lower wobble goal
                ninjabot.wobbleMotor.down();

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
                }
                else
                {  // Robot is still moving to shooter - if robot has been in the current path
                    // for atleast ring0shooterOn msec then turn on the shooter to get
                    // ready to shoot
                    if (this.pathTimer.milliseconds() > this.ring0shooterOn)
                    {
                        ninjabot.shooter.setPower(this.ring0shooterPower);
                        ninjabot.flicker.stop(); // This moves the flicker back as well
                    }

                    telemetry.addData("Ring 0:", "Drive2Shoot: " +
                                      this.currentPath.getSegmentIndex());
                }
            break;

            case STATE_WAIT_FOR_FLICKER:
                // Ninjabot is currently flicking - wait for all flicks to be complete before
                // stopping the shooter and moving to the next state
                if (ninjabot.flicker.flickerState == Flicker.State.STATE_FLICKER_STOPPED)
                { // Flicker has stopped - turn off shooter and determine next state
                    ninjabot.shooter.stop();

                    if (numRings == myblock.SkystoneDeterminationPipeline.RingPosition.FOUR)
                    {
                        // Four ring detected
                    }
                    else if (numRings == myblock.SkystoneDeterminationPipeline.RingPosition.ONE)
                    {
                        // One ring detected
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

                  // Sleep for 200 msec to open the wobbleGate before moving to shooting line
                  this.ninjaSleep(200);

                  // Move to terminate path
                  this.loadPath(this.ring0TermPath);

                  this.newState(State.STATE_RING0_TERMINATE);
                }
                else
                {
                    telemetry.addData("Ring 0:", "Terminate: " +
                            this.currentPath.getSegmentIndex());
                }
            break;

            case STATE_RING0_TERMINATE:
                // Reached the end of program stop ninjabot
                ninjabot.stop();
            break;
        }

        // Update ninjabot state machine and telemetry
        ninjabot.update();
        telemetry.update();
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
                    ninjabot.driveTrain.strafeRight((int) seg.lpos, seg.power);
                break;

                case PATH_SEG_TYPE_STRAFE_RIGHT:
                    ninjabot.driveTrain.strafeRight((int) seg.rpos, seg.power);
                break;
            }
        }
    }

    private boolean isPathComplete()
    {
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
        catch (InterruptedException e){}
    }
}
