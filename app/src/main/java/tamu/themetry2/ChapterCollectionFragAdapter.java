package tamu.themetry2;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by colin on 6/3/2016.
 */
public class ChapterCollectionFragAdapter extends FragmentStatePagerAdapter {

    public ChapterCollectionFragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return ChapterDataSingleton.getSync(ChapterCollection.missionBoardContext).totalNumberOfRecordings();
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            default: return ChapterCollectionFragment.newInstance("" + i,"");
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {}


}
