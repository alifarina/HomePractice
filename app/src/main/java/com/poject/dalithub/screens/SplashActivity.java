package com.poject.dalithub.screens;

import com.poject.dalithub.R;
import com.poject.dalithub.Utils.UserPreferences;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        printKeyHash();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                launchActivity();
            }

        }, 3000);
    }

    @Override
    public void onBackPressed() {

    }

    private void launchActivity() {

        String user_id = new UserPreferences(this).getUserId();

        if (user_id != null && !user_id.equals("")) {
            startActivity(new Intent(this, LandingScreenActivity.class));
            this.finish();
        } else {
            startActivity(new Intent(this, LoginSignupChooserActivity.class));
            this.finish();
        }


    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.poject.dalithub", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("MY KEY HASH:", sign);
//                Toast.makeText(getApplicationContext(),sign,         Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

}
