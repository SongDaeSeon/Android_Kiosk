package org.tensorflow.lite.examples.facerecognition.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.examples.facerecognition.R;
import org.tensorflow.lite.examples.facerecognition.TimerCount;

import java.io.IOException;
import java.util.Locale;

public class StartActivity extends AppCompatActivity {
    private TextToSpeech tts;              // TTS 변수 선언
    private Button button0;

    private MediaRecorder mRecorder;
    private AudioManager audioManager;
    private static double mEMA = 0.0;

    double amp;


    //일정 시간 터치 없을시 자동 처음 화면 돌아가기 위한 코드
    private int count = TimerCount.COUNT;
    private CountDownTimer countDownTimer;
    private Vibrator vibrator;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        button0 = (Button) findViewById(R.id.button0);

        countDownTimer();
        countDownTimer.start();

        // AudioManager 객체 생성
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // MediaRecorder 객체 생성
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setOutputFile("/dev/null");

        // MediaRecorder 시작
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 데시벨 측정 쓰레드 실행
        Runnable runnable = new Runnable() {
            public void run() {
                while (true) {

                    amp = getAmplitudeEMA();
                    setVolume(amp);
                    amp = Math.ceil(amp);

                    TimerCount.AMP = amp;

                    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, ((int) amp / 1500) + 10, 0);

                    try {
                        Thread.sleep(7000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };new Thread(runnable).start();

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                button0.setEnabled(true);
                String text = "음료를 주문하는 무인 기기입니다. 주문 시 화면을 클릭하면 음성을 다시 들을 수 있고 길게 누르면 다음 단계로 넘어갑니다. 이해하셨으면 길게 눌러  주문을 진행해주세요";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");
//
//                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, 0);

                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run() {
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 40000);//40초 정도 딜레이를 준 후 반응 없으면 main으로 돌아감
            }
        });



        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100); // 0.1초간 진동
                String text = "음료를 주문하는 무인 기기입니다. 주문 시 화면을 클릭하면 음성을 다시 들을 수 있고 길게 누르면 다음 단계로 넘어갑니다. 이해하셨으면 길게 눌러  주문을 진행해주세요";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");

            }
        });

        button0.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {

                handler.removeMessages(0);
                Intent intent = new Intent(StartActivity.this, SelectWhereActivity.class);
                startActivity(intent);
                finish();
                return true;  //true 설정
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
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
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

    // 데시벨 값 계산 함수
    public double getAmplitude() {
        if (mRecorder != null) {
            return mRecorder.getMaxAmplitude();
        }
        else {
            return 0;
        }
    }

    // 평균 데시벨 값 계산 함수
    public double getAmplitudeEMA() {
        double amp = getAmplitude();
        mEMA = 0.6 * amp + (1.0 - 0.6) * mEMA;
        return mEMA;
    }

    // 음량 조절 함수
    private void setVolume(double db) {
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        double percentage = db / 100;
        int volume = (int) (percentage * maxVolume);

        if (volume > maxVolume) {
            volume = maxVolume;
        } else if (volume < 0) {
            volume = 0;
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
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
    protected void onStop() {
        super.onStop();
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

}