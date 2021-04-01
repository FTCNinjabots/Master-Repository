package org.firstinspires.ftc.teamcode.RingVision;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.ArrayList;
import java.util.List;

public class RingDetector {
    public static final double minX = 0;
    public static final double maxX = 240;
    public static final double minY = 0;
    public static final double maxY = 320;
    public static final double RING_DIST_THRESH = 1.5;
    private OpenCvWebcam cam;
    private Telemetry telemetry;
    private Ring_Pipeline pipeline;
    public static TelemetryPacket packet = new TelemetryPacket();
    public static ArrayList<Ring> seen_rings;

    public RingDetector(LinearOpMode op){
        int cameraMonitorViewId = op.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", op.hardwareMap.appContext.getPackageName());
        cam = OpenCvCameraFactory.getInstance().createWebcam(op.hardwareMap.get(WebcamName.class, "webacam"), cameraMonitorViewId);
        this.telemetry = op.telemetry;
        setPipeline(pipeline);
    }

    public void start() {
        cam.openCameraDeviceAsync(() -> cam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT));
        FtcDashboard.getInstance().startCameraStream(cam, 0);
    }

    public void stop() {
        cam.stopStreaming();
        cam.closeCameraDevice();
        FtcDashboard.getInstance().stopCameraStream();
    }

    public void setPipeline(Ring_Pipeline Pipeline) {
        cam.setPipeline(Pipeline);
    }
    public void drawRings(){
        seen_rings = pipeline.getRings();
        int i = 0;
        while(i < seen_rings.size()){
            drawRing(seen_rings.get(i));
        }
    }

    public static void drawRing(Ring ring) {
        drawRing(ring, "orange");
    }

    public static void drawRing(Ring ring, String color) {
        double x = ring.getY() - 72;
        double y = 72 - ring.getX();
        packet.fieldOverlay().setFill(color).fillCircle(x, y, 2.5);
        packet.fieldOverlay().setFill("white").fillCircle(x, y, 1.5);
    }



}

class Ring_Pipeline extends OpenCvPipeline {
    public static double HEIGHT_MIN = 15;
    public static double HEIGHT_MAX = 130;
    public static double WIDTH_MIN = 3;
    public static double WIDTH_MAX = 75;
    public static double ANGLE_MIN = 70;
    public static double ANGLE_MAX = 110;

    // Camera Constants
    public static double CAM_HEIGHT = 19;
    public static double CAM_FRONT = 3.25;
    public static double CAM_LEFT = 7.75;
    public static double CAM_PHI = Math.toRadians(28);
    public static double CAM_VFOV = Math.toRadians(40);
    public static double CAM_HFOV = Math.toRadians(55);

    // Image Processing Mats
    private RingProcessor processor;
    private Mat processed = new Mat();

    // Ellipse Variables
    private double width;
    private double height;
    private double angle;
    private double xPix;
    private double yPix;

    // Ring Position Variables
    private ArrayList<Ring> rings = new ArrayList<>();
    private ArrayList<Ring> prevRings = new ArrayList<>();

    public Ring_Pipeline() {
        processor = new RingProcessor("locator");
    }

    @Override
    public Mat processFrame(Mat input) {
        // Process Image
        processed = processor.processFrame(input)[0];

        // Find Contours
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(processed, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        // Loop Through Contours
        for (MatOfPoint contour : contours) {
            Point[] contourArray = contour.toArray();

            // Bound Ellipse if Contour is Large Enough
            if (contourArray.length >= 5) {
                MatOfPoint2f areaPoints = new MatOfPoint2f(contourArray);
                RotatedRect ellipse = Imgproc.fitEllipse(areaPoints);

                // Save Ellipse Data
                width = ellipse.size.width;
                height = ellipse.size.height;
                angle = ellipse.angle;
                xPix = ellipse.center.x / 160 - 1; // x ∈ [-1, 1]
                yPix = 1 - ellipse.center.y / 180; // y ∈ (0, 1)

//                log("x: " + xPix);
//                log("y: " + yPix);
//                log("width: " + width);
//                log("height: " + height);

                // Analyze Valid Contours
                if (HEIGHT_MIN < height && height < HEIGHT_MAX && WIDTH_MIN < width && width < WIDTH_MAX && ANGLE_MIN < angle && angle < ANGLE_MAX) {
                    Imgproc.ellipse(input, ellipse, new Scalar(0, 255, 0), 1);

                    // Calculate Center of Ring if Y is in Valid Domain
                    if (0 < yPix && yPix < 1) {
                        double[] xy = map2Dto3D(xPix, yPix);
                        Ring curRing = new Ring(xy[0], xy[1]);

                        // Save Ring Position
                        if (curRing.getRelY() > 0) {
                            rings.add(curRing);
                        }
                    }
                } else {
                    // Imgproc.ellipse(input, ellipse, new Scalar(255, 0, 0), 1);
                }
            }
        }
        processor.saveMatToDisk("ellipse.jpg", input);

        // Return (0, 0) If No Rings Detected
        if (rings.size() == 0) {
            rings.add(new Ring(0, 0));
        }

        prevRings.clear();
        rings.clear();

        return input;
    }

    // Returns ring's position relative to camera
    private double[] map2Dto3D(double xPix, double yPix) {
        double vfov = CAM_VFOV / 2;
        double hfov = CAM_HFOV / 2;
        double num = (Math.cos(-CAM_PHI - vfov)) / (2 * Math.sin(vfov)) + yPix * Math.sin(CAM_PHI);
        double den = (Math.sin(-CAM_PHI - vfov)) / (2 * Math.sin(vfov)) + yPix * Math.cos(CAM_PHI);
        double y = -CAM_HEIGHT * num / den;
        return new double[] {Math.tan(hfov) * xPix * Math.hypot(CAM_HEIGHT, y) - CAM_LEFT, y + CAM_FRONT};
    }

    // Return rings
    public ArrayList<Ring> getRings() {
        return new ArrayList<>(prevRings);
    }

    // Return a sorted list with up to three coordinate-filtered rings
    public ArrayList<Ring> getRings(double robotX, double robotY, double robotTheta) {
        ArrayList<Ring> rings = getRings();
        int i = 0;
        while (i < rings.size()) {
            try {
                rings.get(i).calcAbsCoords(robotX, robotY, robotTheta);
                i++;
            } catch (NullPointerException e) {
                rings.remove(i);
            }
        }

        rings = Ring.getRings(rings);

        return new ArrayList<>(rings);
    }


}