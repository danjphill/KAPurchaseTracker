package com.danacosoftware.purchasetracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.commonsware.cwac.cam2.CameraActivity;
import com.commonsware.cwac.cam2.Facing;
import com.commonsware.cwac.cam2.ZoomStyle;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import id.zelory.compressor.Compressor;

/**
 * Created by Daniel Phillips on 8/5/2016.
 */
public class NewEditItem extends AppCompatActivity{
    EditText BarcodeValue;
    EditText TypeValue;
    EditText ColourValue;
    EditText PatternValue;
    Spinner GenderValue;
    EditText BrandValue;
    Spinner SizeValue;
    EditText PriceValue;
    EditText StoreValue;
    EditText DescriptionValue;
    EditText PiecesValue;
    EditText AccessoriesValue;
    Button PictureButton;
    ImageView PictureImageView;
    String PictureValue = "";

    Boolean TouchEnabled = true;
    File compressedImage;
    File Temp_File;
    int RecievedID;
    final DatabaseHandler db = new DatabaseHandler(this);

    private static final int CAMERA_PIC_REQUEST = 1111;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_edit_item_layout);
        setTitle("New");
        Bundle Info = getIntent().getExtras();
        Boolean ShowBarcode = Info.getBoolean("ShowBarcode");
        RecievedID = Info.getInt("ItemID");
        BarcodeValue = (EditText) findViewById(R.id.barcode_EditText);
        TypeValue = (EditText) findViewById(R.id.Type_EditText);
        ColourValue = (EditText) findViewById(R.id.Color_EditText);
        PatternValue = (EditText) findViewById(R.id.Pattern_EditText);
        GenderValue = (Spinner) findViewById(R.id.gender_Spinner);
        BrandValue = (EditText) findViewById(R.id.brand_EditText);
        SizeValue = (Spinner) findViewById(R.id.size_Spinner);
        PriceValue = (EditText) findViewById(R.id.price_EditText);
        StoreValue = (EditText) findViewById(R.id.store_EditText);
        DescriptionValue = (EditText) findViewById(R.id.description_EditText);
        PiecesValue = (EditText) findViewById(R.id.pieces_EditText);
        AccessoriesValue = (EditText) findViewById(R.id.accessories_EditText);
        PictureButton = (Button) findViewById(R.id.picture_Button);
        PictureImageView = (ImageView) findViewById(R.id.picture_ImageView);
        BarcodeValue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if((event.getAction()==MotionEvent.ACTION_UP) && (TouchEnabled)) {
                    IntentIntegrator integrator = new IntentIntegrator(NewEditItem.this);
                    integrator.initiateScan();

                }
                return false;
            }
        });

        String [] Genders = {"Boys","Girls","Unisex","Other"};
        String [] Sizes = {"Preemie",
                "Newborn",
                "0-3 Months",
                "3 Months",
                "3-6 Months",
                "6 Months",
                "6-9 Months",
                "9 Months",
                "9-12 Months",
                "12 Months",
                "12-18 Months",
                "18 Months",
                "18-24 Months",
                "24 Months",
                "2T",
                "3T","4T","5T","6T","6X","7T","7/8","8T","10/12","14/16","Other"};


        ArrayAdapter<String> GenderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Genders);
        GenderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        GenderValue.setAdapter(GenderAdapter);

        ArrayAdapter<String>  SizeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Sizes);
        SizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SizeValue.setAdapter(SizeAdapter);

        TypeValue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String [] TypesArray = {"T-Shirt",
                        "Shirt",
                        "Strap Top",
                        "Vest",
                        "Strap Dress",
                        "Pants",
                        "Socks",
                        "Shorts",
                        "Shoes",
                        "Dress",
                        "Cardigan",
                        "Jumper",
                        "Hats",
                        "Sweater",
                        "One Piece",
                        "Bodysuit",
                        "Swimwear",
                        "Skirt",
                        "Formal Dress",
                        "Other"};



                if(((event.getAction()==MotionEvent.ACTION_UP) && (TouchEnabled))) {
                    List<Integer> foundList = new ArrayList<Integer>();


                    for(int i=0;i<TypesArray.length;i++) {
                        if(i == 0 || i == 1){

                            if(TypeValue.getText().toString().contains("T-Shirt")){
                                String T_ShirtGone = TypeValue.getText().toString().replace("T-Shirt","");
                                if (T_ShirtGone.contains("Shirt")){
                                    if(!foundList.contains(0)) {
                                        foundList.add(0);
                                    }
                                    if(!foundList.contains(1)) {
                                        foundList.add(1);
                                    }
                                    Log.d("1", "1");
                                    //Remove the T-Shirt
                                }else{
                                    Log.d("1", "1.5");
                                    if(!foundList.contains(0)) {
                                        foundList.add(0);
                                    }
                                }
                            }else if ((TypeValue.getText().toString().contains("Shirt")) && !(TypeValue.getText().toString().contains("T-Shirt"))) {
                                if(!foundList.contains(1)) {
                                    foundList.add(1);
                                }
                                Log.d("1", "2");
                            }
                        }else if (TypeValue.getText().toString().contains(TypesArray[i])) {
                            foundList.add(i);
                            Log.d("1","4");
                        }
                    }

                    Integer[] foundIntList = new Integer[ foundList.size() ];
                    foundList.toArray( foundIntList );
                    for (int i=0;i<foundIntList.length;i++){
                        Log.d("foundList Item", String.valueOf(foundIntList[i]));
                    }
                    new MaterialDialog.Builder(NewEditItem.this)
                            .title("Item Type")
                            .items(TypesArray)
                            .itemsCallbackMultiChoice(foundIntList, new MaterialDialog.ListCallbackMultiChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                    /**
                                     * If you use alwaysCallMultiChoiceCallback(), which is discussed below,
                                     * returning false here won't allow the newly selected check box to actually be selected.
                                     * See the limited multi choice dialog example in the sample project for details.
                                     **/
                                    TypeValue.setText("");
                                    for(int i=0;i<text.length;i++){
                                        TypeValue.setText(TypeValue.getText()+";"+text[i].toString());
                                    }
                                    try {
                                        if (TypeValue.getText().charAt(0) == 59) {
                                            TypeValue.setText(TypeValue.getText().toString().substring(1));
                                        }
                                    }catch( java.lang.IndexOutOfBoundsException e){
                                        e.printStackTrace();

                                    }
                                    return true;
                                }
                            })
                            .positiveText("Select")
                            .negativeText("Cancel")
                            .show();
                }
                return false;
            }
        });

        ColourValue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String [] ColoursArray = {"Red",
                        "Pink",
                        "Green",
                        "Light Blue",
                        "Dark Blue",
                        "Purple",
                        "Black",
                        "White",
                        "Yellow",
                        "Grey",
                        "Gold",
                        "Silver",
                        "Orange",
                        "Teal",
                        "Turquoise",
                        "Tan",
                        "Brown",
                        "Other"};

                if((event.getAction()==MotionEvent.ACTION_UP) && (TouchEnabled)) {
                    List<Integer> foundList = new ArrayList<Integer>();

                    for(int i=0;i<ColoursArray.length;i++) {
                        if (ColourValue.getText().toString().contains(ColoursArray[i])) {
                            foundList.add(i);
                        }
                    }
                    Integer[] foundIntList = new Integer[ foundList.size() ];
                    foundList.toArray( foundIntList );
                    new MaterialDialog.Builder(NewEditItem.this)
                            .title("Item Colour")
                            .items(ColoursArray)
                            .itemsCallbackMultiChoice(foundIntList, new MaterialDialog.ListCallbackMultiChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                    /**
                                     * If you use alwaysCallMultiChoiceCallback(), which is discussed below,
                                     * returning false here won't allow the newly selected check box to actually be selected.
                                     * See the limited multi choice dialog example in the sample project for details.
                                     **/
                                    ColourValue.setText("");
                                    for(int i=0;i<text.length;i++){
                                        ColourValue.setText(ColourValue.getText()+";"+text[i].toString());
                                    }
                                    try{
                                        if (ColourValue.getText().charAt(0) == 59){
                                            ColourValue.setText(ColourValue.getText().toString().substring(1));
                                        }
                                    }catch( java.lang.IndexOutOfBoundsException e){
                                        e.printStackTrace();
                                    }

                                    return true;
                                }
                            })
                            .positiveText("Select")
                            .negativeText("Cancel")
                            .show();
                }
                return false;
            }
        });

        PatternValue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String [] PatternArray = {"Plain",
                        "Plaid",
                        "Checkered",
                        "Denim",
                        "Floral",
                        "Stripe",
                        "Flower Attached",
                        "Bow Attached",
                        "Sequins",
                        "Glitter",
                        "Picture/Graphics",
                        "Polka Dot",
                        "Other"};

                if((event.getAction()==MotionEvent.ACTION_UP) && (TouchEnabled)) {
                    List<Integer> foundList = new ArrayList<Integer>();

                    for(int i=0;i<PatternArray.length;i++) {
                        if (PatternValue.getText().toString().contains(PatternArray[i])) {
                            foundList.add(i);
                        }
                    }
                    Integer[] foundIntList = new Integer[ foundList.size() ];
                    foundList.toArray( foundIntList );
                    new MaterialDialog.Builder(NewEditItem.this)
                            .title("Item Colour")
                            .items(PatternArray)
                            .itemsCallbackMultiChoice(foundIntList, new MaterialDialog.ListCallbackMultiChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                    /**
                                     * If you use alwaysCallMultiChoiceCallback(), which is discussed below,
                                     * returning false here won't allow the newly selected check box to actually be selected.
                                     * See the limited multi choice dialog example in the sample project for details.
                                     **/
                                    PatternValue.setText("");
                                    for(int i=0;i<text.length;i++){
                                        PatternValue.setText(PatternValue.getText()+";"+text[i].toString());
                                    }
                                    try {
                                        if (PatternValue.getText().charAt(0) == 59) {
                                            PatternValue.setText(PatternValue.getText().toString().substring(1));
                                        }
                                    }catch( java.lang.IndexOutOfBoundsException e){
                                        e.printStackTrace();
                                    }
                                    return true;
                                }
                            })
                            .positiveText("Select")
                            .negativeText("Cancel")
                            .show();
                }
                return false;
            }
        });

        BrandValue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String [] BrandsArray = {"Carters",
                        "Gymboree",
                        "Crazy8",
                        "Walmart",
                        "TK Maxx",
                        "The Childrens Place",
                        "Other"};

                if((event.getAction()==MotionEvent.ACTION_UP) && (TouchEnabled)) {
                    int foundItem = -1;

                    for(int i=0;i<BrandsArray.length;i++) {
                        if (BrandValue.getText().toString().contains(BrandsArray[i])) {
                            foundItem = i;
                        }
                    }

                    new MaterialDialog.Builder(NewEditItem.this)
                            .title("Select Brand")
                            .items(BrandsArray)
                            .itemsCallbackSingleChoice(foundItem, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    /**
                                     * If you use alwaysCallMultiChoiceCallback(), which is discussed below,
                                     * returning false here won't allow the newly selected check box to actually be selected.
                                     * See the limited multi choice dialog example in the sample project for details.
                                     **/
                                    BrandValue.setText(text);

                                    return true;
                                }
                            })
                            .positiveText("Select")
                            .negativeText("Cancel")
                            .show();
                }
                return false;
            }
        });
        StoreValue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final String [] StoreArray = {"Burlington","Carters","Kmart",
                        "Sears",
                        "Macys",
                        "Target",
                        "Walmart",
                        "Kohls",
                        "Craxy 8",
                        "Gymboree",
                        "Justice",
                        "JC Penny",
                        "Marshalls",
                        "Ross",
                        "Boscov's",
                        "Burlington",
                        "The Children's Place",
                        "Other"};

                if((event.getAction()==MotionEvent.ACTION_UP) && (TouchEnabled)) {


                    int foundItem = -1;

                    for(int i=0;i<StoreArray.length;i++) {
                        if (StoreValue.getText().toString().contains(StoreArray[i])) {
                            foundItem = i;
                        }
                    }
                    new MaterialDialog.Builder(NewEditItem.this)
                            .title("Select Store")
                            .items(StoreArray)
                            .itemsCallbackSingleChoice(foundItem, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    /**
                                     * If you use alwaysCallMultiChoiceCallback(), which is discussed below,
                                     * returning false here won't allow the newly selected check box to actually be selected.
                                     * See the limited multi choice dialog example in the sample project for details.
                                     **/
                                    StoreValue.setText(text);
                                    return true;
                                }
                            })
                            .positiveText("Select")
                            .negativeText("Cancel")
                            .show();
                }
                return false;
            }
        });
        PictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(intent, CAMERA_PIC_REQUEST);
                File Dir = new File (getExternalCacheDir()+"/Temp");

                if(!Dir.exists()){
                    Dir.mkdirs();
                }

                String uniqueID = UUID.randomUUID().toString();
                Temp_File = new File(Dir, String.valueOf(uniqueID.hashCode())+".jpeg");

                Intent i=new CameraActivity.IntentBuilder(NewEditItem.this)
                        .skipConfirm()
                        .facing(Facing.BACK)
                        .to(Temp_File)
                        .debug()
                        .zoomStyle(ZoomStyle.SEEKBAR)
                        .build();

                startActivityForResult(i, CAMERA_PIC_REQUEST);
            }
        });
        if (ShowBarcode) {
            IntentIntegrator integrator = new IntentIntegrator(NewEditItem.this);
            integrator.initiateScan();
        }

        BarcodeValue = (EditText) findViewById(R.id.barcode_EditText);
        TypeValue = (EditText) findViewById(R.id.Type_EditText);
        ColourValue = (EditText) findViewById(R.id.Color_EditText);
        PatternValue = (EditText) findViewById(R.id.Pattern_EditText);
        GenderValue = (Spinner) findViewById(R.id.gender_Spinner);
        BrandValue = (EditText) findViewById(R.id.brand_EditText);
        SizeValue = (Spinner) findViewById(R.id.size_Spinner);
        PriceValue = (EditText) findViewById(R.id.price_EditText);
        StoreValue = (EditText) findViewById(R.id.store_EditText);
        DescriptionValue = (EditText) findViewById(R.id.description_EditText);
        PiecesValue = (EditText) findViewById(R.id.pieces_EditText);
        AccessoriesValue = (EditText) findViewById(R.id.accessories_EditText);
        PictureButton = (Button) findViewById(R.id.picture_Button);
        PictureImageView = (ImageView) findViewById(R.id.picture_ImageView);

        if(RecievedID != -1){
            setTitle("Edit");
            BarcodeValue.setText(db.getItem(RecievedID).getBarcode());
            TypeValue.setText(db.getItem(RecievedID).getType());
            ColourValue.setText(db.getItem(RecievedID).getColour());
            PatternValue.setText(db.getItem(RecievedID).getPattern());
            GenderValue.setSelection(GenderAdapter.getPosition(db.getItem(RecievedID).getGender()));
            BrandValue.setText(db.getItem(RecievedID).getBrand());
            SizeValue.setSelection(SizeAdapter.getPosition(db.getItem(RecievedID).getSize()));
            PriceValue.setText(db.getItem(RecievedID).getPrice());
            StoreValue.setText(db.getItem(RecievedID).getStore());
            DescriptionValue.setText(db.getItem(RecievedID).getDescription());
            PiecesValue.setText(db.getItem(RecievedID).getPieces());
            AccessoriesValue.setText(db.getItem(RecievedID).getAccessories());
            PictureValue = (db.getItem(RecievedID).getPicture());
            try {
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(PictureValue, options);
                    Bitmap b1 = ThumbnailUtils.extractThumbnail(bitmap, 200, 150);
                    PictureImageView.setImageBitmap(b1);
                    PictureImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();
                }
            }catch (java.lang.IllegalArgumentException e){
                e.printStackTrace();
            }

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST) {
            //2

            File Dir = new File (getExternalFilesDir(null)+"/DS_KAT/Images");

            if(!Dir.exists()){
                Dir.mkdirs();
            }
            try {

                try {
                    compressedImage = new Compressor.Builder(this)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(getExternalFilesDir(null) + "/DS_KAT/Images")
                            .build()
                            .compressToFile(Temp_File);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(compressedImage.toString(), options);
                    Bitmap b1 = ThumbnailUtils.extractThumbnail(bitmap, 200, 150);
                    PictureImageView.setImageBitmap(b1);
                    PictureImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    PictureValue = compressedImage.toString();
                    Log.d("PictureValue", PictureValue);

                } catch (java.lang.NullPointerException e) {
                    e.printStackTrace();
                }

            }catch (java.lang.IllegalArgumentException e){
                e.printStackTrace();
            }



        }
        if (scanResult != null) {
            String result = scanResult.getContents();
            BarcodeValue.setText(result);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_edit_item_menu, menu);
        return true;

    }
    public void saveItemInfo(){
        final DatabaseHandler db = new DatabaseHandler(NewEditItem.this);
        //Log.d("Insert: ", "Inserting ..");
        final String Barcode = BarcodeValue.getText().toString();
        final String Type = TypeValue.getText().toString();
        final String Colour = ColourValue.getText().toString();
        final String Pattern = PatternValue.getText().toString();
        final String Gender = GenderValue.getSelectedItem().toString();
        final String Brand = BrandValue.getText().toString();
        final String Size = SizeValue.getSelectedItem().toString();
        final String Price = PriceValue.getText().toString();
        final String Store = StoreValue.getText().toString();
        final String Description = DescriptionValue.getText().toString();
        final String Pieces = PiecesValue.getText().toString();
        final String Accessories = AccessoriesValue.getText().toString();
        final String Picture;
        if (PictureValue == "") {
            Picture = "none";
        }else{
            Picture = PictureValue;
        }
        try {
            Float.valueOf(Price);
            if (Barcode.isEmpty() || Type.isEmpty() || Colour.isEmpty() || Pattern.isEmpty() || Gender.isEmpty() || Brand.isEmpty() || Size.isEmpty() || Price.isEmpty() || Store.isEmpty() || Pieces.isEmpty()) {
                new AlertDialogWrapper.Builder(this)
                        .setTitle("Warning")
                        .setMessage("Some Items Are Empty")
                        .setPositiveButton("Save Anyway", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (RecievedID == -1) {
                                    db.addItem(new ItemObject(Barcode,
                                            Type,
                                            Colour,
                                            Pattern,
                                            Gender,
                                            Brand,
                                            Size,
                                            Price,
                                            Store,
                                            Description,
                                            Pieces,
                                            Accessories,
                                            Picture));
                                    finish();
                                } else {
                                    db.updateItem(new ItemObject(RecievedID, Barcode,
                                            Type,
                                            Colour,
                                            Pattern,
                                            Gender,
                                            Brand,
                                            Size,
                                            Price,
                                            Store,
                                            Description,
                                            Pieces,
                                            Accessories,
                                            Picture));
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            } else {
                if (RecievedID == -1) {
                    db.addItem(new ItemObject(Barcode,
                            Type,
                            Colour,
                            Pattern,
                            Gender,
                            Brand,
                            Size,
                            Price,
                            Store,
                            Description,
                            Pieces,
                            Accessories,
                            Picture));
                    finish();
                } else {
                    db.updateItem(new ItemObject(RecievedID, Barcode,
                            Type,
                            Colour,
                            Pattern,
                            Gender,
                            Brand,
                            Size,
                            Price,
                            Store,
                            Description,
                            Pieces,
                            Accessories,
                            Picture));
                    finish();
                }
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
            new AlertDialogWrapper.Builder(this)
                    .setTitle("Error")
                    .setMessage("Price Format Invalid")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:

                if (PictureValue != "") {
                    saveItemInfo();
                    try {
                        Temp_File.delete();
                    }catch(java.lang.NullPointerException e){
                        e.printStackTrace();
                    }

                }else {
                    new AlertDialogWrapper.Builder(this)
                            .setTitle("Error")
                            .setMessage("Item Image Could Not Be Saved")
                            .setPositiveButton("Save Anyway", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    saveItemInfo();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();


                }
                break;
            case R.id.action_disable_menu:
                if(TouchEnabled){
                    TouchEnabled = false;
                    item.setIcon(R.drawable.ic_visibility_white_24dp);
                    Toast.makeText(this,"Touch Menu Disabled",Toast.LENGTH_LONG).show();
                }else{
                    TouchEnabled = true;
                    item.setIcon(R.drawable.ic_visibility_off_white_24dp);
                    Toast.makeText(this,"Touch Menu Enabled",Toast.LENGTH_LONG).show();
                }
                break;


        }
        return true;
    }

    @Override
    public void onBackPressed() {
        new AlertDialogWrapper.Builder(this)
                .setTitle("Warning")
                .setMessage("The Current Item Was NOT Saved, Continue?")
                .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Temp_File.delete();
                            compressedImage.delete();

                        }catch (java.lang.NullPointerException e){
                            e.printStackTrace();
                        }
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }
}
