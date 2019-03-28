package com.lkl.ansuote.hdqlibrary.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * 获取设备的唯一标识
 * Created by huangdongqiang on 2018/4/21.
 */
public class DeviceIdUtil {
    private static final String PRE_NAME = "INSTALLATION";
    private static final String PRE_DEVICE_ID = "pre_device_id";
    private static final String DEVICE_ID_FILE_NAME = ".INSTALLATIONS";

    /**
     * 获取设备唯一标识
     * @param context
     * @return
     */
    public synchronized static String getDeviceId(Context context) {
        if (null != context) {
            //查找sp
            String deviceId = getDeviceIdFromSp(context);
            if (!TextUtils.isEmpty(deviceId)) {
                return deviceId;
            }

            //查找内部存储
            deviceId = getDeviceIdFromStorage(context);
            if (!TextUtils.isEmpty(deviceId)) {
                setDeviceIdToSp(context, deviceId);
                return deviceId;
            }

            //查找外部sdcard存储
            deviceId = getDeviceIdFromSdCard(context);
            if (!TextUtils.isEmpty(deviceId)) {
                setDeviceIdToSp(context, deviceId);
                setDeviceToStorage(context, deviceId);
                return deviceId;
            }

            //查找外部sdcard存储根目录
            deviceId = getDevicedIdFromSdCardRoot();
            if (!TextUtils.isEmpty(deviceId)) {
                setDeviceIdToSp(context, deviceId);
                setDeviceToStorage(context, deviceId);
                setDeviceIdToSdCard(context, deviceId);
                return deviceId;
            }

            //创建唯一标识，并且保存
            deviceId = UUID.randomUUID().toString();
            setDeviceIdToSp(context, deviceId);
            setDeviceToStorage(context, deviceId);
            setDeviceIdToSdCard(context, deviceId);
            setDeviceIdToSdCardRoot(deviceId);
            return deviceId;
        }

        return null;
    }

    /**
     * 从sp获取设备号
     * @param context
     * @return
     */
    private static String getDeviceIdFromSp(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        if (null != sp) {
            String deviceId = sp.getString(PRE_DEVICE_ID, "");
            if (!TextUtils.isEmpty(deviceId)) {
                return deviceId;
            }
        }
        return null;
    }

    /**
     * 把设备号保存到 sp
     * @param context
     * @param deviceId
     */
    private static void setDeviceIdToSp(Context context, String deviceId) {
        if (null != context && null != deviceId) {
            SharedPreferences sp = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
            if (null != sp && null != sp.edit()) {
                sp.edit().putString(PRE_DEVICE_ID, deviceId).apply();
            }
        }
    }

    /**
     * 把设备id保存到内部存储中 /data/data/你的应用的包名/cache/fileName”
     * @param context
     * @param deviceId
     */
    private static void setDeviceToStorage(Context context, String deviceId) {
        try {
            if (null != context) {
                File fileDir = context.getFilesDir();
                File file = new File(fileDir, DEVICE_ID_FILE_NAME);
                boolean fileByDeleteOldFile = FileUtils.createFileByDeleteOldFile(file);
                if (fileByDeleteOldFile) {
                    writeInstallationFile(file, deviceId);
                }
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        }
    }

    /**
     * 从内部存储获取设备id  /data/data/你的应用的包名/cache/fileName”
     * @param context
     * @return
     */
    private static String getDeviceIdFromStorage(Context context) {
        try {
            if (null != context) {
                File fileDir = context.getFilesDir();
                File file = new File(fileDir, DEVICE_ID_FILE_NAME);
                if (FileUtils.isFile(file)) {
                    return readInstallationFile(file);
                }
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        }

        return null;
    }

    /**
     * 将设备号保存到  sdCard/Android/data/你的应用的包名/files/fileName 下
     * @param context
     * @param deviceId
     */
    private static void setDeviceIdToSdCard(Context context, String deviceId) {
        try {
            if (null != context) {
                File fileDir = context.getExternalFilesDir("");
                File file = new File(fileDir, DEVICE_ID_FILE_NAME);
                boolean fileByDeleteOldFile = FileUtils.createFileByDeleteOldFile(file);
                if (fileByDeleteOldFile) {
                    writeInstallationFile(file, deviceId);
                }
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        }
    }


    /**
     * 获取设备id sdCard/Android/data/你的应用的包名/files/fileName 下
     * @param context
     * @return
     */
    private static String getDeviceIdFromSdCard(Context context) {
        try {
            if (null != context) {
                File fileDir = context.getExternalFilesDir("");
                File file = new File(fileDir, DEVICE_ID_FILE_NAME);
                if (FileUtils.isFile(file)) {
                    return readInstallationFile(file);
                }
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        }

        return null;
    }


    /**
     * 将设备号保存到sdCard 的根目录
     * @param deviceId
     */
    private static void setDeviceIdToSdCardRoot(String deviceId) {
        try {
            if (SDCardUtils.isSDCardEnableByEnvironment()) {
                String sdCardPath = SDCardUtils.getSDCardPathByEnvironment();
                File file = new File(sdCardPath, DEVICE_ID_FILE_NAME);
                boolean fileByDeleteOldFile = FileUtils.createFileByDeleteOldFile(file);
                if (fileByDeleteOldFile) {
                    writeInstallationFile(file, deviceId);
                }
            }

        } catch (Exception e) {
            Logger.i(e.toString());
        }
    }

    /**
     * 从sdcard里面获取设备号
     * @return
     */
    private static String getDevicedIdFromSdCardRoot() {
        try {
            if (SDCardUtils.isSDCardEnableByEnvironment()) {
                String sdCardPath = SDCardUtils.getSDCardPathByEnvironment();
                File file = new File(sdCardPath, DEVICE_ID_FILE_NAME);
                if (FileUtils.isFile(file)) {
                    return readInstallationFile(file);
                }
            }

        } catch (Exception e) {
            Logger.i(e.toString());
        }

        return null;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation, String deviceId) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        out.write(deviceId.getBytes());
        out.close();
    }

    /**
     * Return pseudo unique ID
     * @return ID
     */
    public static String getPsuedoUniqueID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their phone or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "35" +
                (Build.BOARD.length() % 10)
                + (Build.BRAND.length() % 10)
                + (Build.CPU_ABI.length() % 10)
                + (Build.DEVICE.length() % 10)
                + (Build.MANUFACTURER.length() % 10)
                + (Build.MODEL.length() % 10)
                + (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their phone, there will be a duplicate entry
        String serial = null;
        try
        {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        }
        catch (Exception e)
        {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
}
