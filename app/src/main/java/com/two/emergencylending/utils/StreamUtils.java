package com.two.emergencylending.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 关流
 */

public class StreamUtils {
    public static boolean closeInputStream(InputStream inStream){
        try {

            if (null != inStream) {
                inStream.close();
                inStream = null;
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public static boolean closeOutputStream(OutputStream outStream){
        try {
            if(outStream != null){
                outStream.close();
                outStream = null;
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return false;

    }
}
