package org.tensorflow.lite.examples.facerecognition.fragments.RecommendFragment;

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

public class RecommendMenuFragment extends Fragment {

    private TextToSpeech tts;
    private Button recommend_menu_btn;
    private Vibrator vibrator;

    String temp;
    String menu ;
    String price;
    String content;

    private static final String TAG_NAME = "m_name";
    private static final String TAG_TEMP = "m_temp";
    private static final String TAG_PRICE = "m_price";

    public RecommendMenuFragment() {

    }

    public RecommendMenuFragment(String temp, String menu, String price) {
        this.temp = temp;
        this.menu = menu;
        this.price = price;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = (ViewGroup) inflater.inflate(R.layout.fragment_recommend_menu, container, false);
        recommend_menu_btn = v.findViewById(R.id.recommend_menu_btn);


        if(temp.equals("없음")) {
            content = menu + "\n" + price + "원";
            SpannableString spannableString = new SpannableString(content);

            // 2
            String word = price +"원";
            int start = content.indexOf(word);
            int end = start + word.length();

            // 보라색 컬러 들고오기
            int color = getActivity().getColor(R.color.purple);
            String purple = "#" + Integer.toHexString(color);

            // 3
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(purple)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(0.95f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

            // 4
            recommend_menu_btn.setText(spannableString);
        }else{

            content = temp + "\n"+ menu + "\n" + price + "원";
            SpannableString spannableString = new SpannableString(content);

            // 2
            String word = price +"원";
            int start = content.indexOf(word);
            int end = start + word.length();

            // 보라색 컬러 들고오기
            int color = getActivity().getColor(R.color.purple);
            String purple = "#" + Integer.toHexString(color);

            // 3
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(purple)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(0.95f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);


            // 4
            recommend_menu_btn.setText(spannableString);
        }

        recommend_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100); // 0.1초간 진동

                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);

                String text;
                if(temp.equals("없음")) {
                    text = menu + price + "원";
                    SpannableString spannableString = new SpannableString(content);

                }else{

                    text = temp + menu + price + "원";
                    SpannableString spannableString = new SpannableString(content);

                }

                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");
            }
        });

        recommend_menu_btn.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), SelectModeActivity.class);
                intent.putExtra("temp", temp);
                intent.putExtra("menu", menu);
                intent.putExtra("price", price);
                startActivity(intent);

                //activity의 finish() 부분
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(RecommendMenuFragment.this).commit();
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
                recommend_menu_btn.setEnabled(true);
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);

                String text;
                if(temp.equals("없음")) {
                    text = menu + price + "원";
                    SpannableString spannableString = new SpannableString(content);

                }else{

                    text = temp + menu + price + "원";
                    SpannableString spannableString = new SpannableString(content);

                }

                tts.speak("현재 화면은 "+text +"입니다.", TextToSpeech.QUEUE_FLUSH, null, "id1");
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

