package com.shuhao.androidwebviewinputfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shuhao.webchromeclient.FileChooserWebChromeClient;

import static com.shuhao.webchromeclient.UploadMessage.FILE_CHOOSER_RESULT_CODE;

public class MainActivity extends AppCompatActivity {

    FileChooserWebChromeClient fileChooserWebChromeClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileChooserWebChromeClient = FileChooserWebChromeClient.createBuild(new FileChooserWebChromeClient.ActivityCallBack() {
            @Override
            public void FileChooserBack(Intent intent) {
                startActivityForResult(intent, FILE_CHOOSER_RESULT_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            fileChooserWebChromeClient.getUploadMessage().onActivityResult(requestCode,resultCode,data);
        }
    }
}
