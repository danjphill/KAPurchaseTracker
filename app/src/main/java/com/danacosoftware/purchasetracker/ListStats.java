package com.danacosoftware.purchasetracker;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Phillips on 8/17/2016.
 */
public class ListStats extends AppCompatActivity {
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_stats_layout);
        setTitle("  Statistics");
        DatabaseHandler db = new DatabaseHandler(this);
        List<ItemObject> itemsList = new ArrayList<ItemObject>();
        itemsList =db.getAllItems();

        final String[] numbers = new String[] {
                "Type", "Color", "Pattern", "Gender","Brand","Size","Price","Store"};

        final GridView StatGrid = (GridView)findViewById(R.id.list_stat_gridView1);
        final PieChart StatChart = (PieChart)findViewById(R.id.list_stat_chart1);

        //Chart Settings
        StatChart.setUsePercentValues(true);
        StatChart.setDescription("");
        StatChart.setExtraOffsets(5, 10, 5, 5);

        StatChart.setDragDecelerationFrictionCoef(0.95f);
        StatChart.setDrawHoleEnabled(true);
        StatChart.setHoleColor(Color.WHITE);

        StatChart.setTransparentCircleColor(Color.WHITE);
        StatChart.setTransparentCircleAlpha(110);

        StatChart.setHoleRadius(38f);
        StatChart.setTransparentCircleRadius(41f);

        StatChart.setDrawCenterText(true);

        StatChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        StatChart.setRotationEnabled(true);
        StatChart.setHighlightPerTapEnabled(true);

        Legend l = StatChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        StatChart.setEntryLabelColor(Color.WHITE);
        StatChart.setEntryLabelTextSize(12f);
        StatChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);


        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.gridview_adapter_layout, numbers);

        StatGrid.setAdapter(adapter);

        final List<ItemObject> finalItemsList = itemsList;

        StatGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            View Previous = null;
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
                ArrayList<Integer> Counts = new ArrayList<Integer>();
                ArrayList<String> Items = new ArrayList<String>();
                String ChartTitle = "";
                try {
                    Previous.setBackgroundColor(Color.WHITE);
                    Previous.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }catch (java.lang.NullPointerException e){
                    e.printStackTrace();
                }
               v.setBackgroundColor(Color.rgb(230,230,230));
                Previous = v;
               if (adapter.getItem(position) == "Type"){
                   ChartTitle = "Item Types";
                   for ( int i=0 ; i < finalItemsList.size();i++) {
                       String TypeValue = finalItemsList.get(i).getType();
                       if(TypeValue.contains(";")){
                           String[] SplitTypeValue =  TypeValue.split(";");
                           for(int j = 0; j<SplitTypeValue.length; j++){
                               if (Items.contains(SplitTypeValue[j])){
                                   Counts.set(Items.indexOf(SplitTypeValue[j]),Counts.get(Items.indexOf(SplitTypeValue[j]))+1);
                                   Log.d("1","1");
                               }else{
                                   Items.add(SplitTypeValue[j]);
                                   Counts.add(1);
                                   Log.d("2","1");
                               }
                           }
                       }else{
                           if (Items.contains(TypeValue)){
                               Log.d("3","1");
                               Counts.set(Items.indexOf(TypeValue),Counts.get(Items.indexOf(TypeValue))+1);

                           }else{
                               Log.d("4","1");
                               Items.add(TypeValue);
                               Counts.add(1);
                           }
                       }

                   }

               }else if (adapter.getItem(position) == "Color"){
                   ChartTitle = "Item Colours";
                   for ( int i=0 ; i < finalItemsList.size();i++) {
                       String ColorValue = finalItemsList.get(i).getColour();
                       if(ColorValue.contains(";")){
                           String[] SplitColorValue =  ColorValue.split(";");
                           for(int j = 0; j<SplitColorValue.length; j++){
                               if (Items.contains(SplitColorValue[j])){
                                   Counts.set(Items.indexOf(SplitColorValue[j]),Counts.get(Items.indexOf(SplitColorValue[j]))+1);
                                   Log.d("1","1");
                               }else{
                                   Items.add(SplitColorValue[j]);
                                   Counts.add(1);
                                   Log.d("2","1");
                               }
                           }
                       }else{
                           if (Items.contains(ColorValue)){
                               Log.d("3","1");
                               Counts.set(Items.indexOf(ColorValue),Counts.get(Items.indexOf(ColorValue))+1);

                           }else{
                               Log.d("4","1");
                               Items.add(ColorValue);
                               Counts.add(1);
                           }
                       }

                   }

               } else if (adapter.getItem(position) == "Pattern"){
                    ChartTitle = "Item Patterns";
                    for ( int i=0 ; i < finalItemsList.size();i++) {
                        String PatternValue = finalItemsList.get(i).getPattern();
                        if(PatternValue.contains(";")){
                            String[] SplitPatternValue =  PatternValue.split(";");
                            for(int j = 0; j<SplitPatternValue.length; j++){
                                if (Items.contains(SplitPatternValue[j])){
                                    Counts.set(Items.indexOf(SplitPatternValue[j]),Counts.get(Items.indexOf(SplitPatternValue[j]))+1);
                                    Log.d("1","1");
                                }else{
                                    Items.add(SplitPatternValue[j]);
                                    Counts.add(1);
                                    Log.d("2","1");
                                }
                            }
                        }else{
                            if (Items.contains(PatternValue)){
                                Log.d("3","1");
                                Counts.set(Items.indexOf(PatternValue),Counts.get(Items.indexOf(PatternValue))+1);

                            }else{
                                Log.d("4","1");
                                Items.add(PatternValue);
                                Counts.add(1);
                            }
                        }

                    }

                }else if (adapter.getItem(position) == "Gender"){
                   ChartTitle = "Item Genders";
                   for ( int i=0 ; i < finalItemsList.size();i++) {
                       String GenderValue = finalItemsList.get(i).getGender();
                       if(GenderValue.contains(";")){
                           String[] SplitGenderValue =  GenderValue.split(";");
                           for(int j = 0; j<SplitGenderValue.length; j++){
                               if (Items.contains(SplitGenderValue[j])){
                                   Counts.set(Items.indexOf(SplitGenderValue[j]),Counts.get(Items.indexOf(SplitGenderValue[j]))+1);
                                   Log.d("1","1");
                               }else{
                                   Items.add(SplitGenderValue[j]);
                                   Counts.add(1);
                                   Log.d("2","1");
                               }
                           }
                       }else{
                           if (Items.contains(GenderValue)){
                               Log.d("3","1");
                               Counts.set(Items.indexOf(GenderValue),Counts.get(Items.indexOf(GenderValue))+1);

                           }else{
                               Log.d("4","1");
                               Items.add(GenderValue);
                               Counts.add(1);
                           }
                       }

                   }

               }else  if (adapter.getItem(position) == "Brand"){
                   ChartTitle = "Item Brands";
                   for ( int i=0 ; i < finalItemsList.size();i++) {
                       String BrandValue = finalItemsList.get(i).getBrand();
                       if(BrandValue.contains(";")){
                           String[] SplitBrandValue =  BrandValue.split(";");
                           for(int j = 0; j<SplitBrandValue.length; j++){
                               if (Items.contains(SplitBrandValue[j])){
                                   Counts.set(Items.indexOf(SplitBrandValue[j]),Counts.get(Items.indexOf(SplitBrandValue[j]))+1);
                                   Log.d("1","1");
                               }else{
                                   Items.add(SplitBrandValue[j]);
                                   Counts.add(1);
                                   Log.d("2","1");
                               }
                           }
                       }else{
                           if (Items.contains(BrandValue)){
                               Log.d("3","1");
                               Counts.set(Items.indexOf(BrandValue),Counts.get(Items.indexOf(BrandValue))+1);

                           }else{
                               Log.d("4","1");
                               Items.add(BrandValue);
                               Counts.add(1);
                           }
                       }

                   }

               }else  if (adapter.getItem(position) == "Size"){
                   ChartTitle = "Item Sizes";
                   for ( int i=0 ; i < finalItemsList.size();i++) {
                       String SizeValue = finalItemsList.get(i).getSize();
                       if(SizeValue.contains(";")){
                           String[] SplitSizeValue =  SizeValue.split(";");
                           for(int j = 0; j<SplitSizeValue.length; j++){
                               if (Items.contains(SplitSizeValue[j])){
                                   Counts.set(Items.indexOf(SplitSizeValue[j]),Counts.get(Items.indexOf(SplitSizeValue[j]))+1);
                                   Log.d("1","1");
                               }else{
                                   Items.add(SplitSizeValue[j]);
                                   Counts.add(1);
                                   Log.d("2","1");
                               }
                           }
                       }else{
                           if (Items.contains(SizeValue)){
                               Log.d("3","1");
                               Counts.set(Items.indexOf(SizeValue),Counts.get(Items.indexOf(SizeValue))+1);

                           }else{
                               Log.d("4","1");
                               Items.add(SizeValue);
                               Counts.add(1);
                           }
                       }

                   }

               }else  if (adapter.getItem(position) == "Price"){
                   ChartTitle = "Item Prices";
                   Items.add("< $2");
                   Counts.add(0);
                   Items.add("> $2 & < $5");
                   Counts.add(0);
                   Items.add("> $5 & < $10");
                   Counts.add(0);
                   Items.add("> $10 & <$15");
                   Counts.add(0);
                   Items.add("> $15 & <$20");
                   Counts.add(0);
                   Items.add("> $20");
                   Counts.add(0);
                   for ( int i=0 ; i < finalItemsList.size();i++) {
                       String PriceValue = finalItemsList.get(i).getPrice();

                       try{
                           Float PriceFloat = Float.valueOf(PriceValue);
                           if (PriceFloat <= 2){
                               Counts.set(0,Counts.get(0)+1);
                           }else if (PriceFloat > 2 && PriceFloat <= 5){
                               Counts.set(1,Counts.get(1)+1);
                           }else if (PriceFloat > 5 && PriceFloat <= 10){
                           Counts.set(2,Counts.get(2)+1);
                       }else if (PriceFloat > 10 && PriceFloat <= 15){
                               Counts.set(3,Counts.get(3)+1);
                           }else if (PriceFloat > 15 && PriceFloat <= 20){
                               Counts.set(4,Counts.get(4)+1);
                           }else{
                               Counts.set(5,Counts.get(5)+1);
                           }
                       }catch (java.lang.NumberFormatException e){
                           e.printStackTrace();
                       }



                   }

               }else  if (adapter.getItem(position) == "Store") {
                   ChartTitle = "Item Stores";
                   for (int i = 0; i < finalItemsList.size(); i++) {
                       String StoreValue = finalItemsList.get(i).getStore();
                       if (StoreValue.contains(";")) {
                           String[] SplitStoreValue = StoreValue.split(";");
                           for (int j = 0; j < SplitStoreValue.length; j++) {
                               if (Items.contains(SplitStoreValue[j])) {
                                   Counts.set(Items.indexOf(SplitStoreValue[j]), Counts.get(Items.indexOf(SplitStoreValue[j])) + 1);
                                   Log.d("1", "1");
                               } else {
                                   Items.add(SplitStoreValue[j]);
                                   Counts.add(1);
                                   Log.d("2", "1");
                               }
                           }
                       } else {
                           if (Items.contains(StoreValue)) {
                               Log.d("3", "1");
                               Counts.set(Items.indexOf(StoreValue), Counts.get(Items.indexOf(StoreValue)) + 1);

                           } else {
                               Log.d("4", "1");
                               Items.add(StoreValue);
                               Counts.add(1);
                           }
                       }

                   }
               }


                   for (int k=0; k< Items.size();k++) {
                    if(Counts.get(k) != 0) {
                        entries.add(new PieEntry((float) (Counts.get(k)), Items.get(k)));
                        Log.d("Eatery " + Items.get(k), String.valueOf(Counts.get(k)));
                    }
                }

                PieDataSet dataSet = new PieDataSet(entries, ChartTitle);
                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(5f);
                ArrayList<Integer> colors = new ArrayList<Integer>();
                for (int c : ColorTemplate.MATERIAL_COLORS)
                    colors.add(c);
                dataSet.setColors(colors);
                PieData data = new PieData(dataSet);
                data.setValueFormatter(new PercentFormatter());
                data.setValueTextSize(11f);
                data.setValueTextColor(Color.WHITE);

                StatChart.setData(data);

                // undo all highlights
                StatChart.highlightValues(null);

                StatChart.invalidate();
                StatChart.animateY(1400);

            }
        });



    }
}
