package org.tensorflow.lite.examples.facerecognition;

import java.util.ArrayList;
import java.util.HashMap;

public class TimerCount {

    //일단 30초 했다
    public static final int MILLISINFUTURE = 120 * 1000;
    public static final int COUNT_DOWN_INTERVAL = 1000;//1초
    public static final int COUNT = 119 * 1000;
    public static int ORDER_COUNT = 1;
    public static final String IP = "172.30.1.58";
    public static String starttime;
    public static String R_COUNT;
    public static String NUM_PAGE;
    public static double AMP = 12.0;

    public static ArrayList<HashMap<String, String>> DELETE_MENU_ARRAY =  new ArrayList<HashMap<String, String>>();
    public static ArrayList<HashMap<String, String>> RECOMMEND_MENU_ARRAY =  new ArrayList<HashMap<String, String>>();

}
