package org.firstinspires.ftc.teamcode.util.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class fiveMotors extends OpMode {
    DcMotorEx one;
    DcMotorEx two;

    DcMotorEx three;
    DcMotorEx four;
    DcMotorEx five;

    @Override
    public void init() {
        one = hardwareMap.get(DcMotorEx.class,"one");
        two = hardwareMap.get(DcMotorEx.class,"two");
        three = hardwareMap.get(DcMotorEx.class,"three");
        four = hardwareMap.get(DcMotorEx.class,"four");
        five = hardwareMap.get(DcMotorEx.class,"four");

    }

    @Override
    public void loop() {

        MotorActions(one,gamepad1.dpadUpWasPressed(),gamepad1.dpadDownWasPressed());

        MotorActions(two, gamepad1.dpadRightWasPressed(), gamepad1.dpadLeftWasPressed());

        MotorActions(three, gamepad1.yWasPressed(), gamepad1.aWasPressed());

        MotorActions(four, gamepad2.dpadUpWasPressed(), gamepad2.dpadDownWasPressed());

        MotorActions(five, gamepad2.dpadRightWasPressed(), gamepad2.dpadLeftWasPressed());




    }


    public void MotorActions(DcMotorEx motor, boolean gamepadUp, boolean gamepadDown){

        if(gamepadUp && motor.getPower() < 1){
            motor.setPower(motor.getPower() + 0.1);
        }

        if(gamepadDown && motor.getPower() > -1){
            motor.setPower(motor.getPower() - 0.1);
        }
    }
}
