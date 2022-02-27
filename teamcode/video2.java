package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvPipeline;

public class video2 extends OpenCvPipeline {

    Telemetry telemetry;
    //Creating a Matrix
    Mat mat = new Mat();
    //Pozitions for the obj
    String LocationString;
    public String location;

    //ROI = region of interest
    /**Region of intrest for the camera**/
    static final Rect LEFT_ROI = new Rect(
            new Point(0, 90),
            new Point(40, 180));
    static final Rect MID_ROI = new Rect(
            new Point(125, 90),
            new Point(180, 180));
    static final Rect RIGHT_ROI = new Rect(
            new Point(280, 90),
            new Point(320, 180));
    //Treshold
    static double PERCENT_COLOR_THRESHOLD = 0.2;

    public video2(Telemetry t) {
        telemetry = t;
    }

    @Override
    public Mat processFrame(Mat input) {
        //transforming the video from BGR to HSV
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);

        /**THE COLOR FOR THE OBJ**/
        /*
        low 110,50,50
        high 130,255,255
        */
        Scalar lowHSV = new Scalar(100,150,0);
        Scalar highHSV = new Scalar(140,255,255);

        // adding the treshold to the matrix
        Core.inRange(mat, lowHSV, highHSV, mat);

        // Creeatin 3 sub matrixes
        Mat left = mat.submat(LEFT_ROI);//sub matrix = submat
        Mat right = mat.submat(RIGHT_ROI);
        Mat mid = mat.submat(MID_ROI);

        //determing the precentige of the white pixels in each submatrix
        double leftValue = Core.sumElems(left).val[0] / LEFT_ROI.area() / 255;
        double rightValue = Core.sumElems(right).val[0] / RIGHT_ROI.area() / 255;
        double midValue = Core.sumElems(mid).val[0] / MID_ROI.area() / 255;
        //releasing the sub matrixes
        left.release();
        right.release();
        mid.release();

        //showing data on the phone screen
        telemetry.addData("valoare stanga", (int) Core.sumElems(left).val[0]);
        telemetry.addData("Valoare dreapta", (int) Core.sumElems(right).val[0]);
        telemetry.addData("valoare mijloc", (int) Core.sumElems(mid).val[0]);
        telemetry.addData("procent stanga", Math.round(leftValue * 100) + "%");
        telemetry.addData("procent dreapta", Math.round(rightValue * 100) + "%");
        telemetry.addData("procent mijloc", Math.round(midValue * 100) + "%");

        // if the precentige of the white pixels is bigger then the treshold
        boolean cevaLeft = leftValue > PERCENT_COLOR_THRESHOLD;
        boolean cevaRight = rightValue > PERCENT_COLOR_THRESHOLD;
        boolean cevaMid = midValue > PERCENT_COLOR_THRESHOLD;

        if (cevaRight) {
            location = "RIGHT";
            telemetry.addData("Locatie", "DREAPTA");
        } else if (cevaLeft) {
            location = "LEFT";
            telemetry.addData("Locatie", "STANGA");
        } else if(cevaMid) {
            location = "MID";
            telemetry.addData("Locatie", "MIJLOC");
        }
        if(!cevaRight && !cevaLeft && !cevaMid){
            location = "NONE";
            telemetry.addData("Locatie", "NIMIC");
        }
            telemetry.update();
            // transforming the matrix from GRAY to BGR
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);

            // creeating 3 rectangles for the roi
            Imgproc.rectangle(mat, LEFT_ROI, new Scalar(0, 255, 0));// (0, 255, 0) == GREEN
            Imgproc.rectangle(mat, MID_ROI, new Scalar(0, 255, 0));
            Imgproc.rectangle(mat, RIGHT_ROI, new Scalar(0, 255, 0));

        return mat;
    }
}
