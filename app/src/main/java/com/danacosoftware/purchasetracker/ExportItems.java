package com.danacosoftware.purchasetracker;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.jar.JarException;

/**
 * Created by Daniel Phillips on 8/25/2016.
 */
public class ExportItems extends AppCompatActivity {
    String EmailDate;
    String EmailTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.export_lists_layout);
        setTitle("Export");
        ListView ExportListView = (ListView) findViewById(R.id.ExportlistView);
        ArrayAdapter<String> adapter;
        ArrayList<String> ExportOptions = new ArrayList<String>();
        ExportOptions.add("Export and Open");
        ExportOptions.add("Export and Email");
        ExportOptions.add("Export Only");
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                ExportOptions);
        ExportListView.setAdapter(adapter);

        ExportListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        try {
                            Uri path = Uri.fromFile(ExportListData());
                            Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                            pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            pdfOpenintent.setDataAndType(path, "text/csv");
                            try {
                                startActivity(pdfOpenintent);
                                Toast.makeText(getApplicationContext(), "Export Successful" , Toast.LENGTH_LONG).show();
                            } catch (ActivityNotFoundException e) {

                            }
                        }catch (NullPointerException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Export Failed" , Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 1:
                        try {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/csv");
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Purchase Tracker Export");
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + ExportListData().toString()));
                            intent.putExtra(Intent.EXTRA_TEXT, "\n\nHi There, \n\nExport of Purchases:  \n-------------------------------------------------\n" + "Date: " + EmailDate + "\nTime: " + EmailTime + "\n-------------------------------------------------\n\nGenerated Using Danaco Software Purchase Tracker. ");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            startActivity(Intent.createChooser(intent, "Send Email"));
                            Toast.makeText(getApplicationContext(), "Export Successful" , Toast.LENGTH_LONG).show();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Export Failed" , Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 2:
                        try{
                            if (ExportListData().isFile()){
                                Toast.makeText(getApplicationContext(), "Export Successful" , Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Export Failed" , Toast.LENGTH_LONG).show();
                            }
                        }catch (NullPointerException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Export Failed" , Toast.LENGTH_LONG).show();
                        }

                        break;
                }
            }
        });

    }

    public java.io.File ExportListData() {
        MaterialDialog OpenProgress = new MaterialDialog.Builder(this)
                .title("Restoring Files")
                .content("Please Wait")
                .progress(true, 0)
                .show();
        DateTime dt = new DateTime(DateTimeZone.forTimeZone(TimeZone.getDefault()));
        EmailDate = dt.toString("EEE, MMM dd, yyyy");
        EmailTime = dt.toString("hh:mm aa");
        String DateandTime = dt.getDayOfMonth()+"_"+(dt.getMonthOfYear())+"_"+dt.getYear()+"-"+dt.getHourOfDay()+"_"+dt.getMinuteOfHour()+"_"+dt.getSecondOfMinute();
        Log.d("DateandTIme",DateandTime);
        File StorageDir = new File (Environment.getExternalStorageDirectory().getAbsolutePath()+"/PurchaseTracker/Exports");
        File ItemsListFile = new File (Environment.getExternalStorageDirectory().getAbsolutePath()+"/PurchaseTracker/Exports/PT_Export_"+DateandTime+".csv");
        DatabaseHandler db = new DatabaseHandler(ExportItems.this);
        List<ItemObject> AllItems = new ArrayList<ItemObject>(db.getAllItems());
        try {
            StorageDir.mkdirs();
            ItemsListFile.createNewFile();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(ItemsListFile.toString()));
            outputStreamWriter.write("Barcode".toUpperCase() + "," + "Type".toUpperCase() + "," + "Colour".toUpperCase() + "," + "Pattern".toUpperCase() + "," + "Gender".toUpperCase() + "," + "Brand".toUpperCase() + "," + "Size".toUpperCase() + "," + "Price".toUpperCase() + "," + "Store".toUpperCase() + "," + "Description".toUpperCase() + "," + "Pieces".toUpperCase() + "," + "Accessories".toUpperCase());
            outputStreamWriter.write(System.getProperty("line.separator"));
            String ItemType;
            String ItemColour;
            String ItemPattern;
            for (int i = 0; i < AllItems.size(); i++) {
                ItemObject CurrentItem = AllItems.get(i);
//                                if(CurrentItem.getType().contains(";")){
//                                    ItemType = CurrentItem.getType().replace(";",",");
//                                }else{
//                                    ItemType = CurrentItem.getType();
//                                }
//
//                                if(CurrentItem.getColour().contains(";")){
//                                    ItemColour = CurrentItem.getColour().replace(";",",");
//                                }else{
//                                    ItemColour = CurrentItem.getColour();
//                                }
//
//                                if(CurrentItem.getPattern().contains(";")){
//                                    ItemPattern = CurrentItem.getPattern().replace(";",",");
//                                }else{
//                                    ItemPattern = CurrentItem.getPattern();
//                                }

                outputStreamWriter.write(CurrentItem.getBarcode() + "," + CurrentItem.getType() + "," + CurrentItem.getColour() + "," + CurrentItem.getPattern() + "," + CurrentItem.getGender() + "," + CurrentItem.getBrand() + "," + CurrentItem.getSize() + ", $" + CurrentItem.getPrice() + "," + CurrentItem.getStore() + "," + CurrentItem.getDescription() + "," + CurrentItem.getPieces() + "," + CurrentItem.getAccessories());
                outputStreamWriter.write(System.getProperty("line.separator"));


            }

            outputStreamWriter.close();
            OpenProgress.dismiss();
            return ItemsListFile;
        }catch (IOException e){
            e.printStackTrace();
            OpenProgress.dismiss();
            return null;
        }
    }
}
