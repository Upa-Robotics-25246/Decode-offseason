package org.firstinspires.ftc.teamcode.util.test;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.util.pedroPathing.Constants;


import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedforward.BasicFeedforwardParameters;

@Configurable
@TeleOp(name = "ABSOLUTE WEEWOO")
public class turretAutoAim extends OpMode {

    DcMotorEx turret;
    Follower follower;
    Pose trackPoint = new Pose(144,144);
    Pose startPose =  new Pose(144, 0, Math.toRadians(90));
    double ppr = 751.8;// from motor, idk the motor im using rn

    ControlSystem pid;



    double gearRatio = 1/(4.6);
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
                .velPid(turretpid)
                .build();

    }

    @Override
    public void loop() {
        double setpoint = Math.toDegrees(Math.atan2(trackPoint.getY()-follower.getPose().getY(),
                trackPoint.getX()-follower.getPose().getX()));
        if(setpoint>180){
            setpoint = 180;
        }else if(setpoint<-180){
            setpoint = -180;
        }
        pid.setGoal(new KineticState(setpoint,0));




        turret.setPower(pid.calculate(new KineticState(getDegrees(),0)));
        telemetry.addData("setPoint",setpoint);
        telemetry.addData("turretPos",turret.getCurrentPosition());
        telemetry.addData("turretAngle",getDegrees());
        telemetry.update();

        follower.update();




    }
}