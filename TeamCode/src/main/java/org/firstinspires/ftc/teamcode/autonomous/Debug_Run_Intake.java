package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.anay.Dcmotor;
import org.firstinspires.ftc.teamcode.common.NinjaBot;

@Disabled
@Autonomous(name = "Debug: Run Intake")
public class Debug_Run_Intake extends LinearOpMode {
    private DcMotor intake = null;
    @Override
    public void runOpMode() throws InterruptedException {
        intake = hardwareMap.get(DcMotor.class, "intake");
        waitForStart();
        while (opModeIsActive()){
            intake.setPower(1.0);
        }
    }
}
