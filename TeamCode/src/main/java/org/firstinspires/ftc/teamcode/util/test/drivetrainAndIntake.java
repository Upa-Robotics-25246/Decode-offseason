package org.firstinspires.ftc.teamcode.util.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
@TeleOp

public class drivetrainAndIntake extends OpMode {

    //I mightve just copied the entire drivetrain code from gm0... i do NOT have this memorized sry


    DcMotorEx fl;
    DcMotorEx bl;
    DcMotorEx fr;
    DcMotorEx br;

    DcMotorEx intake;


    public void init() {



        fl = hardwareMap.get(DcMotorEx.class, "fl");
        fr = hardwareMap.get(DcMotorEx.class, "fr");
        bl = hardwareMap.get(DcMotorEx.class, "bl");
        br = hardwareMap.get(DcMotorEx.class, "br");
        intake = hardwareMap.get(DcMotorEx.class,"intake");

        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);

    }


    public void loop(){

        //driving
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x * 1.1;
        double rx = gamepad1.right_stick_x;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        fl.setPower(frontLeftPower);
        bl.setPower(backLeftPower);
        fr.setPower(frontRightPower);
        br.setPower(backRightPower);



        //intake

        if(gamepad1.dpadUpWasPressed() && intake.getPower() <= 1){
            intake.setPower(intake.getPower() + 0.1);
        }

        if (gamepad1.dpadDownWasPressed() && intake.getPower() >=-1){
            intake.setPower(intake.getPower() - 0.1);
        }

        telemetry.addData("Intake power", intake.getPower());

    }

}
