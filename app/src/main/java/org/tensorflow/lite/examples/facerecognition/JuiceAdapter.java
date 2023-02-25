package org.tensorflow.lite.examples.facerecognition;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.tensorflow.lite.examples.facerecognition.fragments.JuiceMenuFragment.AppleJuiceFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.JuiceMenuFragment.BananaJuiceFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.JuiceMenuFragment.GingerbeerBundabergFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.JuiceMenuFragment.LemonLimeBundabergFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.JuiceMenuFragment.NoJuiceFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.JuiceMenuFragment.OrangeJuiceFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.JuiceMenuFragment.PerrierCarbonatedWaterFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.JuiceMenuFragment.PinkBundabergFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.JuiceMenuFragment.TomatoJuiceFragment;

public class JuiceAdapter extends FragmentStateAdapter {

    public int mCount;

    public JuiceAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        int index = getRealPosition(position);

        if (index == 0) return new OrangeJuiceFragment();
        else if (index == 1) return new TomatoJuiceFragment();
        else if (index == 2) return new BananaJuiceFragment();
        else if (index == 3) return new AppleJuiceFragment();
        else if (index == 4) return  new PinkBundabergFragment();
        else if (index == 5) return new LemonLimeBundabergFragment();
        else if (index == 6) return new GingerbeerBundabergFragment();
        else if (index == 7) return new PerrierCarbonatedWaterFragment();
        else return new NoJuiceFragment();

    }

    private int getRealPosition(int position) {
        return position % mCount;
    }


    @Override
    public int getItemCount() {
        return 2000;
    }
}
