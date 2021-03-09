package org.firstinspires.ftc.teamcode.common;

import java.util.ArrayList;
import java.util.List;

public class Path {

    List<PathSeg> segments;
    private int curIndex;

    public Path(PathSeg seg[])
    {
        // Allocate a dynamic array of segments and add path to segment
        this.segments = new ArrayList<>();
        for (int i = 0; i < seg.length; i++)
        {
            this.addSegment(seg[i]);
        }
        this.resetPath();
    }

    public void addSegment(PathSeg seg)
    {
        this.segments.add(seg);
    }

    public void resetPath()
    {
        this.curIndex = 0;
    }

    public PathSeg getNextSegment()
    {
        PathSeg seg = null;

        if (this.curIndex < this.segments.size())
        {
            seg = this.segments.get(this.curIndex);
            this.curIndex = this.curIndex + 1;
        }

        return seg;
    }

    public boolean isPathComplete()
    {
        return (this.curIndex == this.segments.size());
    }

    public int getSegmentIndex()
    {
        return this.curIndex;
    }
}
