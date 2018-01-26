package com.danacosoftware.purchasetracker;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Daniel Phillips on 8/7/2016.
 */

public class ViewList extends AppCompatActivity {
    ListView ItemsListView;
    List<ItemObject> Items;
    ItemAdapter adapter;
    DatabaseHandler db = new DatabaseHandler(this);
    String SortType = "Order Added";
    int SortTypeInt = 0;
    int SortOrder = 1;
    int SortOrderInt = 0;
    SearchView searchView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Saved Items");
        setContentView(R.layout.view_list_layout);
        Items = new ArrayList<ItemObject>();
        Items = db.getAllItems();
        ItemsListView = (ListView)findViewById(R.id.Items_ListView);
        adapter = new ItemAdapter(this,Items);
        ItemsListView.setAdapter(adapter);
        registerForContextMenu(ItemsListView);

        ItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle ProperitiesCarrier = new Bundle();
                ProperitiesCarrier.putInt("ItemID", adapter.getItem(position).getID());
                Log.d("ItemID",String.valueOf(adapter.getItem(position).getID()));
                Intent startIntent = new Intent(ViewList.this, ItemInfo.class);
                startIntent.putExtras(ProperitiesCarrier);
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startIntent);
            }
        });
    }

    @Override
    protected void onRestart() {
        Intent intent = getIntent();
        finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        super.onRestart();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuinflater = getMenuInflater();
        menuinflater.inflate(R.menu.view_list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // TODO Auto-generated method stub
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        File imagelocation = new File(adapter.getItem(info.position).getPicture());

        switch (item.getItemId()) {

            case R.id.view_list_context_delete:
                Drawable deleteIcon;
                Bitmap deleteBitmap;
                try {

                    if (imagelocation.exists()) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        deleteBitmap = BitmapFactory.decodeFile(adapter.getItem(info.position).getPicture(), options);


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
                                db.deleteItem(adapter.getItem(info.position));
                                Items.remove(adapter.getItem(info.position));
                                //adapter.removeItem(info.position);
                                adapter.notifyDataSetChanged();
                                Intent intent = getIntent();
                                finish();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                //ItemsListView.invalidateViews();


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                return true;
            case R.id.view_list_context_edit:
                Bundle ProperitiesCarrier = new Bundle();
                ProperitiesCarrier.putBoolean("ShowBarcode", false);
                ProperitiesCarrier.putInt("ItemID",adapter.getItem(info.position).getID());
                Intent startIntent = new Intent(ViewList.this, NewEditItem.class);
                startIntent.putExtras(ProperitiesCarrier);
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startIntent);
                return true;
            case R.id.view_list_context_new:
                final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(ViewList.this);
                adapter.add(new MaterialSimpleListItem.Builder(ViewList.this)
                        .content("Use Barcode")
                        .icon(R.mipmap.scanitem)
                        .backgroundColor(Color.WHITE)
                        .build());
                adapter.add(new MaterialSimpleListItem.Builder(ViewList.this)
                        .content("Enter Manually")
                        .icon(R.mipmap.blankitem)
                        .backgroundColor(Color.WHITE)
                        .build());

                new MaterialDialog.Builder(ViewList.this)
                        .adapter(adapter, new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                MaterialSimpleListItem item = adapter.getItem(which);
                                // TODO
                                if (item.toString() == "Use Barcode") {
                                    Bundle ProperitiesCarrier = new Bundle();
                                    ProperitiesCarrier.putBoolean("ShowBarcode", true);
                                    ProperitiesCarrier.putInt("ItemID",-1);
                                    Intent startIntent = new Intent(ViewList.this, NewEditItem.class);
                                    startIntent.putExtras(ProperitiesCarrier);
                                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(startIntent);
                                    dialog.dismiss();
                                } else {
                                    Bundle ProperitiesCarrier = new Bundle();
                                    ProperitiesCarrier.putBoolean("ShowBarcode", false);
                                    ProperitiesCarrier.putInt("ItemID",-1);
                                    Intent startIntent = new Intent(ViewList.this, NewEditItem.class);
                                    startIntent.putExtras(ProperitiesCarrier);
                                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(startIntent);
                                    dialog.dismiss();
                                }
                            }
                        })

                        .show();
                return true;
            case R.id.view_list_context_view_image:
                if (imagelocation.exists()) {
                    Bundle ProperitiesCarrier2 = new Bundle();
                    ProperitiesCarrier2.putString("ImagePath", this.adapter.getItem(info.position).Picture);
                    //Log.d("ItemID",String.valueOf(adapter.getItem(position).getID()));
                    Intent viewImageIntent = new Intent(ViewList.this, ImageViewer.class);
                    viewImageIntent.putExtras(ProperitiesCarrier2);
                    viewImageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(viewImageIntent);
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_list_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.view_list_menu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.view_list_menu_new:
                final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(ViewList.this);
                adapter.add(new MaterialSimpleListItem.Builder(ViewList.this)
                        .content("Use Barcode")
                        .icon(R.mipmap.scanitem)
                        .backgroundColor(Color.WHITE)
                        .build());
                adapter.add(new MaterialSimpleListItem.Builder(ViewList.this)
                        .content("Enter Manually")
                        .icon(R.mipmap.blankitem)
                        .backgroundColor(Color.WHITE)
                        .build());

                new MaterialDialog.Builder(ViewList.this)
                        .adapter(adapter, new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                MaterialSimpleListItem item = adapter.getItem(which);
                                // TODO
                                if (item.toString() == "Use Barcode") {
                                    Bundle ProperitiesCarrier = new Bundle();
                                    ProperitiesCarrier.putBoolean("ShowBarcode", true);
                                    ProperitiesCarrier.putInt("ItemID",-1);
                                    Intent startIntent = new Intent(ViewList.this, NewEditItem.class);
                                    startIntent.putExtras(ProperitiesCarrier);
                                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(startIntent);
                                    dialog.dismiss();
                                } else {
                                    Bundle ProperitiesCarrier = new Bundle();
                                    ProperitiesCarrier.putBoolean("ShowBarcode", false);
                                    ProperitiesCarrier.putInt("ItemID",-1);
                                    Intent startIntent = new Intent(ViewList.this, NewEditItem.class);
                                    startIntent.putExtras(ProperitiesCarrier);
                                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(startIntent);
                                    dialog.dismiss();
                                }
                            }
                        })

                        .show();
                return true;
            case R.id.view_list_menu_sort:
                String [] SortArray = {"Order Added",
                        "Brand",
                        "Size",
                        "Store",
                        "Barcode",
                        "Price",
                        "Pieces",
                        "Gender"};

                new MaterialDialog.Builder(ViewList.this)
                            .title("Sort Items")
                            .items(SortArray)
                            .itemsCallbackSingleChoice(SortTypeInt, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    /**
                                     * If you use alwaysCallMultiChoiceCallback(), which is discussed below,
                                     * returning false here won't allow the newly selected check box to actually be selected.
                                     * See the limited multi choice dialog example in the sample project for details.
                                     **/
                                    //BrandValue.setText(text);
                                   SortType = text.toString();
                                    SortTypeInt = which;
                                    Collections.sort(Items, new Sorter(1, SortType));
                                    ViewList.this.adapter.notifyDataSetChanged();
                                    ItemsListView.invalidateViews();
                                    ItemsListView.setAdapter(ViewList.this.adapter);
                                    return true;
                                }
                            })
                            .positiveText("Sort")
                            .negativeText("Cancel")
                            .show();

                return true;
            case R.id.view_list_menu_search:

                return true;
            case R.id.view_list_menu_sort_reverse:
                String [] ReverseSortArray = {"Ascending", "Descending"};

                new MaterialDialog.Builder(ViewList.this)
                        .title("Sort Items")
                        .items(ReverseSortArray)
                        .itemsCallbackSingleChoice(SortOrderInt, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                /**
                                 * If you use alwaysCallMultiChoiceCallback(), which is discussed below,
                                 * returning false here won't allow the newly selected check box to actually be selected.
                                 * See the limited multi choice dialog example in the sample project for details.
                                 **/
                                //BrandValue.setText(text);
                                SortOrderInt = which;
                                if (text.toString()=="Ascending"){
                                    SortOrder = 1;
                                }else{
                                    SortOrder = -1;
                                }
                                Collections.sort(Items, new Sorter(SortOrder, SortType));
                                ViewList.this.adapter.notifyDataSetChanged();
                                ItemsListView.invalidateViews();
                                ItemsListView.setAdapter(ViewList.this.adapter);
                                return true;
                            }
                        })
                        .positiveText("Sort")
                        .negativeText("Cancel")
                        .show();



        }
        return true;
    }
}
