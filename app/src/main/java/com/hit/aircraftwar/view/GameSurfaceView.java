package com.hit.aircraftwar.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.aircraft.HeroAircraft;
import com.hit.aircraftwar.application.Game;
import com.hit.aircraftwar.application.ImageManager;
import com.hit.aircraftwar.basic.AbstractFlyingObject;

import java.util.List;

@SuppressLint("ViewConstructor")
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private final HeroAircraft heroAircraft = HeroAircraft.getHeroAircraft();
    private final SurfaceHolder mSurfaceHolder;
    private final Paint mPaint;
    private final Game game;
    Canvas canvas ;
    int backGroundTop = 0;
    int screenWidth = 480, screenHeight = 800;
    boolean mbLoop; //控制绘画线程的标志位

    public GameSurfaceView(Context context, Game game) {
        super(context);
        mbLoop = true;
        this.game = game;
        mPaint = new Paint();  //设置画笔
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        this.setFocusable(true);
    }

    public void draw(){
        //通过SurfaceHolder对象的lockCanvans()方法，我们可以获取当前的Canvas绘图对象
        loading_img();
        //绘图的画布
        canvas = mSurfaceHolder.lockCanvas();
        if(canvas == null){
            return;
        }

        //绘制背景，图片滚动
        canvas.drawBitmap(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop-screenHeight, mPaint);
        canvas.drawBitmap(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, mPaint);

        //绘制英雄机
        canvas.drawBitmap(ImageManager.HERO_IMAGE, heroAircraft.getLocationX(), heroAircraft.getLocationY(), mPaint);

        //绘制敌机和子弹
        paintImageWithPositionRevised(game.getAllProp());
        paintImageWithPositionRevised(game.getEnemyAircrafts());
        paintImageWithPositionRevised(game.getEnemyBullets());
        paintImageWithPositionRevised(game.getHeroBullets());

        backGroundTop += 1;
        if(backGroundTop == screenHeight)
            this.backGroundTop = 0;

        //通过unlockCanvasAndPost(mCanvas)方法对画布内容进行提交
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void loading_img(){
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

    private void paintImageWithPositionRevised(List<? extends AbstractFlyingObject> flyingObjects){
        if(flyingObjects.size() != 0){
            for (AbstractFlyingObject flyingObject : flyingObjects) {
                Bitmap image = flyingObject.getImage();
                assert image != null : flyingObject.getClass().getName() + " has no image! ";
                canvas.drawBitmap(image, flyingObject.getLocationX(), flyingObject.getLocationY(), mPaint);
            }
        }
    }

    @Override
    public void run() {
        //设置一个循环来绘制，通过标志位来控制开启绘制还是停止
        while (mbLoop){
            synchronized (mSurfaceHolder){
                draw();
            }
            try {
                Thread.sleep(200);
            }catch (Exception ignored){}
        }
    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        new Thread(this).start();
    }
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        mbLoop = false;
    }
}
