package org.firstinspires.ftc.robotcontroller.external.samples;

public class VisionNavigator {


    public int pixel_to_cm_y(double pixelval){
        double cm_to_pixel_y = 0.079; //0.06879166666666666666666666666667
        return (int) (pixelval * cm_to_pixel_y);
    }

    public int pixel_to_cm_x(double pixelval){
        double cm_to_pixel_x = 0.082; //0.079375
        return (int) (pixelval * cm_to_pixel_x);
    }


    public int[] calculate_world_coords(double pixel_x, double pixel_y){
        //Use pixel to cm ratio and apply it to our pixels
        //From this we should return 2 cm values

        int world_x = pixel_to_cm_x(pixel_x);
        int world_y = pixel_to_cm_y(pixel_y);

        return new int[] {world_x, world_y};

    }
}
