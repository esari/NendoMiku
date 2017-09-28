package com.gromanu.nendomiku;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gromanu.nendomiku.controller.DataController;
import com.gromanu.nendomiku.controller.ImageController;
import com.gromanu.nendomiku.model.MikuItem;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.button;

public class MikuActivity extends Activity implements DataController.DataControllerCallback {

    private ViewGroup itemsContainer;
    private TextView itemsCount;

    private MikuAdapter mikuAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //TODO 4: Find a reference to the {@link ListView} in the layout
        ListView mikuListView = (ListView) findViewById(R.id.items_list);

        //TODO 5: Create a new adapter that takes an empty list of nendoMiku as input
        mikuAdapter = new MikuAdapter(this, new ArrayList<MikuItem>());

        // this part should be move to ViewAdapter class ?
        itemsContainer = (ViewGroup) findViewById(R.id.items_list);
        itemsCount = (TextView) findViewById(R.id.counter);



        //TODO 6: Set the adapter on the {@link ListView} then put it inside the UiThread, so the list can be populated in the user interface

        mikuListView.setAdapter(mikuAdapter);
        //TODO 7: Start to fetch the nendoMiku data
        ((MikuApplication)getApplication()).getDataController().fetchData(this);
    }

    //TODO 8: Save only the itemsCount.setText, then adding the viewAdapter after it
    // by looking at the onPostExcecute () method
    @Override
    public void onDataReceived(final List<MikuItem> itemsList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                itemsCount.setText(getString(R.string.count, itemsList.size()));

                //Clear the adapter from the previous miku itemList
                mikuAdapter.clear();

                //if there is a valid list of {@link MikuItem)s, then add them to adapter
                //data set . This will trigger the ListView to update
                if (itemsList !=null && !itemsList.isEmpty()){
                    mikuAdapter.addAll(itemsList);
                }
            }
        });
    }

    @Override
    public void onDataError(Exception e) {
        showError(e);
    }

    //TODO 1: Create ViewAdapter class and put this methode on it
    //TODO 2: Replace View Newline with View listItemView
    //TODO 3: Replace object newLine with listItemView





        // TODO 9: Set an item click listener on the ListView, to open a selected miku image.





    private void showError(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(MikuActivity.this)
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_error)
                        .setTitle(R.string.error_title)
                        .setMessage(e.getClass().getSimpleName())
                        .show();
            }
        });
    }
}
