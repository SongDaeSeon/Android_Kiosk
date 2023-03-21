package org.tensorflow.lite.examples.facerecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class CheckActivity extends AppCompatActivity {

    private static String TAG = "phpquerytest";
    private static final String TAG_JSON = "cwnu";

    ArrayList<HashMap<String, String>> mArrayList;

    TextView textView;

    private static final String TAG_WHERE = "r_where";
    private static final String TAG_TEMP = "m_temp";
    private static final String TAG_NAME = "m_name";
    private static final String TAG_COUNT = "p_count";
    private static final String TAG_TOTAL = "total_price";
    private static final String TAG_REAL_TOTAL = "real_total_price";
    String mJsonString;

    private TextToSpeech tts;
    private TextToSpeech tts2;
    private Button pay_btn;
    private Button cancel_btn;

    //일정 시간 터치 없을시 자동 처음 화면 돌아가기 위한 코드
    private int count = TimerCount.COUNT;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

//        //        //테스트
        textView = findViewById(R.id.ex);
        textView.setMovementMethod(new ScrollingMovementMethod());

        pay_btn = (Button) findViewById(R.id.pay_btn);
        cancel_btn = (Button) findViewById(R.id.cancel_btn);

        countDownTimer();
        countDownTimer.start();

        mArrayList = new ArrayList<>();


        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                pay_btn.setEnabled(true);
                cancel_btn.setEnabled(true);
                String text = "주문내역 확인 후 결제 취소를 선택해주세요";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_ADD, null, "id1");

            }
        });


        SelectCheck task = new SelectCheck();
        task.execute( TimerCount.starttime);

        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = "주문내역";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");
            }
        });

        pay_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent(CheckActivity.this, PayActivity.class);
                startActivity(intent);
                finish();
                return true;  //true 설정
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = "취소";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");
            }
        });

        cancel_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent(CheckActivity.this, SelectModeActivity.class);
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
                Intent intent = new Intent(CheckActivity.this, MainActivity.class);
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

    private class SelectCheck extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(CheckActivity.this,
                    "Please Wait", null, true, true);
        }


        //여기서 텍스트 뷰 내용 붙이기 구현
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d(TAG, "response - " + result);

            if (result == null){

//                mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                textView.setText(result);
                showResult();

            }
        }

        @Override
        protected String doInBackground(String... params) {

            String r_time = params[0];

            String serverURL = "http://"+TimerCount.IP+"/select_check.php";
            String postParameters = "r_time=" + r_time ;

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();

            } catch (Exception e) {

                Log.d(TAG, "SelectCheck: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    Locale locale = Locale.getDefault();
                    tts.setLanguage(locale);

                    try {
                        JSONObject jsonObject = new JSONObject(mJsonString);
                        JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject item = jsonArray.getJSONObject(i);
                                String r_where = item.getString(TAG_WHERE);
                                String m_temp = item.getString(TAG_TEMP);
                                String m_name = item.getString(TAG_NAME);
                                String p_count = item.getString(TAG_COUNT);
                                String total_price = item.getString(TAG_TOTAL);
                                String real_total_price = item.getString(TAG_REAL_TOTAL);

                                String text = "온도는" + m_temp + "이고 메뉴명은" + m_name + "이고 메뉴 갯수는" + Integer.valueOf(p_count) + "개 입니다.";
                                tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put(TAG_WHERE, r_where);
                                hashMap.put(TAG_TEMP, m_temp);
                                hashMap.put(TAG_NAME, m_name);
                                hashMap.put(TAG_COUNT, p_count);
                                hashMap.put(TAG_TOTAL, total_price);
                                hashMap.put(TAG_REAL_TOTAL, real_total_price);
                                mArrayList.add(hashMap);

                                if(i==jsonArray.length()-1){

                                    tts2 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                                        @Override
                                        public void onInit(int status) {
                                            String text = "포장/매장 중 " + r_where + "이고 총가격은 "+ real_total_price +"입니다.";
                                            Locale locale = Locale.getDefault();
                                            tts2.setLanguage(locale);
                                            tts2.speak(text, TextToSpeech.QUEUE_ADD, null, null);

                                        }
                                    });

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}