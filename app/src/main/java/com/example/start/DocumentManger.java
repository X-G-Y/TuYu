package com.example.start;

public class DocumentManger {

    private  String name;
    private  int imageID;

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
