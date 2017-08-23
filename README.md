# AndroidWebviewInputFile
解决在webview中input标签type="file"不能使用的问题

## 如何导入？

* Android Studio

		allprojects {
			repositories {
			  ...
			  maven { url 'https://jitpack.io' }
			}
		}
		  
		dependencies {
	       compile 'com.github.shuhaoSCode:AndroidWebviewInputFile:-SNAPSHOT'
		}


* eclipse。。。请自行copy class。

## 当前功能
* 让webview支持<input type="file" >

## 如何使用
	//已经写好的WebChromeClient
	FileChooserWebChromeClient webChromeClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //创建FileChooserWebChromeClient
        webChromeClient = new FileChooserWebChromeClient();
        //设置到自己的webview
        webview.setWebChromeClient(webChromeClient);
    }
    
    //下面两个直接copy到代码中就好
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case FileChooserWebChromeClient.INPUT_FILE_REQUEST_CODE:
                webChromeClient.cleanUpOnBackFromFileChooser(this, resultCode, intent);
            default:
                Log.d("", "requestCode:" + requestCode + ", resultCode:" + resultCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case FileChooserWebChromeClient.REQUEST_CAMERA_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    webChromeClient.openCameraGalleryChooser(this);
                    return;
                }
                webChromeClient.callOnReceiveValue(null);
            }
        }
    }
