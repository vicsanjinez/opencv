package com.appdimension.pruebaopencv;

/**
 * Created by studio on 06-02-17.
 */
public class NativeClass {

    public native static String getMessageFromJNI();

    public native static void faceDetection(long addrRgba);

    public native static void faceDetectionXML(long addrRgba, String pathFrontalFace, String pathEyes);

    public native static void humanDetectionXML(long addrRgba, String pathHuman);
}
