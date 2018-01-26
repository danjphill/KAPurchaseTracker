package com.danacosoftware.purchasetracker;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;


import java.util.List;



/**
 * Created by Daniel Phillips on 8/4/2016.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainAdapterViewHolder>  {



        private List<MainObject> contactList;


        public MainAdapter(List<MainObject> contactList) {

            this.contactList = contactList;
        }


        @Override

        public int getItemCount() {

            return contactList.size();

        }


        @Override
        public void onBindViewHolder(MainAdapterViewHolder contactViewHolder, int i) {

            MainObject ci = contactList.get(i);
            contactViewHolder.Title.setText(ci.Title);

            contactViewHolder.Title.setTextColor(Color.BLACK);
            //contactViewHolder.Title.setTextColor(Color.rgb(255,175,78));
            contactViewHolder.Text.setText(ci.Text);
            contactViewHolder.Text.setTextColor(Color.rgb(127,127,127));
            contactViewHolder.Image.setImageResource(ci.Image);
            contactViewHolder.Image.setScaleType(ImageView.ScaleType.FIT_XY);



        }


        @Override

        public MainAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.main_adapter_layout, viewGroup, false);
            return new MainAdapterViewHolder(itemView);

        }

        public static class MainAdapterViewHolder extends RecyclerView.ViewHolder {

            protected TextView Title;
            protected TextView Text;
            protected ImageView Image;



            public MainAdapterViewHolder(View v) {

                super(v);
                Title = (TextView) v.findViewById(R.id.MainAdapter_Title);
                Text = (TextView) v.findViewById(R.id.MainAdapter_Description);
                Image = (ImageView) v.findViewById(R.id.MainAdapter_Image);

            }


        }

    }


