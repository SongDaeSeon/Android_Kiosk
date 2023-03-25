package org.tensorflow.lite.examples.facerecognition.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.facerecognition.R;
import org.tensorflow.lite.examples.facerecognition.TimerCount;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SelectWhereActivity extends AppCompatActivity {

    private static String IP_ADDRESS = TimerCount.IP;//ip주소 업데이트 계속 해줘야함
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON = "cwnu";
    private static final String TAG_NAME = "m_name";
    private static final String TAG_TEMP = "m_temp";
    private static final String TAG_PRICE = "m_price";
    String mJsonString;

    //    //테스트
    TextView textView;

    ArrayList<HashMap<String, String>> mArrayList;

    private TextToSpeech tts;
    private Button button1;
    private Button button2;
    private Vibrator vibrator;

    //디비 관련 날짜
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    //디비 변수
    String r_count = "0";
    String r_price = "0";
    String r_time;
    String r_where;

    //일정 시간 터치 없을시 자동 처음 화면 돌아가기 위한 코드
    private int count = TimerCount.COUNT;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_where);

        //        //테스트
        textView = findViewById(R.id.ex);
        textView.setMovementMethod(new ScrollingMovementMethod());

        mArrayList = new ArrayList<>();

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);

        countDownTimer();
        countDownTimer.start();

        //디비 관련 날짜
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        r_time = mFormat.format(mDate);
        TimerCount.starttime = r_time;

        SelectRecommendMenu task1 = new SelectRecommendMenu();
        task1.execute(r_time);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                button1.setEnabled(true);
                button2.setEnabled(true);
                String text = "포장/매장을 선택해주세요. 포장을 원하면 화면의 상단을, 매장을 원하면 하단을 클릭해주세요";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");

//                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, 0);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100); // 0.1초간 진동

                //일정 시간 터치 없을시 자동 처음 화면 돌아가기 위한 코드
                countDownTimer.cancel();
                countDownTimer.start();
                String text = "포장";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");
            }
        });

        //포장
        button1.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {

                r_where = "포장";
                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/insert_receipt.php", r_count, r_price, r_time, r_where);

                Intent intent = new Intent(SelectWhereActivity.this, SelectDrinkActivity.class);
                startActivity(intent);
                finish();
                return true;  //true 설정
            }
        });

        //매장
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100); // 0.1초간 진동

                //일정 시간 터치 없을시 자동 처음 화면 돌아가기 위한 코드
                countDownTimer.cancel();
                countDownTimer.start();
                String text = "매장";
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");
            }
        });

        button2.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {

                r_where = "매장";
                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/insert_receipt.php", r_count, r_price, r_time, r_where);

                Intent intent = new Intent(SelectWhereActivity.this, SelectDrinkActivity.class);
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
                Intent intent = new Intent(SelectWhereActivity.this, MainActivity.class);
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

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SelectWhereActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

        }


        @Override
        protected String doInBackground( String ... params) {

            String r_count = (String)params[1];
            String r_price = (String)params[2];
            String r_time = (String)params[3];
            String r_where = (String)params[4];

            String serverURL = (String)params[0];
            String postParameters = "r_count=" + r_count + "&r_price=" + r_price + "&r_time=" + r_time + "&r_where=" + r_where;

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

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
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

    private class SelectRecommendMenu extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SelectWhereActivity.this,
                    "Please Wait", null, true, true);
        }


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
//                textView.setText(result);
                showRecommend();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String r_time = params[0];

            String serverURL = "http://"+TimerCount.IP+"/select_recommend_menu.php";
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

                Log.d(TAG, "SelectRecommendMenu: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private void showRecommend(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String m_temp = item.getString(TAG_TEMP);
                String m_name = item.getString(TAG_NAME);
                String m_price = item.getString(TAG_PRICE);

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put(TAG_TEMP, m_temp);
                hashMap.put(TAG_NAME, m_name);
                hashMap.put(TAG_PRICE, m_price);

                mArrayList.add(hashMap);

            }

            TimerCount.RECOMMEND_MENU_ARRAY = mArrayList;


        } catch (JSONException e) {

            Log.d(TAG, "showDelete : ", e);
        }

    }
}