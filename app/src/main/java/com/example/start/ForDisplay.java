package com.example.start;


import android.graphics.Bitmap;

/*     用来填充适配器的照片类  */
/*     含一张bitmap */
public class ForDisplay {

    public Bitmap imageID;

    public ForDisplay(Bitmap imageID){
        this.imageID = imageID;
    }




    public  Bitmap getImageID() {
        return imageID;
    }
    public void SetImage(Bitmap bitmap){ this.imageID = bitmap;}

}
