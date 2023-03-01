package org.tensorflow.lite.examples.facerecognition.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.tensorflow.lite.examples.facerecognition.fragments.AdeMenuFragment.GrapefruitAdeFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.AdeMenuFragment.GreenGrapeAdeFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.AdeMenuFragment.LemonAdeFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.AdeMenuFragment.NoAdeFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.AdeMenuFragment.StrawberryLemonAdeFragment;

public class AdeAdapter extends FragmentStateAdapter {

    public int mCount;

    public AdeAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        int index = getRealPosition(position);

        if (index == 0) return new LemonAdeFragment();
        else if (index == 1) return new GrapefruitAdeFragment();
        else if (index == 2) return new GreenGrapeAdeFragment();
        else if (index == 3) return new StrawberryLemonAdeFragment();
        else return new NoAdeFragment();
    }

    private int getRealPosition(int position) {
        return position % mCount;
    }


    @Override
    public int getItemCount() {
        return 2000;
    }
}
