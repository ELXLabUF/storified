package tamu.themetry2;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

/*
    The end screen after the recording is completed - contains the main screen, badge screen, and confirmation
 */

public class MessagePromptEnd extends FragmentActivity implements
        MessagePromptEndFragment.OnFragmentInteractionListener,
        MessagePromptEndBadgeFragment.OnFragmentInteractionListener,
        MessagePromptEndConfirmationFragment.OnFragmentInteractionListener {

    private TextView mTextView;
    public static int missionNumber;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_prompt_end);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
        mContext = getApplicationContext();
        loadMissionNumber();
        setContentView(R.layout.rect_activity_message_prompt_end);
        ViewPager myVP = (ViewPager) this.findViewById(R.id.pager);
        CircleIndicator indicator = (CircleIndicator) this.findViewById(R.id.indicator);
        myVP.setAdapter(new MessagePromptEndFragAdapter(getSupportFragmentManager()));
        indicator.setViewPager(myVP);
    }

    public void loadMissionNumber() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            missionNumber = bundle.getInt("missionNumber");
        }
    }

    public void returnToMenu(View v) {
        ChapterDataSingleton.getSync(this).updateListMissionData();
        /*
        Intent mainMenu = new Intent(this, NewMainMenu.class);
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                0, v.getWidth(), v.getHeight());
        startActivity(mainMenu, options.toBundle());
        */
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void returnToRecording(View v) {
        Intent recording = new Intent(this, MessageRecord.class);
        ActivityOptions options = ActivityOptions.makeScaleUpAnimation(v, 0,
                0, v.getWidth(), v.getHeight());
        startActivity(recording, options.toBundle());
        finish();
    }

    public void onFragmentInteraction(Uri uri) {

    }

    public void onFragmentInteractionConfirmation(Uri uri) {

    }
}
