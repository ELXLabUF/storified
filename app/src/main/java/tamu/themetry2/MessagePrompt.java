package tamu.themetry2;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
    Initial screen for a prompt - contains the main screen and a confirmation screen
 */

public class MessagePrompt extends FragmentActivity implements
        MessagePromptFragment.OnFragmentInteractionListener,
        MessagePromptConfirmationFragment.OnFragmentInteractionListener {

    private TextView mTextView;
    public static int missionNumber;
    public static Context mContext;
    public String levelType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_prompt);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
        mContext = this;
        loadMissionNumber();
        loadLevelType();
        String currentDateandTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US).format(new Date());
        String levelTimestamp = (missionNumber + 1) + ": " + currentDateandTime + "\n";
        ChapterDataSingleton.getSync(this).writeToFile(Environment.getExternalStorageDirectory().getAbsolutePath(), "levelselect.txt", levelTimestamp);


        System.out.println("levelType: " + levelType.equals("currentLeveL"));
        System.out.println("getNumberRecordings: " + ChapterDataSingleton.getSync(this).getNumberOfRecordings(missionNumber));
        System.out.println("isFirstPlay: " + ChapterDataSingleton.getSync(this).getListOfMissionData().get(missionNumber).isFirstPlay());
        if(levelType.equals("currentLevel") && ChapterDataSingleton.getSync(this).getNumberOfRecordings(missionNumber) > 0){
            enterRecordSelect();
        } else if (ChapterDataSingleton.getSync(this).getListOfMissionData().get(missionNumber).isFirstPlay()){
            playAudio();
            ChapterDataSingleton.getSync(this).getListOfMissionData().get(missionNumber).setFirstPlay(false);
        }

        this.setContentView(R.layout.rect_activity_message_prompt);
        ViewPager myVP = (ViewPager) this.findViewById(R.id.pager);
        CircleIndicator indicator = (CircleIndicator) this.findViewById(R.id.indicator);
        myVP.setAdapter(new MessagePromptFragAdapter(getSupportFragmentManager()));
        indicator.setViewPager(myVP);
        if(ChapterDataSingleton.getSync(this).getListOfMissionData().get(missionNumber).isFirstPlay() && !levelType.equals("currentLevel")) {
            playAudio();
            ChapterDataSingleton.getSync(this).getListOfMissionData().get(missionNumber).setFirstPlay(false);
        }
    }

    public void loadLevelType() {
        SharedPreferences settings = getSharedPreferences("prefName", MODE_PRIVATE);
        levelType = settings.getString("levelType", "");
    }

    public void loadMissionNumber() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            missionNumber = bundle.getInt("missionNumber");
        }
    }

    public void playAudio(){
        if(missionNumber == 0){
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.journalist_audio_1);
            mediaPlayer.start();
        } else if (missionNumber == 1){
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.journalist_audio_2);
            mediaPlayer.start();
        } else if (missionNumber == 2){
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.journalist_audio_3);
            mediaPlayer.start();
        } else if (missionNumber == 3){
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.journalist_audio_4);
            mediaPlayer.start();
        }
    }

    //the fragment requires the view as an argument
    public void enterRecordSelect(View v){
        Intent recording = new Intent(this, MessageRecord.class);
        recording.putExtra("missionNumber", missionNumber);
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                0, v.getWidth(), v.getHeight());
        startActivity(recording, options.toBundle());
        finish();
    }

    public void enterRecordSelect(){
        Intent recording = new Intent(this, MessageRecord.class);
        recording.putExtra("missionNumber", missionNumber);
        startActivity(recording);
        finish();
    }

    public void onFragmentInteraction(Uri uri){

    }

    public void onFragmentInteractionConfirmation(Uri uri){

    }
}
