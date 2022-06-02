package com.hit.aircraftwar.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.aircraft.BossEnemy;
import com.hit.aircraftwar.aircraft.EliteEnemy;
import com.hit.aircraftwar.aircraft.HeroAircraft;
import com.hit.aircraftwar.aircraft.MobEnemy;
import com.hit.aircraftwar.application.GameSurfaceView;
import com.hit.aircraftwar.application.ImageManager;
import com.hit.aircraftwar.bullet.EnemyBullet;
import com.hit.aircraftwar.bullet.HeroBullet;
import com.hit.aircraftwar.prop.BombSupplyProp;
import com.hit.aircraftwar.prop.FireSupplyProp;
import com.hit.aircraftwar.prop.HpSupplyProp;

public class GameActivity extends Activity {
    public static int screenWidth;
    public static int screenHeight;
    private HeroAircraft heroAircraft;

    //触摸点数据，因为只有一组数据故设为静态变量，方便取得
    public static  float getHeroLocationX() {
        return heroLocationX;
    }

    public static float getHeroLocationY() {
        return heroLocationY;
    }

    private static float heroLocationX;
    private static float heroLocationY;
    GameSurfaceView gameSurfaceView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getScreenHW();
        //准备图片
        prepareImageResources();
        prepareHashMapImages();

        Intent intent = getIntent();
        String difficulty = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        gameSurfaceView = new GameSurfaceView(this, difficulty);
        heroAircraft = HeroAircraft.getHeroAircraft();
        if(screenHeight !=0 && screenWidth !=0){
            heroLocationX = (float) screenWidth / 2;
            heroLocationY =(float) (screenHeight - ImageManager.HERO_IMAGE.getHeight() / 2);
        }
        setContentView(gameSurfaceView);

    }

    /**
     * 获取屏幕触摸点位置
     * @return true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        heroLocationX = event.getX();
        heroLocationY = event.getY();
        return true;

    }
    public void getScreenHW() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }



    private void prepareImageResources() {

        ImageManager.BACKGROUND_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        ImageManager.HERO_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        ImageManager.MOB_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.mob);
        ImageManager.ELITE_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.elite);
        ImageManager.BOSS_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.boss);
        ImageManager.PROP_BLOOD_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_blood);
        ImageManager.PROP_BOMB_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_bomb);
        ImageManager.PROP_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.prop_bullet);
        ImageManager.HERO_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bullet_hero);
        ImageManager.ENEMY_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bullet_enemy);
        ImageManager.BACKGROUND_IMAGE_MEDIUM = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
        ImageManager.BACKGROUND_IMAGE_DIFFICULT = BitmapFactory.decodeResource(getResources(), R.drawable.bg5);

    }

    private void prepareHashMapImages(){
        ImageManager.CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ImageManager.ELITE_ENEMY_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), ImageManager.MOB_ENEMY_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), ImageManager.HERO_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(), ImageManager.BOSS_ENEMY_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(HpSupplyProp.class.getName(),ImageManager.PROP_BLOOD_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(BombSupplyProp.class.getName(), ImageManager.PROP_BOMB_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(FireSupplyProp.class.getName(), ImageManager.PROP_BULLET_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), ImageManager.HERO_BULLET_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ImageManager.ENEMY_BULLET_IMAGE);
    }


}