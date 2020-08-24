package com.example.start;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DocumentActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST_CODE = 1;
    //int IMAGE_REQUEST_CODE = 1;
    String path;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        Toolbar toolbarr = findViewById(R.id.documentToolbar);
        imageView = findViewById(R.id.ImageForText);


        setSupportActionBar(toolbarr);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        //显示返回键
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //不显示标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 获取menu
        getMenuInflater().inflate(R.menu.menuindocument, menu);
        // 设置SearchView
        MenuItem menuItem = menu.findItem(R.id.AddPicture);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AddPicture:
                // User chose the "Settings" item, show the app settings UI...
                //在这里跳转到手机系统相册里面
                /*Intent intent = new Intent(
                        Intent.ACTION_GET_CONTENT,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //允许进行多选操作，并返回多个uri
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, IMAGE_REQUEST_CODE);

                 */


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//意图：文件浏览器
                intent.setType("*/*");//无类型限制
                //允许进行多选操作，并返回多个uri
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);//关键！多选参数
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                return true;

            case R.id.home:
                finish();
                return true;



            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        List<Bitmap> fileList = new ArrayList<>();
        /*

        if (requestCode == IMAGE_REQUEST_CODE && data != null) {
            ClipData imageNames = data.getClipData();
            if (imageNames != null) {
                for (int i = 0; i < imageNames.getItemCount(); i++) {
                    Uri imageUri = imageNames.getItemAt(i).getUri();
                    //fileList.add(imageUri.toString());
                    //System.out.println(imageUri);
                    imageView.setImageBitmap(transformUri(fileList, imageUri));

                }
                //uri = imageNames.getItemAt(0).getUri();
            } else {
                uri = data.getData();
                //fileList.add(uri.toString());
                imageView.setImageBitmap(transformUri(fileList, uri));

            }
        } else {
            uri = data.getData();
            //fileList.add(uri.toString());
            imageView.setImageBitmap(transformUri(fileList, uri));
        }

         */




        if (resultCode == DocumentActivity.RESULT_OK) {
            if (data.getData() != null) {
                //单次点击未使用多选的情况
                try {
                    Uri uri = data.getData();
                    //TODO 对获得的uri做解析，这部分在另一篇文章讲解
                    //String path = getPath(getApplicationContext(),uri);
                    //TODO 对转换得到的真实路径path做相关处理
                    imageView.setImageBitmap(transformUri(fileList, uri));
                } catch (Exception e) { }
            }
            else{
                //长按使用多选的情况
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    List<String> pathList=new ArrayList<>();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Uri uri = item.getUri();
                        //TODO 对获得的uri做解析，这部分在另一篇文章讲解
                        //String path = getPath(getApplicationContext(),uri);
                        //routers.add(path);
                        pathList.add(uri.toString());
                        imageView.setImageBitmap(transformUri(fileList, uri));
                    }
                    //TODO 对转换得到的真实路径path做相关处理

                }
            }
        }



        //在相册里面选择好相片之后调回到现在的这个activity中
        /*switch (requestCode) {
            case IMAGE_REQUEST_CODE://这里的requestCode是我自己设置的，就是确定返回到那个Activity的标志
                if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                    try {
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        path = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        if (ContextCompat.checkSelfPermission(DocumentActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(DocumentActivity.this, new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                        } else {
                            imageView.setImageBitmap(bitmap);
                        }

                    } catch (Exception e) {
                        // TODO Auto-generatedcatch block
                        e.printStackTrace();
                    }
                }
                break;
        }


    }
     */
    }



    public Bitmap transformUri(List list,Uri uri){

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            list.add(bitmap);
            return bitmap;
        }catch (Exception c){

        }
        return bitmap;

    }



}