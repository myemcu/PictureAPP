package com.myemcu.pictureapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.Manifest.permission.*; // 手打，new String[]{READ_EXTERNAL_STORAGE}用

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_STORAGE = 3; // 定义私有静态整形常量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取当前外存读取权限
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) { //若未取得权限,则向用户提出申请
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
        }
        else {
            // 权限已有，可进行档案存取
            readThumbnails(); // 读取缩略图
        }
    }

    private void readThumbnails() { // 读取缩略图
    }

    @Override
    // 重写该方法(Ctrl+O后在V4.app.FragmentActivity的倒数第3行找)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_READ_STORAGE:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    readThumbnails(); // 取得外存读取权限,进行存取
                }else {
                    // 权限拒绝,弹出对话框告知
                    new AlertDialog.Builder(this)
                                   .setMessage("须允许外存读取权限才能显示图片")
                                   .setPositiveButton("确定", null)
                                   .show();
                }
                break;
        }
    }
}
