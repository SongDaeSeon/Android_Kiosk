package org.tensorflow.lite.examples.facerecognition.fragments.DeleteFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.examples.facerecognition.DeleteActivity;
import org.tensorflow.lite.examples.facerecognition.PayActivity;
import org.tensorflow.lite.examples.facerecognition.R;
import org.tensorflow.lite.examples.facerecognition.SelectModeActivity;
import org.tensorflow.lite.examples.facerecognition.SelectWhereActivity;
import org.tensorflow.lite.examples.facerecognition.TimerCount;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;

public class BlankFragment extends Fragment {

    private TextToSpeech tts;
    private Button blank_btn;
    String mJsonString;
    String temp;
    String menu ;
    String price;

    private static String TAG = "phpquerytest";
    private static final String TAG_CHECK_COUNT  = "p_count";
    private static final String TAG_JSON = "cwnu";
    private static final String TAG_NAME = "m_name";
    private static final String TAG_TEMP = "m_temp";
    private static final String TAG_PRICE = "m_price";
    private static String p_count;

//    //테스트
//    TextView textView;

    public BlankFragment() {
    }

    public BlankFragment(String temp, String menu, String price) {
        this.temp = temp;
        this.menu = menu;
        this.price = price;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = (ViewGroup)inflater.inflate(R.layout.fragment_blank, container, false);

        blank_btn = v.findViewById(R.id.blank_btn);



        if(temp.equals("없음")) {
            String content = menu + "\n" + price + "원";
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
            blank_btn.setText(spannableString);
        }else{

            String content = temp + "\n"+ menu + "\n" + price + "원";
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
            blank_btn.setText(spannableString);
        }


        //테스트
//        textView = v.findViewById(R.id.ex);
//        textView.setMovementMethod(new ScrollingMovementMethod());

//        Toast.makeText(getActivity(),String.valueOf(TimerCount.DELETE_MENU_ARRAY.size()), Toast.LENGTH_SHORT).show();
        GetData task = new GetData();
        task.execute( TimerCount.starttime, temp, menu);


        blank_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = temp + menu + price;
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");

            }
        });

        blank_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                UpdateDownReceipt task5 = new UpdateDownReceipt();
                task5.execute( TimerCount.starttime, temp, menu);

//                Toast.makeText(getActivity(), menu,Toast.LENGTH_SHORT).show();

                //product 한개 남았으면 product 삭제로
                if(p_count.equals("1")){

                    DeleteProduct task3 = new DeleteProduct();
                    task3.execute("http://" + TimerCount.IP + "/delete_product.php", TimerCount.starttime, temp, menu);

                }else{

                    UpdateDownProduct task2 = new UpdateDownProduct();
                    task2.execute("http://" + TimerCount.IP + "/update_down_product.php", TimerCount.starttime, temp, menu);
                }


                if(TimerCount.R_COUNT.equals("1")){

                    Intent intent = new Intent(getActivity(), SelectWhereActivity.class);
                    startActivity(intent);

                }else{

                    Intent intent = new Intent(getActivity(), SelectModeActivity.class);
                    startActivity(intent);

                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(BlankFragment.this).commit();
                fragmentManager.popBackStack();

                return true;
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                blank_btn.setEnabled(true);
                Locale locale = Locale.getDefault();
                tts.setLanguage(locale);
                String text = temp + menu + price;
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

    class UpdateDownProduct extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getContext(),
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String r_time = (String)params[1];
            String m_temp = (String)params[2];
            String m_name = (String)params[3];

            String serverURL = (String)params[0];
            String postParameters = "r_time=" + r_time + "&m_temp=" + m_temp + "&m_name=" + m_name;


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

                Log.d(TAG, "UpdateDownProductData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

    class DeleteProduct extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getContext(),
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String r_time = (String)params[1];
            String m_temp = (String)params[2];
            String m_name = (String)params[3];

            String serverURL = (String)params[0];
            String postParameters = "r_time=" + r_time + "&m_temp=" + m_temp + "&m_name=" + m_name;


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

                Log.d(TAG, "DeleteProduct: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getContext(),
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

            }


        }


        @Override
        protected String doInBackground(String... params) {

            String r_time = params[0];
            String m_temp = params[1];
            String m_menu = params[2];


            String serverURL = "http://"+TimerCount.IP+"/select_count_product.php";
            String postParameters = "r_time=" + r_time + "&m_temp=" + m_temp + "&m_menu=" + m_menu;


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

                JSONObject item = jsonArray.getJSONObject(i);


                p_count = item.getString(TAG_CHECK_COUNT);

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put(TAG_CHECK_COUNT, p_count);

            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    class UpdateDownReceipt extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getContext(),
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String r_time = params[0];
            String m_temp = params[1];
            String m_name = params[2];

            String serverURL = "http://"+TimerCount.IP+"/update_down_receipt.php";
            String postParameters = "r_time=" + r_time + "&m_temp=" + m_temp + "&m_name=" + m_name;


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

                Log.d(TAG, "UpdateDownReceipt: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
}