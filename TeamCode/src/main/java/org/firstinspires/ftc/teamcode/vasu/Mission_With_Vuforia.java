package org.firstinspires.ftc.teamcode.vasu;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.teamcode.anay.vision.Vuforia_2;
import org.firstinspires.ftc.teamcode.anay.vision.OpenCVVision;

@Disabled
@Autonomous(name="Autonomous", group = "Autonomous")

public class Mission_With_Vuforia extends LinearOpMode {
    OpenCVVision Test1;
    public Vuforia_2 NinjaVuforia;
    //DcMotor bl = hardwareMap.get(DcMotor.class, "bl");
    //DcMotor br = hardwareMap.get(DcMotor.class, "br");
    //DcMotor fl = hardwareMap.get(DcMotor.class, "fl");
    //DcMotor fr = hardwareMap.get(DcMotor.class, "fr");


    OpenGLMatrix Square_A = null; // 0 Rings
    OpenGLMatrix Square_B = null; // 1 Ring
    OpenGLMatrix Square_C = null; // 4 Rings


    @Override
    public void runOpMode() {



        NinjaVuforia.setupVuforia();
        Test1.runOpMode();
        //Vu1.runOpMode();


        Square_A = NinjaVuforia.createMatrix(30, 0, 210, 0, 0, 0); // We need to set this, we calculated these rough values earlier
        Square_B = NinjaVuforia.createMatrix(60, 0, 150, 0, 0, 0); // We need to set this, we calculated these rough values earlier
        Square_C = NinjaVuforia.createMatrix(30, 0, 120, 0, 0, 0); // We need to set this, we calculated these rough values earlier




        if(Test1.position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.FOUR){
            telemetry.addData("Position: ", "Four Rings");

            //Move forward x degrees, turn 180

            //Shoot Rings, turn 180

            //Move forward, collect rings

            //turn 180, shoot again

            //actual mission part is here
            OpenGLMatrix Ninja_latestLocation = null;
            if (NinjaVuforia.listener.isVisible())
            {
                Ninja_latestLocation = NinjaVuforia.listener.getUpdatedRobotLocation();
            }
            else if (NinjaVuforia.listener1.isVisible()){
                Ninja_latestLocation = NinjaVuforia.listener1.getUpdatedRobotLocation();
            }
            if (Ninja_latestLocation != null){
                NinjaVuforia.lastKnownLocation = Ninja_latestLocation;
            }

            NinjaVuforia.Vuforia_Move(NinjaVuforia.lastKnownLocation, Square_C);


            //add the arm servo part here
        }

        else if(Test1.position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.ONE){
            telemetry.addData("Position: ", "One Ring");
            telemetry.update();

            //actual mission part is here
            OpenGLMatrix Ninja_latestLocation = null;
            if (NinjaVuforia.listener.isVisible())
            {
                Ninja_latestLocation = NinjaVuforia.listener.getUpdatedRobotLocation();
            }
            else if (NinjaVuforia.listener1.isVisible()){
                Ninja_latestLocation = NinjaVuforia.listener1.getUpdatedRobotLocation();
            }
            if (Ninja_latestLocation != null){
                NinjaVuforia.lastKnownLocation = Ninja_latestLocation;
            }

            NinjaVuforia.Vuforia_Move(NinjaVuforia.lastKnownLocation, Square_B);

            //arm servo goes here
        }

        else if(Test1.position == OpenCVVision.SkystoneDeterminationPipeline.RingPosition.NONE){
            telemetry.addData("Position: ", "No Rings");

            //actual mission part is here
            OpenGLMatrix Ninja_latestLocation = null;
            if (NinjaVuforia.listener.isVisible())
            {
                Ninja_latestLocation = NinjaVuforia.listener.getUpdatedRobotLocation();
            }
            else if (NinjaVuforia.listener1.isVisible()){
                Ninja_latestLocation = NinjaVuforia.listener1.getUpdatedRobotLocation();
            }
            if (Ninja_latestLocation != null){
                NinjaVuforia.lastKnownLocation = Ninja_latestLocation;
            }

            NinjaVuforia.Vuforia_Move(NinjaVuforia.lastKnownLocation, Square_A);

            //arm servo goes here
        }

    }
}
