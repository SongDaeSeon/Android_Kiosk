package org.tensorflow.lite.examples.facerecognition.fragments.SmoothieMenuFragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.tensorflow.lite.examples.facerecognition.CoffeeActivity;
import org.tensorflow.lite.examples.facerecognition.R;
import org.tensorflow.lite.examples.facerecognition.SelectDrinkActivity;
import org.tensorflow.lite.examples.facerecognition.SelectWhereActivity;
import org.tensorflow.lite.examples.facerecognition.fragments.DrinkFragment.CoffeeFragment;

import java.util.Locale;

public class NoSmoothieFragment extends Fragment {
    private TextToSpeech tts;
    private Button nosmoothie_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = (ViewGroup) inflater.inflate(R.layout.fragment_no_smoothie, container, false);

        nosmoothie_btn = v.findViewById(R.id.nosmoothie_btn);

        nosmoothie_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "선택안함";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");
            }
        });

        nosmoothie_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(getActivity(), SelectDrinkActivity.class);
                startActivity(intent);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(NoSmoothieFragment.this).commit();
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
            public void onInit(int status) {
                nosmoothie_btn.setEnabled(true);
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);

                String text = "선택안함";
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