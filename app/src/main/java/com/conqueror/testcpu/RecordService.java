package com.conqueror.testcpu;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class RecordService extends Service {
//    CPU温度：/sys/class/thermal/thermal_zone7/temp
//    AP温度：/sys/class/thermal/thermal_zone8/temp

    public static final String CPUPath = "/sys/class/thermal/thermal_zone7/temp";
    public static final String APath = "/sys/class/thermal/thermal_zone8/temp";
    private Timer timer;
//    private long t;

    public RecordService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        long time = System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss");
        Date d1 = new Date(time);
        final String t1 = format.format(d1);

        super.onCreate();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    recordFile(CPUPath, APath, "/storage/sdcard0/" + t1 + "record.txt");
//                    recordFile("/storage/sdcard0/one.txt", "/storage/sdcard0/two.txt", "/storage/sdcard0/" + t1 + "record.txt");
                    Log.e("john", "---sd----" + getInnerSDCardPath("www"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1 * 1000 * 60);
    }


    private void recordFile(String readPath1, String readPath2, String savePath) throws IOException {
        //字符缓冲输入流

//        BufferedReader bufferedReader = new BufferedReader(new FileReader("d://readme.txt"));
        BufferedReader bufferedReader = new BufferedReader(new FileReader(readPath1));
        BufferedReader bufferedReader2 = new BufferedReader(new FileReader(readPath2));

        //字符缓冲输出流
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(savePath, true));
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(savePath));

        String date = getDate();
        bufferedWriter.write(date);
        String line = null;

        //每次读取一行，如果读到null就停止读取文件内容

        while ((line = bufferedReader.readLine()) != null) {
            int v = ConvertUtil.convertToInt(line, 0);
            String s = v / 1000 + "℃";
            bufferedWriter.write(" CPU:" + s);
            bufferedWriter.flush();
        }
        String lin = null;
        while ((lin = bufferedReader2.readLine()) != null) {
            int v = ConvertUtil.convertToInt(lin, 0);
            String s = v / 1000 + "℃";
            bufferedWriter.write(" AP:" + s);
            bufferedWriter.flush();
        }

        bufferedWriter.newLine();

        //关闭流

        bufferedReader.close();
        bufferedReader2.close();
        bufferedWriter.close();

    }

    @NonNull
    private String getDate() {
        long time = System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
        Date d1 = new Date(time);
        return "[ " + format.format(d1) + " ]";
    }


    /**
     * 获取内置SD卡路径
     *
     * @return
     */
    public String getInnerSDCardPath(String filePath) {
        String path = Environment.getExternalStorageDirectory().getPath();

        return path + filePath;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
