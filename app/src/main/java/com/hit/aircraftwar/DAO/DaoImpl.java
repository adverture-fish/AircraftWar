package com.hit.aircraftwar.DAO;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.hit.aircraftwar.activity.EndActivity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qh
 * 接口使用DAO
 * 具体实现数据增删查
 */
public class DaoImpl implements DAO {

    public DaoImpl(){

    }

    @Override
    public void doDelete() {

    }

    @Override
    public void doAdd(List<Record> data, String path, AppCompatActivity activity) throws IOException {
        try {
            FileOutputStream fos = activity.openFileOutput(path, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            data.sort((x, y) -> Integer.compare(y.getScore(), x.getScore()));
            for (Record record1 : data) {
                oos.writeObject(record1);
            }
            oos.close();
            fos.close();
        } catch (EOFException ignored) {

        }


    }

    @Override
    public List<Record> getAll(String path, AppCompatActivity activity) throws IOException{
        List<Record> records = new ArrayList<>();
        Record record;
        try {
            FileInputStream fin = activity.openFileInput(path);
            ObjectInputStream ois = new ObjectInputStream(fin);
            record = (Record) ois.readObject();
            while(record != null){
                records.add(record);
                record = (Record) ois.readObject();
            }
        } catch(EOFException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return records;
    }
}

