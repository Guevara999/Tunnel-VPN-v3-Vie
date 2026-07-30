package com.vienetworks.originvpn.util;

import android.content.pm.PackageInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.annotation.SuppressLint;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import android.view.inputmethod.InputMethodManager;
import android.app.Activity;
import android.view.WindowManager;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Build;
import com.romzkie.ultrasshservice.SocksHttpService;
import java.util.Locale;


public class Utils
{
    public static String CurrentSMSVersion = "00";
    private static final long B = 1;
    private static final long KB = B * 1024;
    private static final long MB = KB * 1024;
    private static final long GB = MB * 1024;
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static void copyToClipboard(Context context, String text) throws Exception {
        int sdk = android.os.Build.VERSION.SDK_INT;

        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                .getSystemService(context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        }
        else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
                .getSystemService(context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData
                .newPlainText(
                "Message", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    public static PackageInfo getAppInfo(Context context){
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFromAssets(Context context, String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));

        // do reading, usually loop until end of file reading
        StringBuilder sb = new StringBuilder();
        String mLine = reader.readLine();
        while (mLine != null) {
            sb.append(mLine + '\n'); // process line
            mLine = reader.readLine();
        }
        reader.close();
        return sb.toString();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (activity.getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                                                     0);
        }
    }

    public static void exitAll(Activity activity) {
        Intent stopTunnel = new Intent(SocksHttpService.TUNNEL_SSH_STOP_SERVICE);
        LocalBroadcastManager.getInstance(activity)
            .sendBroadcast(stopTunnel);

        if (Build.VERSION.SDK_INT >= 16) {
            activity.finishAffinity();
        }

        System.exit(0);
    }


    public static String parseSpeed(double bytes, boolean inBits) {
        double value = inBits ? bytes * 8 : bytes;
        if (value < KB) {
            return String.format(Locale.getDefault(), "%.1f " + (inBits ? "b" : "B") + "/s", value);
        } else if (value < MB) {
            return String.format(Locale.getDefault(), "%.1f K" + (inBits ? "b" : "B") + "/s", value / KB);
        } else if (value < GB) {
            return String.format(Locale.getDefault(), "%.1f M" + (inBits ? "b" : "B") + "/s", value / MB);
        } else {
            return String.format(Locale.getDefault(), "%.2f G" + (inBits ? "b" : "B") + "/s", value / GB);
        }
    }
    public static String byteCountToDisplaySize(long bytes, boolean si)
    {
        // http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java/3758880#3758880
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String byteTrafficToCount(long bytes, boolean mbit)
    {
        if (mbit)
            bytes = bytes * 8;
        int unit = mbit ? 1000 : 1024;
        if (bytes < unit)
            return bytes + (mbit ? " bit" : " B");
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (mbit ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (mbit ? "" : "");
        if (mbit)
            return String.format(Locale.getDefault(), "%.1f %sb/s", bytes / Math.pow(unit, exp), pre);
        else
            return String.format(Locale.getDefault(), "%.1f %sB/s", bytes / Math.pow(unit, exp), pre);
    }
    public static String humanBytes(long size) {
        long limit = 10 * 1024;
        long limit2 = limit * 2 - 1;
        String negative = "";
        if(size < 0) {
            negative = "-";
            size = Math.abs(size);
        }

        if(size < limit) {
            return String.format("%s%s bytes", negative, size);
        } else {
            size = Math.round((double) size / 1024);
            if (size < limit2) {
                return String.format("%s%s kB", negative, size);
            } else {
                size = Math.round((double)size / 1024);
                if (size < limit2) {
                    return String.format("%s%s MB", negative, size);
                } else {
                    size = Math.round((double)size / 1024);
                    if (size < limit2) {
                        return String.format("%s%s GB", negative, size);
                    } else {
                        size = Math.round((double)size / 1024);
                        return String.format("%s%s TB", negative, size);
                    }
                }
            }
        }
    }
}
