package tamu.themetry2;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by colin on 6/3/2016.
 */
public class LevelSelectFragAdapter extends FragmentStatePagerAdapter {

    public LevelSelectFragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return ChapterDataSingleton.getSync(LevelSelect.missionBoardContext).getListOfMissionData().size();
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            default: return LevelSelectFragment.newInstance("" + i,"");
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {}


}
