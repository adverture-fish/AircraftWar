package com.hit.aircraftwar.application;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.hit.aircraftwar.R;

import java.util.HashMap;

public class MusicService extends Service {
    private HashMap<Integer, Integer> soundID;
    private SoundPool mSoundPool;
    private MediaPlayer player;
    private MyBinder myBinder;
    private static final String Tag = "MusicService";

    public MusicService() {

    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.bgm);
        player.setLooping(true);
        Log.d(Tag, "MusicService Created");
        if (Build.VERSION.SDK_INT < 21) {
            mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        } else {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(2);
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            builder.setAudioAttributes(attrBuilder.build());
            mSoundPool = builder.build();
        }
        soundID = new HashMap<>();
        soundID.put(1, mSoundPool.load(this, R.raw.bullet_hit, 1));
        soundID.put(2, mSoundPool.load(this, R.raw.game_over, 1));
        soundID.put(3, mSoundPool.load(this, R.raw.bullet, 1));
        soundID.put(4, mSoundPool.load(this, R.raw.bomb_explosion, 1));
        soundID.put(5, mSoundPool.load(this, R.raw.get_supply,1));
        myBinder = new MyBinder();
    }

    @Override
    public IBinder onBind(Intent intent){
        if (myBinder == null) {
            myBinder = new MyBinder();
        }
        return myBinder;
    }

    /**
     * 相关指令设置
     */
    public class MyBinder extends Binder {

        public void tryBinder(){
            Log.d(Tag, "Try");
        }

        public void playBgm(){
            if(player.isPlaying()){
                stopMusic();
            }
            playBgmMusic();
        }

        public void playBossBgm(){
            if(player.isPlaying()){
                stopMusic();
            }
            playBossBgmMusic();
        }

        public void playHit(){
            if(soundID.get(1) != null){
                mSoundPool.play(soundID.get(1) , 1, 1, 0,0,1);
            }
        }

        public void playGameOver(){
            if(soundID.get(2) != null){
                mSoundPool.play(soundID.get(2), 1, 1, 0, 0, 1);
            }
        }

        public void playShoot(){
            if(soundID.get(3) != null){
                mSoundPool.play(soundID.get(3), 1, 1, 0, 0, 1);
            }
        }

        public void playBomb(){
            if(soundID.get(4) != null){
                mSoundPool.play(soundID.get(4), 1, 1, 0, 0, 1);
            }
        }

        public void playSupply(){
            if(soundID.get(5) != null){
                mSoundPool.play(soundID.get(5), 1, 1, 0, 0, 1);
            }
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    //播放音乐
    public void playBgmMusic(){
        if(player == null){
            player = MediaPlayer.create(this, R.raw.bgm);
            player.setLooping(true);
        }
        player.start();
    }

    public void playBossBgmMusic(){
        if(player == null){
            player = MediaPlayer.create(this, R.raw.bgm_boss);
            player.setLooping(true);
        }
        player.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMusic();
    }
    /**
     * 停止播放
     */
    public void stopMusic() {
        if (player != null) {
            player.stop();
            player.reset();//重置
            player.release();//释放
            player = null;
        }
    }
}

