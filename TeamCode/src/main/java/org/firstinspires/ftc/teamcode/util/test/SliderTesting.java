package org.firstinspires.ftc.teamcode.util.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp()
public class SliderTesting extends OpMode {
    public DcMotor coolSliderThing;
    int mode = 0;

    @Override
    public void init() {
        coolSliderThing = hardwareMap.get(DcMotor.class, "elevator");
        coolSliderThing.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        coolSliderThing.setTargetPosition(0);
        coolSliderThing.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        coolSliderThing.setPower(0.8);

    }

    @Override
    public void loop() {
        telemetry.addData("mode:", mode);
        telemetry.addData("target position:", coolSliderThing.getTargetPosition());
        telemetry.addData("current position", coolSliderThing.getCurrentPosition());
        telemetry.addData("motor speed", coolSliderThing.getPower());
        if(gamepad1.aWasPressed()) {
            mode = mode + 1;
        } else if(gamepad1.a) {
        }
        if(mode == 3) {
            mode = 0;
        }
        if(gamepad1.left_trigger_pressed && mode == 0) {
            coolSliderThing.setTargetPosition((int)(-3000.0 * (gamepad1.left_trigger)));
        } else if(mode == 1) {
            if(gamepad1.bWasPressed()) {
                coolSliderThing.setTargetPosition(coolSliderThing.getTargetPosition() - 50);
            } else if(gamepad1.a) {

            } else if(gamepad1.xWasPressed()) {
                coolSliderThing.setTargetPosition((coolSliderThing.getTargetPosition()) - 500);
            } else if(gamepad1.yWasPressed()) {
                coolSliderThing.setTargetPosition(coolSliderThing.getTargetPosition() + 500);
            } else {
            }
        } else if(mode == 2 ) {
            if(coolSliderThing.getPower() > 0.5) {
                coolSliderThing.setPower(0.1);
            }
            if(gamepad1.bWasPressed()) {
                coolSliderThing.setPower(coolSliderThing.getPower() + 0.05);
            } else if(gamepad1.xWasPressed()) {
                coolSliderThing.setPower(coolSliderThing.getPower() - 0.05);
            } else {
            }
        }
        else if (gamepad1.leftTriggerWasReleased()){
            coolSliderThing.setTargetPosition(0);

        if(coolSliderThing.getTargetPosition() > 0) {
            coolSliderThing.setTargetPosition(0);
        }

    }
}}


