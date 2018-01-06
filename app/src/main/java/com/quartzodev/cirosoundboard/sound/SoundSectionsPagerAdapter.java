package com.quartzodev.cirosoundboard.sound;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.quartzodev.cirosoundboard.data.Section;

import java.util.List;

/**
 * Created by victoraldir on 17/12/2017.
 */

public class SoundSectionsPagerAdapter extends FragmentStatePagerAdapter {

    private List<Section> mSectionList;


    public SoundSectionsPagerAdapter(FragmentManager fm,
                                     List<Section> sectionList) {
        super(fm);
        mSectionList = sectionList;
        addFavorite();
    }

    private void addFavorite(){
        Section favSection = new Section(new Long(0),"Favorite",0);
        mSectionList.add(0,favSection);
    }

    public void swap(List<Section> sections){
        if(sections != null)
            setList(sections);
    }

    private void setList(List<Section> sections) {
        mSectionList = sections;
        addFavorite();
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return SoundFragment.newInstance(mSectionList.get(position).getId());
    }

    public int getItemPosition(Object item) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mSectionList.get(position).getLabel();
    }

    @Override
    public int getCount() {
        return mSectionList.size();
    }
}
