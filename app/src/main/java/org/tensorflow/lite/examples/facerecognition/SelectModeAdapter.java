package org.tensorflow.lite.examples.facerecognition;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.tensorflow.lite.examples.facerecognition.fragments.ModeFragment.AddOrderFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.ModeFragment.BasketFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.ModeFragment.DeleteFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.ModeFragment.PayFragment;

public class SelectModeAdapter extends FragmentStateAdapter {

    public int mCount;

    public SelectModeAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index == 0) return new PayFragment();
        else if(index == 1) return new AddOrderFragment();
        else if(index == 2) return new DeleteFragment();
        else return new BasketFragment();

    }

    @Override
    public int getItemCount() {
        return 2000;
    }

    public int getRealPosition(int position) { return position % mCount; }

}
