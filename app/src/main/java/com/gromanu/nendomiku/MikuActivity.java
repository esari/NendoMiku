package com.gromanu.nendomiku;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gromanu.nendomiku.controller.DataController;
import com.gromanu.nendomiku.controller.ImageController;
import com.gromanu.nendomiku.model.MikuItem;

import java.util.List;

public class MikuActivity extends Activity implements DataController.DataControllerCallback {

    private ViewGroup itemsContainer;
    private TextView itemsCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        itemsContainer = (ViewGroup) findViewById(R.id.items_list);
        itemsCount = (TextView) findViewById(R.id.counter);
        // TODO this is the start line. The end line is in the onImageReceived() method in onCreateNewItemLine().
        // TODO on a separate sheet of paper make a chronological and threaded list of all the methods called (only the methods from our project classes, not the android native methods)
        ((MikuApplication)getApplication()).getDataController().fetchData(this);
    }

    @Override
    public void onDataReceived(final List<MikuItem> itemsList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                itemsCount.setText(getString(R.string.count, itemsList.size()));

                itemsContainer.removeAllViews();
                LayoutInflater inflater = LayoutInflater.from(MikuActivity.this);
                for (MikuItem item: itemsList) {
                    itemsContainer.addView(createNewItemLine(inflater, item));
                }
            }
        });
    }

    @Override
    public void onDataError(Exception e) {
        showError(e);
    }

    private View createNewItemLine(LayoutInflater inflater, MikuItem item) {
        View newLine = inflater.inflate(R.layout.item_line, itemsContainer, false);

        TextView label = (TextView) newLine.findViewById(R.id.name);
        label.setText(getString(R.string.item_label, item.getNumber(), item.getName()));

        TextView price = (TextView) newLine.findViewById(R.id.price);
        price.setText(getString(R.string.item_price, item.getPrice()));

        final ImageView imageView = (ImageView) newLine.findViewById(R.id.image);
        final View progressBar = newLine.findViewById(R.id.image_progress);
        imageView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        ((MikuApplication)getApplication()).getImageController().fetchImage(item.getPictureURL(), new ImageController.ImageControllerCallback() {
            @Override
            public void onImageReceived(final Bitmap image) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(image);
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        //TODO end line
                    }
                });
            }

            @Override
            public void onImageError(Exception e) {
                showError(e);
            }
        });
        return newLine;
    }

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
