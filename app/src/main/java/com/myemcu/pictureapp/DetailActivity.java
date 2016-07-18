package com.myemcu.pictureapp;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

public class DetailActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{

    private int position;
    private ImageView image;
    private Cursor cursor; // 数据库查询结果

    GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detector = new GestureDetector(this, this);

        position = getIntent().getIntExtra("POSITION", 0);
        image = (ImageView) findViewById(R.id.imageView);

        CursorLoader loader = new CursorLoader(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        cursor = loader.loadInBackground();
        cursor.moveToPosition(position);

        updateImage();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    private void updateImage() {
        String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        image.setImageBitmap(bitmap);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    // 快速滑屏后释放
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        float distance = e2.getX()-e1.getX();

        if (distance > 100) { //右滑屏(向前翻)
            if (!cursor.moveToPrevious()) {
                cursor.moveToLast();
            }
            updateImage();
        }else if (distance < -100) { //左滑屏(向后翻)
            if (!cursor.moveToNext()) {
                cursor.moveToFirst();
            }
            updateImage();
        }

        return false;
    }
}
