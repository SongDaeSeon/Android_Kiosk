package org.tensorflow.lite.examples.facerecognition.fragments.DrinkFragment;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.tensorflow.lite.examples.facerecognition.AdeActivity;
import org.tensorflow.lite.examples.facerecognition.R;
import org.tensorflow.lite.examples.facerecognition.SelectWhereActivity;

import java.util.Locale;

public class AdeFragment extends Fragment {
    private TextToSpeech tts;
    private Button button7;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = (ViewGroup) inflater.inflate(
                R.layout.fragment_ade, container, false);

        button7 = v.findViewById(R.id.button7);

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "에이드";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");
            }
        });

        button7.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), AdeActivity.class);
                startActivity(intent);

                //activity의 finish() 부분
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(AdeFragment.this).commit();
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
                button7.setEnabled(true);
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);

                String text = "에이드";
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
    }
}
