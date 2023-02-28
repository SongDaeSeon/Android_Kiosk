package org.tensorflow.lite.examples.facerecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class BasketActivity extends AppCompatActivity {
    private TextToSpeech tts;
    private Button basket_btn;

    private int count = TimerCount.COUNT;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        basket_btn = (Button) findViewById(R.id.basket_btn);

        countDownTimer();
        countDownTimer.start();

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                basket_btn.setEnabled(true);
                String text = "주문목록입니다.";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");

                }
        });

        basket_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "테스트입니다";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");

            }
        });

        basket_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(BasketActivity.this, SelectModeActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        });
    }

    //일정 시간 터치 없을시 자동 처음 화면 돌아가기 위한 코드
    public void countDownTimer(){

        countDownTimer = new CountDownTimer(TimerCount.MILLISINFUTURE, TimerCount.COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                count --;
            }
            public void onFinish() {
                Intent intent = new Intent(BasketActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        };
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        //일정 시간 터치 없을시 자동 처음 화면 돌아가기 위한 코드
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;

        super.onDestroy();
    }
}