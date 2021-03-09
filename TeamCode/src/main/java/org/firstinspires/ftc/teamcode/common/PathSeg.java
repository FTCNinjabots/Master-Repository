package org.firstinspires.ftc.teamcode.common;

public class PathSeg {
    public enum PathSegType
    {
        PATH_SEG_TYPE_DRIVE,
        PATH_SEG_TYPE_TURN_CLOCKWISE,
        PATH_SEG_TYPE_TURN_COUNTER_CLOCKWISE,
        PATH_SEG_TYPE_STRAFE_RIGHT,
        PATH_SEG_TYPE_STRAFE_LEFT
    };

    public PathSeg.PathSegType type;
    public double lpos;
    public double rpos;
    public double power;

    public PathSeg(PathSeg.PathSegType ptype, double lpos, double rpos, double power)
    {
        this.type = ptype;
        this.lpos = lpos;
        this.rpos = rpos;
        this.power = power;
    }
}
