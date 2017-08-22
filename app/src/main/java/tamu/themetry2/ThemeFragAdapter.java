package tamu.themetry2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by colin on 6/3/2016.
 */
public class ThemeFragAdapter extends FragmentPagerAdapter {

    public ThemeFragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 1;//3;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            //case 0: return ThemeFragment.newInstance("Spy","first");
            //case 1: return ThemeFragment.newInstance("Adventurer","");
            default: return ThemeFragment.newInstance("Journalist","last");
        }
    }

}
