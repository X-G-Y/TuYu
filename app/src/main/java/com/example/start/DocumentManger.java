package com.example.start;


/*     用来填充适配器的文档类  */
/*     含一张图片和分类对应的名字 */
public class DocumentManger {

    public String name;
    public int imageID;

    public DocumentManger(String name, int imageID){
        this.name = name;
        this.imageID = imageID;

    }

    public  String getName(){
        return name;
    }

    public  int getImageID() {
        return imageID;
    }
}
