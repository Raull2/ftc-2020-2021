package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Disabled
@Autonomous (name = "!testAuto")
public class testAuto extends LinearOpMode {
    hardwarepapiu2021 robot = new hardwarepapiu2021();
    @Override
    public void runOpMode(){
            SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
            robot.init(hardwareMap);
            robot.EncoderReset();
            Pose2d StartRosuBottom = new Pose2d(-34.5, -64, Math.toRadians(270));
            drive.setPoseEstimate(StartRosuBottom);

            Trajectory splineStraight = drive.trajectoryBuilder(StartRosuBottom)
                .lineTo(new Vector2d(-34.5, -30))
                    .splineTo(new Vector2d(-11.5,-10), Math.toRadians(90))
                .build();

            waitForStart();

            if(opModeIsActive()){
                drive.followTrajectory(splineStraight);
            }
    }
}
