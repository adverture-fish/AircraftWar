package com.hit.aircraftwar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;

import com.hit.aircraftwar.aircraft.HeroAircraft;
import com.hit.aircraftwar.application.Game;
import com.hit.aircraftwar.view.GameSurfaceView;

public class GameActivity extends AppCompatActivity {
    public static int screenWidth;
    public static int screenHeight;
    private final HeroAircraft heroAircraft = HeroAircraft.getHeroAircraft();
    GameSurfaceView gameSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getScreenHW();
        Game game = new Game();
        gameSurfaceView = new GameSurfaceView(this, game);
        setContentView(gameSurfaceView);
        game.action();
        heroMoving();
    }
    public void getScreenHW(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }
    public void heroMoving(){
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