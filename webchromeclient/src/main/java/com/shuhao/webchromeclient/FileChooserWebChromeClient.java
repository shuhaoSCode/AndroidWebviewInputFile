package com.shuhao.webchromeclient;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.io.File;
import java.io.IOException;


public class FileChooserWebChromeClient extends WebChromeClient {

    public static final int REQUEST_CAMERA_PERMISSION = 1;
    public static final int INPUT_FILE_REQUEST_CODE = 2;


    private File photoFile;
    private ValueCallback<Uri[]> filePathCallback;
    private FileChooserParams fileChooserParams;

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        this.filePathCallback = filePathCallback;
        this.fileChooserParams = fileChooserParams;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA};
            if (!hasPermissions(webView.getContext(), PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) (webView.getContext()), PERMISSIONS, REQUEST_CAMERA_PERMISSION);
                return true;
            }
        }
        openCameraGalleryChooser((AppCompatActivity) (webView.getContext()));
        return true;
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (context == null || permissions == null) {
            throw new IllegalArgumentException("パーミッションチェックには、Contextとチェックしたいpermissionが必要です");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void openCameraGalleryChooser(AppCompatActivity activity) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // 参考
            // https://akira-watson.com/android/camera-intent.html
            // https://inthecheesefactory.com/blog/how-to-share-access-to-file-with-fileprovider-on-android-nougat
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.w("",ex.getStackTrace().toString());
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID +".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            }
        }
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("*/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            contentSelectionIntent.putExtra(Intent.EXTRA_MIME_TYPES, fileChooserParams.getAcceptTypes());
            contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, fileChooserParams.getMode() == FileChooserParams.MODE_OPEN_MULTIPLE);
        }

        Intent chooserIntent = Intent.createChooser(contentSelectionIntent, "pick image");
        Intent[] intentArray = new Intent[]{takePictureIntent};
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        activity.startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
    }

    public void cleanUpOnBackFromFileChooser(Context context, int resultCode, Intent intent) {
        if (resultCode != Activity.RESULT_OK || filePathCallback == null) {
            return;
        }

        if (intent != null) {
            Uri onlyOneSelectedImageUri = intent.getData();
            ClipData multipleSelectedImageUriData = intent.getClipData();
            if (multipleSelectedImageUriData != null) {
                final int selectedFilesCount = multipleSelectedImageUriData.getItemCount();
                Uri[] results = new Uri[selectedFilesCount];
                for (int i = 0; i < selectedFilesCount; i++) {
                    results[i] = multipleSelectedImageUriData.getItemAt(i).getUri();
                }
                filePathCallback.onReceiveValue(results);
            } else if (onlyOneSelectedImageUri != null) {
                filePathCallback.onReceiveValue(new Uri[]{onlyOneSelectedImageUri});
            } else {
                if (photoFile != null) {
                    Uri uri = registerContentResolver(context, photoFile.getAbsolutePath());
                    filePathCallback.onReceiveValue(new Uri[]{uri});
                    photoFile = null;
                } else {
                    filePathCallback.onReceiveValue(null);
                }
            }
        } else {
            filePathCallback.onReceiveValue(null);
        }
        filePathCallback = null;
    }

    public void callOnReceiveValue(Uri[] uris) {
        filePathCallback.onReceiveValue(uris);
    }

    private File createImageFile() throws IOException {
        long timeStamp = System.currentTimeMillis();
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        return File.createTempFile(imageFileName,".jpg", storageDir);
    }

    private Uri registerContentResolver(Context context, String filePath) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, filePath);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        return context.getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

    }

}
