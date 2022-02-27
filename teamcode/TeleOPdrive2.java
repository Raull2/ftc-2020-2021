package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.concurrent.TimeUnit;

@TeleOp (name = "TeleOPDrive")
public class TeleOPdrive2 extends LinearOpMode {
    int liftCountZ=1;
    boolean isUp, isOpen, isMoving=false, isTeamElement=false;
    hardwarepapiu2021 robot = new hardwarepapiu2021();
    @Override
    public void runOpMode() throws InterruptedException {

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        /** Initialize the hardware map and reset encoders (base and intake motors) **/

        robot.init(hardwareMap);
        robot.EncoderReset();
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.bratz.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.bratz.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.bratz.setTargetPosition(0);
        robot.bratx.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Claws up
        robot.bascula1.setPosition(0.409);
        robot.bascula2.setPosition(0.52);
        liftCountZ=1;

        waitForStart();

        while (!isStopRequested()) {

            /** Base Movement **/

            // Limit speed while right bumper is pressed
            if(gamepad2.left_bumper){
                drive.setWeightedDrivePower(
                        new Pose2d(
                                -gamepad2.left_stick_y/3,
                                -gamepad2.left_stick_x/3,
                                -gamepad2.right_stick_x/3
                        )
                );
            } else {
                drive.setWeightedDrivePower(
                        new Pose2d(
                                -gamepad2.left_stick_y/1.2,
                                -gamepad2.left_stick_x/1.2,
                                -gamepad2.right_stick_x/1.2
                        )
                );
            }
            drive.update();

            /** Intake arm movement **/

                if (gamepad2.dpad_right) {
                    // Intake extension forward
                    robot.bratx.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.bratx.setPower(0.30);
                } else if (gamepad2.dpad_left) {
                    // Intake extension backwards (less power so that the arm doesn't recoil)
                    robot.bratx.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.bratx.setPower(-0.28);
                } else {
                    robot.bratx.setPower(0);
                }

            if(gamepad2.dpad_up) {
                // Limit movement so that it doesn't go above max height
                liftCountZ++;

                if (liftCountZ > 2) {
                    liftCountZ = 2;
                }
                else {
                    runToPosition(36, "up");
                }
            }

            if(gamepad2.dpad_down) {
                // Limit movement so that it doesn't go below min height
                liftCountZ--;

                if (liftCountZ < 1) {
                    liftCountZ = 1;
                }
                else {
                    runToPosition(36, "down");
                }
            }

            /** Carousel and claw **/

            if(gamepad2.a) {
                // Carousel duck thingy
                isMoving=!isMoving;
                rataMovement(isMoving);
            }

            if(gamepad2.y) {
                // Claw open/closed
                isOpen=!isOpen;
                claw(isOpen);
            }

            if(gamepad2.x){
                // Used for capping the team element
                isTeamElement=!isTeamElement;
                basculaUp(isTeamElement);
            }

            if(gamepad2.b) {
                // Claw up/down, 90 degrees
                isUp=!isUp;
                bascula(isUp);
            }

            /** Telemetry **/

            telemetry.addData("lift level bratz: ", liftCountZ);
            telemetry.addData("bratz ticks: ", robot.bratz.getCurrentPosition());
            telemetry.addData("bratz target", robot.bratz.getTargetPosition());
            telemetry.addData("team element grabbed", isTeamElement);
            telemetry.update();
        }
    }

    /** Varibile pentru facut automatizare la teleop */

    double PI = 3.1415;
    double GEAR_MOTOR_40_TICKS = 1120;
    double GEAR_MOTOR_ORBITAL20_TICKS = 537.6;
    double WHEEL_DIAMETER_CM = 4;
    //double TICKS_PER_CM_Z = GEAR_MOTOR_40_TICKS / (WHEEL_DIAMETER_CM * PI);
    double TICKS_PER_CM_Z = GEAR_MOTOR_ORBITAL20_TICKS / (WHEEL_DIAMETER_CM * PI);
    double TICKS_PER_CM_X = GEAR_MOTOR_ORBITAL20_TICKS / (WHEEL_DIAMETER_CM * PI);


    public void runToPosition(double distance, String rotation)
    {

        // Calcule
        int ticks;
        ticks = (int)(distance * TICKS_PER_CM_Z);

        /** Nu merge asa dintrun motiv (primul nivel nu merge tot timpul (ignora idle-ul) ) **/
//        if(rotation == "up") {
//            robot.bratz.setTargetPosition(-ticks);
//        }
//        else {
//            robot.bratz.setTargetPosition(ticks);
//        }
//
//        robot.bratz.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        robot.bratz.setPower(0.6);
//
//        while( robot.bratz.isBusy()  )
//        {
//            telemetry.addData("lift busy?", robot.bratz.isBusy());
//            telemetry.update();
//        }
//        if(liftCountZ!=0) {
//            robot.bratz.setPower(0.002);
//        }
//        else {
//            robot.bratz.setPower(0);
//        }
        if(liftCountZ == 1){
            robot.bratz.setTargetPosition(0);
        } else if(rotation == "up"){
            robot.bratz.setTargetPosition(robot.bratz.getCurrentPosition() - ticks);
        } else if(rotation == "down"){
            robot.bratz.setTargetPosition(robot.bratz.getCurrentPosition() + ticks);
        }
        robot.bratz.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.bratz.setPower(0.6);

    }

    public void claw(boolean isOpen)
    {
        if(isOpen){
            // Closed
            robot.cleste1.setPosition(0.5);
            robot.cleste2.setPosition(0.28);
        }
        if(!isOpen){
            // Open
            robot.cleste1.setPosition(0.4);
            robot.cleste2.setPosition(0.37);
        }
        try{
            TimeUnit.MILLISECONDS.sleep(300);
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();}
    }
    public void bascula(boolean isUp)
    {
        if(isUp){
            //Down
            robot.bascula1.setPosition(0.07);
            robot.bascula2.setPosition(0.85);
        }
        if(!isUp){
            // Up
            robot.bascula1.setPosition(0.409);
            robot.bascula2.setPosition(0.52);
        }
        try {
            TimeUnit.MILLISECONDS.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public void rataMovement(boolean isMoving){
        if(isMoving){
            robot.rata.setPower(0.4);
        }
        if(!isMoving){
            robot.rata.setPower(0);
        }
        try{
            TimeUnit.MILLISECONDS.sleep(300);
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    public void basculaUp(boolean isTeamElement)
    {
        if(isTeamElement){
            robot.bascula1.setPosition(0.65);
            robot.bascula2.setPosition(0.28);
            liftCountZ++;
            if (liftCountZ > 2) {
                liftCountZ = 2;
            }
            else runToPosition(38, "up");
        // TODO: change variable! ^^ (and finish)
        }
        if(!isTeamElement){
            // Up
            robot.bascula1.setPosition(0.409);
            robot.bascula2.setPosition(0.52);
            liftCountZ--;
            if (liftCountZ < 1) {
                liftCountZ = 1;
            }
            else runToPosition(38, "down");
        }
        try{
            TimeUnit.MILLISECONDS.sleep(300);
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
