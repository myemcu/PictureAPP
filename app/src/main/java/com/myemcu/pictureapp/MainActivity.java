package com.myemcu.pictureapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import static android.Manifest.permission.*; // 手打，new String[]{READ_EXTERNAL_STORAGE}用

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private static final int REQUEST_READ_STORAGE = 3; // 定义私有静态整形常量
    SimpleCursorAdapter adapter;                       // 定义游标适配器,专在涉及数据库查询中用

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

        // 获取网格对象
        GridView grid = (GridView) findViewById(R.id.grid); //控件id可与控件对象重名
        // 定义显示来源
        String[] from = {MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Media.DISPLAY_NAME};
        // 定义显示目地
        int[] to = new int[]{R.id.thumb_image, R.id.thumb_text};
        // 创建适配器对象
        adapter = new SimpleCursorAdapter(getBaseContext(),R.layout.thumb_item,null,from,to,0);
        grid.setAdapter(adapter);
        // 使用CursorLoader节约存取开销(存取外部SD卡)
        getLoaderManager().initLoader(0, null, this); // this红浪后按Alt+Enter键生成下面的3个方法

        // 添加点击事件处理器
        grid.setOnItemClickListener(this); //此处,Alt+Enter实现生成的两个方法
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // 保存查询的资料位置
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        // 产生并返回资料读取器(CursorLoader)对象,并将uri传递给它
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // 当资料读取器(CursorLoader)在内容提供者中查询完成时,会自动呼叫onLoadFinished()方法,即呼叫
        // adapter的swapCursor()方法替换adapter内的Cursor对象。
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override// 实现画面转换
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,DetailActivity.class); //活动跳转
        intent.putExtra("POSITION", position); //将点击的位置传入Intent对象中
        startActivity(intent);
    }
}
