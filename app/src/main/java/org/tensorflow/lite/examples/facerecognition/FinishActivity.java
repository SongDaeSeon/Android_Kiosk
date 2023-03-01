package org.tensorflow.lite.examples.facerecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.widget.Button;

import java.util.Locale;

public class FinishActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private Button fin_btn;

    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        fin_btn = (Button) findViewById(R.id.fin_btn);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                fin_btn.setEnabled(true);
                String text = "결제가 완료 되었습니다.";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");

                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run() {
                        Intent intent = new Intent(FinishActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 10000);// 18초 정도 딜레이를 준 후 반응 없으면 main으로 돌아감
            }
        });
    }
}