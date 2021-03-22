package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.BuildConfig;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.security.Policy;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

public class DriveTrain {

    public boolean moving;

    private final DcMotor fl;
    private final DcMotor fr;
    private final DcMotor br;
    private final DcMotor bl;
    private DcMotor.RunMode runMode;
    private double currentPower;
    private double flPower;
    private double frPower;
    private double blPower;
    private double brPower;
    private int targetFL;
    private int targetFR;
    private int targetBL;
    private int targetBR;
    private final int motorTolerance = 10; // Encoder count
    private final double ticksPerInch = 93.023; // Computed emperically
    private final Telemetry telemetry;

    public DriveTrain(HardwareMap hardwareMap, Telemetry tele)
    {
        this.fl = hardwareMap.get(DcMotor.class, "fl");
        this.fr = hardwareMap.get(DcMotor.class, "fr");
        this.bl = hardwareMap.get(DcMotor.class, "bl");
        this.br = hardwareMap.get(DcMotor.class, "br");
        this.telemetry = tele;

        // Default is to run to position
        this.resetEncoders(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void setRunMode(DcMotor.RunMode mode)
    {
        this.runMode = mode;
        this.bl.setMode(this.runMode);
        this.br.setMode(this.runMode);
        this.fl.setMode(this.runMode);
        this.fr.setMode(this.runMode);
    }

    public void resetEncoders(DcMotor.RunMode runmode)
    {
        // Reset encoders
        this.bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Right side of AndyMark drive train has direction reversed
       this.br.setDirection(DcMotor.Direction.REVERSE);
       this.fr.setDirection(DcMotor.Direction.REVERSE);

        this.setPosition(0,0, 0, 0);

        // Next set run mode
        this.setRunMode(runmode);

        // Finally set power
        this.setPower(0);
    }

    public void setPower(double flpow, double frpow, double brpow, double blpow)
    {
        // If we are running without encoder then we need to adjust the power based on
        // current and target position else with run to position/encoder power does not need
        // to be adjusted

        switch(this.runMode)
        {
            case RUN_TO_POSITION:
                // Power direction does not matter
                this.flPower = flpow;
                this.frPower = frpow;
                this.brPower = brpow;
                this.blPower = blpow;
                break;
            case RUN_USING_ENCODER:
                // Power direction is dependent on current and target position
                if (this.getBRPosition() < this.targetBR)
                {
                    this.brPower = brpow;
                }
                else {
                    this.brPower = -1 * brpow;
                }

                if (this.getBLPosition() < this.targetBL)
                {
                    this.blPower = blpow;
                }
                else
                {
                    this.blPower = -1 * blpow;
                }

                if (this.getFRPosition() < this.targetFR)
                {
                    this.frPower = frpow;
                }
                else
                {
                    this.frPower = -1 * frpow;
                }

                if (this.getFLPostion() < this.targetFL)
                {
                    this.flPower = flpow;
                }
                else
                {
                    this.flPower = -1 * flpow;
                }
                break;
            case RUN_WITHOUT_ENCODER:
                break;
        }

        // Apply power to the left motors
        bl.setPower(this.blPower);
        fl.setPower(this.flPower);

        // Now apply power to the right motors
        br.setPower(this.brPower);
        fr.setPower(this.frPower);

        this.moving = (0 != this.blPower) || (0 != this.brPower) ||
                (0 != this.flPower) || (0 != this.frPower);
    }

    public void setPower(double lPower, double rPower)
    {
        this.setPower(lPower, rPower, rPower, lPower);
    }

    public void setPower(double power)
    {
        this.setPower(power, power);
    }

    public void stop()
    {
        // Simply set the power to 0
        this.setPower(0);
    }

    public int getBLPosition()
    {
        return bl.getCurrentPosition();
    }

    public int getBRPosition()
    {
        return br.getCurrentPosition();
    }

    public int getFLPostion()
    {
        return fl.getCurrentPosition();
    }

    public int getFRPosition()
    {
        return fr.getCurrentPosition();
    }

    public void setPosition(int flpos, int frpos, int brpos, int blpos)
    {
        this.targetFL = flpos;
        this.targetFR = frpos;
        this.targetBR = brpos;
        this.targetBL = blpos;

        bl.setTargetPosition(this.targetBL);
        fl.setTargetPosition(this.targetFL);
        br.setTargetPosition(this.targetBR);
        fr.setTargetPosition(this.targetFR);
    }

    public void setPosition(int left, int right)
    {
        this.setPosition(left, right, right, left);
    }

    public void setPosition(int pos)
    {
        this.setPosition(pos, pos, pos, pos);
    }

    public void updatePosition(int flpos, int frpos, int brpos, int blpos)
    {
        // Read current position and update target position by delta
        setPosition(this.targetFL + flpos, this.targetFR + frpos,
                this.targetBR + brpos, this.targetBL + blpos);
    }

    public void strafeLeft(int deltaPos, double power)
    {
        deltaPos = Math.abs(deltaPos);

        // Update position - FL & BR go back and BL & FR go forward
        this.updatePosition(-1 * deltaPos, deltaPos, -1 * deltaPos, deltaPos);

        // Apply power for strafing equally to all motors
        this.setPower(power);

    }

    public void strafeRight(int deltaPos, double power)
    {
        deltaPos = Math.abs(deltaPos);

        // Update position - FL & BR go forward and BL & FR go back
        this.updatePosition(deltaPos, -1 * deltaPos, deltaPos, -1 * deltaPos);

        // Apply power for strafing equally to all motors
        this.setPower(power);
    }

    public void strafeNE(int deltaPos, double power)
    {
        deltaPos = Math.abs(deltaPos);

        // Update position - FL & BR go forward and BL & FR stay at current position
        this.updatePosition(deltaPos, 0, deltaPos, 0);

        // Apply power for strafing equally to all motors
        this.setPower(power);
    }

    public void strafeSE(int deltaPos, double power)
    {
        deltaPos = Math.abs(deltaPos);

        // Update position - FL & BR stay at current position and BL & FR go back
        this.updatePosition(0, -1 * deltaPos, 0, -1 * deltaPos);

        // Apply power for strafing equally to all motors
        this.setPower(power);
    }

    public void strafeSW(int deltaPos, double power)
    {
        deltaPos = Math.abs(deltaPos);

        // Update position - FL & BR go back and BL & FR stay at current position
        this.updatePosition(-1 * deltaPos, 0, -1 * deltaPos, 0);

        // Apply power for strafing equally to all motors
        this.setPower(power);
    }

    public void strafeNW(int deltaPos, double power)
    {
        deltaPos = Math.abs(deltaPos);

        // Update position - FL & BR stay at current position and BL & FR go forward
        this.updatePosition(0, deltaPos, 0, deltaPos);

        // Apply power for strafing equally to all motors
        this.setPower(power);
    }

    public void goForward(int deltaPos, double power)
    {
        deltaPos = Math.abs(deltaPos);

        // Update position equally for all motors
        this.updatePosition(deltaPos, deltaPos, deltaPos, deltaPos);

        // Apply power equally to all motors
        this.setPower(power);
    }

    public void goBack(int deltaPos, double power)
    {
        deltaPos = Math.abs(deltaPos);

        // Update position equally for all motors. Negative value for deltaPos
        this.updatePosition( -1 * deltaPos, -1 * deltaPos, -1 * deltaPos, -1 * deltaPos);

        // Apply power equally to all motors
        this.setPower(power);
    }

    public void drive(double leftInches, double rightInches, double power)
    {
        // Convert inches to position and update left/right position
        this.updatePosition((int) Math.round(leftInches * this.ticksPerInch),
                            (int) Math.round(rightInches * this.ticksPerInch),
                            (int) Math.round(rightInches * this.ticksPerInch),
                            (int) Math.round(leftInches * this.ticksPerInch));

        // Apply power equally to all motors
        this.setPower(power);
    }

    public void clockwiseTurn(int deltaPos, double power)
    {
        deltaPos = Math.abs(deltaPos);

        // Clock wise turn: FL & BL go forward and FR & BR go backward by equal amount
        this.updatePosition(deltaPos, -1 * deltaPos, -1 * deltaPos, deltaPos);

        // Apply power equally to all motors
        this.setPower(power);
    }

    public void counterClockWiseTurn(int deltaPos, double power)
    {
        deltaPos = Math.abs(deltaPos);

        // Counter clock wise turn: FL & BL go backward and FR & BR go forward by equal amount
        this.updatePosition(-1 * deltaPos, deltaPos, deltaPos, -1 * deltaPos);

        // Apply power equally to all motors
        this.setPower(power);
    }

    public boolean isMoving()
    {
        return this.moving;
    }

    public void update()
    {
        // If robot is moving then update current position
        if (this.moving)
        {
            switch (this.runMode)
            {
                case RUN_USING_ENCODER:
                    if ((this.getBLPosition() >= this.targetBL) &&
                        (this.getBRPosition() >= this.targetBR) &&
                        (this.getFLPostion() >= this.targetFL) &&
                        (this.getFRPosition() >= this.targetFR))
                    {
                        this.stop();
                    }
                    break;
                case RUN_TO_POSITION:
                    if ((!bl.isBusy() && !br.isBusy() && !fl.isBusy() && !fr.isBusy()) ||
                        ((Math.abs(this.getBLPosition() - this.targetBL) < this.motorTolerance) &&
                         (Math.abs(this.getBRPosition() - this.targetBR) < this.motorTolerance) &&
                         (Math.abs(this.getFLPostion() - this.targetFL) < this.motorTolerance) &&
                         (Math.abs(this.getFRPosition() - this.targetFR) < this.motorTolerance)))
                    {
                        this.stop();
                    }
                    break;
            }
            this.telemetry.addData("DriveTrain:", "RunMode: " + this.runMode);
            this.telemetry.addData("DriveTrain: Current ", "FL: " + this.getFLPostion() +
                                "FR: " + this.getFRPosition() + "BR: " + this.getBRPosition() +
                               "BL: " + this.getBLPosition());
            this.telemetry.addData("DriveTrain: Target ", "FL: " + this.targetFL +
                                 "FR: " + this.targetFR + "BR: " + this.targetBR +
                                 "BL: " + this.targetBL);
             this.telemetry.addData("DriveTrain: Power ", "FL: " + this.flPower +
                     "FR: " + this.frPower + "BR: " + this.brPower + "BL: " + this.blPower);

        }
        else
        {
            this.telemetry.addData("DriveTrain:", "Not Moving");
            this.telemetry.addData("DriveTrain: Current ", "FL: " + this.getFLPostion() +
                    "FR: " + this.getFRPosition() + "BR: " + this.getBRPosition() +
                    "BL: " + this.getBLPosition());
            this.telemetry.addData("DriveTrain: Target ", "FL: " + this.targetFL +
                    "FR: " + this.targetFR + "BR: " + this.targetBR +
                    "BL: " + this.targetBL);
            this.telemetry.addData("DriveTrain: Power ", "FL: " + this.flPower +
                    "FR: " + this.frPower + "BR: " + this.brPower + "BL: " + this.blPower);
        }
    }
}
