package tamu.themetry2;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
    Shows all of the recordings completed
 */

public class ChapterCollection extends FragmentActivity implements ChapterCollectionFragment.OnFragmentInteractionListener {

    private TextView mTextView;
    public static String unitSelected;
    public static boolean updateData;
    public static Context missionBoardContext;
    public static ViewPager myVP;
    public static ChapterCollectionFragAdapter myFA;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_collection);
        unitSelected = getIntent().getStringExtra("unitSelected");
        updateData = getIntent().getBooleanExtra("updateData", false);
        missionBoardContext = getApplicationContext();
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });

        ChapterDataSingleton.getSync(this).updateListMissionData();
        this.setContentView(R.layout.rect_activity_chapter_collection);
        myVP = new ViewPager(this);
        myVP.setId(View.generateViewId());
        myFA = new ChapterCollectionFragAdapter(getSupportFragmentManager());
        myVP.setAdapter(myFA);
        LinearLayout linL = (LinearLayout) this.findViewById(R.id.myLayout);
        linL.addView(myVP);
        updateScreenData();
    }

    public void updateScreenData(){
        if(updateData){
            ChapterCollectionFragAdapter updateFA = new ChapterCollectionFragAdapter(getSupportFragmentManager());
            myVP.getAdapter().notifyDataSetChanged();
            myVP.setAdapter(updateFA);
            myVP.getAdapter().notifyDataSetChanged();
        }
    }

    //this function is called when the theme button is pressed in its respective fragment
    public void onFragmentInteraction(View v, String missionNumberInUnit){
        //goes in here if the mission has not been completed already
        String convertedMissionNumber = missionNumberForAll(missionNumberInUnit);
        SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
        SharedPreferences.Editor edit= preferences.edit();
        ChapterDataSingleton.getSync(missionBoardContext).updateListMissionData();
        if(!Boolean.valueOf(ChapterDataSingleton.getSync(missionBoardContext).get(Integer.parseInt(convertedMissionNumber)).getIsMissionCompleted())) {
            edit.putString("lastRunMission", convertedMissionNumber);
        }
        edit.putString("missionNumber", convertedMissionNumber);
        edit.apply();
    }

    public String missionNumberForAll(String missionNumberInUnit){
        Integer missionNumberInUnitInt = Integer.parseInt(missionNumberInUnit);
        Integer counter = 0;
        String convertedMissionNumber = "";
        for(int i = 0; i < ChapterDataSingleton.getSync(missionBoardContext).size(); i++){
            if(ChapterDataSingleton.getSync(missionBoardContext).get(i).getNameOfUnit().equals(unitSelected) && counter < missionNumberInUnitInt){
                counter++;
            } else if (ChapterDataSingleton.getSync(missionBoardContext).get(i).getNameOfUnit().equals(unitSelected) && counter.equals(missionNumberInUnitInt)){
                convertedMissionNumber = "" + i;
                counter++;
            }
        }
        return convertedMissionNumber;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ChapterCollectionFragAdapter updateFA = new ChapterCollectionFragAdapter(getSupportFragmentManager());
        myVP.getAdapter().notifyDataSetChanged();
        myVP.setAdapter(updateFA);
        myVP.getAdapter().notifyDataSetChanged();
    }
}
