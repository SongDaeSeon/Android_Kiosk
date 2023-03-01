package org.tensorflow.lite.examples.facerecognition.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.tensorflow.lite.examples.facerecognition.fragments.DrinkFragment.AdeFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DrinkFragment.CoffeeFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DrinkFragment.DesertFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DrinkFragment.JuiceFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DrinkFragment.LatteFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DrinkFragment.SmoothieFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DrinkFragment.TeaFragment;


public class Myadapter extends FragmentStateAdapter {

    public int mCount;

    public Myadapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index==0) return new CoffeeFragment();
        else if(index==1) return new LatteFragment();
        else if(index==2) return new SmoothieFragment();
        else if(index==3) return new TeaFragment();
        else if(index==4) return new AdeFragment();
        else if(index==5) return new JuiceFragment();
        else return new DesertFragment();
    }

    @Override
    public int getItemCount() {
        return 2000;
    }

    public int getRealPosition(int position) { return position % mCount; }


}
