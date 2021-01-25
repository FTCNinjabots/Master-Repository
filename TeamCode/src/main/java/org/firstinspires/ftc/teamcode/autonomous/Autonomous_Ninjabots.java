package org.firstinspires.ftc.teamcode.autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.common.myblock;
import org.firstinspires.ftc.teamcode.anay.vision.OpenCVVision;

@Autonomous(name="Autonomous Ninjabots")

public class Autonomous_Ninjabots extends myblock {

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

        if(position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.FOUR){
            telemetry.addData("Position: ", "Four Rings"); // C Site
            telemetry.update();
            MoveTank(9500, 0.5);



            //wobble goes here
        }

        else if(position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.ONE) {
            telemetry.addData("Position: ", "One Ring"); // B Site
            telemetry.update();

            MoveTank(6400, 0.5);
            Strafe(2300, 0.25);
            MoveTank(-2000, -0.5);


            //wobble goes here
        }

        else if(position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.NONE){
            telemetry.addData("Position: ", "No Rings"); // A Site
            telemetry.update();
            MoveTank(6400, 0.5);
            Strafe(1500, 0.25);



            //wobble goes here
        }

    }
}
