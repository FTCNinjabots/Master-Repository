package org.firstinspires.ftc.teamcode.anay;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Run Flicker")
@Disabled
public class RunFlicker extends LinearOpMode {
    private DcMotor shooter = null;
    private DcMotor flicker = null;
    @Override
    public void runOpMode() throws InterruptedException {
        flicker = hardwareMap.get(DcMotor.class, "flicker");
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        flicker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flicker.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooter.setPower(1.0);
        int flicker_target = -200;
        int flicker_direction = -1;
        int flicker_current = flicker.getCurrentPosition();
        while(opModeIsActive()){

           while (flicker_target != flicker_current){
               flicker.setPower(0.3 * flicker_direction);
               flicker_current = flicker.getCurrentPosition();
               if (flicker_target == flicker_current){
                    flicker_direction *= -1;
                    if (flicker_target == -200){
                        flicker_target = 0;
                    }
                    else{
                        flicker_target = -200;
                    }
                    sleep(2000);
               }
           }
        }
    }
}
