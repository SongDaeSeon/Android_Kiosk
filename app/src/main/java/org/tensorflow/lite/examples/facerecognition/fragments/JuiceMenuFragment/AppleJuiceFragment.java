package org.tensorflow.lite.examples.facerecognition.fragments.JuiceMenuFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.tensorflow.lite.examples.facerecognition.R;
import org.tensorflow.lite.examples.facerecognition.SelectModeActivity;
import org.tensorflow.lite.examples.facerecognition.SelectWhereActivity;
import org.tensorflow.lite.examples.facerecognition.fragments.AdeMenuFragment.GrapefruitAdeFragment;

import java.util.Locale;


public class AppleJuiceFragment extends Fragment {
    private TextToSpeech tts;
    private Button apple_juice_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = (ViewGroup) inflater.inflate(R.layout.fragment_apple_juice, container, false);

        apple_juice_btn = v.findViewById(R.id.apple_juice_btn);

        apple_juice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "애플주스 3000원";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");
            }
        });

        apple_juice_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(getActivity(), SelectModeActivity.class);
                startActivity(intent);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(AppleJuiceFragment.this).commit();
                fragmentManager.popBackStack();

                return true;
            }
        });

        String content = apple_juice_btn.getText().toString();
        SpannableString spannableString = new SpannableString(content);

        // 2
        String word = "3000원";
        int start = content.indexOf(word);
        int end = start + word.length();

        // 보라색 컬러 들고오기
        int color = getActivity().getColor(R.color.purple);
        String purple = "#" + Integer.toHexString(color);

        // 3
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(purple)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(0.95f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 4
        apple_juice_btn.setText(spannableString);

        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                apple_juice_btn.setEnabled(true);
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);

                String text = "애플주스 3000원";
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