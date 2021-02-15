/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.anay.Dcmotor;

import java.security.CryptoPrimitive;

import static java.lang.Thread.sleep;


@TeleOp(name="TeleOp Release")
public class TeleOp_Ninjabots extends OpMode
{
    private DcMotor br = null;
    private DcMotor bl = null;
    private DcMotor fl = null;
    private DcMotor fr = null;
    private DcMotor intake = null;
    private DcMotor wobble = null;
    private DcMotor flicker = null;
    private DcMotor shooter = null;

    private Servo wobble_gate = null;
    private CRServo rack_pinion = null;

    @Override
    public void init() {
        bl = hardwareMap.get(DcMotor.class, "bl");
        br = hardwareMap.get(DcMotor.class, "br");
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        intake = hardwareMap.get(DcMotor.class, "intake");
        wobble = hardwareMap.get(DcMotor.class, "wobble");
        flicker = hardwareMap.get(DcMotor.class, "flicker");
        shooter = hardwareMap.get(DcMotor.class, "shooter");

        wobble_gate = hardwareMap.get(Servo.class, "wobble_gate");
        rack_pinion = hardwareMap.get(CRServo.class, "rack_pinion");

        //voltage = hardwareMap.get(ModernRoboticsUsbDcMotorController.class, "Control Hub Portal");
        bl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        br.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wobble.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flicker.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wobble.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flicker.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        br.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);


    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {

    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {

    }

    int strafe_value = 0;
    boolean flicker_resetting = false;
    boolean flicker_setup = false;
    boolean flicker_override = false;
    @Override
    public void loop() {
        if (gamepad1.left_trigger > 0.5) {
            strafe_value = 1;
        } else if (gamepad1.right_trigger > 0.5) {
            strafe_value = 2;
        } else if (gamepad1.left_stick_y != 0 || gamepad1.right_stick_y != 0) {
            strafe_value = 0;
        } else {
            strafe_value = 4;
        }

        if (strafe_value == 1) {
            //strafe left - LT
            bl.setPower(gamepad1.left_trigger);
            br.setPower(-gamepad1.left_trigger);
            fl.setPower(-gamepad1.left_trigger);
            fr.setPower(gamepad1.left_trigger);

        } else if (strafe_value == 2) {
            //strafe right - RT
            bl.setPower(-gamepad1.right_trigger);
            br.setPower(gamepad1.right_trigger);
            fl.setPower(gamepad1.right_trigger);
            fr.setPower(-gamepad1.right_trigger);
        } else if (strafe_value == 0) {
            //Moving
            br.setPower(-gamepad1.right_stick_y);
            fl.setPower(-gamepad1.left_stick_y);
            fr.setPower(-gamepad1.right_stick_y);
            bl.setPower(-gamepad1.left_stick_y);

        } else if (strafe_value == 4) {
            br.setPower(0.0);
            fl.setPower(0.0);
            fr.setPower(0.0);
            bl.setPower(0.0);

        }


        //Wobble Goal - LB to go inside, RB to go outside, a to stop motor power
        if (gamepad1.left_bumper) {
            wobble.setPower(0.4);
        } else if (gamepad1.right_bumper) {
            wobble.setPower(-0.4);
        }

        if (gamepad1.a) {
            wobble.setPower(0.0);
        }

        //Wobble Goal Gate - DPAD Down to open, DPAD Up to Close

        if (gamepad1.x) {
            wobble_gate.setPosition(1.0);
        } else if (gamepad1.b) {
            wobble_gate.setPosition(0.0);
        }


        ///CONTROLLER 2 -----------------------------------------------------------------








        //Intake - A to start, Y to stop


        if (gamepad2.a) {
            //intake.setPower(1.0);
            intake.setPower(1.0);
        } else if (gamepad2.y) {
            intake.setPower(0.0);
        }

        //Rack + Pinion - LB and RB, b to stop
        // continues until stopped
        if (gamepad2.left_bumper) {
            rack_pinion.setPower(1.0);
            flicker_resetting = true;

        } else if (gamepad2.right_bumper) {
            rack_pinion.setPower(-1.0);

        }

        if (gamepad2.b) { // come back to this later if we want to use joystick
            rack_pinion.setPower(0.0);
        }

        //FLicker - LT + RT
        if (gamepad2.left_trigger > 0 /*&& current_value > -250*/) {
            flicker.setPower(-0.75);
            flicker_override = true;
            telemetry.addData("left trigger:", "pressed");
            telemetry.update();
        }
        else if (gamepad2.right_trigger > 0 /*&& current_value < 0*/) {
            flicker.setPower(0.75);
            telemetry.addData("right trigger", "pressed");
            telemetry.update();
            flicker_override = true;
        }

        else{
                flicker.setPower(0.0);

            }




        //Shooter - DPAD UP TO START, DPAD DOWN TO STOP
        if (gamepad2.dpad_up) {
            //shooter.setPower(0.1 * voltage.getVoltage() - 1.9);
            //telemetry.addData("Voltage: ", voltage.getVoltage());
            //telemetry.addData("Speed: ", 0.1 * voltage.getVoltage() - 1.9);
            //telemetry.update();

            shooter.setPower(0.7);
        } else if (gamepad2.dpad_down) {
            shooter.setPower(0.0);
        }






    }




    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        telemetry.addData("Program Status", "Stopped");
        telemetry.update();
    }

}
