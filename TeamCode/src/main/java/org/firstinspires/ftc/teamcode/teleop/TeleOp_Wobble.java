package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.NinjaBot;

@TeleOp(name = "Wobble Test")
public class TeleOp_Wobble extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {
        NinjaBot ninjabot = new NinjaBot(hardwareMap, telemetry, true);

        waitForStart();
            while (opModeIsActive()){
                if (gamepad2.right_trigger > 0.5){
                    ninjabot.wobbleMotor.down();
                }
                else if (gamepad2.left_trigger > 0.5){
                    ninjabot.wobbleMotor.raise();
                }

                if (gamepad2.right_bumper){
                    ninjabot.wobbleGate.close();
                }
                else if (gamepad2.left_bumper){
                    ninjabot.wobbleGate.open();
                }

                ninjabot.update();
            }
    }
}
