package org.tensorflow.lite.examples.facerecognition;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.facerecognition.Adapter.SelectModeAdapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import me.relex.circleindicator.CircleIndicator3;

public class SelectModeActivity extends FragmentActivity {

    private static String TAG = "phpquerytest";
    private static final String TAG_JSON = "cwnu";
    private static final String TAG_CHECK_COUNT  = "p_count";
    private String check_count ; // p_count 저장할 전역변수

//    //테스트
//    TextView textView;

    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 4;
    private CircleIndicator3 mIndicator;
    String mJsonString;


    //일정 시간 터치 없을시 자동 처음 화면 돌아가기 위한 코드
    private int count = TimerCount.COUNT;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);

        Intent intent = getIntent();
        String temp = intent.getStringExtra("temp");
        String menu = intent.getStringExtra("menu");
        String price = intent.getStringExtra("price");

//        Toast.makeText(SelectModeActivity.this,temp,Toast.LENGTH_SHORT).show();
//        Toast.makeText(SelectModeActivity.this,menu,Toast.LENGTH_SHORT).show();
//        Toast.makeText(SelectModeActivity.this,price,Toast.LENGTH_SHORT).show();

//        //테스트
//        textView = findViewById(R.id.ex);
//        textView.setMovementMethod(new ScrollingMovementMethod());

        //ViewPager2
        mPager = findViewById(R.id.select_mode_viewpager);
        //Adapter
        pagerAdapter = new SelectModeAdapter(this, num_page);
        mPager.setAdapter(pagerAdapter);
        //Indicator
        mIndicator = findViewById(R.id.select_mode_indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.createIndicators(num_page,0);
        //ViewPager Setting
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        mPager.setCurrentItem(1000,false);
        mPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);

        countDownTimer();
        countDownTimer.start();

        //여기에 php문 연동하는 거 넣기
        if(temp != null){
            GetData task = new GetData();
            task.execute( TimerCount.starttime, temp, menu);


//                Toast.makeText(SelectModeActivity.this,check_count,Toast.LENGTH_SHORT).show();


        }

        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mIndicator.animatePageSelected(position%num_page);
            }

        });

        final float pageMargin= getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        final float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);

        mPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float myOffset = position * -(2 * pageOffset + pageMargin);
                if (mPager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(mPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.setTranslationX(-myOffset);
                    } else {
                        page.setTranslationX(myOffset);
                    }
                } else {
                    page.setTranslationY(myOffset);
                }
            }
        });
    }

    public void countDownTimer(){
        countDownTimer = new CountDownTimer(TimerCount.MILLISINFUTURE, TimerCount.COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                count --;
            }
            public void onFinish() {
                Intent intent = new Intent(SelectModeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        };
    }


    @Override
    protected void onPause() {

        //일정 시간 터치 없을시 자동 처음 화면 돌아가기 위한 코드
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //일정 시간 터치 없을시 자동 처음 화면 돌아가기 위한 코드
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;

        super.onDestroy();
    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SelectModeActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d(TAG, "response - " + result);

            if (result == null){

                mJsonString = errorString;

            }
            else {

                mJsonString = result;
                showResult();
//                textView.setText(result);
            }


        }


        @Override
        protected String doInBackground(String... params) {

            String r_time = params[0];
            String m_temp = params[1];
            String m_menu = params[2];


            String serverURL = "http://"+TimerCount.IP+"/select_count_product.php";
            String postParameters = "r_time=" + r_time + "&m_temp=" + m_temp + "&m_menu" + m_menu;


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

                Log.d(TAG, "select_count_product: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++) {
                //p_count 값 하나만 들고오기 때문에 반복문 지움
                JSONObject item = jsonArray.getJSONObject(i);


                String p_count = item.getString(TAG_CHECK_COUNT);

                check_count = p_count;

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put(TAG_CHECK_COUNT, p_count);
            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }


}