package org.firstinspires.ftc.teamcode.vasu;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.anay.vision.OpenCVVision;

@Disabled
@Autonomous(name="Autonomous", group = "Autonomous")

public class OpenCV_Missions extends LinearOpMode {
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
        }

        else if(Test1.position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.ONE){
            telemetry.addData("Position: ", "One Ring");
            telemetry.update();
        }

        else if(Test1.position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.NONE){
            telemetry.addData("Position: ", "No Rings");
        }

    }
}
