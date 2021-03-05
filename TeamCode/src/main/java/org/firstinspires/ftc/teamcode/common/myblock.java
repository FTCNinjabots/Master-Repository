package org.firstinspires.ftc.teamcode.common;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
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

@Disabled
@TeleOp
public class myblock extends LinearOpMode{

    public SkystoneDeterminationPipeline pipeline;
    public SkystoneDeterminationPipeline.RingPosition position;
    OpenCvWebcam webcam;

    private DcMotor bl = null;
    private DcMotor br = null;
    private DcMotor fl = null;
    private DcMotor fr = null;

    public void MoveTank(int targetposition, double motorpower){
        int starting_position = 0;
        boolean going_backwards = false;
        if (targetposition < 0){
            //we are going backwards
            going_backwards = true;

        }

        boolean has_stopped = false;

        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");

        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        br.setDirection(DcMotor.Direction.REVERSE);
        fr.setDirection(DcMotor.Direction.REVERSE);

        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        bl.setPower(motorpower);
        br.setPower(motorpower);
        fl.setPower(motorpower);
        fr.setPower(motorpower);

        while (! has_stopped){
            starting_position = bl.getCurrentPosition();
            if (starting_position >= targetposition && ! going_backwards){
                bl.setPower(0.0);
                br.setPower(0.0);
                fl.setPower(0.0);
                fr.setPower(0.0);
                has_stopped = true;


            }

            else if (starting_position <= targetposition &&  going_backwards) {
                bl.setPower(0.0);
                br.setPower(0.0);
                fl.setPower(0.0);
                fr.setPower(0.0);
                has_stopped = true;
            }
            telemetry.addData("Current Position: ", starting_position);
            telemetry.addData("Target Position: ", targetposition);
            telemetry.update();
        }

    }


    public void Strafe(int target_position, double motorpower){
        // negative targetposition is left, positive is right
        // in order to strafe left, we must have the left two go towards, and the right two go away
        // Also, in order to strafe right, we must have the right two go towards, and the left two go away
        motorpower = Math.abs(motorpower);
        double frpower;
        double blpower;
        double brpower;
        double flpower;
        if (target_position > 0){ // right
            blpower = -1 * motorpower;
            flpower = motorpower;
            brpower = motorpower;
            frpower = -1 * motorpower;

        }
        else{
            blpower = motorpower;
            flpower = -1 * motorpower;
            brpower = -1 * motorpower;
            frpower = motorpower;

        }

        if (target_position > 0){ // right
            blpower = -1 * motorpower;
            flpower = motorpower;
            brpower = motorpower;
            frpower = -1 * motorpower;

        }
        else{
            blpower = motorpower;
            flpower = -1 * motorpower;
            brpower = -1 * motorpower;
            frpower = motorpower;

        }





        boolean has_stopped = false;

        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");


        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        br.setDirection(DcMotor.Direction.REVERSE);
        fr.setDirection(DcMotor.Direction.REVERSE);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        bl.setPower(blpower);
        br.setPower(brpower);
        fl.setPower(flpower);
        fr.setPower(frpower);

        while (!has_stopped) {
            int current_position = fl.getCurrentPosition();
            telemetry.update();
            if (current_position >= target_position && frpower == -1 * motorpower) {
                bl.setPower(0.0);
                br.setPower(0.0);
                fl.setPower(0.0);
                fr.setPower(0.0);
                has_stopped = true;

            }
            else if (current_position <= target_position && frpower ==  motorpower) {
                bl.setPower(0.0);
                br.setPower(0.0);
                fl.setPower(0.0);
                fr.setPower(0.0);
                has_stopped = true;

            }
        }


    }



    public void Turn (int degrees, double motorpower){
        // negative targetposition is left, positive is right
        // in order to strafe left, we must have the left two go towards, and the right two go away
        // Also, in order to strafe right, we must have the right two go towards, and the left two go away
        double frpower;
        double blpower;
        double brpower;
        double flpower;
        boolean going_right = false;

        if (degrees > 0){ // right
            blpower = motorpower;
            flpower = motorpower;
            brpower = -1 * motorpower;
            frpower = -1 * motorpower;
            going_right = true;

        }
        else{
            blpower = motorpower;
            flpower = motorpower;
            brpower = -1 * motorpower;
            frpower = -1 * motorpower;
            telemetry.addData("Going Left", "True");
            telemetry.update();

        }



        degrees *= 20;
        boolean turn_has_stopped = false;

        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");


        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        br.setDirection(DcMotor.Direction.REVERSE);
        fr.setDirection(DcMotor.Direction.REVERSE);
        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int current_position = fr.getCurrentPosition();

        while (!turn_has_stopped) {
            if (current_position <= degrees && going_right) {
                bl.setPower(blpower);
                br.setPower(brpower);
                fl.setPower(flpower);
                fr.setPower(frpower);
                telemetry.addData("Target Degrees:", degrees);
                telemetry.update();
                current_position = fl.getCurrentPosition();



            }
            else if (current_position >= degrees && !going_right ) {
                bl.setPower(blpower);
                br.setPower(brpower);
                fl.setPower(flpower);
                fr.setPower(frpower);
                telemetry.addData("Target Degrees:", degrees);
                telemetry.update();
                current_position = fl.getCurrentPosition();
            }
            else{

                    bl.setPower(0.0);
                    br.setPower(0.0);
                    fl.setPower(0.0);
                    fr.setPower(0.0);
                    turn_has_stopped = true;
                }
            }

        }






    public SkystoneDeterminationPipeline.RingPosition detect() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "webcam"), cameraMonitorViewId);

        pipeline = new SkystoneDeterminationPipeline();

        webcam.setPipeline(pipeline);


        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
        // out when the RC activity is in portrait. We do our actual image processing assuming
        // landscape orientation, though
        //webcam.setViewportRenderingPolicy(OpenCvWebcam.ViewportRenderingPolicy.OPTIMIZE_VIEW);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240);
            }
        });
        //pipeline.processFrame();

        return SkystoneDeterminationPipeline.RingPosition.FOUR;




        //This should be commented out
        //while (opModeIsActive()) {
        //   telemetry.addData("Analysis", pipeline.getAnalysis());
        //  telemetry.addData("Position", SkystoneDeterminationPipeline.position);
        //   telemetry.update();

        // Don't burn CPU cycles busy-looping in this sample
        //   sleep(50);








    }

    @Override
    public void runOpMode() throws InterruptedException {
        //null
    }

public static class SkystoneDeterminationPipeline extends OpenCvPipeline {

        /*
         * An enum to define the skystone position
         */
        public enum RingPosition {
            FOUR,
            ONE,
            NONE
        }

        /*
         * Some color constants
         */
        static final Scalar BLUE = new Scalar(0, 0, 255);
        static final Scalar GREEN = new Scalar(0, 255, 0);

        /*
         * The core values which define the location and size of the sample regions
         */
        static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(100, 40);
        //static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(181, 98);

        static final int REGION_WIDTH = 50;
        static final int REGION_HEIGHT = 50;

        final int FOUR_RING_THRESHOLD = 133;
        final int ONE_RING_THRESHOLD = 129;

        //final int FOUR_RING_THRESHOLD = 150;
        //final int ONE_RING_THRESHOLD = 135;

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
        public volatile SkystoneDeterminationPipeline.RingPosition position = SkystoneDeterminationPipeline.RingPosition.FOUR;

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

            position = SkystoneDeterminationPipeline.RingPosition.FOUR; // Record our analysis
            if (avg1 > FOUR_RING_THRESHOLD) {
                position = SkystoneDeterminationPipeline.RingPosition.FOUR;
            } else if (avg1 > ONE_RING_THRESHOLD) {
                position = SkystoneDeterminationPipeline.RingPosition.ONE;
            } else {
                position = SkystoneDeterminationPipeline.RingPosition.NONE;
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