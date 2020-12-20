package org.firstinspires.ftc.teamcode.vasu;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.vasu.A_Site;
import org.firstinspires.ftc.teamcode.vasu.C_Site;
import org.firstinspires.ftc.teamcode.vasu.B_Site;

import org.firstinspires.ftc.teamcode.anay.vision.OpenCVVision;


@Autonomous(name="Autonomous1", group = "Autonomous")

public class OpenCV_Missions extends LinearOpMode {

    //Vuforia_Webcam Vu1;
    C_Site c_motor;
    B_Site b_motor;
    A_Site a_motor;
    private DcMotor bl = null;
    private DcMotor br = null;
    private DcMotor fl = null;
    private DcMotor fr = null;
    OpenCVVision Test1 = new OpenCVVision();



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

            //add the arm servo part here
        }

        else if(Test1.position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.ONE) {
            telemetry.addData("Position: ", "One Ring");
            telemetry.update();
        }

        else if(Test1.position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.NONE){
            telemetry.addData("Position: ", "No Rings");

            //arm servo goes here
        }

    }
}
