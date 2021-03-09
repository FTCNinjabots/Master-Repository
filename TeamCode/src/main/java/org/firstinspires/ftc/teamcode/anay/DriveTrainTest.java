package org.firstinspires.ftc.teamcode.anay;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.DriveTrain;
import org.firstinspires.ftc.teamcode.common.NinjaBot;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

@TeleOp(name = "Drive Train Test")

public class DriveTrainTest extends OpMode {

    private NinjaBot ninjabot;

    public DriveTrainTest()
    {
       // ninjabot = new NinjaBot(hardwareMap);
    }

    @Override
    public void init()
    {
        ninjabot = new NinjaBot(hardwareMap, telemetry);
    }

    @Override
    public void init_loop()
    {
        // Reset drivetrain incase it is moved during init
        ninjabot.driveTrain.resetEncoders(DcMotor.RunMode.RUN_TO_POSITION);
    }

    @Override
    public void start()
    {

    }

    @Override
    public void loop()
    {
        if (gamepad1.a)
        { // Move drive train forward
            ninjabot.driveTrain.goForward(1000, 1.0);
        }
        else if (gamepad1.b)
        { // Move drive train back
            ninjabot.driveTrain.goBack(1000, 1.0);
        }
        else if (gamepad1.x)
        { // Stafe drivetrain left
            ninjabot.driveTrain.strafeLeft(100, 1.0);
        }
        else if (gamepad1.y) { // Strafe drivetrain right
            ninjabot.driveTrain.strafeRight(100, 1.0);
        }
        else if (gamepad1.right_bumper)
        {
            ninjabot.driveTrain.clockwiseTurn(100, 1.0);
        }
        else if (gamepad1.left_bumper)
        {
            ninjabot.driveTrain.counterClockWiseTurn(100, 1);
        }

       // telemetry.addData("Test Position: ", "FL: " + ninjabot.driveTrain.getFLPostion() +
       //         "FR: "  + ninjabot.driveTrain.getFRPosition() + "BR: " + ninjabot.driveTrain.getBRPosition() +
       //         "BL: " + ninjabot.driveTrain.getBLPosition());

        ninjabot.update();
    }

}
