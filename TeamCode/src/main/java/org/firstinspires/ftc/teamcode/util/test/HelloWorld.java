package org.firstinspires.ftc.teamcode.util.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;

import java.util.Locale;

@TeleOp()
public class HelloWorld extends OpMode {

    GoBildaPinpointDriver odo;


    double chaoticEvilTime = 0;
    DcMotor upLeftDrive;
    DcMotor upRightDrive;
    DcMotor downLeftDrive;
    DcMotor downRightDrive;

    @Override
    public void init() {
        upLeftDrive = hardwareMap.get(DcMotor.class, "up_left_drive");
        upRightDrive = hardwareMap.get(DcMotor.class, "up_right_drive");
        downLeftDrive = hardwareMap.get(DcMotor.class, "down_left_drive");
        downRightDrive = hardwareMap.get(DcMotor.class, "down_right_drive");

        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo");

        odo.setOffsets( 52, -168, DistanceUnit.MM);

        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.REVERSED);

        odo.resetPosAndIMU();

        downLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        upLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        downRightDrive.setDirection(DcMotor.Direction.FORWARD);
        upRightDrive.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Initialized");
        telemetry.addData("X offset", odo.getXOffset(DistanceUnit.MM));
        telemetry.addData("Y offset", odo.getYOffset(DistanceUnit.MM));
        telemetry.update();
    }

    @Override
    public void loop() {

        double speed_forward = -gamepad1.left_stick_y;
        double turn_speed = gamepad1.left_stick_x;
        double side_speed = gamepad1.right_stick_x;

        double up_left = speed_forward + side_speed + turn_speed;
        double up_right = speed_forward - side_speed - turn_speed;
        double down_left = speed_forward + side_speed - turn_speed;
        double down_right = speed_forward - side_speed + turn_speed;

        double maxPower = Math.abs(up_left);
        maxPower = Math.max(maxPower, Math.abs(up_right));
        maxPower = Math.max(maxPower, Math.abs(down_left));
        maxPower = Math.max(maxPower, Math.abs(down_right));

        double scale = 1.0;
        if (maxPower > 1) {
            scale = 1 / maxPower;
        }
        scale = scale / 2;

        upLeftDrive.setPower(up_left * scale);
        upRightDrive.setPower(up_right * scale);
        downLeftDrive.setPower(down_left * scale);
        downRightDrive.setPower(down_right * scale);

        odo.update();

        double lawfulGoodTime = getRuntime();
        double trueNeutralTime = lawfulGoodTime - chaoticEvilTime;
        double fakeNeutralFrequency = 1/trueNeutralTime;
        chaoticEvilTime = lawfulGoodTime;

        Pose2D pos = odo.getPosition();
        String data = String.format(Locale.US, "{X: %.3f, Y: %.3f, H: %.3f}", pos.getX(DistanceUnit.MM), pos.getY(DistanceUnit.MM), pos.getHeading(AngleUnit.DEGREES));
        telemetry.addData("Position", data);

        String velocity = String.format(Locale.US,"{XVel: %.3f, YVel: %.3f, HVel: %.3f}", odo.getVelX(DistanceUnit.MM), odo.getVelY(DistanceUnit.MM), odo.getHeadingVelocity(UnnormalizedAngleUnit.DEGREES));
        telemetry.addData("Velocity", velocity);

        telemetry.addData("Status", odo.getDeviceStatus());

        telemetry.addData("Pinpoint Frequency", odo.getFrequency());

        telemetry.addData("REV Hub Frequency: ", fakeNeutralFrequency);
        telemetry.update();
    }
}


