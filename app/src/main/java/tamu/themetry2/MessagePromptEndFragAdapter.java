package tamu.themetry2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by colin on 9/10/2016.
 */
public class MessagePromptEndFragAdapter extends FragmentPagerAdapter {

    public MessagePromptEndFragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        if(ChapterDataSingleton.getSync(MessagePromptEnd.mContext).getNumberOfRecordings(MessagePromptEnd.missionNumber) > 2){
            return 3;
        }
        return 2;
    }

    @Override
    public Fragment getItem(int i) {
        if(ChapterDataSingleton.getSync(MessagePromptEnd.mContext).getNumberOfRecordings(MessagePromptEnd.missionNumber) > 2){
            switch(i) {
                case 0: return MessagePromptEndFragment.newInstance("", "");
                case 1: return MessagePromptEndBadgeFragment.newInstance("", "");
                default: return MessagePromptEndConfirmationFragment.newInstance("", "");
            }
        } else {
            switch(i) {
                case 0: return MessagePromptEndFragment.newInstance("", "");
                default: return MessagePromptEndConfirmationFragment.newInstance("", "");
            }
        }
    }
}

