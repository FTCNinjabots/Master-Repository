package org.firstinspires.ftc.teamcode.vasu;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.vasu.A_Site;
import org.firstinspires.ftc.teamcode.vasu.C_Site;
import org.firstinspires.ftc.teamcode.vasu.B_Site;

import org.firstinspires.ftc.teamcode.anay.vision.OpenCVVision;
import org.firstinspires.ftc.teamcode.common.myblock;

@Autonomous(name="Autonomous1", group = "Autonomous")

public class OpenCV_Missions extends OpenCVVision {

    C_Site c_motor = new C_Site();
    B_Site b_motor = new B_Site();
    A_Site a_motor = new A_Site();


    @Override
    public void runOpMode(){
        DcMotor bl = hardwareMap.get(DcMotor.class, "bl");
        DcMotor br = hardwareMap.get(DcMotor.class, "br");
        DcMotor fl = hardwareMap.get(DcMotor.class, "fl");
        DcMotor fr = hardwareMap.get(DcMotor.class, "fr");

        waitForStart();

        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


         br.setDirection(DcMotor.Direction.REVERSE);
         fr.setDirection(DcMotor.Direction.REVERSE);

        detect();
        //Vu1.runOpMode();

        if(position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.FOUR){
            telemetry.addData("Position: ", "Four Rings");
            telemetry.update();
            try {
                c_motor.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //add the arm servo part here
        }

        else if(position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.ONE) {
            telemetry.addData("Position: ", "One Ring");
            telemetry.update();
            try {
                b_motor.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        else if(position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.NONE){
            telemetry.addData("Position: ", "No Rings");
            telemetry.update();
            try {
                a_motor.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            //arm servo goes here
        }

    }
}
