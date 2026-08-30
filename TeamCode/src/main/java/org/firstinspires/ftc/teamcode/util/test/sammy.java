package org.firstinspires.ftc.teamcode.util.test;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class sammy extends OpMode {
    DcMotor ian;
    ElapsedTime bob = new ElapsedTime();
    @Override
    public void init() {
        ian = hardwareMap.get(DcMotor.class, "pokemon worlds friday btw");
        bob.startTime();
    }

    @Override
    public void loop() {
        if(bob.seconds() > 5){
            ian.setPower(0);
        }
        else{
            ian.setPower(1);
        }
    }
}
