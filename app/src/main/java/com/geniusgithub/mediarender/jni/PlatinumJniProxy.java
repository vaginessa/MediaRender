package com.geniusgithub.mediarender.jni;

import java.io.UnsupportedEncodingException;

public class PlatinumJniProxy {

    static {
        System.loadLibrary("platinum-jni");
    }

    public static native void startMediaRender(byte[] friendname, byte[] uuid);

    public static native void stopMediaRender();

    public static native void responseGenaEvent(int cmd, byte[] value, byte[] data);


    public static void startMediaRender(String friendname, String uuid) {
        if (friendname == null) friendname = "";
        if (uuid == null) uuid = "";
        try {
            startMediaRender(friendname.getBytes("utf-8"), uuid.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void responseGenaEvent(int cmd, String value, String data) {
        if (value == null) value = "";
        if (data == null) data = "";
        try {
            responseGenaEvent(cmd, value.getBytes("utf-8"), data.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
