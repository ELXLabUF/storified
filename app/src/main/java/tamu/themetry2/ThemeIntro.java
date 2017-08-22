package tamu.themetry2;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
    The initial class for the application. Immediately skips to NewMainMenu if a theme has already been chosen.
 */

public class ThemeIntro extends FragmentActivity implements
        ThemeIntroFragment.OnFragmentInteractionListener,
        ThemeIntroTopicFragment.OnFragmentInteractionListener,
        ThemeIntroDescriptionFragment.OnFragmentInteractionListener,
        ThemeIntroKeywordsFragment.OnFragmentInteractionListener,
        ThemeIntroConfirmationFragment.OnFragmentInteractionListener {

    private TextView mTextView;
    private static final int REQUEST_PERMISSIONS = 1;
    private static String[] PERMISSIONS_LIST = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.VIBRATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_intro);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
        verifyStoragePermissions(this);

        //skipping theme selection, take this out if you want to select a theme
        ChapterDataSingleton.getSync(this).setTheme("journalist");

        String currentDateandTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US).format(new Date());
        String openTimestamp = currentDateandTime + "\n";
        ChapterDataSingleton.getSync(this).writeToFile(Environment.getExternalStorageDirectory().getAbsolutePath(), "enterapp.txt", openTimestamp);
        setFirstOpeningFalse();
        checkIfThemeChosen();
        setContentView(R.layout.rect_activity_theme_intro);
        ViewPager myVP = (ViewPager) this.findViewById(R.id.pager);

        CircleIndicator indicator = (CircleIndicator) this.findViewById(R.id.indicator);

        myVP.setAdapter(new ThemeIntroFragAdapter(getSupportFragmentManager()));

        indicator.setViewPager(myVP);
    }

    public void setFirstOpeningFalse(){
        SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean("firstOpening", false);
        edit.apply();
    }

    //skips immediately to the main menu if a theme has already been chosen
    public void checkIfThemeChosen(){
        SharedPreferences settings = getSharedPreferences("prefName", MODE_PRIVATE);
        String themeExistCheck = settings.getString("theme", "");
        if(!themeExistCheck.isEmpty()){
            Intent mainMenu = new Intent(this, NewMainMenu.class);
            startActivity(mainMenu);
            finish();
        }
    }

    public void enterThemeSelect(View v){
        //SKIPPING THEME FOR THE CLASS STUDY
        /*
        Intent theme = new Intent(this, Theme.class);
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                0, v.getWidth(), v.getHeight());
        startActivity(theme, options.toBundle());
        finish();
        */

        SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("theme", "journalist");
        edit.apply();

        edit.putBoolean("firstOpening", true);
        edit.apply();

        Intent mainMenu = new Intent(this, NewMainMenu.class);
        mainMenu.putExtra("firstTime", true);
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                0, v.getWidth(), v.getHeight());
        startActivity(mainMenu, options.toBundle());
        finish();
    }

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionRecord = ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED || permissionRecord != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_LIST,
                    REQUEST_PERMISSIONS
            );
            SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("systemPermissionsGiven", false);
            edit.apply();
        } else {

            SharedPreferences settings = getSharedPreferences("prefName", MODE_PRIVATE);
            Boolean needToDeleteAudio = settings.getBoolean("audioFilesDeleted", false);

            SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("systemPermissionsGiven", true);
            edit.apply();

            if(!needToDeleteAudio){
                deleteOldAudioFiles();
                edit.putBoolean("audioFilesDeleted", true);
                edit.apply();
            }
        }
    }

    void deleteOldAudioFiles(){
        int i = 0;
        int j = 0;
        boolean stillMoreFiles = true;
        String currentFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + i + "_chapter_recording_" + j + ".wav";
        while(stillMoreFiles) {
            File file = new File(currentFileName);
            if (!file.exists() && j != 0) {
                i++;
                j = 0;
            } else if(!file.exists() && j == 0){
                stillMoreFiles = false;
            } else {
                boolean deleted = file.delete();
                j++;
            }
            currentFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + i + "_chapter_recording_" + j + ".wav";
        }
    }

    public void onFragmentInteraction(Uri uri){

    }

    public void onFragmentInteractionTopic(Uri uri){

    }

    public void onFragmentInteractionDescription(Uri uri){

    }

    public void onFragmentInteractionKeywords(Uri uri){

    }

    public void onFragmentInteractionConfirmation(Uri uri){

    }
}
