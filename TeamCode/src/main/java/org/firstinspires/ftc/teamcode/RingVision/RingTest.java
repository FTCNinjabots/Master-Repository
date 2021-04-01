package org.firstinspires.ftc.teamcode.RingVision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class RingTest extends LinearOpMode {
    RingDetector detector;
    private LinearOpMode RingTest;

    @Override
    public void runOpMode() throws InterruptedException {
        detector = new RingDetector(RingTest);
        detector.start();
        waitForStart();
        while(opModeIsActive()){
            detector.drawRings();
        }
    }
}
