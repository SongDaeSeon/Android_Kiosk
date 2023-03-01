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

import org.tensorflow.lite.examples.facerecognition.R;
import org.tensorflow.lite.examples.facerecognition.SelectDrinkActivity;
import org.tensorflow.lite.examples.facerecognition.fragments.SmoothieMenuFragment.NoSmoothieFragment;

import java.util.Locale;


public class AddOrderFragment extends Fragment {

    private TextToSpeech tts;
    private Button add_order_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = (ViewGroup) inflater.inflate(
                R.layout.fragment_add_order, container, false);

        add_order_btn = v.findViewById(R.id.add_order_btn);

        add_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "추가주문";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");

            }
        });

        add_order_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent(getActivity(), SelectDrinkActivity.class);
                startActivity(intent);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(AddOrderFragment.this).commit();
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
                add_order_btn.setEnabled(true);
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                String text = "추가주문";
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