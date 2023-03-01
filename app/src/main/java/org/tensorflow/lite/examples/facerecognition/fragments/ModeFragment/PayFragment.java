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

import org.tensorflow.lite.examples.facerecognition.PayActivity;
import org.tensorflow.lite.examples.facerecognition.R;
import org.tensorflow.lite.examples.facerecognition.SelectModeActivity;
import org.tensorflow.lite.examples.facerecognition.fragments.JuiceMenuFragment.AppleJuiceFragment;

import java.util.Locale;

public class PayFragment extends Fragment {

    private TextToSpeech tts;
    private Button pay_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = (ViewGroup)inflater.inflate(R.layout.fragment_pay, container, false);

        pay_btn = v.findViewById(R.id.pay_btn);

        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "결제";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");

            }
        });

        pay_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(getActivity(), PayActivity.class);
                startActivity(intent);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(PayFragment.this).commit();
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
                pay_btn.setEnabled(true);
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                String text = "결제";
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