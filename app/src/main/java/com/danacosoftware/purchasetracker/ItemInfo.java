package com.danacosoftware.purchasetracker;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Daniel Phillips on 8/8/2016.
 */
public class ItemInfo extends AppCompatActivity {
    final DatabaseHandler db = new DatabaseHandler(this);
    int ItemID;
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_info);

        Bundle Info = getIntent().getExtras();
        ItemID = Info.getInt("ItemID");
        Log.d("ItemID", String.valueOf(ItemID));

        TextView ID = (TextView) findViewById(R.id.ID_ListInfo);
        TextView Barcode = (TextView) findViewById(R.id.Barcode_ListInfo);
        TextView Type = (TextView) findViewById(R.id.Type_ListInfo);
        TextView Colour = (TextView) findViewById(R.id.Colour_ListInfo);
        TextView Pattern = (TextView) findViewById(R.id.Pattern_ListInfo);
        TextView Gender = (TextView) findViewById(R.id.Gender_ListInfo);
        TextView Brand = (TextView) findViewById(R.id.Brand_ListInfo);
        TextView Size = (TextView) findViewById(R.id.Size_ListInfo);
        TextView Price = (TextView) findViewById(R.id.Price_ListInfo);
        TextView Store = (TextView) findViewById(R.id.Store_ListInfo);
        TextView Description = (TextView) findViewById(R.id.Description_ListInfo);
        TextView Pieces = (TextView) findViewById(R.id.Pieces_ListInfo);
        TextView Accessories = (TextView) findViewById(R.id.Accessories_ListInfo);
        ImageView Picture = (ImageView) findViewById(R.id.Picture_ListInfo);

        ID.setText("ID: " + String.valueOf(db.getItem(ItemID).getID()));
        Barcode.setText("Barcode: " + db.getItem(ItemID).getBarcode());
        Type.setText("Type: " + db.getItem(ItemID).getType());
        Colour.setText("Colour: " + db.getItem(ItemID).getColour());
        Pattern.setText("Pattern: " + db.getItem(ItemID).getPattern());
        Gender.setText("Gender: " + db.getItem(ItemID).getGender());
        Brand.setText("Brand: " + db.getItem(ItemID).getBrand());
        Size.setText("Size: " + db.getItem(ItemID).getSize());
        Price.setText("Price: " + db.getItem(ItemID).getPrice());
        Store.setText("Store: " + db.getItem(ItemID).getStore());
        Description.setText("Description: " + db.getItem(ItemID).getDescription());
        Pieces.setText("Pieces: " + db.getItem(ItemID).getPieces());
        Accessories.setText("Accessories: " + db.getItem(ItemID).getAccessories());

        String ImageLocation = db.getItem(ItemID).getPicture();

        final File imagelocation = new File(ImageLocation);
        if (imagelocation.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(ImageLocation, options);
            Bitmap b1 = ThumbnailUtils.extractThumbnail(bitmap, 110, 110);
            Picture.setImageBitmap(b1);
        } else {
            Picture.setImageResource(R.mipmap.image_error);
        }
        Picture.setScaleType(ImageView.ScaleType.FIT_CENTER);


        Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagelocation.exists()) {
                    Bundle ProperitiesCarrier = new Bundle();
                    ProperitiesCarrier.putString("ImagePath", db.getItem(ItemID).getPicture());
                    //Log.d("ItemID",String.valueOf(adapter.getItem(position).getID()));
                    Intent startIntent = new Intent(ItemInfo.this, ImageViewer.class);
                    startIntent.putExtras(ProperitiesCarrier);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startIntent);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_edit:
                Bundle ProperitiesCarrier = new Bundle();
                ProperitiesCarrier.putBoolean("ShowBarcode", false);
                ProperitiesCarrier.putInt("ItemID",db.getItem(ItemID).getID());
                Intent startIntent = new Intent(ItemInfo.this, NewEditItem.class);
                startIntent.putExtras(ProperitiesCarrier);
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startIntent);
                break;
            case R.id.action_delete:
                Drawable deleteIcon;
                Bitmap deleteBitmap;
                try {
                    File imagelocation = new File(db.getItem(ItemID).getPicture());
                    if (imagelocation.exists()) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        deleteBitmap = BitmapFactory.decodeFile(db.getItem(ItemID).getPicture(), options);


                    }else {
                        deleteBitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.image_error);

                    }

                }catch (java.lang.NullPointerException e){
                    e.printStackTrace();
                    deleteBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.image_error);
                }
                deleteBitmap = ThumbnailUtils.extractThumbnail(deleteBitmap, 350, 300);
                deleteIcon = new BitmapDrawable(deleteBitmap);
                new AlertDialogWrapper.Builder(this)
                        .setTitle("Delete")
                        .setMessage("Are You Sure You Want to Delete This Item?")
                        .setIcon(deleteIcon)
                        .setPositiveButton("Delete Item", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteItem(db.getItem(ItemID));
                                finish();
                                //ItemsListView.invalidateViews();


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();


        }
        return true;
    }

    @Override
    protected void onRestart() {

        Intent intent = getIntent();
        finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        super.onRestart();
    }
}