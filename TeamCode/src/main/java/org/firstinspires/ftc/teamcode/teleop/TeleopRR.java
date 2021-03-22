package org.firstinspires.ftc.teamcode.teleop;

import android.annotation.SuppressLint;

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

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.common.Flicker;
import org.firstinspires.ftc.teamcode.common.NinjaBot;
import org.firstinspires.ftc.teamcode.common.WobbleGate;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.List;

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
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";
    private static final String VUFORIA_KEY =
            "AVf/E1n/////AAABmdmpK/BSpk2CsqjNWH2CbgJ3vzF4yBNs8E23FuAgf6bxJDLaISLFPXcVK2zFti6+PvQexl9t9tSP87VXP8rCgkgVzsMEfKLrU1/Lw37iyCp0ItD+DgXoRE0vEIEML77Zpl5Y3FifVaR5iZ4iVrpQ1T1tX2vIBndVAZmLxaTNZkcgDxwl/f5lxdJZ0ukhi2SRB8xc2MAMzJN4Sh0jUDGzncgajNXg6qJqwLGdEDrogl3lKc8/ddVZk4ELZ/5Ws+VDAM8lvJHWMFzc8sALnJtQfGKA4cIxfy25hTFwIu6KgjVypQgQKj2TgEyBKPwHdDdXuPm8M4Da1a3T7h/NTDXrmxi4YMz0wiZZ0ft4+4BiL3Az";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;



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
        initVuforia();
        initTfod();
        if (tfod != null) {
            tfod.activate();
        }

    }

    @Override
    public void init_loop()
    {

    }

    @Override
    public void start()
    {

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void loop() {


        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                // step through the list of recognitions and display boundary info.
                int i = 0;
                for (Recognition recognition : updatedRecognitions) {
                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());
                }
            }
        }


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

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "webcam");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }
}

