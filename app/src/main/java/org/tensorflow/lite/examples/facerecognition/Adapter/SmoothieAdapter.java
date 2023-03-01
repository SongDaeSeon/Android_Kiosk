package org.tensorflow.lite.examples.facerecognition.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.tensorflow.lite.examples.facerecognition.fragments.SmoothieMenuFragment.BlueberrySmoothieFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.SmoothieMenuFragment.NoSmoothieFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.SmoothieMenuFragment.StrawberrySmoothieFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.SmoothieMenuFragment.YogurtSmoothieFragment;

public class SmoothieAdapter extends FragmentStateAdapter {

    public int mCount;

    public SmoothieAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        int index = getRealPosition(position);

        if (index == 0) return new StrawberrySmoothieFragment();
        else if (index == 1) return new BlueberrySmoothieFragment();
        else if (index == 2) return new YogurtSmoothieFragment();
        else return new NoSmoothieFragment();

    }

    private int getRealPosition(int position) {
        return position % mCount;
    }


    @Override
    public int getItemCount() {
        return 2000;
    }
}
