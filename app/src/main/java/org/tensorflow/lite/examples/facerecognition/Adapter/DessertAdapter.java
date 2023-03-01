package org.tensorflow.lite.examples.facerecognition.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.tensorflow.lite.examples.facerecognition.fragments.DessertMenuFragment.BlueberryMuffinFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DessertMenuFragment.CheeseMuffinFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DessertMenuFragment.CheeseSconeFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DessertMenuFragment.ChocoMacaronFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DessertMenuFragment.ChocoMousseCakeFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DessertMenuFragment.ChocoMuffinFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DessertMenuFragment.ChocoTiramisuCakeFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DessertMenuFragment.CranberrySconeFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DessertMenuFragment.CreamCheeseBagelFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DessertMenuFragment.CroquemonsieurFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DessertMenuFragment.MochaBunFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DessertMenuFragment.NewYorkCheeseCakeFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DessertMenuFragment.NoDessertFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DessertMenuFragment.PlainMuffinFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DessertMenuFragment.RaspberryMacaronFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.DessertMenuFragment.VanillaMacaronFragment;

public class DessertAdapter extends FragmentStateAdapter {

    public int mCount;

    public DessertAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        int index = getRealPosition(position);

        if(index == 0) return new CroquemonsieurFragment();
        else if (index == 1) return new CreamCheeseBagelFragment();
        else if (index == 2) return new ChocoMuffinFragment();
        else if (index == 3) return new PlainMuffinFragment();
        else if (index == 4) return new BlueberryMuffinFragment();
        else if (index == 5) return new CheeseMuffinFragment();
        else if (index == 6) return new MochaBunFragment();
        else if (index == 7) return new CranberrySconeFragment();
        else if (index == 8) return new CheeseSconeFragment();
        else if (index == 9) return new RaspberryMacaronFragment();
        else if (index == 10) return new VanillaMacaronFragment();
        else if (index == 11) return new ChocoMacaronFragment();
        else if (index == 12) return new ChocoTiramisuCakeFragment();
        else if (index == 13) return new ChocoMousseCakeFragment();
        else if (index == 14) return new NewYorkCheeseCakeFragment();
        else return new NoDessertFragment();

    }

    private int getRealPosition(int position) {
        return position % mCount;
    }

    @Override
    public int getItemCount() {
        return 2000;
    }
}
