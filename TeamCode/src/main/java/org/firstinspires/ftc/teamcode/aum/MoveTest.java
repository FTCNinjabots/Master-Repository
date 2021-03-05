package org.firstinspires.ftc.teamcode.aum;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.myblock;
@Disabled
@TeleOp(name = "Narkar Turning")
public class MoveTest extends myblock {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        while(opModeIsActive()){
            Strafe(-2000, 0.75);
            sleep(100000000);
        }

    }
}
