package tamu.themetry2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.WatchViewStub;
import android.widget.ImageView;
import android.widget.TextView;

/*
    Main menu for the app. Application skips to this class if it has already been here.
 */

public class NewMainMenu extends FragmentActivity implements
        NewMainMenuFragment.OnFragmentInteractionListener {

    private TextView mTextView;
    private String themeName;
    public static String unitSelected;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main_menu);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });

        mContext = this;

        if(!checkIfFirstOpening()){
            checkIfNotification();
        }

        SharedPreferences settings = getSharedPreferences("prefName", MODE_PRIVATE);
        themeName = settings.getString("theme", "");
        ChapterDataSingleton.getSync(this).setTheme(themeName);
        unitSelected = ControlDataSingleton.getSync(this).getUnitSelected();
        setContentView(R.layout.rect_activity_new_main_menu);
        setMainMenuTheme();

        ViewPager myVP = (ViewPager) this.findViewById(R.id.pager);

        CircleIndicator indicator = (CircleIndicator) this.findViewById(R.id.indicator);

        myVP.setAdapter(new NewMainMenuFragAdapter(getSupportFragmentManager()));

        indicator.setViewPager(myVP);


        long notificationDelay = 1000 * 60 * 60 * 5; //5 hours, starts in milliseconds
        for(int i = 0; i < ChapterDataSingleton.getSync(this).getListOfNumberRecordings().size(); i++){
            if(ChapterDataSingleton.getSync(this).getNumberOfRecordings(i) == 0){

                final int notificationNumber = i;
                setAlarm(notificationNumber, notificationDelay);
                i = ChapterDataSingleton.getSync(this).getListOfNumberRecordings().size();

            }
        }

    }

    public void setAlarm(final int notificationNumber, long notificationDelay) {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override public void onReceive( Context context, Intent _ ) {
                MessagePromptNotification mpn = new MessagePromptNotification(mContext);
                mpn.createNotification(notificationNumber);
                context.unregisterReceiver( this ); // this == BroadcastReceiver, not Activity
            }
        };

        this.registerReceiver( receiver, new IntentFilter("com.blah.blah.somemessage") );

        PendingIntent pintent = PendingIntent.getBroadcast( this, 0, new Intent("com.blah.blah.somemessage"), 0 );
        AlarmManager manager = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));

        // set alarm to fire 5 sec (1000*5) from now (SystemClock.elapsedRealtime())
        manager.set( AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + notificationDelay, pintent );
    }

    public boolean checkIfFirstOpening(){
        System.out.println("got in first opening function");
        SharedPreferences settings = getSharedPreferences("prefName", MODE_PRIVATE);
        boolean firstOpening = settings.getBoolean("firstOpening", false);
        if(firstOpening){
            Intent levelSelect = new Intent(this, LevelSelect.class);
            startActivity(levelSelect);
            return true;
        }
        return false;
    }

    //skips immediately to ChapterCollection if from a notification
    public void checkIfNotification(){
        SharedPreferences settings = getSharedPreferences("prefName", MODE_PRIVATE);
        String startedFrom = settings.getString("startedFrom", "");
        System.out.println("startedFrom: " + startedFrom);
        if(startedFrom.equals("notification")){
            Intent levelSelect = new Intent(this, LevelSelect.class);
            startActivity(levelSelect);
        }
    }

    public void setMainMenuTheme(){
        ImageView mainMenuImage = (ImageView) findViewById(R.id.mainMenuTopImage);
        TextView mainMenuText = (TextView) findViewById(R.id.mainMenuTopText);
        switch (ChapterDataSingleton.getSync(this).getTheme()) {
            case "spy":
                mainMenuImage.setImageResource(R.drawable.spy_menu_background);
                mainMenuText.setText("Hello,\nAgent");
                break;
            case "adventurer":
                mainMenuImage.setImageResource(R.drawable.adventurer_menu_background);
                mainMenuText.setText("Hello,\nExplorer");
                break;
            case "journalist":
                mainMenuImage.setImageResource(R.drawable.journalist_menu_background);
                mainMenuText.setText("Hello,\nReporter");
                break;
        }
        //FOR SCHOOL TEST WITH KIDDOS, DELETE THIS AFTERWARDS
        //OVERWRITING SWITCH CASES ABOVE
        mainMenuText.setText("Hello there");
    }

    public void onFragmentInteraction(Uri uri){

    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(ChapterDataSingleton.getSync(this).getPromptOrder() != null && ChapterDataSingleton.getSync(this).getPromptOrder().peek() != null){
            findViewById(R.id.menuSelectButton).setEnabled(true);
        } else {
            findViewById(R.id.menuSelectButton).setEnabled(false);
        }
        setMainMenuTheme();
    }
   */
}
