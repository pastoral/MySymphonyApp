package aboutdevice.com.munir.symphony.mysymphony.utils;

import android.app.ActivityManager;


import android.content.Context;
import android.hardware.camera2.*;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.util.SizeF;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aboutdevice.com.munir.symphony.mysymphony.model.CameraSensorList;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.CAMERA_SERVICE;

/**
 * Created by munirul.hoque on 1/26/2017.
 */

public class SpecException {

    public String[] exceptionSpec = new String[4];
    public String cam1, cam2;
    CameraSensorList cameraSensorList = new CameraSensorList();
    String backCamPixel, frontCamPixel;
    public double getTotalRAM() {

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();
            String temp = load;
             //Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                 System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        //return lastValue;
        return totRam;
    }

    public void setRAM(double ramSize){
        if(ramSize > 454000){
            exceptionSpec[0] = "512MB RAM";
        }
        if(ramSize > 700000 && ramSize < 1200000){
            exceptionSpec[0] = "1GB RAM";
        }
        if(ramSize > 1700000 && ramSize < 2300000){
            exceptionSpec[0] = "2GB RAM";
        }
        if(ramSize > 2500000 && ramSize <3300000){
            exceptionSpec[0] = "3GB RAM";
        }
        if(ramSize > 3500000 && ramSize <4300000){
            exceptionSpec[0] = "4GB RAM";
        }
    }

    public long getTotalROM() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        long size = blockSize * totalBlocks;
        if (size >= 1024) {
            size /= 1024;
            if (size >= 1024) {
                size /= 1024;
            }
        }
        return size;
    }

    public void setROM(long size){

        if(size > 1400 && size < 2000){
            exceptionSpec[1] = "4GB ROM";
        }
        if(size > 3200 && size < 7000){
            exceptionSpec[1] = "8GB ROM";
        }
        if(size > 7000 && size <23000 ){
            exceptionSpec[1] = "16GB ROM";
        }
        if(size > 23000 && size <52000 ){
            exceptionSpec[1] = "32GB ROM";
        }
        if(size > 52000 && size <118000 ){
            exceptionSpec[1] = "64GB ROM";
        }
        if(size > 118000 ){
            exceptionSpec[1] = "128GB ROM";
        }
    }

    public void getCameraPixel(){
        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        int camIndex[];
        int j =0 ;
        try {
            reader = new RandomAccessFile("/proc/driver/camera_info", "r");
            load = reader.readLine();
            camIndex = new int[5];
            String load1 = load.replace("[","");
            String load2 = load1.replace("]","");
            String load3 = load2.replace("[OFILM]","");
            String load4 = load3.replace(" ","");
            String lookFor = ":";
            for (int i = -1; (i = load4.indexOf(lookFor, i + 1)) != -1; ){
                camIndex[j] = i;
                j++;
            }
            cam1 = Character.toString(load4.charAt(camIndex[0]+1))+ Character.toString(load4.charAt(camIndex[0]+2))
                    +Character.toString(load4.charAt(camIndex[0]+3))+ Character.toString(load4.charAt(camIndex[0]+4))
                    +Character.toString(load4.charAt(camIndex[0]+5))+ Character.toString(load4.charAt(camIndex[0]+6));
            cam2 = Character.toString(load4.charAt(camIndex[1]+1))+ Character.toString(load4.charAt(camIndex[1]+2))
                    +Character.toString(load4.charAt(camIndex[1]+3))+ Character.toString(load4.charAt(camIndex[1]+4))
                    +Character.toString(load4.charAt(camIndex[1]+5))+ Character.toString(load4.charAt(camIndex[1]+6));
            reader.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {

            // Streams.close(reader);
        }

    }

    public void setCameraPixel(String cam1, String cam2) {
        if(cameraSensorList.sensorList.containsKey(cam1)&& cameraSensorList.sensorList.containsKey(cam2)){
            backCamPixel=cameraSensorList.sensorList.get(cam1);
            frontCamPixel=cameraSensorList.sensorList.get(cam2);
            exceptionSpec[2] = backCamPixel + "  Back Camera  " + "\n" + frontCamPixel + " Front Camera  ";
        }

        else{
            exceptionSpec[2] = "Camera sensor data missing";
        }

    }

}
