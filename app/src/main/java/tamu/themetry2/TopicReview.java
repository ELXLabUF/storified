package tamu.themetry2;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
    Shows the initial description as seen in ThemeIntroDescriptionFragment
 */

public class TopicReview extends FragmentActivity implements TopicReviewFragment.OnFragmentInteractionListener {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_review);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
        this.setContentView(R.layout.rect_activity_topic_review);
        ViewPager myVP = (ViewPager) this.findViewById(R.id.pager);
        myVP.setAdapter(new TopicReviewFragAdapter(getSupportFragmentManager()));
    }

    public void onFragmentInteraction(View v){

    }

}