package com.poject.dalithub.screens;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.poject.dalithub.R;
import com.poject.dalithub.Utils.AppUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Farina on 12/27/2015.
 */
public class BitesFullScreenView extends DalitHubBaseActivity implements View.OnClickListener {
    private ImageView leftTopButton;


    private ImageView rightTopButton;
    private ImageView fullImageView;
    private TextView headerTitle;
    private String bite_id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_view_layout);
        initViews();

        Intent in = getIntent();
        String postName = in.getStringExtra("PostName");
        String imageUrl = in.getStringExtra("PostUrl");
        bite_id = in.getStringExtra("PostId");

        setScreenListeners(postName, imageUrl);
    }

    private Bitmap bitesBitmap;

    private void setScreenListeners(String postName, String url) {
        leftTopButton.setOnClickListener(this);
        rightTopButton.setOnClickListener(this);
        headerTitle.setText(postName);

        if (url != null && !url.equals("")) {
            Picasso.with(this).load(url).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    hideProgressDialog();
                    if (bitmap != null) {
                        fullImageView.setImageBitmap(bitmap);
                        bitesBitmap = bitmap;
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    fullImageView.setImageDrawable(errorDrawable);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    showProgressDialog("Loading...");
                }
            });
        } else {
            AppUtils.showToast(BitesFullScreenView.this, "No image available");
            BitesFullScreenView.this.finish();
        }

    }

    private void initViews() {
        leftTopButton = (ImageView) findViewById(R.id.left_button);
        rightTopButton = (ImageView) findViewById(R.id.right_button);
        headerTitle = (TextView) findViewById(R.id.title);

        fullImageView = (ImageView) findViewById(R.id.post_image);
        rightTopButton.setImageResource(R.drawable.add_member);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_button:
                goBackScreen();
                break;
            case R.id.right_button:
                saveImageToGallery();
                break;
            default:
                break;
        }

    }

    private void saveImageToGallery() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgressDialog("Saving image..");
            }

            @Override
            protected String doInBackground(Void... voids) {
                FileOutputStream output;
                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DalitHub/");
                if (!dir.exists())
                    dir.mkdir();

                try {
                    File imgFile = new File(dir, "image_" + bite_id + ".jpg");
                    if (imgFile.exists()) {
                        return "Image already exist";
                    }
                    output = new FileOutputStream(imgFile);

                    // Compress into png format image from 0% - 100%
                    bitesBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                    output.flush();
                    output.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String aVoid) {
                super.onPostExecute(aVoid);
                hideProgressDialog();
                if (aVoid != null)
                    AppUtils.showToast(BitesFullScreenView.this, aVoid);
            }
        }.execute();


    }

    private void goBackScreen() {
        this.finish();
    }
}
