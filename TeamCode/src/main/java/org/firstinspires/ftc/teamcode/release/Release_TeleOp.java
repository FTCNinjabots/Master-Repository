package org.firstinspires.ftc.teamcode.release;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Main TeleOp")
public class Release_TeleOp extends LinearOpMode {
    private DcMotor fl = null;
    private DcMotor br = null;
    private DcMotor fr = null;
    private DcMotor bl = null;
    private DcMotor intake_motor = null;
    private Servo main = null;

    @Override
    public void runOpMode() throws InterruptedException {
        fl = hardwareMap.dcMotor.get("fl");
        br = hardwareMap.dcMotor.get("br");
        fr = hardwareMap.dcMotor.get("fr");
        bl = hardwareMap.dcMotor.get("bl");
        main = hardwareMap.get(Servo.class, "crtest");
        intake_motor = hardwareMap.get(DcMotor.class, "test");
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        while (opModeIsActive()) {
            /*
            br.setPower(gamepad1.right_stick_y);
            fl.setPower(gamepad1.left_stick_y);
            fr.setPower(gamepad1.right_stick_y);
            bl.setPower(gamepad1.left_stick_y);

            if (gamepad1.left_trigger != 0){
                //strafe left
                bl.setPower(gamepad1.left_trigger);
                br.setPower(-gamepad1.left_trigger);
                fl.setPower(-gamepad1.left_trigger);
                fr.setPower(gamepad1.left_trigger);
            }
            else if (gamepad1.right_trigger != 0){
                //strafe left
                bl.setPower(-gamepad1.right_trigger);
                br.setPower(gamepad1.right_trigger);
                fl.setPower(gamepad1.right_trigger);
                fr.setPower(-gamepad1.right_trigger);
            }
            */
            if (gamepad1.a) {

                intake_motor.setPower(1.0);
            } else if (gamepad1.b) {
                intake_motor.setPower(0.0);
            }
            if (gamepad1.right_trigger != 0) {
                    main.setPosition(1.0);
                }


            else if (gamepad1.left_trigger != 0) {
                    main.setPosition(0.0);
                }


            }
            if (gamepad1.x){
                main.setPosition(main.getPosition());
            }

        }
    }
