package org.firstinspires.ftc.teamcode.aum;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.myblock;
@TeleOp(name = "Test Turning")
public class MoveTest extends myblock {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        while(opModeIsActive()){
            Turn(90, 0.5);
            sleep(100000000);
        }

    }
}
