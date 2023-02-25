package org.tensorflow.lite.examples.facerecognition;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.tensorflow.lite.examples.facerecognition.fragments.TeaMenuFragment.BlackTeaEarlgrayFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.TeaMenuFragment.ChamomileFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.TeaMenuFragment.GrapefruitTeaFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.TeaMenuFragment.HibiscusFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.TeaMenuFragment.LemonIcedTeaFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.TeaMenuFragment.LemonTeaFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.TeaMenuFragment.NoTeaFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.TeaMenuFragment.PeachIcedTeaAddShotFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.TeaMenuFragment.PeachIcedTeaFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.TeaMenuFragment.PeppermintFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.TeaMenuFragment.YuzaTeaFragment;

public class TeaAdapter extends FragmentStateAdapter {

    public int mCount;

    public TeaAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        int index = getRealPosition(position);

        if(index == 0) return new LemonTeaFragment();
        else if (index == 1) return new GrapefruitTeaFragment();
        else if (index == 2) return new YuzaTeaFragment();
        else if (index == 3) return new BlackTeaEarlgrayFragment();
        else if (index == 4) return new ChamomileFragment();
        else if (index == 5) return new HibiscusFragment();
        else if (index == 6) return new PeppermintFragment();
        else if (index == 7) return new LemonIcedTeaFragment();
        else if (index == 8) return new PeachIcedTeaFragment();
        else if (index == 9) return new PeachIcedTeaAddShotFragment();
        else return new NoTeaFragment();

    }

    private int getRealPosition(int position) {
        return position % mCount;
    }

    @Override
    public int getItemCount() {
        return 2000;
    }
}
