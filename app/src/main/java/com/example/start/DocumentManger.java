package com.example.start;


/*     用来填充适配器的文档类  */
/*     含一张图片和分类对应的名字 */
public class DocumentManger {

    public String name;
    public int imageID;
    //用来显示分类名字的TextView
    //public TextView textView;

    public DocumentManger(String name, int imageID){
        this.name = name;
        this.imageID = imageID;
        //this.textView = textView;
        //textView.setText(name);

    }

    public  String getName(){

        return name;
    }
    /*
    public TextView getTextView(){
        return textView;
    }


     */
    public  int getImageID() {
        return imageID;
    }

    public void Rename(String name){
        this.name = name;
    }
}
