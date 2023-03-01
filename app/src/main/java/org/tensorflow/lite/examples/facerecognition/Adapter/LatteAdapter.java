package org.tensorflow.lite.examples.facerecognition.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.tensorflow.lite.examples.facerecognition.fragments.LatteMenuFragment.BlackteaLatteFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.LatteMenuFragment.ChocolateLatteFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.LatteMenuFragment.GreenteaLatteFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.LatteMenuFragment.NoLatteFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.LatteMenuFragment.StrawberryLatteFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.LatteMenuFragment.SweetPotatoLatteFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.LatteMenuFragment.ToffeenutLatteFragment;

public class LatteAdapter extends FragmentStateAdapter {

    public int mCount;

    public LatteAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        int index = getRealPosition(position);

        if (index == 0) return new ChocolateLatteFragment();
        else if (index == 1) return new GreenteaLatteFragment();
        else if (index == 2) return new BlackteaLatteFragment();
        else if (index == 3) return new SweetPotatoLatteFragment();
        else if (index == 4) return new ToffeenutLatteFragment();
        else if (index == 5) return new StrawberryLatteFragment();
        else return new NoLatteFragment();
    }

    private int getRealPosition(int position) {
        return position % mCount;
    }

    @Override
    public int getItemCount() {
        return 2000;
    }
}
