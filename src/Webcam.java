/**
 * Created by Lua_b on 10.01.2016.
 */

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class Webcam {

    public static void main (String args[]) {
        System.out.println("Hello, OpenCV");
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println(Core.NATIVE_LIBRARY_NAME);

        VideoCapture camera = new VideoCapture(0);

        try
        {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!camera.isOpened()){
            System.out.println("Camera Error");
        }
        else{
            System.out.println("Camera OK?");
        }
        Mat firstframe = new Mat();
       firstframe = Imgcodecs.imread("capture.jpg");
       // camera.read(firstframe);
        Imgproc.cvtColor(firstframe,firstframe,Imgproc.COLOR_BGR2GRAY);
        Mat frame = new Mat();
        camera.read(frame);
        Imgcodecs.imwrite("raw.jpg", frame);
        System.out.println(frame.size());
        System.out.println(firstframe.size());

        Imgproc.cvtColor(frame,frame,Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(frame,frame,new Size(21,21),0);

        Mat dist = new Mat();
        Core.absdiff(frame,firstframe,dist);
       // Imgproc.threshold(dist,dist, 30, 255, Imgproc.THRESH_BINARY);
        Imgproc.adaptiveThreshold(dist, dist, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C,Imgproc.THRESH_BINARY_INV, 5, 2);
        Imgcodecs.imwrite("redacted.jpg", frame);
        Imgcodecs.imwrite("capture.jpg", dist);

    }
}