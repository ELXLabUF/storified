package tamu.themetry2;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
    Menu screen for selecting which level
 */

public class LevelSelect extends FragmentActivity implements LevelSelectFragment.OnFragmentInteractionListener {

    private TextView mTextView;
    public static Context missionBoardContext;
    public static int missionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
        missionBoardContext = this;
        setContentView(R.layout.rect_activity_level_select);
        ViewPager myVP = (ViewPager) this.findViewById(R.id.pager);
        myVP.setAdapter(new LevelSelectFragAdapter(getSupportFragmentManager()));
        checkIfNotification();
    }

    public void selectLevel(View v){
        Intent messagePrompt = new Intent(this, MessagePrompt.class);
        messagePrompt.putExtra("startedFrom", "missionBoard");

        SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt("levelNumber", missionNumber);
        edit.apply();
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                0, v.getWidth(), v.getHeight());
        startActivityForResult(messagePrompt, 1, options.toBundle());
        finish();
    }

    //skips immediately to MessagePrompt if from a notification
    public void checkIfNotification(){
        SharedPreferences settings = getSharedPreferences("prefName", MODE_PRIVATE);
        String startedFrom = settings.getString("startedFrom", "");
        int notificationMissionNumber = settings.getInt("notificationMissionNumber", 0);
        if(startedFrom.equals("notification") && ChapterDataSingleton.getSync(this).getNumberOfRecordings(notificationMissionNumber) < 3){
            SharedPreferences.Editor edit = settings.edit();
            edit.putInt("levelNumber", notificationMissionNumber);
            edit.putString("startedFrom", "general");
            edit.apply();
            Intent levelSelect = new Intent(this, MessagePrompt.class);
            startActivity(levelSelect);
        }
    }

    public void onFragmentInteraction(Uri uri){

    }
}
