package org.firstinspires.ftc.teamcode.RingVision;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "RingVisionTest TEST")
public class RingVisionTest extends LinearOpMode {
    private Ring_Pipeline pipeline;
    private FtcDashboard dashboard;


    @Override
    public void runOpMode() throws InterruptedException {
        dashboard = FtcDashboard.getInstance();
        pipeline = new Ring_Pipeline(this, hardwareMap, dashboard);
        Pose2d cur_pose = new Pose2d(10, 10, 0);
        waitForStart();
        pipeline.StreamtoDash();

        while(opModeIsActive()) {
            pipeline.DetectRings(cur_pose);
        }
    }
}
