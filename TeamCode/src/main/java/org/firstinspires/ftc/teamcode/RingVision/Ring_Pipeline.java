package org.firstinspires.ftc.teamcode.RingVision;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.List;

public class Ring_Pipeline{
    private static final String VUFORIA_KEY = "AVf/E1n/////AAABmdmpK/BSpk2CsqjNWH2CbgJ3vzF4yBNs8E23FuAgf6bxJDLaISLFPXcVK2zFti6+PvQexl9t9tSP87VXP8rCgkgVzsMEfKLrU1/Lw37iyCp0ItD+DgXoRE0vEIEML77Zpl5Y3FifVaR5iZ4iVrpQ1T1tX2vIBndVAZmLxaTNZkcgDxwl/f5lxdJZ0ukhi2SRB8xc2MAMzJN4Sh0jUDGzncgajNXg6qJqwLGdEDrogl3lKc8/ddVZk4ELZ/5Ws+VDAM8lvJHWMFzc8sALnJtQfGKA4cIxfy25hTFwIu6KgjVypQgQKj2TgEyBKPwHdDdXuPm8M4Da1a3T7h/NTDXrmxi4YMz0wiZZ0ft4+4BiL3Az";
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";

    private static final double CAM_VFOV = Math.toRadians(20.2); // In degrees, angle of the height of the image at fixed length
    private static final double CAM_HFOV = Math.toRadians(44.7); // In degrees, angle of the width of the image at fixed length
    private static final double CAM_PHI = Math.toRadians(28); // In degrees, angle of view of camera lens
    private static final double CAM_HEIGHT = 3; //2; // Relative to height of ground in cm, height of camera
    private static final double CAM_LEFT = 3; //3.25; // In cm, camera x position relative to center of robot
    private static final double CAM_FRONT = 5; //7.75; //In cm, camera y position relative to center of robot
    private static final double DIST_THRESH = 1.5;
    public static double minX = 0;
    public static double minY = 0;
    public static double maxX = 640;
    public static double maxY = 480; // Debugging

    private VuforiaLocalizer vuforia;
    public TFObjectDetector tfod;
    private HardwareMap hwMap;
    private Telemetry telemetry;
    private ArrayList<Ring> rings;
    private FtcDashboard dash;


    public Ring_Pipeline(OpMode Op, HardwareMap hwmapp, FtcDashboard dashboard)
    {
        this.hwMap = hwmapp;
        this.telemetry = Op.telemetry;
        this.dash = dashboard;
        InitVuforia();
        InitTfod();
        if (this.tfod != null) {
            tfod.activate();
          //  tfod.setZoom(2.5, 16.0/9.0);
        }
        rings = new ArrayList<>();

    }


    private void InitVuforia(){
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = this.hwMap.get(WebcamName.class, "webcam");


        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        vuforia.setFrameQueueCapacity(60);


        // Loading trackables is not necessary for the TensorFlow Object Detection engine.

    }

    private void InitTfod()
    {
        int tfodMonitorViewId = this.hwMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", this.hwMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    public void StreamtoDash()
    {
        dash.setTelemetryTransmissionInterval(25);
        dash.startCameraStream(tfod, 0);
    }

    public ArrayList<Ring> DetectRings(Pose2d robot_pose)
    {
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        TelemetryPacket packet = new TelemetryPacket();
        Canvas fieldOverlay = packet.fieldOverlay();

        if (updatedRecognitions != null)
        {
            double cX, cY;
            int i = 0;
            double[] relCoords;
            Ring cRing;

            rings.clear();
            for (Recognition recognition : updatedRecognitions) {
                telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                        recognition.getLeft(), recognition.getTop());
                telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                        recognition.getRight(), recognition.getBottom());
                cX = ((recognition.getLeft() + recognition.getRight()) / 2);
                cY = ((recognition.getTop() + recognition.getBottom()) / 2);
                telemetry.addData("Centers: ", "x: " + cX + " y: " + cY);
                telemetry.update();

                cX = cX / 320 - 1; // [-1, 1]
                cY = 1 - cY / 480; // (0, 1)

                packet.put("Normalized coordinates: ", "X: " + cX + "Y: " + cY);

                relCoords = map2Dto3D(cX, cY);
                cRing = new Ring(relCoords[0], relCoords[1]);
                packet.put("Rel Coords: ", "x: " + cRing.getRelX() + " y: " + cRing.getRelY());

                rings.add(cRing);

            }

            //Remove Rings out of specified bounds
            clip_rings();

            //Filter Rings too close to each other
            filter_rings();

            // Project relative coords to real world coords
            setRRCoords(robot_pose);

            i = 0;
            for (Ring iring : rings)
            {

                packet.put("Ring " + i, "X: " + iring.getX() + "Y: " +  iring.getY());

                dash.sendTelemetryPacket(packet);
                i = i + 1;
            }

        }
        else
        {
            //rings.clear();
         //   telemetry.addData("PASSED INTO LOOP: ", "FALSE");
        }
        //telemetry.addData("Rings, ", rings.size());
        //telemetry.update();


        return new ArrayList<>(rings);
    }

    private double[] map2Dto3D(double xPix, double yPix) {
        double vfov = CAM_VFOV / 2;
        double hfov = CAM_HFOV / 2;
        double num = (Math.cos(-CAM_PHI - vfov)) / (2 * Math.sin(vfov)) + yPix * Math.sin(CAM_PHI);
        double den = (Math.sin(-CAM_PHI - vfov)) / (2 * Math.sin(vfov)) + yPix * Math.cos(CAM_PHI);
        double y = -CAM_HEIGHT * num / den;
        return new double[] {Math.tan(hfov) * xPix * Math.hypot(CAM_HEIGHT, y) - CAM_LEFT, y + CAM_FRONT};
    }

    private void setRRCoords(Pose2d robot)
    {
        for (Ring iRing : rings)
        {
            iRing.calcAbsCoords(robot.getX(), robot.getY(), robot.getHeading());
        }
    }

    private void filter_rings()
    {
        int i = 0;
        while (i < rings.size()) {
            double xi = rings.get(i).getX();
            double yi = rings.get(i).getY();
            int j = i + 1;

            while (j < rings.size()) {
                Ring ring = rings.get(j);
                if (Math.abs(xi - ring.getX()) < DIST_THRESH && Math.abs(yi - ring.getY()) < DIST_THRESH) {
                    // Log Removal of Rings for Testing
                    telemetry.addData("Original Ring: ", "x: " + xi + " y: " + yi);
                    telemetry.addData("Removed Ring:", "x: " + ring.getX() + " y: " + ring.getY());
                    //telemetry.update();
                    //rings.remove(j);
                } else {
                    j++;
                }
            }
            i++;
        }
    }

    private void clip_rings()
    {
        int i = 0;
        while (i < rings.size()) {
            Ring ring = rings.get(i);
            if (ring.getX() < minX || ring.getX() > maxX || ring.getY() < minY || ring.getY() > maxY) {
                telemetry.addData("Clipped Ring: ", "x: " + ring.getX() + " y: " + ring.getY());
                //telemetry.update();
                //rings.remove(i);

            } else {
                i++;
            }
        }
    }

}
