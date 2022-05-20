package com.hit.aircraftwar.strategy;

import com.hit.aircraftwar.aircraft.AbstractAircraft;
import com.hit.aircraftwar.application.ImageManager;
import com.hit.aircraftwar.bullet.BaseBullet;
import com.hit.aircraftwar.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 具体直射射击策略的类
 *
 * @author lxl,qh
 */

public class EnemyStraightShootStrategy implements ShootStrategy{
    @Override
    public List<BaseBullet> shootStrategy(int shootNum, int direction, int power, AbstractAircraft airCraft){
        List<BaseBullet> bullets = new LinkedList<>();
        int x = airCraft.getLocationX();
        int y = airCraft.getLocationY() + direction*2;
        int speedX = 0;
        int speedY = airCraft.getSpeedY() + direction*5;
        BaseBullet baseBullet;

        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            baseBullet = new EnemyBullet(x + (i*2 - shootNum + 1)*10, y + speedY + ImageManager.ENEMY_BULLET_IMAGE.getHeight() / 2, speedX, speedY, power);
            bullets.add(baseBullet);
        }
        return bullets;
    }
}
