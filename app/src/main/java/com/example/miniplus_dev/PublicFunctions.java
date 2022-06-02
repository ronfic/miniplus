package com.example.miniplus_dev;


import android.util.Log;

import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.List;

public class PublicFunctions {

    public static void clearUserData(){
        PublicValues.userId = "";
        PublicValues.userSecondId = "";
        PublicValues.userName = "Guest";
        PublicValues.location = "";
        PublicValues.userGender = "Male";
        PublicValues.userHeight = "";
        PublicValues.userWeight = "";
        PublicValues.userBirth = "";
        PublicValues.userClub = "";
        PublicValues.director = "0";
        PublicValues.userAge = 0;
    }


    public double calculateAverage(List<Double> mValue) {
        Double sum = 0.0;
        if (!mValue.isEmpty()) {
            for (Double mark : mValue) {
                sum += mark;
            }
            return sum.doubleValue() / mValue.size();
        }
        return sum;
    }

    public double calculateMax(List<Double> mValue) {
        Double max = 0.0;
        if (!mValue.isEmpty()) {
            for (Double mark : mValue) {
                if(mark > max)
                    max = mark;
            }
            return max;
        }
        return max;
    }

    private static byte[] intTobyte(int integer, ByteOrder order) {

        ByteBuffer buff = ByteBuffer.allocate(Integer.SIZE / 8);
        buff.order(order);
        buff.putInt(integer);

        return buff.array();
    }

    public static String intToOneByte(int value) {
        byte[] ByteValue = intTobyte((value), ByteOrder.LITTLE_ENDIAN);
        String convertValue = "00";
        if (intTobyte((value), ByteOrder.LITTLE_ENDIAN).length >= 2)
            convertValue = String.format("%02X", ByteValue[0]);
        return convertValue;
    }

    public static byte[] hexStrToByteArr(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
            //Log.i("TT", "" + i + " == " + data[i / 2]);
        }
        return data;
    }


    public static byte makeChkSum(byte[] data) {
        byte bChk = 0;
        for (int i = 3; i <= 17; i++) {
            bChk ^= data[i];
        }
        return bChk;
    }


    public static String byteArrToHexStr(byte[] bytes) {

        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)    sb.append(String.format("%02X", b & 0xff));

        return sb.toString();
    }

    //지역 시간 설정
    public static void setupTimeZone(String timeZoneName) {
        //TimeManager timeManager = TimeManager.getInstance();
        //timeManager.setTimeZone(timeZoneName);
    }

    //macAddress 호출
    public static String getMacAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (int i = 0; i < mac.length; i++)
                    buf.append(String.format("%02X:", mac[i]));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public String intToByte100(int value) {
        byte[] ByteValue = intTobyte((value * 100), ByteOrder.LITTLE_ENDIAN);
        String convertValue = "0000";
        if (ByteValue.length >= 2)
            convertValue = String.format("%02X", ByteValue[1]) + String.format("%02X", ByteValue[0]);

        return convertValue;
    }

    public static String intToByte(int value) {
        byte[] ByteValue = intTobyte((value), ByteOrder.LITTLE_ENDIAN);
        String convertValue = "0000";
        if (ByteValue.length >= 2)
            convertValue = String.format("%02X", ByteValue[1]) + String.format("%02X", ByteValue[0]);

        return convertValue;
    }

    public String intTo1Byte(int value) {
        byte[] ByteValue = intTobyte((value), ByteOrder.LITTLE_ENDIAN);
        String convertValue = "00";
        if (ByteValue.length >= 1)
            convertValue = String.format("%02X", ByteValue[0]);

        return convertValue;
    }
}
