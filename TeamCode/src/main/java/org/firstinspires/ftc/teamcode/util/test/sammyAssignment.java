package org.firstinspires.ftc.teamcode.util.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp()
public class sammyAssignment extends OpMode {
    DcMotor ian;
    @Override
    public void init(){
        ian = hardwareMap.get(DcMotor.class, "worlds today");
    }

    @Override
    public void loop() {
        if(gamepad1.aWasPressed()){
            ian.setPower(1);
        }
        if(gamepad1.bWasPressed()){
            ian.setPower(0);
        }
    }
}
