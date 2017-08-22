package tamu.themetry2;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.WatchViewStub;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class MessagePromptNoRecording extends Activity {

    private TextView mTextView;
    public static int missionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_prompt_no_recording);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
        loadMissionNumber();
        this.setContentView(R.layout.rect_activity_message_prompt_no_recording);
        playAudio();
        TextView tvPrompt = (TextView) findViewById(R.id.messagePromptNoRecordingText);
        tvPrompt.setText(Html.fromHtml(ChapterDataSingleton.getSync(this).get(MessagePrompt.missionNumber).getMissionPrompt()));
    }

    public void loadMissionNumber() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            missionNumber = bundle.getInt("missionNumber");
        }
    }

    public void mainMenuSelect(View v){
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
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
}
