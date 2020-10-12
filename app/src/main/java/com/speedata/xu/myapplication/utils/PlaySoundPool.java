package com.speedata.xu.myapplication.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.SparseIntArray;

import com.speedata.xu.myapplication.R;

import java.util.HashMap;

import static android.os.VibrationEffect.DEFAULT_AMPLITUDE;

/**
 * @author xuyan
 */
public class PlaySoundPool {

    @SuppressLint("StaticFieldLeak")
    private static PlaySoundPool playSoundPool;
    private Context context;
    private SoundPool soundPool; // 定义SoundPool 对象
    private HashMap<Integer, Integer> soundPoolMap; // 定义HASH表
    private static SparseIntArray mapSRC;
    private Vibrator vibrator;
    float currentVolume;
    /**
     * 正常扫描声音
     */
    private static final int LASER = 1;
    /**
     * 错误声音
     */
    private static final int ERROR = 2;

    private AudioManager mgr;

    // OFF/ON/OFF/ON
    final long[] pattern = {10, 250, 50, 250};

    public static PlaySoundPool getPlaySoundPool(Context context) {
        if (playSoundPool == null) {
            playSoundPool = new PlaySoundPool(context);
        }
        return playSoundPool;
    }

    private PlaySoundPool(Context context) {
        this.context = context;
        initSounds();
        loadSounds();

    }


    /**
     * 初始化
     */
    private void initSounds() {
        // 初始化soundPool 对象,
        // 第一个参数是允许有多少个声音流同时播放,
        // 第2个参数是声音类型,
        // 第三个参数是声音的品质
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);

        // 初始化HASH表
        soundPoolMap = new HashMap<>();

        mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        currentVolume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * 把资源中的音效加载到指定的ID (播放的时候就对应到这个ID播放就行了)
     *
     * @param raw 声音资源ID
     * @param ID  自定义指定ID
     */
    private void loadSfx(int raw, int ID) {
        soundPoolMap.put(ID, soundPool.load(context, raw, ID));
    }

    /**
     * 播放声音
     *
     * @param sound
     */
    private void play(int sound) {

        soundPool.play(//
                soundPoolMap.get(sound), // 播放的音乐id
                1, // 左声道音量
                1, // 右声道音量
                0, // 优先级，0为最低
                0, // 循环次数，0 不循环，-1 永远循环
                1f); // 回放速度 ，该值在0.5-2.0之间，1为正常速度
    }

    /**
     * 加载声音
     */
    private void loadSounds() {
        loadSfx(R.raw.scan, LASER);
        loadSfx(R.raw.error, ERROR);
    }

    /**
     * 播放扫描成功声音
     */
    public void playLaser() {
        System.out.println("*******************LASER*******************");
        play(LASER);
    }

    /**
     * 播放错误声音
     */
    public void playError() {
        System.out.println("*******************ERROR*******************");
        play(ERROR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                vibrator.vibrate(VibrationEffect.createOneShot(500, DEFAULT_AMPLITUDE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            vibrator.vibrate(pattern, -1);
        }
    }

}
