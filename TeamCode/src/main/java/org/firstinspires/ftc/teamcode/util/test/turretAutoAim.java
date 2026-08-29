package org.firstinspires.ftc.teamcode.util.test;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.pedroPathing.Constants;


import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedforward.BasicFeedforwardParameters;

@Configurable
@TeleOp()
public class turretAutoAim extends OpMode {

    DcMotorEx turret;
    Follower follower;
    Pose trackPoint = new Pose(144,144);
    Pose startPose =  new Pose(144, 0, Math.toRadians(90));
    ElapsedTime wee = new ElapsedTime();

    double ppr = 751.8;// from motor, idk the motor im using rn

    ControlSystem pid;



    double gearRatio = 4.6;
    double getDegrees(){
        return turret.getCurrentPosition() * 360.0/(ppr*gearRatio);
    }
    public static PIDCoefficients turretpid = new PIDCoefficients(0,0,0);

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        turret = hardwareMap.get(DcMotorEx.class,"turret");

        pid = ControlSystem.builder()
                .posPid(turretpid)
                .build();


    }


    @Override
    public void loop() {

        follower.update();
        double setpoint = Math.toDegrees(Math.atan2(trackPoint.getY()-follower.getPose().getY(),
                trackPoint.getX()-follower.getPose().getX())-follower.getPose().getHeading());
        if(setpoint>180){
            setpoint = 180;
        }else if(setpoint<-180){
            setpoint = -180;
        }
        pid.setGoal(new KineticState(setpoint,0));
        if(gamepad1.aWasPressed()){
            turretpid.kP+=0.001;
        }
        if(gamepad1.bWasPressed()){
            turretpid.kD+=0.001;
        }




        turret.setPower(pid.calculate(new KineticState(getDegrees(),0)));
        telemetry.addData("setPoint",setpoint);
        telemetry.addData("turretPos",turret.getCurrentPosition());
        telemetry.addData("turretAngle",getDegrees());
        telemetry.addData("kp",turretpid.kP);
        telemetry.addData("kD",turretpid.kD);
        telemetry.update();

        follower.update();




    }
}
