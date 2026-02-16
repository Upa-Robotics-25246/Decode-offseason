package org.firstinspires.ftc.teamcode.TeleOp;

import static java.lang.Math.abs;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.pedroPathing.Constants;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedforward.BasicFeedforwardParameters;

@TeleOp()
public class SDKTeleOp extends OpMode {
    public static DcMotorSimple.Direction flDirection = DcMotorEx.Direction.REVERSE;
    public static DcMotorEx.Direction frDirection = DcMotorEx.Direction.FORWARD;
    public static DcMotorSimple.Direction blDirection = DcMotorEx.Direction.REVERSE;
    public static DcMotorSimple.Direction brDirection = DcMotorEx.Direction.FORWARD;

    public static PIDCoefficients FlypidCoefficients = new PIDCoefficients( 0.0000009, 0, 0.000001);
    public static BasicFeedforwardParameters Flyff = new BasicFeedforwardParameters(0.000455,0,0.000463);
    public static PIDCoefficients turretpid = new PIDCoefficients(0,0,0);


    Follower follower;
    Pose trackPoint = new Pose(0,144);
    Pose startPose =  new Pose(56, 8, Math.toRadians(180));
    ControlSystem flypidf;
    PIDCoefficients pidCoefficients = FlypidCoefficients;
    BasicFeedforwardParameters ff = Flyff;
    double velocity = 1800;
    boolean flywheelPID = false;
    double hoodPos=0;
    boolean flywheelReversed = false;


    enum IntakeState{
        FORWARD,
        REVERSE,
        OFF
    }
    IntakeState intakeState = IntakeState.OFF;
    enum TransferState{
        FORWARD,
        REVERSE,
        OFF
    }
    TransferState transferState = TransferState.OFF;
    double ppr = 751.8;
    double gearRatio = 1/(4.6);
    double getDegrees(DcMotorEx turret){
        return turret.getCurrentPosition() * 360.0/(ppr*gearRatio);
    }
    DcMotorEx fr,fl,br,bl,flywheel,intake,transfer,turret;
    Servo hood;

    ControlSystem pid;//turret

    boolean isTurretManual = false;
    double setpoint;

    @Override
    public void init() {
        fr = hardwareMap.get(DcMotorEx.class,"fr");
        fl = hardwareMap.get(DcMotorEx.class,"fl");
        bl = hardwareMap.get(DcMotorEx.class,"bl");
        br = hardwareMap.get(DcMotorEx.class,"br");
        flywheel = hardwareMap.get(DcMotorEx.class,"flywheel");
        flywheel.setDirection(DcMotorSimple.Direction.REVERSE);
        hood = hardwareMap.get(Servo.class,"hood");
        intake = hardwareMap.get(DcMotorEx.class,"intake");
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        transfer = hardwareMap.get(DcMotorEx.class,"transfer");
        transfer.setDirection(DcMotorSimple.Direction.REVERSE);
        turret = hardwareMap.get(DcMotorEx.class,"turret");

        // other stuff for init of motors/ servos go here
        fl.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);


        fl.setDirection(flDirection);
        bl.setDirection(blDirection);
        fr.setDirection(frDirection);
        br.setDirection(brDirection);

        flypidf = ControlSystem.builder()
                .velPid(pidCoefficients)
                .basicFF(ff)
                .build();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);
        turret = hardwareMap.get(DcMotorEx.class,"turret");

        pid = ControlSystem.builder()
                .posPid(turretpid)
                .build();


        //telemetry
        telemetry.addData("Is flywheel On",()-> (flywheel.getPower()>0));
        telemetry.addData("Is Transfer On",()-> (transfer.getPower()>0));
        telemetry.addData("Is Intake On",()-> (intake.getPower()>0));
        telemetry.addData("flywheelspeed",flywheel.getVelocity());
        telemetry.addData("flywheel target", velocity);
        telemetry.addData("hood pos", hood.getPosition());
        telemetry.addData("setPoint", setpoint);




        intake.setPower(0);
        transfer.setPower(0);
        flywheel.setPower(0);

    }

    @Override
    public void loop() {



        //flywheel

            //velocity manual adjustment
        if(gamepad1.dpadLeftWasPressed()){
            velocity -=100;
        }else if(gamepad1.dpadRightWasPressed()){
            velocity+= 100;
        }

            //flywheel toggles
        if (gamepad2.xWasPressed()){
            flywheelPID = !flywheelPID;
        }

        if (gamepad2.yWasPressed()){
            flywheelReversed = !flywheelReversed;
        }
            //flywheel toggle handlers
        if(!flywheelReversed) {
            if (flywheelPID) {
                flywheel.setPower(flypidf.calculate(new KineticState(
                        0, flywheel.getVelocity())));
                flypidf.setGoal(new KineticState(0, velocity));

            } else {
                flypidf.setGoal(new KineticState(0, 0));
                flywheel.setPower(0);
            }

        }else {
            if (flywheelPID) {
                flywheel.setPower(flypidf.calculate(new KineticState(
                        0, flywheel.getVelocity())));
                flypidf.setGoal(new KineticState(0, -velocity));

            } else {
                flypidf.setGoal(new KineticState(0, 0));
                flywheel.setPower(0);
            }
        }

        //intake

            //Intake INWARDS
                if(gamepad2.leftBumperWasPressed()){
                    switch (intakeState) {
                        case FORWARD:
                            intakeState =intakeState.OFF;
                            break;
                        case REVERSE:
                           intakeState = intakeState.FORWARD;
                            break;
                        case OFF:
                           intakeState =intakeState.FORWARD;
                            break;
                    }
                }
            //Extake -INTAKE OUTWARDS

                if(gamepad2.rightBumperWasPressed()) {
                    switch (intakeState) {
                        case FORWARD:
                            intakeState = intakeState.REVERSE;
                            break;
                        case REVERSE:
                            intakeState = intakeState.OFF;
                            break;
                        case OFF:
                            intakeState = intakeState.REVERSE;
                            break;
                    }
                }
        //Transfer


            //TransferInward

            if (gamepad2.aWasPressed()){
                switch (transferState) {
                    case FORWARD:
                        transferState = transferState.OFF;
                        break;
                    case OFF:
                        transferState =transferState.FORWARD;
                        break;
                    case REVERSE:
                        transferState = transferState.FORWARD;
                        break;

                }
            }

            //Transfer Outward

            if(gamepad2.bWasPressed()){
                switch (transferState) {
                    case FORWARD:
                        transferState = transferState.REVERSE;
                        break;
                    case OFF:
                        transferState = transferState.REVERSE;
                        break;
                    case REVERSE:
                        transferState =transferState.OFF;
                        break;
                }
            }


            //transfer and turret state handlers

            switch(transferState){
                case OFF:
                    transfer.setPower(0);
                    break;
                case FORWARD:
                    transfer.setPower(1);
                    break;
                case REVERSE:
                    transfer.setPower(-1);
                    break;
            }
            switch(intakeState){
                case OFF:
                    intake.setPower(0);
                    break;
                case FORWARD:
                    intake.setPower(1);
                    break;
                case REVERSE:
                    intake.setPower(-1);
                    break;
            }
        //Turret
            //turret auto aim
            setpoint = Math.toDegrees(Math.atan2(trackPoint.getY()-follower.getPose().getY(),
                    trackPoint.getX()-follower.getPose().getX()));
            if(setpoint>180){
                setpoint = 180;
            }else if(setpoint<-180){
                setpoint = -180;
            }
            pid.setGoal(new KineticState(setpoint,0));


            if(!isTurretManual) {
                turret.setPower(pid.calculate(new KineticState(getDegrees(turret), 0)));
            }else {

                //turret manual control
                turret.setPower(gamepad1.right_trigger - gamepad1.left_trigger);
            }

            if(gamepad1.rightBumperWasPressed()){
               isTurretManual = !isTurretManual;
            }





        //Hood
            //Hood postion setters
        if(gamepad1.dpadUpWasPressed()){
            hoodPos+=0.1;
        }else if(gamepad1.dpadDownWasPressed()){
            hoodPos-=0.1;
        }
            //setting hood pos
        hood.setPosition(hoodPos);



        //drive code
        double drive = (gamepad1.left_stick_y * -1);
        double turn = (gamepad1.right_stick_x);
        double strafe = (gamepad1.left_stick_x);

        double FLspeed = drive + turn + strafe;
        double FRspeed = drive - turn - strafe;
        double BLspeed = drive + turn - strafe;
        double BRspeed = drive - turn + strafe;


        double maxF = Math.max((abs(FLspeed)),(abs(FRspeed)));
        double maxB = Math.max((abs(BLspeed)),(abs(BRspeed)));
        double maxFB_speed = Math.max(abs(maxF), abs(maxB));

        if(maxFB_speed > 1){
            FLspeed = FLspeed / maxFB_speed;
            FRspeed = FRspeed / maxFB_speed;
            BLspeed = BLspeed / maxFB_speed;
            BRspeed = BRspeed / maxFB_speed;
        }

        fl.setPower(FLspeed);
        fr.setPower(FRspeed);
        bl.setPower(BLspeed);
        br.setPower(BRspeed);

        telemetry.update();
    }
}
