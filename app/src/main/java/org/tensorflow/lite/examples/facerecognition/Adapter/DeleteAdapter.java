package org.tensorflow.lite.examples.facerecognition.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.tensorflow.lite.examples.facerecognition.BlankFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.ModeFragment.PayFragment;

public class DeleteAdapter extends FragmentStateAdapter {

    public int mCount;

    public DeleteAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        int index = getRealPosition(position);

        for(int i=0; i<mCount-1; i++){
            if(index == i) return new BlankFragment();
        }

        return new PayFragment();


    }

    private int getRealPosition(int position) {
        return position % mCount;
    }


    @Override
    public int getItemCount() {
        return 2000;
    }
}
