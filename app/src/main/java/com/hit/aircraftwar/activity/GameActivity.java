package com.hit.aircraftwar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.aircraft.BossEnemy;
import com.hit.aircraftwar.aircraft.EliteEnemy;
import com.hit.aircraftwar.aircraft.HeroAircraft;
import com.hit.aircraftwar.aircraft.MobEnemy;
import com.hit.aircraftwar.application.Game;
import com.hit.aircraftwar.application.ImageManager;
import com.hit.aircraftwar.bullet.EnemyBullet;
import com.hit.aircraftwar.bullet.HeroBullet;
import com.hit.aircraftwar.prop.BombSupplyProp;
import com.hit.aircraftwar.prop.FireSupplyProp;
import com.hit.aircraftwar.prop.HpSupplyProp;
import com.hit.aircraftwar.view.GameSurfaceView;

public class GameActivity extends AppCompatActivity {
    public static int screenWidth;
    public static int screenHeight;
    private final HeroAircraft heroAircraft = null;
    GameSurfaceView gameSurfaceView;

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


    }

    private void hashMapImages(){
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        getScreenHW();
        prepareImageResources();
        hashMapImages();
        Game game = new Game();
        gameSurfaceView = new GameSurfaceView(this, game);
        setContentView(gameSurfaceView);
        game.action();
        heroMoving();
    }

    public void getScreenHW() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    public void heroMoving() {
        int speed = 10;
        gameSurfaceView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //获取由哪个键触发的事件
                switch (event.getKeyCode()) {
                    //控制飞机下上左右移
                    case KeyEvent.KEYCODE_S:
                        heroAircraft.setLocation(heroAircraft.getLocationX(), heroAircraft.getLocationY() + speed);
                        break;
                    case KeyEvent.KEYCODE_W:
                        heroAircraft.setLocation(heroAircraft.getLocationX(), heroAircraft.getLocationY() - speed);
                        break;
                    case KeyEvent.KEYCODE_A:
                        heroAircraft.setLocation(heroAircraft.getLocationX() + speed, heroAircraft.getLocationY());
                        break;
                    case KeyEvent.KEYCODE_D:
                        heroAircraft.setLocation(heroAircraft.getLocationX() - speed, heroAircraft.getLocationY());
                        break;
                    default:
                        break;
                }
                //通知planeView组件重绘
                gameSurfaceView.invalidate();
                return true;
            }
        });
    }
}