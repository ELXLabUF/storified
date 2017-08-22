package tamu.themetry2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by colin on 8/6/2016.
 */
public class ThemeIntroFragAdapter extends FragmentPagerAdapter {

    public ThemeIntroFragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            case 0: return ThemeIntroFragment.newInstance("", "");
            default: return ThemeIntroDescriptionFragment.newInstance("", "");
        }
    }
}
