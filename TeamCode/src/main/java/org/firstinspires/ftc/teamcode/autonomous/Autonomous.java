package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.DriveTrain;
import org.firstinspires.ftc.teamcode.common.Intake;
import org.firstinspires.ftc.teamcode.common.IntakeGate;
import org.firstinspires.ftc.teamcode.common.NinjaBot;
import org.firstinspires.ftc.teamcode.common.Shooter;
import org.firstinspires.ftc.teamcode.common.WobbleGate;
import org.firstinspires.ftc.teamcode.common.WobbleMotor;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="Autonomous Release")
public class Autonomous extends OpMode {

    private NinjaBot ninjabot;
    private ElapsedTime timer;
    private ElapsedTime currentTime;

    public Autonomous()
    {
        ninjabot = new NinjaBot(hardwareMap);
        timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        currentTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    }

    @Override
    public void init()
    {
        // TODO: Init robot state
        ninjabot.init();
    }

    @Override
    public void init_loop()
    {
     // Reset drivetrain incase it is moved during init
        ninjabot.driveTrain.resetEncoders(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void start()
    {

    }

    @Override
    public void loop()
    {

    }

    @Override
    public void stop()
    {
        // Stop the bot
        ninjabot.stop();
    }
}
