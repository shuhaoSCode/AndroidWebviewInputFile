# AndroidWebviewInputFile
解决在webview中input标签type="file"不能使用的问题

## 如何导入？	[![](https://jitpack.io/v/shuhaoSCode/AndroidUIImageView.svg)](https://jitpack.io/#shuhaoSCode/AndroidUIImageView)


* Android Studio

		allprojects {
			repositories {
			  ...
			  maven { url 'https://jitpack.io' }
			}
		}
		  
		dependencies {
	       compile 'com.github.shuhaoSCode:AndroidWebviewInputFile:1.0.0'
		}


* eclipse。。。请自行copy class。

## 当前功能
* 让webview支持<input type="file" >

## 如何使用 (给个star吧亲🤕)
	//初始化
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
		//设置到自己的webview
		webview.setWebChromeClient(webChromeClient);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            fileChooserWebChromeClient.getUploadMessage().onActivityResult(requestCode,resultCode,data);
        }
    }
