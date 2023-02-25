package org.tensorflow.lite.examples.facerecognition;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.tensorflow.lite.examples.facerecognition.fragments.CoffeeMenuFragment.AmericanoFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.CoffeeMenuFragment.CafeLatteFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.CoffeeMenuFragment.CafeMochaFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.CoffeeMenuFragment.CappuccinoFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.CoffeeMenuFragment.CaramelMacchiatoFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.CoffeeMenuFragment.DolceLatteFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.CoffeeMenuFragment.EspressoFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.CoffeeMenuFragment.HazelnutFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.CoffeeMenuFragment.HazelnutLatteFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.CoffeeMenuFragment.NoCoffeeFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.CoffeeMenuFragment.VanillaLatteFragment;

public class CoffeeAdapter extends FragmentStateAdapter {

    public int mCount;

    public CoffeeAdapter(FragmentActivity fa, int count){
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        int index = getRealPosition(position);

        if (index == 0) return new AmericanoFragment();
        else if (index == 1) return new EspressoFragment();
        else if (index == 2) return new HazelnutFragment();
        else if (index == 3) return new CappuccinoFragment();
        else if (index == 4) return new CafeLatteFragment();
        else if (index == 5) return new VanillaLatteFragment();
        else if (index == 6) return new HazelnutLatteFragment();
        else if (index == 7) return new CaramelMacchiatoFragment();
        else if (index == 8) return new CafeMochaFragment();
        else if (index == 9) return new DolceLatteFragment();
        else return new NoCoffeeFragment();

    }

    public int getRealPosition(int position) {
        return position % mCount;
    }

    @Override
    public int getItemCount() {
        return 2000;
    }
}
