package com.hit.aircraftwar.application;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import com.hit.aircraftwar.R;
import com.hit.aircraftwar.aircraft.BossEnemy;
import com.hit.aircraftwar.aircraft.EliteEnemy;
import com.hit.aircraftwar.aircraft.HeroAircraft;
import com.hit.aircraftwar.aircraft.MobEnemy;
import com.hit.aircraftwar.bullet.EnemyBullet;
import com.hit.aircraftwar.bullet.HeroBullet;
import com.hit.aircraftwar.prop.BombSupplyProp;
import com.hit.aircraftwar.prop.FireSupplyProp;
import com.hit.aircraftwar.prop.HpSupplyProp;

import java.util.HashMap;
import java.util.Map;

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 *
 * @author lxl,qh
 */
public class ImageManager {

    /**
     * 类名-图片 映射，存储各基类的图片
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    public static final Map<String, Bitmap> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public static Bitmap BACKGROUND_IMAGE;
    public static Bitmap HERO_IMAGE;
    public static Bitmap HERO_BULLET_IMAGE ;
    public static Bitmap ENEMY_BULLET_IMAGE ;
    public static Bitmap MOB_ENEMY_IMAGE;
    public static Bitmap ELITE_ENEMY_IMAGE;
    public static Bitmap BOSS_ENEMY_IMAGE;
    public static Bitmap PROP_BLOOD_IMAGE;
    public static Bitmap PROP_BOMB_IMAGE;
    public static Bitmap PROP_BULLET_IMAGE;
    public static Bitmap BACKGROUND_IMAGE_MEDIUM;
    public static Bitmap BACKGROUND_IMAGE_DIFFICULT;



    public static Bitmap get(String className){
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public static Bitmap get(Object obj){
        if (obj == null){
            return null;
        }
        return get(obj.getClass().getName());
    }

}
