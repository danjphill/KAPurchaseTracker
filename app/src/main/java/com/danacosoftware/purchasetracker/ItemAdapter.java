package com.danacosoftware.purchasetracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends BaseAdapter implements Filterable {
    private List<ItemObject> mOriginalValues; // Original Values
    private List<ItemObject> mDisplayedValues;    // Values to be displayed
    LayoutInflater inflater;

    public ItemAdapter(Context context, List<ItemObject> mProductArrayList) {
        this.mOriginalValues = mProductArrayList;
        this.mDisplayedValues = mProductArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public ItemObject getItem(int position) {
        return mDisplayedValues.get(position);
    }
    public void removeItem(int position){
        Log.d("mOriginalValues",mDisplayedValues.get(position).getBarcode());
        try {
            mOriginalValues.remove(mDisplayedValues.get(position));
            mDisplayedValues.remove(position);
        }catch (java.lang.IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView Brand;
        TextView Store;
        TextView Type;
        TextView Barcode;
        TextView Gender;
        ImageView Image;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_adapter_layout, null);
            holder.Brand = (TextView) convertView.findViewById(R.id.ItemAdapter_Brand);
            holder.Store = (TextView) convertView.findViewById(R.id.ItemAdapter_Store);
            holder.Type = (TextView) convertView.findViewById(R.id.ItemAdapter_Type);
            holder.Barcode = (TextView) convertView.findViewById(R.id.ItemAdapter_Barcode);
            holder.Gender = (TextView) convertView.findViewById(R.id.ItemAdapter_Gender);
            holder.Image = (ImageView) convertView.findViewById(R.id.ItemAdapter_ImageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.Brand.setText(mDisplayedValues.get(position).getBrand());
        holder.Store.setText(mDisplayedValues.get(position).getStore());
        holder.Type.setText(mDisplayedValues.get(position).getType());
        holder.Barcode.setText(mDisplayedValues.get(position).getBarcode());
        holder.Gender.setText(mDisplayedValues.get(position).getGender());
        try {
            File imagelocation = new File(mDisplayedValues.get(position).getPicture());
            if (imagelocation.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(mDisplayedValues.get(position).getPicture(), options);
                Bitmap b1 = ThumbnailUtils.extractThumbnail(bitmap, 200, 150);
                holder.Image.setImageBitmap(b1);
                holder.Image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Log.d("Picture", mDisplayedValues.get(position).getPicture());
            }else {
                holder.Image.setImageResource(R.mipmap.image_error);
            }

        }catch (java.lang.NullPointerException e){
            e.printStackTrace();
            holder.Image.setImageResource(R.mipmap.image_error);
        }
        return convertView;
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<ItemObject>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<ItemObject> FilteredArrList = new ArrayList<ItemObject>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<ItemObject>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {
                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        if ((mOriginalValues.get(i).getDescription().toLowerCase().contains(constraint))
                                || (mOriginalValues.get(i).getStore().toLowerCase().contains(constraint))
                                || (mOriginalValues.get(i).getPrice().toLowerCase().contains(constraint.toString()))
                                || (mOriginalValues.get(i).getGender().toLowerCase().contains(constraint.toString()))
                                ||(mOriginalValues.get(i).getBarcode().toLowerCase().contains(constraint.toString()))
                                ||(mOriginalValues.get(i).getType().toLowerCase().contains(constraint.toString()))
                                ||(mOriginalValues.get(i).getBrand().toLowerCase().contains(constraint.toString()))
                                ||(mOriginalValues.get(i).getSize().toLowerCase().contains(constraint.toString()))
                                ||(mOriginalValues.get(i).getAccessories().toLowerCase().contains(constraint.toString()))) {
                            FilteredArrList.add(new ItemObject(mOriginalValues.get(i).getID(), mOriginalValues.get(i).getBarcode(), mOriginalValues.get(i).getType(), mOriginalValues.get(i).getColour(), mOriginalValues.get(i).getPattern(), mOriginalValues.get(i).getGender(), mOriginalValues.get(i).getBrand(), mOriginalValues.get(i).getSize(), mOriginalValues.get(i).getPrice(), mOriginalValues.get(i).getStore(),mOriginalValues.get(i).getDescription(), mOriginalValues.get(i).getPieces(), mOriginalValues.get(i).getAccessories(), mOriginalValues.get(i).getPicture()));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
