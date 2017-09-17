package english.spanish.translate;

import android.*;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import english.spanish.translate.util.AnalyticsApplication;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by jiteshdugar on 9/17/17.
 */

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    Button scanBTN, genBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        setTracker();
        setUpInteraction();
        setUpListeners();
    }


    private void setUpInteraction() {
        scanBTN = (Button) findViewById(R.id.scanBTN);
        genBTN = (Button) findViewById(R.id.generateBTN);
    }

    private Tracker mTracker;

    public void setTracker() {
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);

        // Send a screen view.
        mTracker.setScreenName(getString(R.string.app_name));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.ScreenViewBuilder()
                .set(getString(R.string.app_name), getString(R.string.app_name))
                .build());

    }

    private void setUpListeners() {
        scanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readCameraPerm();

            }
        });
        genBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, GeneratorActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }
    private  void openScanIntent(){
        Intent myIntent = new Intent(MainActivity.this, ScannerActivity.class);
        MainActivity.this.startActivity(myIntent);
    }
    private static final int RC_CAMERA = 101;
    public void readCameraPerm() {
        String[] perms = {android.Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            openScanIntent();
        } else {
            // Ask for both permissions
            Log.d("else", "part");
            EasyPermissions.requestPermissions(this, "Allow Camera Scanning?",
                    RC_CAMERA, perms);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
// EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d("", "onPermissionsGranted:" + requestCode + ":" + perms.size());
        openScanIntent();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d("", "onPermissionsDenied:" + requestCode + ":" + perms.size());
        Toast.makeText(getApplicationContext(), "Permission is Compulsory to Proceed", Toast.LENGTH_SHORT).show();
    }

    private void actionRateUs() {
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
        }
    }
}
