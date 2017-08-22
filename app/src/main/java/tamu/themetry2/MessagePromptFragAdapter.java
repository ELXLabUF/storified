package tamu.themetry2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by colin on 11/10/2016.
 */
public class MessagePromptFragAdapter extends FragmentPagerAdapter {

    public MessagePromptFragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        if(ChapterDataSingleton.getSync(MessagePrompt.mContext).get(MessagePrompt.missionNumber).getWithRecording()){
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            case 0: return MessagePromptFragment.newInstance("", "");
            default: return MessagePromptConfirmationFragment.newInstance("", "");
        }
    }
}