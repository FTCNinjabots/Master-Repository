package org.firstinspires.ftc.teamcode.anay.vision;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.vuforia.HINT;
import com.vuforia.Vuforia;
// IMPORTANT: X is left - right, Y is up and down, Z if forward and back
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.R;

/*
 * This OpMode was written for the VuforiaDemo Basics video. This demonstrates basic principles of
 * using VuforiaDemo in FTC.
 */
@Autonomous(name = "Vuforia_2")
public class Vuforia_2 extends LinearOpMode
{
    // Variables to be used for later
    private DcMotor bl;
    private DcMotor br;
    private DcMotor fl;
    private DcMotor fr;
    private WebcamName webcam;
    private VuforiaLocalizer vuforiaLocalizer;
    private VuforiaLocalizer.Parameters parameters;
    private VuforiaTrackables visionTargets;
    private VuforiaTrackable target;
    private VuforiaTrackable target1;
    public VuforiaTrackableDefaultListener listener;
    public VuforiaTrackableDefaultListener listener1;

    public OpenGLMatrix lastKnownLocation;
    private OpenGLMatrix phoneLocation;

    private static final String VUFORIA_KEY = "AVf/E1n/////AAABmdmpK/BSpk2CsqjNWH2CbgJ3vzF4yBNs8E23FuAgf6bxJDLaISLFPXcVK2zFti6+PvQexl9t9tSP87VXP8rCgkgVzsMEfKLrU1/Lw37iyCp0ItD+DgXoRE0vEIEML77Zpl5Y3FifVaR5iZ4iVrpQ1T1tX2vIBndVAZmLxaTNZkcgDxwl/f5lxdJZ0ukhi2SRB8xc2MAMzJN4Sh0jUDGzncgajNXg6qJqwLGdEDrogl3lKc8/ddVZk4ELZ/5Ws+VDAM8lvJHWMFzc8sALnJtQfGKA4cIxfy25hTFwIu6KgjVypQgQKj2TgEyBKPwHdDdXuPm8M4Da1a3T7h/NTDXrmxi4YMz0wiZZ0ft4+4BiL3Az"; // Insert your own key here

    private float robotX = 0;
    private float robotY = 0;
    private float robotAngle = 0;

    public void runOpMode() throws InterruptedException
    {
        setupVuforia();

        // We don't know where the robot is, so set it to the origin
        // If we don't include this, it would be null, which would cause errors later on
        lastKnownLocation = createMatrix(0, 0, 0, 0, 0, 0);

        //waitForStart();

        // Start tracking the targets
        visionTargets.activate();
        /*
        while(opModeIsActive())
        {
            // Ask the listener for the latest information on where the robot is
            OpenGLMatrix latestLocation = null;
            if (listener.isVisible())
            {
                latestLocation = listener.getUpdatedRobotLocation();
            }
            else if (listener1.isVisible()){
                latestLocation = listener1.getUpdatedRobotLocation();
            }


            // The listener will sometimes return null, so we check for that to prevent errors
            if(latestLocation != null)
                lastKnownLocation = latestLocation;

            float[] coordinates = lastKnownLocation.getTranslation().getData();

            robotX = coordinates[0];
            robotY = coordinates[1];
            robotAngle = Orientation.getOrientation(lastKnownLocation, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;

            // Send information about whether the target is visible, and where the robot is
            if (listener.isVisible()){
                telemetry.addData("Tracking " + target.getName(), listener.isVisible());
                telemetry.addData("Last Known Location", formatMatrix(lastKnownLocation));

            }
            else if (listener1.isVisible()){
                telemetry.addData("Tracking " + target1.getName(), listener1.isVisible());
                telemetry.addData("Last Known Location", formatMatrix(lastKnownLocation));
            }
            else {
                telemetry.addData("Targets Seen: ", "None");
            }


            // Send telemetry and idle to let hardware catch up
            telemetry.update();
            idle();
        } */
    }

    public void setupVuforia()
    {
        // Setup parameters to create localizer
        webcam = hardwareMap.get(WebcamName.class, "webcam");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.useExtendedTracking = false;
        vuforiaLocalizer = ClassFactory.getInstance().createVuforia(parameters);

        // These are the vision targets that we want to use
        // The string needs to be the name of the appropriate .xml file in the assets folder
        visionTargets = vuforiaLocalizer.loadTrackablesFromAsset("UltimateGoal");
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 3);

        // Setup the target to be tracked
        target = visionTargets.get(3); // 0 corresponds to the wheels target
        target.setName("Blue Alliance");
        target.setLocation(createMatrix(0, 0, 90, 0, 0, 0));


        target1 = visionTargets.get(0);
        target1.setName("Blue Tower Goal");
        target1.setLocation(createMatrix(0, 0, 150, 0, 0, 0));

        // Set phone location on robot
        phoneLocation = createMatrix(0, 0, 0, 0, 0, 0);

        // Setup listener and inform it of phone information
        listener = (VuforiaTrackableDefaultListener) target.getListener();
        listener.setPhoneInformation(phoneLocation, parameters.cameraDirection);
        listener1 = (VuforiaTrackableDefaultListener) target1.getListener();
        listener1.setPhoneInformation(phoneLocation, parameters.cameraDirection);

    }

    // Creates a matrix for determining the locations and orientations of objects
    // Units are millimeters for x, y, and z, and degrees for u, v, and w
    public OpenGLMatrix createMatrix(float x, float y, float z, float u, float v, float w)
    {
        return OpenGLMatrix.translation(x, y, z).
                multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, u, v, w));
    }

    // Formats a matrix into a readable string
    public String formatMatrix(OpenGLMatrix matrix)
    {
        return matrix.formatAsTransform();
    }
    public void Vuforia_Move(OpenGLMatrix current_pos, OpenGLMatrix target_pos)
    {
        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");

        br.setDirection(DcMotor.Direction.REVERSE);
        fr.setDirection(DcMotor.Direction.REVERSE);


        // We want to make code to strafe the x and move forward untill the Z's are the same
        float[] current_coordinates = current_pos.getTranslation().getData();
        int current_x_pos = (int) current_coordinates[0];
        int current_z_pos = (int) current_coordinates[2];

        float[] target_coordinates = target_pos.getTranslation().getData();
        int target_x_pos = (int) target_coordinates[0];
        int target_z_pos = (int) current_coordinates[2];

        int strafeleft = -1;
        if (current_x_pos > target_x_pos){
            strafeleft = 1;
        }

        while (current_x_pos != target_x_pos){
            //strafe
            bl.setPower(0.25 * strafeleft);
            br.setPower(0.25 * strafeleft);
            fl.setPower(0.25 * -1 * strafeleft);
            fr.setPower(0.25 * -1 * strafeleft);

            OpenGLMatrix latestLocation = null;
            if (listener.isVisible())
            {
                latestLocation = listener.getUpdatedRobotLocation();
            }
            else if (listener1.isVisible()){
                latestLocation = listener.getUpdatedRobotLocation();
            }
            else{ // Predicting Extended Tracking
                float[] temp_coordinates = lastKnownLocation.getTranslation().getData();
                temp_coordinates[0] += strafeleft * -1;
                latestLocation = createMatrix(temp_coordinates[0], temp_coordinates[1], temp_coordinates[2], temp_coordinates[3], temp_coordinates[4], temp_coordinates[5]);

            }


            // The listener will sometimes return null, so we check for that to prevent errors

            lastKnownLocation = latestLocation;
            float[] coordinates = lastKnownLocation.getTranslation().getData();



            current_x_pos = (int) coordinates[0];



        }

        int forward = 1;
        if (current_z_pos < target_z_pos) {
            forward = -1;
        }
        while (current_z_pos != target_z_pos){
            bl.setPower(0.5 * forward);
            br.setPower(0.5 * forward);
            fl.setPower(0.5 * forward);
            fr.setPower(0.5 * forward);

            OpenGLMatrix latestLocation = null;
            if (listener.isVisible())
            {
                latestLocation = listener.getUpdatedRobotLocation();
            }
            else if (listener1.isVisible()){
                latestLocation = listener.getUpdatedRobotLocation();
            }


            // The listener will sometimes return null, so we check for that to prevent errors
            if(latestLocation != null)
                lastKnownLocation = latestLocation;

            float[] coordinates = lastKnownLocation.getTranslation().getData();

            current_z_pos = (int) coordinates[2];



        }
    }
}
