
package org.firstinspires.ftc.teamcode.RingVision;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;


import java.util.ArrayList;
import java.util.Collections;

import static org.firstinspires.ftc.teamcode.RingVision.Ring_Pipeline.maxY;


public class Ring {
    private final double relX;
    private final double relY;
    private double absX;
    private double absY;
    private static double DIST_THRESH = 1.5;
    public static double minX = 60;
    public static double minY = 94;
    public static double maxX = 132;
    public static double maxY = 142;

    public Ring(double relX, double relY, double absX, double absY) {
        this.relX = relX;
        this.relY = relY;
        this.absX = absX;
        this.absY = absY;
    }

    public Ring(double relX, double relY) {
        this.relX = relX;
        this.relY = relY;
    }


    // Calculate ring absolute coordinates using relative coordinates and robot position
    public void calcAbsCoords(double robotX, double robotY, double robotTheta) {
        absX = robotX + relX * Math.sin(robotTheta) + relY * Math.cos(robotTheta);
        absY = robotY - relX * Math.cos(robotTheta) + relY * Math.sin(robotTheta);
    }

    @SuppressLint("DefaultLocale")
    public String toString() {
        return "R(" + String.format("%.3f", relX) + ", " + String.format("%.3f", relY) + "), A(" + String.format("%.3f", absX) + ", " + String.format("%.3f", absY) + ")";
    }

    public Ring clone() {
        return new Ring(relX, relY, absX, absY);
    }


    public double getAbsDist(double robotX, double robotY) {
        return Math.hypot(absX - robotX, absY - robotY);
    }

    public double getX() {
        return absX;
    }

    public double getY() {
        return absY;
    }

    public double getRelY(){
        return relY;
    }
    public double getRelX(){
        return relX;
    }
}
