package com.hit.aircraftwar.application;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.hit.aircraftwar.activity.EndActivity;
import com.hit.aircraftwar.activity.GameActivity;
import com.hit.aircraftwar.aircraft.AbstractAircraft;
import com.hit.aircraftwar.aircraft.HeroAircraft;
import com.hit.aircraftwar.basic.AbstractFlyingObject;
import com.hit.aircraftwar.bullet.BaseBullet;
import com.hit.aircraftwar.factory.AbstractEnemyFactory;
import com.hit.aircraftwar.factory.BossEnemyFactory;
import com.hit.aircraftwar.factory.EliteEnemyFactory;
import com.hit.aircraftwar.factory.MobEnemyFactory;
import com.hit.aircraftwar.observer.AircraftObserver;
import com.hit.aircraftwar.prop.AbstractProp;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 游戏主面板，游戏启动
 *
 * @author lxl,qh
 */
@SuppressLint("ViewConstructor")
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable{


    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private final int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<AbstractProp> allProp;
    public final AircraftObserver aircraftObserver;

    public static boolean bossExist = false;
    private static int score = 0;
    private boolean bossHasAppear = false;
    private int stage = 0;
    protected int time = 0;
    private final String difficulty;
    private Bitmap backGroundImage = ImageManager.BACKGROUND_IMAGE;

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int heroShootCycleTime = 0;
    private int enemyShootCycleTime = 0;
    private int enemyCreateCycleTime = 0;

    /**
     *  游戏难度相关设置
     */
    protected boolean isBossAppear = true;
    protected int enemyMaxNumber = 5;
    protected int eliteEnemyAppear = 20;
    protected double bossHpRate = 1;
    protected double enemyHpRate = 1;
    protected int bossAppear = 500;
    protected int heroShootCycleDuration = 800;
    protected int enemyShootCycleDuration = 1200;
    protected int enemyCreateCycleDuration = 600;
    protected int bossDefeatNumber = 0;


//    private final MusicService.MyBinder myBinder;
    private final SurfaceHolder mSurfaceHolder;
    private final Paint mPaint;
    Canvas canvas ;
    int backGroundTop = 0;
    int screenWidth = GameActivity.screenWidth, screenHeight = GameActivity.screenHeight;
    boolean gameOverFlag;

    public GameSurfaceView(Context context, String difficulty) {

        super(context);
        gameOverFlag = false;

        executorService = new ScheduledThreadPoolExecutor(3);

//        this.myBinder = myBinder;
        this.difficulty = difficulty;
        if("medium".equals(difficulty)){
            backGroundImage = ImageManager.BACKGROUND_IMAGE_MEDIUM;
        }
        else if("difficult".equals(difficulty)){
            backGroundImage = ImageManager.BACKGROUND_IMAGE_DIFFICULT;
        }
        System.out.println("game begin---");
        mPaint = new Paint();  //设置画笔
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        this.setFocusable(true);

        heroAircraft =HeroAircraft.getHeroAircraft();
        aircraftObserver = new AircraftObserver();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        allProp = new LinkedList<>();

    }

    //***********************
    //      Action 各部分
    //***********************

    private void action(){

        Runnable task = ()->{
            time += timeInterval;

            // 周期性执行（控制频率）
            if (heroShootTimeCountAndNewCycleJudge()) {
                // 飞机射出子弹
                heroShootAction();
            }
            if (enemyShootTimeCountAndNewCycleJudge()){
                enemyShootAction();
            }

            if(enemyCreateTimeCountAndNewCycleJudge()){
                // 新敌机产生
                if(score - stage * bossAppear >= bossAppear){
                    stage++;
                    if(!bossExist && isBossAppear){
                        bossExist = true;
                        bossHasAppear = true;
//                    myBinder.playBossBgm();
                        AbstractEnemyFactory factory = new BossEnemyFactory();
                        enemyAircrafts.add(factory.createEnemy(bossHpRate));
                    }
                }
                if (enemyAircrafts.size() < enemyMaxNumber) {
                    Random r = new Random();
                    int randomNumber = r.nextInt(100);
                    AbstractEnemyFactory factory;
                    if(randomNumber < eliteEnemyAppear) {factory = new EliteEnemyFactory();}
                    else {factory = new MobEnemyFactory();}
                    AbstractAircraft enemyAircraft = factory.createEnemy(enemyHpRate);
                    enemyAircrafts.add(enemyAircraft);
                    aircraftObserver.addAircraft(enemyAircraft);
                }
            }

            heroMoving();

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            // 撞击检测
            crashCheckAction();

            // 后处理
            postProcessAction();

            changeDifficulty();
            // 绘图
            draw();



        // 游戏结束检查
        if (heroAircraft.getHp() <= 0) {
            // 游戏结束,跳转结束页面
            Context context = getContext(); // from GameSurfaceView/Activity
            gameOverFlag = true;
//            myBinder.playGameOver();
            System.out.println("Game Over!");
            Intent intent = new Intent(context, EndActivity.class);
            context.startActivity(intent);


        }
        };

        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);
    }

    private boolean heroShootTimeCountAndNewCycleJudge() {
        heroShootCycleTime += timeInterval;
        if (heroShootCycleTime >= heroShootCycleDuration && heroShootCycleTime - timeInterval < heroShootCycleTime) {
            // 跨越到新的周期
            heroShootCycleTime %= heroShootCycleDuration;
            return true;
        } else {
            return false;
        }
    }
    private boolean enemyShootTimeCountAndNewCycleJudge() {
        enemyShootCycleTime += timeInterval;

        if (enemyShootCycleTime >= enemyShootCycleDuration && enemyShootCycleTime - timeInterval < enemyShootCycleTime) {
            // 跨越到新的周期
            enemyShootCycleTime %= enemyShootCycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private boolean enemyCreateTimeCountAndNewCycleJudge() {
        enemyCreateCycleTime += timeInterval;
        if (enemyCreateCycleTime >= enemyCreateCycleDuration && enemyCreateCycleTime - timeInterval < enemyCreateCycleTime) {
            // 跨越到新的周期
            enemyCreateCycleTime %= enemyCreateCycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void heroShootAction() {

        // 英雄射击
//        myBinder.playShoot();
        heroBullets.addAll(heroAircraft.shoot());
    }

    private void enemyShootAction() {
        // 敌机射击
//        myBinder.playShoot();
        for(AbstractAircraft aircraft : enemyAircrafts){
            List<BaseBullet> bullets = aircraft.shoot();
            enemyBullets.addAll(bullets);
            aircraftObserver.addBullets(bullets);
        }
    }


    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
        for (AbstractProp prop : allProp) {
            prop.forward();
        }
    }

    public void heroMoving(){
        heroAircraft.setLocation(GameActivity.getHeroLocationX(), GameActivity.getHeroLocationY());
    }

    public void changeDifficulty(){
        if(time % 10000 == 0 && time > 200 && enemyCreateCycleDuration > 200 ){
            if("medium".equals(difficulty)){
                bossHpRate *= 1.1;
                enemyHpRate *= 1.1;
                enemyCreateCycleDuration =(int) ((double)enemyCreateCycleDuration / 1.25);
                System.out.println("difficulty increase");


            }
            if("difficult".equals(difficulty)){
                bossHpRate *= 1.5;
                enemyHpRate *= 1.25;
                enemyCreateCycleDuration =(int) ((double)enemyCreateCycleDuration / 1.5);
                enemyShootCycleDuration = (int)((double) enemyShootCycleDuration / 1.25);
                heroShootCycleDuration *= 1.25;
            }
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                // 英雄机撞击到敌机子弹
                // 英雄机损失一定生命值
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
                aircraftObserver.removeBullet(bullet);
            }
        }
        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
//                    myBinder.playHit();
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    aircraftObserver.removeBullet(bullet);
                    if (enemyAircraft.notValid()) {
                        //  获得分数，产生道具补给
                        aircraftObserver.removeAircraft(enemyAircraft);
                        if(enemyAircraft.isBoss()){
//                            myBinder.playBgm();
                            bossExist = false;
                            bossDefeatNumber++;
                        }
                        if(enemyAircraft.leaveProp() != null) {allProp.addAll(enemyAircraft.leaveProp());}
                        score += 10;
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    aircraftObserver.addAircraft(enemyAircraft);
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 我方获得道具，道具生效
        for (AbstractProp prop : allProp) {
            if (prop.notValid()) {
                continue;
            }
            if (heroAircraft.crash(prop)) {
                // 英雄机捡到道具
                // 道具生效
//                myBinder.playSupply();
                score += prop.effect(heroAircraft, aircraftObserver);
                prop.vanish();
            }
        }
    }

    public static  int getScore(){
        return score;
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 删除无效道具
     * 4. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */


    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        allProp.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
    }


    //***********************
    //      Draw 各部分
    //***********************


    private void draw(){
        //通过SurfaceHolder对象的lockCanvans()方法，我们可以获取当前的Canvas绘图对象
        canvas = mSurfaceHolder.lockCanvas();
        //绘图的画布
        if(canvas == null){
            return;
        }

        //绘制背景，图片滚动
        canvas.drawBitmap(backGroundImage, 0, this.backGroundTop- ImageManager.BACKGROUND_IMAGE.getHeight(), mPaint);
        canvas.drawBitmap(backGroundImage, 0, this.backGroundTop, mPaint);
        backGroundTop += 1;
        if(backGroundTop == GameActivity.screenHeight)
            this.backGroundTop = 0;

        //绘制英雄机
        canvas.drawBitmap(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - (float)ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - (float)ImageManager.HERO_IMAGE.getHeight() / 2, mPaint);

        //绘制敌机和子弹
        paintImageWithPositionRevised(allProp);
        paintImageWithPositionRevised(enemyAircrafts);
        paintImageWithPositionRevised(enemyBullets);
        paintImageWithPositionRevised(heroBullets);

        paintScoreAndLife();

        //通过unlockCanvasAndPost(mCanvas)方法对画布内容进行提交
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void paintImageWithPositionRevised(List<? extends AbstractFlyingObject> flyingObjects){
        if(flyingObjects.size() != 0){
            for (int i = 0; i < flyingObjects.size(); i++) {
                AbstractFlyingObject flyingObject = flyingObjects.get(i);
                Bitmap image = flyingObject.getImage();
                assert image != null : flyingObject.getClass().getName() + " has no image! ";
                canvas.drawBitmap(image, flyingObject.getLocationX() - (float)image.getWidth() / 2 , flyingObject.getLocationY() - (float)image.getHeight() / 2, mPaint);
            }
        }
    }

    private void paintScoreAndLife() {
        int x = 25;
        int y = 60;
        mPaint.setTextSize(80);
        mPaint.setColor(Color.LTGRAY);
        canvas.drawText("SCORE:" + score, x, y, mPaint);
        y = y + 80;
        canvas.drawText("LIFE:" + this.heroAircraft.getHp(), x, y, mPaint);
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */

    public void run() {
        //设置一个循环来绘制，通过标志位来控制开启绘制还是停止
        synchronized (mSurfaceHolder){
            action();
        }

    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        new Thread(this).start();
    }
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        gameOverFlag = true;
    }
}
