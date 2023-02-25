package org.tensorflow.lite.examples.facerecognition.fragments.DrinkFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.tensorflow.lite.examples.facerecognition.CoffeeActivity;
import org.tensorflow.lite.examples.facerecognition.MainActivity;
import org.tensorflow.lite.examples.facerecognition.R;
import org.tensorflow.lite.examples.facerecognition.StartActivity;
import org.tensorflow.lite.examples.facerecognition.TimerCount;

import java.util.Locale;

public class CoffeeFragment extends Fragment {
    private TextToSpeech tts;
    private Button button3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = (ViewGroup) inflater.inflate(
                R.layout.fragment_coffee, container, false);

        button3 = v.findViewById(R.id.button3);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "커피";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");

            }
        });

        button3.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), CoffeeActivity.class);
                startActivity(intent);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(CoffeeFragment.this).commit();
                fragmentManager.popBackStack();

                return true;  //true 설정
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
                button3.setEnabled(true);
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);

                String text = "커피";
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
