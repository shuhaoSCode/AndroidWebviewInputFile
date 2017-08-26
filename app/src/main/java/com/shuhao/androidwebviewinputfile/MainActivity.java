package com.shuhao.androidwebviewinputfile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.shuhao.webchromeclient.FileChooserWebChromeClient;

public class MainActivity extends AppCompatActivity {

    FileChooserWebChromeClient fileChooserWebChromeClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileChooserWebChromeClient = new FileChooserWebChromeClient();
    }
}
