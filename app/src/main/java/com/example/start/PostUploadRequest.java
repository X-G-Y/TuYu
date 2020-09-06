package com.example.start;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PostUploadRequest extends Request<String> {

    // 用于回调，回复监听器
    private ResponseListener mListener ;
    // 传入图片
    private List<FormImg> mListItem ;

    private String BOUNDARY = "--------------520-13-14"; //数据分隔线
    private String MULTIPART_FORM_DATA = "multipart/form-data";

    public PostUploadRequest(String url, List<FormImg> listItem, ResponseListener listener) {
        super(Method.POST, url, listener);
        this.mListener = listener ;
        setShouldCache(false);
        mListItem = listItem ;
        //设置请求的响应事件，因为文件上传需要较长的时间，所以在这里加大了，设为5秒
        setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    // 解析 Response
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String mString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.v("zgy", "====mString===" + mString);

            return Response.success(mString,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    /**
     * 回调正确的数据
     * @param response The parsed response returned by
     */
    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public byte[] getBody() throws AuthFailureError {
        if (mListItem == null||mListItem.size() == 0){
            return super.getBody() ;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        int N = mListItem.size() ;
        FormImg formImage ;
        for (int i = 0; i < N ;i++){
            formImage = mListItem.get(i) ;
            StringBuilder sb= new StringBuilder() ;
            /*第一行*/
            //`"--" + BOUNDARY + "\r\n"`
            sb.append("--").append(BOUNDARY);
            sb.append("\r\n") ;
            /*第二行*/
            //Content-Disposition: form-data; name="参数的名称"; filename="上传的文件名" + "\r\n"
            sb.append("Content-Disposition: form-data;");
            sb.append(" name=\"");
            sb.append(formImage.getName()) ;
            sb.append("\"") ;
            sb.append("; filename=\"") ;
            sb.append(formImage.getFileName()) ;
            sb.append("\"");
            sb.append("\r\n") ;
            /*第三行*/
            //Content-Type: 文件的 mime 类型 + "\r\n"
            sb.append("Content-Type: ");
            sb.append(formImage.getMime()) ;
            sb.append("\r\n") ;
            /*第四行*/
            //"\r\n"
            sb.append("\r\n") ;
            try {
                bos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
                /*第五行*/
                //文件的二进制数据 + "\r\n"
                bos.write(formImage.getValue());
                bos.write("\r\n".getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        /*结尾行*/
        //`"--" + BOUNDARY + "--" + "\r\n"`
        String endLine = "--" + BOUNDARY + "--" + "\r\n" ;
        try {
            bos.write(endLine.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("zgy","=====formImage====\n"+bos.toString()) ;
        return bos.toByteArray();
    }
    //Content-Type: multipart/form-data; boundary=----------8888888888888
    @Override
    public String getBodyContentType() {
        return MULTIPART_FORM_DATA+"; boundary="+BOUNDARY;
    }
}
