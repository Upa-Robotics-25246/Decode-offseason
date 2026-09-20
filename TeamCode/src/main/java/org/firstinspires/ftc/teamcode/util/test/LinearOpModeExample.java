package org.firstinspires.ftc.teamcode.util.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class LinearOpModeExample extends LinearOpMode {
    // can declare variables and Objects like motors or servos here

    @Override
    public void runOpMode() throws InterruptedException {

        //can also declare them in here!


        //initialization goes here, hardware maps and encoders and such


        //wait for start button being clicked on the driver station
        waitForStart();

        while(opModeIsActive()){
            //actual game code goes here! gamepads and stuff
            //the while statement runs as long as the opMode is active, so it loops!
        }
    }
}
