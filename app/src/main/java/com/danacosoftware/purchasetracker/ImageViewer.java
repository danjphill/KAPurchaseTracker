package com.danacosoftware.purchasetracker;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Daniel Phillips on 8/8/2016.
 */
public class ImageViewer extends AppCompatActivity {
    String ItemImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_viewer_layout);
        setTitle("Image Viewer");
        ImageView Image = (ImageView) findViewById(R.id.ImageViewer_ImageView);
        Bundle Info = getIntent().getExtras();
        ItemImage = Info.getString("ImagePath");
        Drawable image = Drawable.createFromPath(ItemImage);
        Image.setImageDrawable(image);
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(Image);
        mAttacher.update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.image_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_share:

                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/jpeg");
                    share.putExtra(Intent.EXTRA_STREAM, Uri.parse(ItemImage));
                    startActivity(Intent.createChooser(share, "Share Image"));



            }
        return super.onOptionsItemSelected(item);
    }
}