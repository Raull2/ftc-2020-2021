package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.drive.Drive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.slf4j.Marker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Autonomous(name = "!auto", group = "Autonomie2021")

public class AutonomieRataStart extends LinearOpMode {
    String MarkerLocation;
    // Initialize hardware maps
    public Pose2d startingSomething = new Pose2d(0,0);
    hardwarepapiu2021 robot = new hardwarepapiu2021();
//    poseStorage storePose = new poseStorage();
    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        robot.init(hardwareMap);
        robot.EncoderReset();

        /** CAMERA STUFF **/

        WebcamName webcam = hardwareMap.get(WebcamName.class, "Webcam 1");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        telemetry.addLine("Am initializat camera");
        telemetry.update();
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcam, cameraMonitorViewId);

        //creem variabila detector
        video2 detector = new video2(telemetry);
        //setam un pipline pt detector
        camera.setPipeline(detector);

        //incepem sa dam stream
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {

                camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {}
        });


        /** INITIAL POSITIONS **/

        // Set initial position of claw/other thing to closed to keep pre loaded object in place
        robot.cleste1.setPosition(0.5);
        robot.cleste2.setPosition(0.28);

        // Set initial position of robot
        Pose2d StartRosuBottom = new Pose2d(-34.5, -64, Math.toRadians(270));
        drive.setPoseEstimate(StartRosuBottom);

        /** TRAJECTORIES **/

        // Build the trajectory from start to carousel
        Trajectory StartToCarousel = drive.trajectoryBuilder(StartRosuBottom)
                .lineToLinearHeading(new Pose2d(-59.5,-62, Math.toRadians(35)),
                        // Limit speed of trajectory
                        SampleMecanumDrive.getVelocityConstraint(28, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();

        /** Build the trajectories for the 3 team object positions (LEFT/MID/RIGHT) **/

        // Starting from carousel
        Trajectory RataMidT = drive.trajectoryBuilder(StartToCarousel.end())
                .splineToLinearHeading(new Pose2d(-14, -47.5, Math.toRadians(90)), Math.toRadians(90),
                        // Limit speed of trajectory
                        SampleMecanumDrive.getVelocityConstraint(36, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(5, ()->{
                    // Up/Initial position
                    robot.bascula1.setPosition(0.409);
                    robot.bascula2.setPosition(0.52);
                    runToPozitionX(7, "right");
                    runToPositionZ(14, "up", 0.6);
                })
                .build();
        Trajectory RataRightT = drive.trajectoryBuilder(StartToCarousel.end())
                .addDisplacementMarker(3, ()->{
                    // Up/Initial position
                    robot.bascula1.setPosition(0.409);
                    robot.bascula2.setPosition(0.52);
                    runToPositionZ(36, "up", 0.6);
                    runToPozitionX(8, "right");
                })
                .splineToLinearHeading(new Pose2d(-14, -47.5, Math.toRadians(90)), Math.toRadians(90),
                        // Limit speed of trajectory
                        SampleMecanumDrive.getVelocityConstraint(36, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory RataLeftT = drive.trajectoryBuilder(StartToCarousel.end())
                .splineToLinearHeading(new Pose2d(-14, -47.5, Math.toRadians(90)), Math.toRadians(90),
                        // Limit speed of trajectory
                        SampleMecanumDrive.getVelocityConstraint(36, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(5, ()->{
                    // Up/Initial position
                    robot.bascula1.setPosition(0.409);
                    robot.bascula2.setPosition(0.52);
                })
                .build();

        /** Pre loaded duck **/
        Trajectory DuckLeft = drive.trajectoryBuilder(RataLeftT.end())
                .addDisplacementMarker(15, ()->{
                    runToPozitionX(18, "right");
                })
                .addDisplacementMarker(10, ()->{
                    // Lower claws to grab duck
                    robot.bascula1.setPosition(0.07);
                    robot.bascula2.setPosition(0.85);
                })
                .lineToLinearHeading(new Pose2d(4, -52, Math.toRadians(90)),
                        // Limit speed of trajectory
                        SampleMecanumDrive.getVelocityConstraint(36, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory DuckMid = drive.trajectoryBuilder(RataLeftT.end())
                .lineTo(new Vector2d(14.1, -52),
                        // Limit speed of trajectory
                        SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(23, ()->{
                    runToPozitionX(18, "right");
                })
                .addDisplacementMarker(4, ()->{
                    runToPozitionX(0, "left");
                    runToPositionZ(0, "down", 0.6);
                })
                .addDisplacementMarker(10, ()->{
                    // Lower claws to grab duck
                    robot.bascula1.setPosition(0.07);
                    robot.bascula2.setPosition(0.85);
                })
                .build();
        Trajectory DuckLeftReverse = drive.trajectoryBuilder(DuckLeft.end())
                .lineToLinearHeading(RataLeftT.end(),
                        // Limit speed of trajectory
                        SampleMecanumDrive.getVelocityConstraint(36, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(0.1, ()->{
                    // Up/Initial position
                    runToPozitionX(0, "left");
                    robot.bascula1.setPosition(0.409);
                    robot.bascula2.setPosition(0.52);
                })
                .build();
        Trajectory DuckMidReverse = drive.trajectoryBuilder(DuckMid.end())
                .lineToLinearHeading(RataLeftT.end(),
                        // Limit speed of trajectory
                        SampleMecanumDrive.getVelocityConstraint(36, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(0.1, ()->{
                    // Up/Initial position
                    runToPozitionX(0, "left");
                    robot.bascula1.setPosition(0.409);
                    robot.bascula2.setPosition(0.52);
                })
                .build();
        Trajectory DuckRight = drive.trajectoryBuilder(RataLeftT.end())
                .addDisplacementMarker(5, ()->{
                    runToPositionZ(0, "down", 0.6);
                })
                .addDisplacementMarker(5, ()->{
                    runToPozitionX(15, "right");
                    // Lower claws to grab duck
                    robot.bascula1.setPosition(0.07);
                    robot.bascula2.setPosition(0.85);
                })
                .lineToLinearHeading(new Pose2d(10.5,-39, Math.toRadians(0)),
                        // Limit speed of trajectory
                        SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .build();
        Trajectory DuckRightReverse = drive.trajectoryBuilder(DuckRight.end())
                .lineToLinearHeading(RataLeftT.end(),
                        // Limit speed of trajectory
                        SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(0.1, ()->{
                    // Up/Initial position
                    runToPozitionX(0, "left");
                    robot.bascula1.setPosition(0.409);
                    robot.bascula2.setPosition(0.52);
                })
                .build();

        /** Shipping hub to warehouse wall trajectory **/
        Trajectory ShippingToHub  = drive.trajectoryBuilder(RataLeftT.end())
                .lineToLinearHeading(new Pose2d(11.5, -74, Math.toRadians(0)),
                        // Limit speed of trajectory
                        SampleMecanumDrive.getVelocityConstraint(36, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(5, ()->{
                        runToPositionZ(0, "down", 0.6);
                        runToPozitionX(0, "left");
                })
                .build();
        // Warehouse wall to warehouse
        Trajectory WallToHub = drive.trajectoryBuilder(ShippingToHub.end())
                .lineToLinearHeading(new Pose2d(46.5, -74, Math.toRadians(0)),
                        // Limit speed of trajectory
                        SampleMecanumDrive.getVelocityConstraint(36, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker( 1, ()->{
                    // Move intake arm into position to grab freight (gambling)
                    runToPozitionX(15, "right");
                    // Lower claws to grab freight
                    robot.bascula1.setPosition(0.07);
                    robot.bascula2.setPosition(0.85);
                })
                .build();

        // Warehouse to wall, then to shipping hub (freight in robot hopefully)
        Trajectory WallToHubReverse = drive.trajectoryBuilder(WallToHub.end())
                .lineToConstantHeading(new Vector2d(6, -74))
                .addDisplacementMarker(2, ()->{
                    // Move intake arm into initial position
                    runToPozitionX(0, "left");
                    // Claws up
                    robot.bascula1.setPosition(0.409);
                    robot.bascula2.setPosition(0.52);
                })
                .build();
        Trajectory ShippingToHubReverse  = drive.trajectoryBuilder(WallToHubReverse.end())
                .lineToLinearHeading(new Pose2d(-14, -49.5, Math.toRadians(90)),
                        // Limit speed of trajectory
                        SampleMecanumDrive.getVelocityConstraint(DriveConstants.MAX_VEL/1.3, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(4, ()->{
                    // Lift 3rd level
                    runToPositionZ(36, "up", 0.6);
                    runToPozitionX(9, "right");
                })
                .build();
        Trajectory ShippingToHubFinal = drive.trajectoryBuilder(ShippingToHubReverse.end())
                .lineToLinearHeading(new Pose2d(11.5, -74, Math.toRadians(0)),
                        // Limit speed of trajectory
                        SampleMecanumDrive.getVelocityConstraint(36, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH),
                        SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                .addDisplacementMarker(()->{
                    runToPositionZ(0, "down", 0.4);
                    runToPozitionX(0, "left");
                })
                .build();
        Trajectory WallToHubFinal = drive.trajectoryBuilder(ShippingToHub.end())
                .lineTo(new Vector2d(44.5, -74))
                .build();

        waitForStart();

        /** FOLLOW TRAJECTORIES **/

        if (opModeIsActive()) {
            // Stop the camera and save the team marker position
            MarkerLocation = detector.location;
            camera.stopStreaming();

            drive.followTrajectory(StartToCarousel);
            // Start timer for how much the carousel motor should move
            resetStartTime();
            while(getRuntime()<2.2){
                robot.rata.setPower(0.36); //0.43
            }
            robot.rata.setPower(0);

            // Choose path based on team marker position
            if(MarkerLocation == "RIGHT"){
                drive.followTrajectory(RataRightT);
            }
            else if(MarkerLocation == "MID") {
                drive.followTrajectory(RataMidT);
            }
            else if(MarkerLocation == "LEFT"){
                drive.followTrajectory(RataLeftT);
            }
            else if(MarkerLocation == "NONE"){
                // If robot doesn't detect team marker use right position
                telemetry.addLine("No object in sight (probably broken!!!!!!!)");
                telemetry.update();
                drive.followTrajectory(RataRightT);
            }
            // Open claw to drop pre loaded object
            robot.cleste1.setPosition(0.3);
            robot.cleste2.setPosition(0.41);
            sleep(300);


            if(MarkerLocation == "LEFT"){
                drive.followTrajectory(DuckLeft);
            } else if(MarkerLocation == "MID"){
                drive.followTrajectory(DuckMid);
            } else if(MarkerLocation == "RIGHT" || MarkerLocation == "NONE"){
                drive.followTrajectory(DuckRight);
            }
            sleep(400);
            // Closed
            robot.cleste1.setPosition(0.6);
            robot.cleste2.setPosition(0.18);
            sleep(300);
            if(MarkerLocation == "LEFT"){
                drive.followTrajectory(DuckLeftReverse);
            } else if(MarkerLocation == "MID"){
                drive.followTrajectory(DuckMidReverse);
            } else if(MarkerLocation == "RIGHT" || MarkerLocation == "NONE"){
                drive.followTrajectory(DuckRightReverse);
            }
            sleep(100);
            // Open claw to drop pre loaded object
            robot.cleste1.setPosition(0.4);
            robot.cleste2.setPosition(0.365);
            sleep(200);


            // Hub to wall then warehouse
            drive.followTrajectory(ShippingToHub); // TODO: Optimise this
            drive.followTrajectory(WallToHub);
            sleep(300);


            // Close claw to grab freight
            robot.cleste1.setPosition(0.5);
            robot.cleste2.setPosition(0.28);
            sleep(300);


            drive.followTrajectory(WallToHubReverse);
            drive.followTrajectory(ShippingToHubReverse);
            // Open claw to drop freight
            sleep(300);
            robot.cleste1.setPosition(0.4);
            robot.cleste2.setPosition(0.365);
            sleep(300);


            // Final hub to wall then parking
            drive.followTrajectory(ShippingToHubFinal);
            drive.followTrajectory(WallToHubFinal);

//            storePose.lastPose = drive.getPoseEstimate();

        }
    }
    /** Motor constants **/
    double PI = 3.1415;
    double GEAR_MOTOR_40_TICKS = 1120;
    double GEAR_MOTOR_ORBITAL20_TICKS = 537.6;
    double WHEEL_DIAMETER_CM = 4;
    double TICKS_PER_CM_Z = GEAR_MOTOR_ORBITAL20_TICKS / (WHEEL_DIAMETER_CM * PI);
    double TICKS_PER_CM_X = GEAR_MOTOR_ORBITAL20_TICKS / (WHEEL_DIAMETER_CM * PI);


    public void runToPositionZ(double distance, String rotation, double speed)
    {
        // Calcule
        int ticks = (int)(distance * TICKS_PER_CM_Z);

        if(rotation == "up") {
            robot.bratz.setTargetPosition(-ticks);
        }
        if(rotation == "down") {
            robot.bratz.setTargetPosition(ticks);
        }

        robot.bratz.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.bratz.setPower(speed);
    }
    public void runToPozitionX(double distance, String rotation)
    {
        // Calcule
        int ticks = (int)(distance * TICKS_PER_CM_X);

        if(rotation == "right"){
            robot.bratx.setTargetPosition(ticks);
        }
        if(rotation == "left"){
            robot.bratx.setTargetPosition(-ticks);
        }

        robot.bratx.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.bratx.setPower(0.6);
    }
}