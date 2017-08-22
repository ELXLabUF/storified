package tamu.themetry2;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by colin on 11/7/2016.
 */
public class NewMainMenuFragAdapter extends FragmentStatePagerAdapter {

    public NewMainMenuFragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            case 0: return NewMainMenuFragment.newInstance("current story","");
            //case 1: return NewMainMenuFragment.newInstance("past stories", "");
            case 1: return NewMainMenuFragment.newInstance("review","");
            case 2: return NewMainMenuFragment.newInstance("recordings", "");
            default: return NewMainMenuFragment.newInstance("sync","");
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {}

}
