package org.firstinspires.ftc.teamcode.vasu;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.anay.vision.OpenCVVision;

@Disabled
@Autonomous(name="Autonomous1", group = "Autonomous")

public class OpenCV_Missions extends LinearOpMode {
    OpenCVVision Test1;
    //Vuforia_Webcam Vu1;
    DcMotor bl = null;
    DcMotor br = null;
    DcMotor fl = null;
    DcMotor fr = null;



    @Override
    public void runOpMode() {
         bl = hardwareMap.get(DcMotor.class, "bl");
         br = hardwareMap.get(DcMotor.class, "br");
         fl = hardwareMap.get(DcMotor.class, "fl");
         fr = hardwareMap.get(DcMotor.class, "fr");

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

        Test1.runOpMode();
        //Vu1.runOpMode();

        if(Test1.position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.FOUR){
            telemetry.addData("Position: ", "Four Rings");

            //actual mission part is here

            bl.setTargetPosition(1440);
            br.setTargetPosition(1440);
            fl.setTargetPosition(1440);
            fr.setTargetPosition(1440);

            //add the arm servo part here

            sleep(700);

            bl.setTargetPosition(-1440);
            br.setTargetPosition(-1440);
            fl.setTargetPosition(-1440);
            fr.setTargetPosition(-1440);



            //add the arm servo part here
        }

        else if(Test1.position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.ONE){
            telemetry.addData("Position: ", "One Ring");
            telemetry.update();

            bl.setTargetPosition(1440);
            br.setTargetPosition(1440);
            fl.setTargetPosition(1440);
            fr.setTargetPosition(1440);

            sleep(700);

            bl.setTargetPosition(-360);
            br.setTargetPosition(-360);
            fl.setTargetPosition(360);
            fr.setTargetPosition(360);

            //arm servo goes here

            sleep(700);

            bl.setTargetPosition(360);
            br.setTargetPosition(360);
            fl.setTargetPosition(-360);
            fr.setTargetPosition(-360);

            sleep(700);

            bl.setTargetPosition(-1440);
            br.setTargetPosition(-1440);
            fl.setTargetPosition(-1440);
            fr.setTargetPosition(-1440);

        }

        else if(Test1.position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.NONE){
            telemetry.addData("Position: ", "No Rings");

            bl.setTargetPosition(1620);
            br.setTargetPosition(1620);
            fl.setTargetPosition(1620);
            fr.setTargetPosition(1620);

            sleep(700);

            bl.setTargetPosition(-1620);
            br.setTargetPosition(-1620);
            fl.setTargetPosition(-1620);
            fr.setTargetPosition(-1620);



            //arm servo goes here
        }

    }
}
