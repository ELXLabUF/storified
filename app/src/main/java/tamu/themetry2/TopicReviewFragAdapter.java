package tamu.themetry2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by colin on 6/10/2016.
 */
public class TopicReviewFragAdapter extends FragmentPagerAdapter {

    public TopicReviewFragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            default: return TopicReviewFragment.newInstance("", "");
        }
    }
}
