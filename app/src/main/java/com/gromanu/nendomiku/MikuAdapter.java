package com.gromanu.nendomiku;

import android.app.Activity;
import android.content.Context;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gromanu.nendomiku.controller.ImageController;
import com.gromanu.nendomiku.model.MikuItem;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.button;

/**
 * Created by twiyatni on 9/28/2017.
 */

public class MikuAdapter extends ArrayAdapter<MikuItem> {

    private final Activity activity;
    private Map<String, Bitmap> imageBuffer = new HashMap<>();

    /**
     * Create a new {@link MikuAdapter} object.
     *
     * @param activity is the current context (i.e. Activity) that the adapter is being created in.
     * @param mikuItems is the list of {@link MikuItem}s to be displayed.
     */
    public MikuAdapter(Activity activity, List<MikuItem> mikuItems){
        super(activity, 0, mikuItems);
        this.activity = activity;
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        //Check if an existing view is being reused, otherwise inflate the view
         View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_line, parent, false);
        }

         final MikuItem currentItem = getItem(position);



        TextView label = (TextView) listItemView.findViewById(R.id.name);
        label.setText(getContext().getString(R.string.item_label, currentItem.getNumber(), currentItem.getName()));

        TextView price = (TextView) listItemView.findViewById(R.id.price);
        //price.setText(String.format("%s",getContext().getString(R.string.item_price, currentItem.getPrice())));

        price.setText(currentItem.getPrice());

        final Button button = (Button) listItemView.findViewById(R.id.image_button);

        final ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);
        final View progressBar = listItemView.findViewById(R.id.image_progress);

        if(imageBuffer.containsKey(currentItem.getPictureURL())){
            imageView.setImageBitmap(imageBuffer.get(currentItem.getPictureURL()));
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            button.setVisibility(View.INVISIBLE);

        } else {
            imageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            button.setVisibility(View.VISIBLE);

        }


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                button.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                ((MikuApplication) activity.getApplication()).getImageController().fetchImage(currentItem.getPictureURL(), new ImageController.ImageControllerCallback() {
                    @Override
                    public void onImageReceived(final Bitmap image) {
                        imageBuffer.put(currentItem.getPictureURL(), image);

                        activity.runOnUiThread(new Runnable() {
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
                    }
                });
            }

        });

        return listItemView;
    }
}
