package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//@Disabled
@TeleOp
public class EncoderValues extends OpMode {
    hardwarepapiu2021 robot= new hardwarepapiu2021();
    int countmotor=1,liftcount=1,liftcount2=1;
    @Override
    public void init(){
        // Initialize the hardware map and reset encoders (base and intake motors)
        robot.init(hardwareMap);
        robot.EncoderReset();
    }
    public void loop(){
        /** Encoder tests and stuff **/
        telemetry.addData("bratx ticks", robot.bratx.getCurrentPosition());
        telemetry.addData("bratz ticks", robot.bratz.getCurrentPosition());
        telemetry.addData("back-left ticks", robot.leftRear.getCurrentPosition());
        telemetry.addData("back-right ticks", robot.rightRear.getCurrentPosition());
        telemetry.addData("front-left ticks", robot.leftFront.getCurrentPosition());
        telemetry.addData("front-right ticks", robot.rightFront.getCurrentPosition());

        if(gamepad2.a) {
            robot.EncoderReset();
        }

        telemetry.update();
    }
}
