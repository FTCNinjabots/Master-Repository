package org.firstinspires.ftc.teamcode.vasu;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.anay.vision.OpenCVVision;

@Disabled
@Autonomous(name="Autonomous", group = "Autonomous")

public class        OpenCV_Missions extends LinearOpMode {
    OpenCVVision Test1;
    DcMotor bl = hardwareMap.get(DcMotor.class, "bl");
    DcMotor br = hardwareMap.get(DcMotor.class, "br");
    DcMotor fl = hardwareMap.get(DcMotor.class, "fl");
    DcMotor fr = hardwareMap.get(DcMotor.class, "fr");
    @Override
    public void runOpMode() {


         br.setDirection(DcMotor.Direction.REVERSE);
         fr.setDirection(DcMotor.Direction.REVERSE);

        Test1.runOpMode();
        if(Test1.position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.FOUR){
            telemetry.addData("Position: ", "Four Rings");

            //actual mission part is here

            bl.setPower(1);
            br.setPower(1);
            fl.setPower(1);
            fr.setPower(1);

            //add the arm servo part here
        }

        else if(Test1.position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.ONE){
            telemetry.addData("Position: ", "One Ring");
            telemetry.update();

            bl.setPower(1);
            br.setPower(1);
            fl.setPower(1);
            fr.setPower(1);

            sleep(700);
            //sleep stops the robot for a 700 milliseconds and then carry's out the next task
            //if the robot has not been stopped it will combine all movements at once

            bl.setPower(-0.25);
            br.setPower(-0.25);
            fl.setPower(0.25);
            fr.setPower(0.25);

            //arm servo goes here
        }

        else if(Test1.position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.NONE){
            telemetry.addData("Position: ", "No Rings");

            bl.setPower(1);
            br.setPower(1);
            fl.setPower(1);
            fr.setPower(1);

            //arm servo goes here
        }

    }
}
