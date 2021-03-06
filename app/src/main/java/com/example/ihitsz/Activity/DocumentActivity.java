package com.example.ihitsz.Activity;


import android.Manifest;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ihitsz.Adapter.RecyclerViewAdapterForPicture;
import com.example.ihitsz.Helper.GridSpacingItemDecoration;
import com.example.ihitsz.Myclass.FormImg;
import com.example.ihitsz.Myclass.ImageManager;
import com.example.ihitsz.R;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DocumentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private com.example.ihitsz.Adapter.RecyclerViewAdapterForPicture myRecyclerviewAdapter;
    private List<com.example.ihitsz.Myclass.FormImg> listAll = new ArrayList<>();
    private ImageButton showit;
    private Tencent mTencent;
    private String datapath;
    private Bitmap imagebitmap;
    private String lag = "chi_sim";
    private int size;
    private int Num = 0;//当前照片数量
    private String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);



        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
    // 其中APP_ID是分配给第三方应用的appid，类型为String。
    // 其中Authorities为 Manifest文件中注册FileProvider时设置的authorities属性值
        mTencent = Tencent.createInstance("1111138164", this.getApplicationContext(), "ihitsz.xyz");
// 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        TextView textView = findViewById(R.id.TextViewInDocuemnt);
        textView.setText(name);

        //获得查看放大图片的布局中控件
        size = Getdata();
        Num = size;
        PULLmBitmap();



        //Toolbar
        Toolbar toolbarr = findViewById(R.id.documentToolbar);
        setSupportActionBar(toolbarr);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        //显示返回键
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //不显示标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);





        //recyclerView
        recyclerView = findViewById(R.id.myrecyclerview);
        //创建一个网格视图管理器
        GridLayoutManager manager = new GridLayoutManager(
                this, 4
        );
        //瀑布流
        //StaggeredGridLayoutManager manager = new
        //StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        //设置管理器
        recyclerView.setLayoutManager(manager);

        myRecyclerviewAdapter = new RecyclerViewAdapterForPicture(listAll);
        myRecyclerviewAdapter.GetManger(manager);
        recyclerView.setAdapter(myRecyclerviewAdapter);
        //设置颜色分割线
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //recyclerView.addItemDecoration(new GridDivider(this, 20, this.getResources().getColor(R.color.black)));
        //通过列数设置Item间距

        int spanCount = 4; // 3 columns
        int spacing = 15; // 15px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


        //设置默认动画效果
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //设置RecyclerView的每一项的长按事件
        myRecyclerviewAdapter.setOnItemLongClickListener(new RecyclerViewAdapterForPicture.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                ShowPops(view, position);
            }
        });

        //设置RecyclerView的每一项的点击事件
        myRecyclerviewAdapter.setOnItemClickListener(new RecyclerViewAdapterForPicture.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                IntentArray();
            }
        });


    }

    private void Ocrr(){
        for(int i=Num;i<listAll.size();i++){
            imagebitmap = listAll.get(i).getmBitmap();
            initOcr();
            String ocr = doOcr(imagebitmap, "/chi_sim");
            listAll.get(i).Rename(ocr);
            //Toast.makeText(DocumentActivity.this, ocr, Toast.LENGTH_SHORT).show();
        }
        Num = listAll.size();
        Save();

    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取外存目录
        }
        return sdDir.toString();
    }


    public String doOcr(Bitmap bitmap, String language) {

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.init(datapath, language);
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        baseApi.setImage(bitmap);
        String resultTxt = baseApi.getUTF8Text();
        baseApi.clear();
        baseApi.end();
        return resultTxt;
        //get resultTxt to do something
    }


    /**
     * @param dir
     * @param language chi_sim eng
     */
    private void checkFile(File dir, String language) {
        //如果目前不存在则创建方面，然后在判断训练数据文件是否存在
        if (!dir.exists() && dir.mkdirs()) {
            copyFiles(lag);
        }
        if (dir.exists()) {
            String datafilepath = datapath + "/tessdata/" + language + ".traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles(lag);
            }
        }
    }

    /**
     把训练数据放到手机内存
     * @param language "chi_sim" ,"eng"
     */
    private void copyFiles(String language) {
        try {
            String filepath = datapath + "/tessdata/" + language + ".traineddata";
            AssetManager assetManager = getAssets();
            InputStream instream = assetManager.open("tessdata/" + language + ".traineddata");
            OutputStream outstream = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化ocr识别需要用到的训练数据
     */
    private void initOcr() {
        datapath = getFilesDir() + "/tesseract/";
        checkFile(new File(datapath + "tessdata/"), "chi_sim");
    }

    //将数据放到intent中并传输
    private void IntentArray(){
        Intent intent = new Intent(DocumentActivity.this, ShowBigPicture.class);
        Bundle bundle = new Bundle();
        ArrayList list = new ArrayList();
        list.add(listAll);
        bundle.putParcelableArrayList("list", list);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 获取menu
        getMenuInflater().inflate(R.menu.menuindocument, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AddPicture:
                //获取读取手机储存权限
                if (ContextCompat.checkSelfPermission(DocumentActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DocumentActivity.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                } else {
                }
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




    protected void ShowPops(View v,int pos){
        QMUIPopups.quickAction(getBaseContext(),
                QMUIDisplayHelper.dp2px(getBaseContext(), 56),
                QMUIDisplayHelper.dp2px(getBaseContext(), 56))
                .shadow(true)
                .skinManager(QMUISkinManager.defaultInstance(getBaseContext()))
                .edgeProtection(QMUIDisplayHelper.dp2px(getBaseContext(), 20))
                .addAction(new QMUIQuickAction.Action().icon(R.drawable.icon_quick_action_share).text("分享").onClick(
                        new QMUIQuickAction.OnClickListener() {
                            @Override
                            public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                Uri uri = Uri.parse(listAll.get(position).getMime());
                                Toast.makeText(DocumentActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                                onClickShare(uri);
                                quickAction.dismiss();
                            }
                        }
                ))
                .addAction(new QMUIQuickAction.Action().icon(R.drawable.icon_quick_action_copy).text("重命名").onClick(
                        new QMUIQuickAction.OnClickListener() {
                            @Override
                            public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                quickAction.dismiss();
                                showRenameDialog(pos);
                                Toast.makeText(getBaseContext(), "修改成功", Toast.LENGTH_SHORT).show();

                            }
                        }
                ))
                .addAction(new QMUIQuickAction.Action().icon(R.drawable.icon_popup_close_dark).text("删除").onClick(
                        new QMUIQuickAction.OnClickListener() {
                            @Override
                            public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                quickAction.dismiss();
                                Toast.makeText(getBaseContext(), "删除成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                )).show(v);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == DocumentActivity.RESULT_OK) {
            if (data.getData() != null) {
                //单次点击未使用多选的情况
                try {
                    Uri uri = data.getData();
                    transformUri(uri);
                    Toast.makeText(DocumentActivity.this, "选择照片", Toast.LENGTH_SHORT).show();
                    myRecyclerviewAdapter.notifyItemInserted(myRecyclerviewAdapter.getItemCount());
                    recyclerView.scrollToPosition(myRecyclerviewAdapter.getItemCount());
                    //TODO 对获得的uri做解析，这部分在另一篇文章讲解
                    //String path = getPath(getApplicationContext(),uri);
                    //TODO 对转换得到的真实路径path做相关处理
                    //imageView.setImageBitmap(transformUri(fileList, uri));
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
                        System.out.println(uri.toString());
                        transformUri(uri);
                        myRecyclerviewAdapter.notifyItemInserted(myRecyclerviewAdapter.getItemCount());
                        recyclerView.scrollToPosition(myRecyclerviewAdapter.getItemCount());

                    }
                    Toast.makeText(DocumentActivity.this, "选择照片", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }


    //新增图片
    private void newManger(){
        myRecyclerviewAdapter.notifyItemInserted(myRecyclerviewAdapter.getItemCount());
        recyclerView.scrollToPosition(myRecyclerviewAdapter.getItemCount());
    }






    //将uri转化为bitmap并且将其保存在list中
    public void transformUri(Uri uri){

        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            FormImg display = new FormImg("null", "null", uri.toString(), bitmap);
            listAll.add(display);
            Ocrr();
        }catch (Exception c){

        }
        TransFormBitmap(listAll);

    }

    //设置重命名时候的dialog
    public void showRenameDialog(final int position){
        String text = listAll.get(position).getName();
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(this);
        builder.setTitle("修改关键字")
                .setPlaceholder(text)
                //设置点击外部时dialog是否关闭
                .setCanceledOnTouchOutside(true)
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("确认", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence editText = builder.getEditText().getText();
                        if (editText.length() == 0 ) {
                            //期望是dialog的动态刷新，目前以Toast的方式提示用户
                            Toast.makeText(basic.myActivity, R.string.toastForNone1, Toast.LENGTH_SHORT).show();
                            Toast.makeText(basic.myActivity, R.string.toastForNone2, Toast.LENGTH_SHORT).show();
                        } else {
                            listAll.get(position).Rename(editText.toString());
                            myRecyclerviewAdapter.notifyItemChanged(position);
                            //实时保存数据
                            dialog.dismiss();
                        }
                    }
                })

                .addAction("取消", new QMUIDialogAction.ActionListener() {

                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .show();
        return ;
    }


    public  String getRealFilePath( final Context context, final Uri uri ) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }



    /**
     * 获取文件选择器选中的文件路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getPath(Context context, Uri uri) {
        String path;
        //使用第三方应用打开
        if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
            return path;
        }
        //4.4以后
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            path = getPathFromNewSdk(context, uri);
            //4.4以下下系统调用方法
        } else {
            path = getPathFromOldSdk(context, uri);
        }
        return path;
    }

    private static String getPathFromOldSdk(Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    private static String getPathFromNewSdk(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }




    //QQ分享图片
    private void onClickShare(Uri uri) {
        Bundle params = new Bundle();

        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,getPathFromNewSdk(getApplicationContext(), uri));
        text = getPathFromNewSdk(getApplicationContext(), uri);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "1111138164");
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        mTencent.shareToQQ(DocumentActivity.this, params, new BaseUiListener());
    }


    class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {

            Toast.makeText(DocumentActivity.this,"分享成功",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(UiError e) {
            // 分享异常
            Toast.makeText(DocumentActivity.this,"code:" + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail,Toast.LENGTH_SHORT).show();
            Toast.makeText(DocumentActivity.this,text,Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onCancel() {
            //分享取消
            Toast.makeText(DocumentActivity.this,"分享取消",Toast.LENGTH_SHORT).show();

        }
    }



    //ForDisplay类型数据储存接口
    //传进来一个List，里面是ForDisplay的数据
    //可以看java文件里的ForDisplay类
    private void TransFormBitmap(List<FormImg> list){
        //todo
        ImageManager imageManager = new ImageManager();
        for(int i=size;i<list.size();i++){
            imageManager.PushImageRequest(list.get(i).getmBitmap(), "name"+i, getBaseContext());
        }
    }

    private void PULLmBitmap(){
        //todo
        ImageManager imageManager = new ImageManager();
        Bitmap bitmap;
        for(int i=0;i<size;i++){
            //todo
        }
    }

    public void Save(){
        SharedPreferences.Editor editor = getSharedPreferences("Doc", Context.MODE_PRIVATE).edit();
        editor.putInt("num", listAll.size());

    }
    public int Getdata(){
        int num;
        SharedPreferences pre = getSharedPreferences("Doc", Context.MODE_PRIVATE);
        num = pre.getInt("num", 0);
        return num;
    }


}