package org.firstinspires.ftc.teamcode.util.test;


import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class HelloNight {

    private CRServo servo;


    public void init(HardwareMap hwMap) {

        servo = hwMap.get(CRServo.class, "spinny servo");
    }

    public void setServoPower(double power) {
        servo.setPower(power);

    }
}