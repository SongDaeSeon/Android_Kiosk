package org.tensorflow.lite.examples.facerecognition.Adapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.tensorflow.lite.examples.facerecognition.TimerCount;
import org.tensorflow.lite.examples.facerecognition.fragments.RecommendFragment.NoRecommendFragment;
import org.tensorflow.lite.examples.facerecognition.fragments.RecommendFragment.RecommendMenuFragment;

public class RecommendAdapter extends FragmentStateAdapter {

    private static final String TAG_NAME = "m_name";
    private static final String TAG_TEMP = "m_temp";
    private static final String TAG_PRICE = "m_price";
    public int mCount;

    public RecommendAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        int index = getRealPosition(position);

        for(int i=0; i<mCount-1; i++){

            if(index == i) return new RecommendMenuFragment(
                    //추천 메뉴에서 긁어와야 해서 수정해야함
                    TimerCount.RECOMMEND_MENU_ARRAY.get(i).get(TAG_TEMP),
                    TimerCount.RECOMMEND_MENU_ARRAY.get(i).get(TAG_NAME),
                    TimerCount.RECOMMEND_MENU_ARRAY.get(i).get(TAG_PRICE));

        }
       return new NoRecommendFragment();
    }

    private int getRealPosition(int position) {
        return position % mCount;
    }


    @Override
    public int getItemCount() {
        return 2000;
    }
}
