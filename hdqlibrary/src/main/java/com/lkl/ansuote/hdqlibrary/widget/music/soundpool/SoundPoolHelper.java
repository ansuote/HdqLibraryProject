package com.lkl.ansuote.hdqlibrary.widget.music.soundpool;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;

import java.util.HashMap;
import java.util.Map;

/**
 * SoundPool管理类
 */
public class SoundPoolHelper {

    /**
     * AudioManager.STREAM_MUSIC
     * AudioManager.STREAM_ALARM
     * AudioManager.STREAM_RING
     */
    private static int DEFAULT_STREAM_TYPE = AudioManager.STREAM_MUSIC;


    /*变量*/
    private SoundPool soundPool;
    /**
     * RingtoneManager.TYPE_ALARM
     * RingtoneManager.TYPE_NOTIFICATION
     * RingtoneManager.TYPE_RINGTONE
     */
    private int NOW_RINGTONE_TYPE = RingtoneManager.TYPE_NOTIFICATION;
    private int maxStream;
    private Map<String,Integer> ringtoneIds;

    /**
     * 左右声道音量默认比例 范围 0.0~1.0
     */
    private static float DEFAULT_VOLUME = 1.0f;

    /*方法*/

    public SoundPoolHelper() {
        this(1,DEFAULT_STREAM_TYPE);
    }

    public SoundPoolHelper(int maxStream) {
        this(maxStream,DEFAULT_STREAM_TYPE);
    }

    public SoundPoolHelper(int maxStream, int streamType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder spBuilder = new SoundPool.Builder();
            AudioAttributes.Builder builder = new AudioAttributes.Builder();
            spBuilder.setMaxStreams(maxStream);
            builder.setLegacyStreamType(streamType);
            spBuilder.setAudioAttributes(builder.build());
            soundPool = spBuilder.build();
        } else  {
            soundPool = new SoundPool(maxStream,streamType,1);
        }
        this.maxStream = maxStream;
        ringtoneIds = new HashMap<>();
    }

    /**
     * 设置RingtoneType，这只是关系到加载哪一个默认音频
     *  需要在load之前调用
     * @param ringtoneType  ringtoneType
     * @return  this
     */
    public SoundPoolHelper setRingtoneType(int ringtoneType) {
        NOW_RINGTONE_TYPE = ringtoneType;
        return this;
    }

    /**
     * 加载音频资源
     * @param context   上下文
     * @param resId     资源ID
     * @return  this
     */
    public SoundPoolHelper load(Context context,@NonNull String ringtoneName, @RawRes int resId) {
        if (maxStream==0){
            return this;
        }
        maxStream--;
        ringtoneIds.put(ringtoneName,soundPool.load(context,resId,1));
        return this;
    }

    public void setOnLoadCompleteListener(SoundPool.OnLoadCompleteListener onLoadCompleteListener) {
        if (null != soundPool) {
            soundPool.setOnLoadCompleteListener(onLoadCompleteListener);
        }
    }

    /**
     * 加载默认的铃声
     * @param context 上下文
     * @return  this
     */
    public SoundPoolHelper loadDefault(Context context) {
        Uri uri = getSystemDefaultRingtoneUri(context);
        if (uri==null) {
            //load(context,"default", android.R.raw.reminder);
        }
        else {
            load(context,"default",uri2Path(context,uri));
        }
        return this;
    }

    /**
     * 加载铃声
     * @param context   上下文
     * @param ringtoneName 自定义铃声名称
     * @param ringtonePath 铃声路径
     * @return  this
     */
    public SoundPoolHelper load(Context context, @NonNull String ringtoneName, @NonNull String ringtonePath) {
        if (maxStream==0) {
            return this;
        }
        maxStream--;
        ringtoneIds.put(ringtoneName,soundPool.load(ringtonePath,1));
        return this;
    }

    /**
     *  int play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate) ：
     *  1)该方法的第一个参数指定播放哪个声音；
     *  2) leftVolume 、
     *  3) rightVolume 指定左、右的音量：
     *  4) priority 指定播放声音的优先级，数值越大，优先级越高；
     *  5) loop 指定是否循环， 0 为不循环， -1 为循环；
     *  6) rate 指定播放的比率，数值可从 0.5 到 2 ， 1 为正常比率。
     *
     *  volume 左右声道音量的比例 0.0~1.0
     */
    public void play(@NonNull String ringtoneName, boolean isLoop) {
        play(ringtoneName, isLoop, DEFAULT_VOLUME);
    }

    /**
     *  int play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate) ：
     *  1)该方法的第一个参数指定播放哪个声音；
     *  2) leftVolume 、
     *  3) rightVolume 指定左、右的音量：
     *  4) priority 指定播放声音的优先级，数值越大，优先级越高；
     *  5) loop 指定是否循环， 0 为不循环， -1 为循环；
     *  6) rate 指定播放的比率，数值可从 0.5 到 2 ， 1 为正常比率。
     *
     *  volume 左右声道音量的比例 0.0~1.0
     */
    public void play(@NonNull String ringtoneName, boolean isLoop, float volume) {
        if (ringtoneIds.containsKey(ringtoneName)) {
            soundPool.play(ringtoneIds.get(ringtoneName),volume,volume,1,isLoop?-1:0,1);
        }
    }

    public boolean isReadyPlay(@NonNull String ringtoneName) {
        return ringtoneIds.containsKey(ringtoneName);
    }

    public void stop(@NonNull String ringtoneName) {
        if (ringtoneIds.containsKey(ringtoneName)) {
            soundPool.stop(ringtoneIds.get(ringtoneName));
        }
    }


    public void playDefault() {
        play("default",false);
    }

    /**
     * 释放资源
     */
    public void release() {
        if (soundPool!=null) {
            soundPool.release();
        }
    }

    /**
     * 获取系统默认铃声的Uri
     * @param context  上下文
     * @return  uri
     */
    private Uri getSystemDefaultRingtoneUri(Context context) {
        try {
            return RingtoneManager.getActualDefaultRingtoneUri(context, NOW_RINGTONE_TYPE);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 把 Uri 转变 为 真实的 String 路径
     * @param context 上下文
     * @param uri  URI
     * @return 转换结果
     */
    public static String uri2Path(Context context, Uri uri) {
        if ( null == uri ) {
            return null;
        }
        String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null ){
            data = uri.getPath();
        }
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}