package org.firstinspires.ftc.teamcode.util.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class sammyAssignment2 extends OpMode {
    DcMotor ian;
    double power = 0;
    @Override
    public void init() {
        ian = hardwareMap.get(DcMotor.class, "hello");
    }

    @Override
    public void loop() {
        if(gamepad1.dpadUpWasPressed() && power <= 1){
            power = power + 0.1;
            ian.setPower(power);
        }
        if(gamepad1.dpadDownWasPressed() && power >= -1){
            power = power - 0.1;
            ian.setPower(power);
        }
        telemetry.addData("motor speed", power);
    }
}
