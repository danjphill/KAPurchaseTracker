package com.danacosoftware.purchasetracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

//import com.afollestad.materialdialogs.MaterialDialog;
//import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
//import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;

import java.io.PushbackReader;
import java.util.ArrayList;

/**
 * Created by Daniel Phillips on 8/3/2016.
 */
public class MainActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setTitle("Item Tracker");
        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        // llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setHasFixedSize(true);
        recList.setLayoutManager(llm);

        ArrayList<MainObject> items = new ArrayList<MainObject>();
        MainAdapter ca = new MainAdapter(items);
        recList.setAdapter(ca);
        MainObject NewItem = new MainObject();
        NewItem.Image = R.mipmap.new_item;
        NewItem.Text = "Create a New Item.";
        NewItem.Title = "New Item";
        items.add(NewItem);
        MainObject ViewList = new MainObject();
        ViewList.Image = R.mipmap.list_item;
        ViewList.Text = "View List of Saved Items.";
        ViewList.Title = "View Items";
        items.add(ViewList);
        MainObject ListStats = new MainObject();
        ListStats.Image = R.mipmap.stats;
        ListStats.Text = "View Pie Charts of Saved Items.";
        ListStats.Title = "Item Statistics";
        items.add(ListStats);
        MainObject BackupList = new MainObject();
        BackupList.Image = R.mipmap.backup;
        BackupList.Text = "Backup Saved Items to Google Drive.";
        BackupList.Title = "Backup Items";
        items.add(BackupList);
        MainObject ExportList = new MainObject();
        ExportList.Image = R.mipmap.export;
        ExportList.Text = "Export Spreadsheet of Items to an Email, Google Drive or SD Card.";
        ExportList.Title = "Export Items";
        items.add(ExportList);
        MainObject Settings = new MainObject();
        Settings.Image = R.mipmap.about;
        Settings.Text = "View Version Information, Report an Error.";
        Settings.Title = "About App";
        items.add(Settings);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
				/*
				 * The following method, "handleShakeEvent(count):" is a stub //
				 * method you would use to setup whatever you want done once the
				 * device has been shook.
				 */
                handleShakeEvent(count);
            }
        });

        ItemClickSupport.addTo(recList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if (position == 0) {
                    final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(MainActivity.this);
                    adapter.add(new MaterialSimpleListItem.Builder(MainActivity.this)
                            .content("Use Barcode")
                            .icon(R.mipmap.scanitem)
                            .backgroundColor(Color.WHITE)
                            .build());
                    adapter.add(new MaterialSimpleListItem.Builder(MainActivity.this)
                            .content("Enter Manually")
                            .icon(R.mipmap.blankitem)
                            .backgroundColor(Color.WHITE)
                            .build());

                    new MaterialDialog.Builder(MainActivity.this)
                            .adapter(adapter, new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                    MaterialSimpleListItem item = adapter.getItem(which);
                                    // TODO
                                    if (item.toString() == "Use Barcode") {
                                        Bundle ProperitiesCarrier = new Bundle();
                                        ProperitiesCarrier.putBoolean("ShowBarcode", true);
                                        ProperitiesCarrier.putInt("ItemID", -1);
                                        Intent startIntent = new Intent(MainActivity.this, NewEditItem.class);
                                        startIntent.putExtras(ProperitiesCarrier);
                                        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(startIntent);
                                        dialog.dismiss();
                                    } else {
                                        Bundle ProperitiesCarrier = new Bundle();
                                        ProperitiesCarrier.putBoolean("ShowBarcode", false);
                                        ProperitiesCarrier.putInt("ItemID", -1);
                                        Intent startIntent = new Intent(MainActivity.this, NewEditItem.class);
                                        startIntent.putExtras(ProperitiesCarrier);
                                        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(startIntent);
                                        dialog.dismiss();
                                    }
                                }
                            })

                            .show();
                } else if (position == 1) {
                    Intent startIntent = new Intent(MainActivity.this, ViewList.class);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startIntent);
                } else if (position == 2) {
                    Intent startIntent = new Intent(MainActivity.this, ListStats.class);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startIntent);
                } else if (position == 3) {
                    Intent startIntent = new Intent(MainActivity.this, BackupItems.class);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startIntent);
                } else if (position == 4) {
                    Intent startIntent = new Intent(MainActivity.this, ExportItems.class);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startIntent);
                }else if (position == 5) {
                    Intent startIntent = new Intent(MainActivity.this, About.class);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startIntent);
                }
            }
        });
        ItemClickSupport.addTo(recList).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                if (position == 5) {
                    ReportError re = new ReportError();
                    re.NewReport(MainActivity.this);
                }
                return false;
            }
        });
    }
    private void handleShakeEvent(int count) {
        ReportError re = new ReportError();
        re.NewReport(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}
