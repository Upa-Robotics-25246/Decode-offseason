package org.firstinspires.ftc.teamcode.util.test;

;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class axonTest extends OpMode{

    Servo axon;
    CRServo CRaxon;
//    AnalogInput axonInput;
//    AnalogInput CRaxonInput;
    @Override
    public void init() {

        axon = hardwareMap.get(Servo.class, "axon");
//        axonInput = hardwareMap.get(AnalogInput.class, "axonInput");
        CRaxon = hardwareMap.get(CRServo.class, "CRaxon");
       // CRaxonInput = hardwareMap.get(AnalogInput.class, "CRaxonInput");
        axon.setPosition(0);
        CRaxon.setPower(0);

    }

    @Override
    public void loop() {


       // telemetry.addData("Axon Input",axonInput.getVoltage());
        //telemetry.addData("CR Axon Input",CRaxonInput.getVoltage());

        telemetry.addData("Axon Pos", axon.getPosition());
        telemetry.addData("CR Axon Power", CRaxon.getPower());

        if(gamepad1.dpadUpWasPressed() && axon.getPosition() <= 1) {
            axon.setPosition(axon.getPosition() + 0.1);
        }
        if(gamepad1.dpadDownWasPressed() && axon.getPosition() >= -1 ){
            axon.setPosition(axon.getPosition() - 0.1);
        }
        if(gamepad1.dpadRightWasPressed()&& CRaxon.getPower() <= 1 ){
            CRaxon.setPower(CRaxon.getPower() + 0.1);
        }
        if(gamepad1.dpadLeftWasPressed() && CRaxon.getPower() >= -1 ){
            CRaxon.setPower(CRaxon.getPower() - 0.1);
        }

    }
}
