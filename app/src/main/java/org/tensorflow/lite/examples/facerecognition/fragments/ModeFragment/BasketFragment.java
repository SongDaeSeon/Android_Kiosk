package org.tensorflow.lite.examples.facerecognition.fragments.ModeFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.tensorflow.lite.examples.facerecognition.BasketActivity;
import org.tensorflow.lite.examples.facerecognition.R;
import org.tensorflow.lite.examples.facerecognition.SelectDrinkActivity;

import java.util.Locale;

public class BasketFragment extends Fragment {

    private TextToSpeech tts;
    private Button basket_btn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = (ViewGroup)inflater.inflate(R.layout.fragment_basket, container, false);

        basket_btn = v.findViewById(R.id.basket_btn);

        basket_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "장바구니";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");

            }
        });

        basket_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent(getActivity(), BasketActivity.class);
                startActivity(intent);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(BasketFragment.this).commit();
                fragmentManager.popBackStack();
                return true;
            }
        });



        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                basket_btn.setEnabled(true);
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                String text = "장바구니";
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        super.onDestroy();
    }
}