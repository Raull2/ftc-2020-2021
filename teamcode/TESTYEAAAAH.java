package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.concurrent.TimeUnit;
@Disabled
@TeleOp (name = "TESTYEAAAAAAAAH")
public class TESTYEAAAAH extends LinearOpMode {
    public DcMotorEx leftFront;
    int ass;
    hardwarepapiu2021 robot = new hardwarepapiu2021();
    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        // Initialize the hardware map and reset encoders (base and intake motors)

        robot.init(hardwareMap);
        robot.EncoderReset();
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.bratz.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.bratz.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.bratz.setTargetPosition(0);
        robot.bratx.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // Up
        robot.bascula1.setPosition(0.409);
        robot.bascula2.setPosition(0.52);

        waitForStart();
        while (opModeIsActive()){
            if(robot.leftFront.getCurrentPosition()>0){

            }
        }
    }
    public void countup(){
        ass++;
        try{
            TimeUnit.MILLISECONDS.sleep(300);
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
