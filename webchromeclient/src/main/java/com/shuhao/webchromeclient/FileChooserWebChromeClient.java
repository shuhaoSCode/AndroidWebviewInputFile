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
        FileChooserWebChromeClientBuild(ActivityCallBack callBack){
            this.uploadMessage = new UploadMessage();
            this.callBack = callBack;
        }
        public FileChooserWebChromeClient build(){
            return new FileChooserWebChromeClient(this);
        }
    }
    public static FileChooserWebChromeClient createBuild(ActivityCallBack callBack){
        return new FileChooserWebChromeClientBuild(callBack).build();
    }
    FileChooserWebChromeClientBuild build;
    private FileChooserWebChromeClient(FileChooserWebChromeClientBuild build){
        this.build = build;
    }

    public interface ActivityCallBack{
        void FileChooserBack(Intent intent);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        build.uploadMessage.setUploadMessageAboveL(filePathCallback);
        build.callBack.FileChooserBack(build.uploadMessage.openImageChooserActivity(fileChooserParams.getAcceptTypes()));
        return true;
    }
    public void openFileChooser(ValueCallback<Uri> valueCallback) {
        //uploadMessage = valueCallback;
        build.uploadMessage.setUploadMessage(valueCallback);
        build.callBack.FileChooserBack (build.uploadMessage.openImageChooserActivity("image/*"));
    }

    // For Android  >= 3.0
    public void openFileChooser(ValueCallback valueCallback, String acceptType) {
        build.uploadMessage.setUploadMessage(valueCallback);
        build.callBack.FileChooserBack(build.uploadMessage.openImageChooserActivity(acceptType));
    }

    //For Android  >= 4.1
    public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
        build.uploadMessage.setUploadMessage(valueCallback);
        build.callBack.FileChooserBack(build.uploadMessage.openImageChooserActivity(acceptType));
    }

    public UploadMessage getUploadMessage(){
        return build.uploadMessage;
    }
}
