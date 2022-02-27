package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.concurrent.TimeUnit;

public class hardwarepapiu2021 {
    public DcMotor leftFront = null;
    public DcMotor rightFront = null;
    public DcMotor leftRear = null;
    public DcMotor rightRear = null;
    public DcMotor rata = null;
    public DcMotor bratz = null;
    public DcMotor bratx = null;
    public Servo cleste1 = null, bascula1 = null;
    public Servo cleste2 = null, bascula2 = null;

    HardwareMap hwMap = null;

    public hardwarepapiu2021(){}

    public void init(HardwareMap ahwMap){
        hwMap = ahwMap;

        /** Motoare Baza **/
        leftFront = hwMap.get(DcMotorEx.class, "leftFront");
        leftRear = hwMap.get(DcMotorEx.class, "leftRear");
        rightRear = hwMap.get(DcMotorEx.class, "rightRear");
        rightFront = hwMap.get(DcMotorEx.class, "rightFront");
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        /** Motoare/servo-uri brat **/
        rata = hwMap.get(DcMotorEx.class, "Rata" );
        bratz = hwMap.get(DcMotor.class, "bratz");
        bratx = hwMap.get(DcMotor.class, "bratx");
        cleste1 = hwMap.get(Servo.class, "cleste1");
        cleste2 = hwMap.get(Servo.class, "cleste2");
        bascula1 = hwMap.get(Servo.class, "bascula1");
        bascula2 = hwMap.get(Servo.class, "bascula2");

        /** Initial positions **/;
        cleste1.setPosition(0.4); //0.5 pt inchis
        cleste2.setPosition(0.37); //0.28 pt inchis
        bascula1.setPosition(0.07);
        bascula2.setPosition(0.85);
        rightRear.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rata.setDirection(DcMotorSimple.Direction.REVERSE);
        bratz.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }


    public void EncoderReset(){
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rata.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bratz.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bratz.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bratz.setTargetPosition(0);
        bratx.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bratx.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rata.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
