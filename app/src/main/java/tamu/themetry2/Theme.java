package tamu.themetry2;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
    Prompts the user to select a theme. Currently not being used as of the Johnson Elementary study.
 */

//TODO: currently not using, needs to be put back in when themes are wanted again

public class Theme extends FragmentActivity implements ThemeFragment.OnFragmentInteractionListener {

    private TextView mTextView;
    public static DeactivatableViewPager myVP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
        setContentView(R.layout.rect_activity_theme);
        myVP = new DeactivatableViewPager(this);

        CircleIndicator indicator = (CircleIndicator) this.findViewById(R.id.indicator);

        myVP.setId(View.generateViewId());
        myVP.setAdapter(new ThemeFragAdapter(getSupportFragmentManager()));
        RelativeLayout relL = (RelativeLayout) this.findViewById(R.id.myLayout);
        relL.addView(myVP);

        indicator.setViewPager(myVP);

    }

    //this function is called when the theme button is pressed in its respective fragment
    public void onFragmentInteraction(View v, String themeName){
        SharedPreferences preferences = getSharedPreferences("prefName", MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("theme", themeName);
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

    public class DeactivatableViewPager extends ViewPager {
        public DeactivatableViewPager(Context context) {
            super(context);
        }

        public DeactivatableViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return !isEnabled() || super.onTouchEvent(event);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            return isEnabled() && super.onInterceptTouchEvent(event);
        }
    }

}