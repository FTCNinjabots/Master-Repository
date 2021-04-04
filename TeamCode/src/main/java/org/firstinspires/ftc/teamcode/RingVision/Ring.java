package org.firstinspires.ftc.teamcode.RingVision;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;

public class Ring
{
    private double RelX;
    private double RelY;
    private double AbsX;
    private double AbsY;
    private static Telemetry tele;


    public Ring(double relX, double relY, double absX, double absY, Telemetry telemetry)
    {
        this.RelX = relX;
        this.RelY = relY;
        this.AbsX = absX;
        this.AbsY = absY;
        this.tele = telemetry;
    }

    public Ring (double relX, double relY)
    {
        this.RelX = relX;
        this.RelY = relY;
    }

    public static ArrayList<Ring> getRings(ArrayList<Ring> rings)
    {
        // See if the rings are out of our bounds
        int i = 0;
        while(i < rings.size()){
            Ring curRing = rings.get(i);
            if (curRing.getX() < RingDetector.minX || curRing.getX() > RingDetector.maxX || curRing.getY() < RingDetector.minY || curRing.getY() > RingDetector.maxY)
            {
                //Clip rings
                tele.addData("Clipped Ring from boundary: ", i);
                tele.update();
                rings.remove(i);


            }
            i++;
        }

        //See if Rings are too close to each other
        i = 0;
        while (i < rings.size()){
            double xi = rings.get(i).getX();
            double yi = rings.get(i).getY();

            int j = i + 1;
            while (j < rings.size()){
                Ring curRing = rings.get(j);
                if (Math.abs(xi - curRing.getX()) < RingDetector.RING_DIST_THRESH || Math.abs(yi - curRing.getY()) < RingDetector.RING_DIST_THRESH)
                {

                    tele.addData("Clipped Ring from threshold: ", j);
                    tele.update();
                    rings.remove(j);
                }
                j ++;

            }
            i++;
        }

    return rings;
    }

    public void calcAbsCoords(double robotX, double robotY, double robotTheta) {
        AbsX = robotX + RelX * Math.sin(robotTheta) + RelY * Math.cos(robotTheta);
        AbsY = robotY - RelX * Math.cos(robotTheta) + RelY * Math.sin(robotTheta);
    }

    public double[] getAbsCoords() {
        return new double[] {AbsX, AbsY};
    }

    public double getRelDist() {
        return Math.hypot(RelX, RelY);
    }

    public double getAbsDist(double robotX, double robotY) {
        return Math.hypot(AbsX - robotX, AbsY - robotY);
    }

    public double getRelX() {
        return RelX;
    }

    public double getRelY() {
        return RelY;
    }

    public double getX() {
        return AbsX;
    }

    public double getY() {
        return AbsY;
    }

}
