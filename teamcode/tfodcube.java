package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
@Disabled
@TeleOp(name = "cube detector")
public class tfodcube extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
            "Cube",
    };
    private static final String VUFORIA_KEY = "AYbTfIX/////AAABmb/r0zSwYkJghZIi+JUhe7gGVCU2S/s0LvhaJsnH2QIr4zyh+Zy86KhspEmR1gVCfCKA9eE7iJWS0I1bg+54j6UN64ejks/4D++BW7mgzdCia+IGQG95l+PYcUBp0mmjtfU2Tnzf9KvuElxd7m+4JcjzqFTvg3pK46ywpjbGk5ZINzoYWhEL4k89ECwodXurIgi5+nHOvYAQa0ZpuUeCOpQ62tzuEe8BFNzbhj0cvmtJU0hlgWrzDkZpY+vpZwg3oLe//S3VPSsgsBfli6Snq0Damzc7MimAQRxJ4wJX1j3mxMF5qJl9xBcrWVmLPVCbQWwqezZIYhXHV3RJYdHdp2RsYwJbKBn46gS1PuepSooD";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {
        //pentru ca pentru detectarea de obiect folosin vuforia asa ca o initializam prma
        initVuforia();
        initTfod();

        /**Detectam cu TensorFlow pana inainte sa dam start**/
        if (tfod != null) {
            tfod.activate();
            //setam un zoom de 2.5
            tfod.setZoom(2.5, 16.0/9.0);
        }
        //sa vad daca ajunge aici :))
        telemetry.addData("ceva", "FUNCTIONEZ");
        telemetry.update();
        waitForStart();

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                if (tfod != null) {
                   //afisam ce va vedea camera
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        //arata cate obiecte sunt detectate
                        telemetry.addData("Obiecte detectate", updatedRecognitions.size());
                        // afisam ce obiect este pe ecran
                        int i = 0;
                        for (Recognition recognition : updatedRecognitions) {
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            i++;
                        }
                        telemetry.update();
                    }
                }
            }
        }
    }

    /**
     * se initializeaza enginul de Vuforia
     */
    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //enginul de Vuforia
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

    }

    /**
     * se face detectarea de obiect cu TensorFlow
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        //creem un parametru care dau map la un imput la un output
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
}
