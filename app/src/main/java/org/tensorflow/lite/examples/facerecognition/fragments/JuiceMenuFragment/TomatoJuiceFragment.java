package org.tensorflow.lite.examples.facerecognition.fragments.JuiceMenuFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.tensorflow.lite.examples.facerecognition.R;
import org.tensorflow.lite.examples.facerecognition.activity.SelectModeActivity;

import java.util.Locale;

public class TomatoJuiceFragment extends Fragment {
    private TextToSpeech tts;
    private Button tomato_juice_btn;
    private Vibrator vibrator;

    String text = "토마토주스 3300원";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = (ViewGroup) inflater.inflate(R.layout.fragment_tomato_juice, container, false);

        tomato_juice_btn = v.findViewById(R.id.tomato_juice_btn);

        tomato_juice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100); // 0.1초간 진동

                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");
            }
        });
        tomato_juice_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(getActivity(), SelectModeActivity.class);
                intent.putExtra("menu", "토마토주스");
                intent.putExtra("price", "3300");
                intent.putExtra("temp", "아이스");
                startActivity(intent);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(TomatoJuiceFragment.this).commit();
                fragmentManager.popBackStack();

                return true;
            }
        });
        String content = tomato_juice_btn.getText().toString();
        SpannableString spannableString = new SpannableString(content);

        // 2
        String word = "주스";
        int start = content.indexOf(word);
        int end = start + word.length();

        // 보라색 컬러 들고오기
        int color = getActivity().getColor(R.color.gray);
        String purple = "#" + Integer.toHexString(color);

        // 3
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(purple)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(0.55f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 4
        tomato_juice_btn.setText(spannableString);

        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tomato_juice_btn.setEnabled(true);
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);

                String text = "토마토주스 3300원";
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
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