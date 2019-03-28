package com.lkl.ansuote.hdqlibrary.util

import android.content.Context
import android.os.Build
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.RomUtils
import java.io.File

/**
 *
 *
 * @author huangdongqiang
 * @date 28/02/2019
 */
object CallRecordUtil {

    /**
     * 获取不同ROM的通话录音文件存放目录
     * @param context Context?
     * @return String
     */
    fun getCallRecordPath(context: Context?): String {
        var path = ""
        try {
            if (null != context) {

                path = when {
                   RomUtils.isHuawei() -> {
                       //EMUI8.0 及其以上版本
                       if (DeviceUtils.getSDKVersionCode() >= Build.VERSION_CODES.O) {
                           "/Sounds/CallRecord/"
                       } else {
                           "/record/"
                       }
                   }
                    RomUtils.isXiaomi() -> {
                        "/MIUI/sound_recorder/call_rec/"
                    }
                    RomUtils.isVivo() -> {
                        "/录音/通话录音/"
                    }
                    RomUtils.isOppo() -> {
                        "/Recordings/"
                    }
                    RomUtils.isOneplus() -> {
                        "/Record/PhoneRecord/"
                    }
                    RomUtils.isSamsung() -> {
                        "/Call/"
                    }
                    RomUtils.isMeizu() -> {
                        "/Recorder/call/"
                    }
                    RomUtils.is360() -> {
                        "/360OS/My Records/Call Records/"
                    }
                    else -> ""
               }
            }

            if (!path.isBlank()) {
                return PathUtils.getExternalStoragePath() + path
            }

        } catch (ex: Exception) {

        }

        return ""

    }


    /**
     * 判断是否是录音文件
     *
     * @param file File
     * @return Boolean
     */
    fun isCallRecordFile(file: File) : Boolean {
        if (file.name.endsWith("mp3", true)
                || file.name.endsWith("amr", true)
                || file.name.endsWith("aac", true)
                || file.name.endsWith("wav", true)
                || file.name.endsWith("M4a", true)
                || file.name.endsWith("3GA", true)
                || file.name.endsWith("ogg", true)){
            return true
        }

        return false
    }

    /**
     * 通过时间戳查找文件
     *
     * @param file File         判断的文件
     * @param targetStamp Long  查找的时间戳
     * @param offSize Long      允许的误差偏移
     * @return Boolean
     */
    fun isCallRecordFileByTimeStamp(file: File, targetStamp: Long, offSize: Long = 5 * 1000L): Boolean {
        if (FileUtils.isFile(file) && file.lastModified() - targetStamp <= offSize && file.lastModified() - targetStamp >= -1000L) {
            return true
        }
        return false
    }
}