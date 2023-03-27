package org.tensorflow.lite.examples.facerecognition.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.examples.facerecognition.R;
import org.tensorflow.lite.examples.facerecognition.TimerCount;

import java.util.Locale;

public class SelectIceHotActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "192.168.0.14";//ip주소 업데이트 계속 해줘야함
    private static String TAG = "phptest";

    private TextToSpeech tts;
    private Button button_ice;
    private Button button_hot;

    //일정 시간 터치 없을시 자동 처음 화면 돌아가기 위한 코드
    private int count = TimerCount.COUNT;
    private CountDownTimer countDownTimer;

    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ice_hot);

        //데이터받기
        Intent intent = getIntent();
        String menu = intent.getStringExtra("menu");
        String price = intent.getStringExtra("price");

        button_ice = (Button) findViewById(R.id.button_ice);
        button_hot = (Button) findViewById(R.id.button_hot);

        countDownTimer();
        countDownTimer.start();

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                button_ice.setEnabled(true);
                button_hot.setEnabled(true);
                String text = "아이스/ 핫을 선택해주세요. 아이스를 원하면 화면의 상단을, 핫을 원하면 하단을 클릭해주세요";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");
            }
        });
        button_ice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100); // 0.1초간 진동

                String text = "아이스";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");
            }
        });

        button_ice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent(SelectIceHotActivity.this, SelectModeActivity.class);
                intent.putExtra("temp", "아이스");
                intent.putExtra("menu", menu);
                intent.putExtra("price", price);
                startActivity(intent);
                finish();
                return true;  //true 설정
            }
        });

        button_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100); // 0.1초간 진동

                String text = "핫";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");
            }
        });

        button_hot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent(SelectIceHotActivity.this, SelectModeActivity.class);
                intent.putExtra("temp", "핫");
                intent.putExtra("menu", menu);
                intent.putExtra("price", price);
                startActivity(intent);
                finish();
                return true;  //true 설정
            }
        });
    }

    public void countDownTimer(){
        countDownTimer = new CountDownTimer(TimerCount.MILLISINFUTURE, TimerCount.COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                count --;
            }
            public void onFinish() {
                Intent intent = new Intent(SelectIceHotActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        };
    }

    @Override
    protected void onPause() {

        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }

        //일정 시간 터치 없을시 자동 처음 화면 돌아가기 위한 코드
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;

        super.onPause();
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