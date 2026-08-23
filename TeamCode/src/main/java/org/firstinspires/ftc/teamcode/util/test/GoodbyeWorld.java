package org.firstinspires.ftc.teamcode.util.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


@TeleOp()
public class GoodbyeWorld extends OpMode {
    public DcMotor coolSliderThing;
    public ColorSensor colour;
    public DistanceSensor color;
    public boolean pressed = false;
    int mode = 0;
    @Override
    public void init() {
        coolSliderThing = hardwareMap.get(DcMotor.class, "elevator");
        coolSliderThing.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        coolSliderThing.setTargetPosition(0);
        coolSliderThing.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        coolSliderThing.setPower(0.8);
        colour = hardwareMap.get(ColorSensor.class, "color");
        color = hardwareMap.get(DistanceSensor.class, "color");

    }

    @Override
    public void loop() {
        telemetry.addData("motor_position", coolSliderThing.getCurrentPosition());
        telemetry.addData("motor_target", coolSliderThing.getTargetPosition());
        telemetry.addData("gamepad_a", gamepad1.a);
        telemetry.addData("Distance", color.getDistance(DistanceUnit.MM));
        telemetry.addData("color_red", colour.red());
        telemetry.addData("color_green", colour.green());
        telemetry.addData("color_blue", colour.blue());
        telemetry.addData("color_a", colour.alpha());
        telemetry.addData("mode", mode);
        telemetry.addData("motor_power", coolSliderThing.getPower());
        double r = colour.red();
        double g = colour.green();
        double b = colour.blue();

        String color_name;

        if  (color.getDistance(DistanceUnit.MM) > 50) {
            color_name = "great rhombidodecahedron (too far!)";
        } else if (r > 2 * g && r > b) {
            color_name = "purple";
        } else if (r > g && r > b) {
            color_name = "red";
        } else if ( g > r && g > b && r > 2 * b) {
            color_name = "yellow";
        }  else if ( g > r && g > b) {
            color_name = "green";
        } else if (b > r && b > g && Math.abs(g - r) < 0.3 * r) {
            color_name = "purple";
        } else  if (b > r && b > g) {
            color_name = "blue";
        } else {
            color_name = "?";
        }

        telemetry.addData("Likely color", color_name);
        if(gamepad1.a && !gamepad1.aWasPressed()) {
            mode = mode + 1;
        } else if(gamepad1.a) {
        }
        if(mode == 3) {
            mode = 0;
        }
        if(gamepad1.left_trigger_pressed && mode == 0) {
            coolSliderThing.setTargetPosition((int)(-6000.0 * (gamepad1.left_trigger - 0.5)));
        } else if(mode == 1) {
            if(gamepad1.b && !gamepad1.bWasPressed()) {
                coolSliderThing.setTargetPosition(coolSliderThing.getTargetPosition() + 300);
            } else if(gamepad1.a) {

            } else if(gamepad1.x && !gamepad1.xWasPressed()) {
                coolSliderThing.setTargetPosition((coolSliderThing.getTargetPosition()) - 300);
            }
        } else if(mode == 2 ) {
            if(coolSliderThing.getPower() > 0.5) {
                coolSliderThing.setPower(0.1);
            }
            if(gamepad1.b && !gamepad1.bWasPressed()) {
                coolSliderThing.setPower(coolSliderThing.getPower() + 0.05);
            } else if(gamepad1.x && !gamepad1.xWasPressed()) {
                coolSliderThing.setPower(coolSliderThing.getPower() - 0.05);
            }
        }
        else {
            coolSliderThing.setTargetPosition(0);
        }
        if (coolSliderThing.getTargetPosition() > 12345) {
            coolSliderThing.setTargetPosition(3000);
        }
        if(coolSliderThing.getTargetPosition() < 0) {
            coolSliderThing.setTargetPosition(0);
        }

    }
}

