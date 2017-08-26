package com.shuhao.webchromeclient;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


public class FileChooserWebChromeClient extends WebChromeClient {
    static class FileChooserWebChromeClientBuild{
        UploadMessage uploadMessage;
        ActivityCallBack callBack;
        FileChooserWebChromeClientBuild(ActivityCallBack callBack,UploadMessage uploadMessage){
            this.callBack = callBack;
            this.uploadMessage = uploadMessage;
        }
        public FileChooserWebChromeClient build(){
            return new FileChooserWebChromeClient(callBack,uploadMessage);
        }
    }
    public static FileChooserWebChromeClient createBuild(ActivityCallBack callBack,UploadMessage uploadMessage){
        return new FileChooserWebChromeClientBuild(callBack,uploadMessage).build();
    }
    public static final int REQUEST_CAMERA_PERMISSION = 1;
    public static final int INPUT_FILE_REQUEST_CODE = 2;
    private FileChooserWebChromeClient(){
        uploadMessage = new UploadMessage();
    }
    private FileChooserWebChromeClient(ActivityCallBack callBack,UploadMessage uploadMessage){
        this.uploadMessage = uploadMessage;
        setActivityCallBack(callBack);
    }
    UploadMessage uploadMessage;
    public void setActivityCallBack(ActivityCallBack callBack){
        this.callBack = callBack;
    }
    private ActivityCallBack callBack;
    public interface ActivityCallBack{
        void FileChooserBack(Intent intent);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        uploadMessage.setUploadMessageAboveL(filePathCallback);
        callBack.FileChooserBack(uploadMessage.openImageChooserActivity(fileChooserParams.getAcceptTypes()));
        return true;
    }
    public void openFileChooser(ValueCallback<Uri> valueCallback) {
        //uploadMessage = valueCallback;
        uploadMessage.setUploadMessage(valueCallback);
        callBack.FileChooserBack (uploadMessage.openImageChooserActivity("image/*"));
    }

    // For Android  >= 3.0
    public void openFileChooser(ValueCallback valueCallback, String acceptType) {
        uploadMessage.setUploadMessage(valueCallback);
        callBack.FileChooserBack(uploadMessage.openImageChooserActivity(acceptType));
    }

    //For Android  >= 4.1
    public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
        uploadMessage.setUploadMessage(valueCallback);
        callBack.FileChooserBack(uploadMessage.openImageChooserActivity(acceptType));
    }

    public UploadMessage getUploadMessage(){
        return uploadMessage;
    }
}
