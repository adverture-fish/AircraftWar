package com.hit.aircraftwar.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.hit.aircraftwar.DAO.DAO;
import com.hit.aircraftwar.DAO.DaoImpl;
import com.hit.aircraftwar.DAO.Record;
import com.hit.aircraftwar.R;
import com.hit.aircraftwar.application.GameSurfaceView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EndActivity extends AppCompatActivity {
    private String name = "test";

    private List<Record> res = new ArrayList<>();

    private DAO dao;

    private String path = "java/com/hit/aircraftwar/rankingList/easyRanking.dat";

    private String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        if("medium".equals(difficulty)){
            path = "app/src/main/res/rankingList/ranking.dat";
        }
        else if ("difficult".equals(difficulty)) {
            path = "app/src/main/java/com/hit/aircraftwar/rankingList/difficultRanking.dat";
        }
        ListView listView = (ListView) findViewById(R.id.rankingList);
        dao = new DaoImpl();
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        Record data = new Record(name, dateFormat.format(date), GameSurfaceView.getScore());
        try {
            res = dao.getAll(path);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        res.add(data);
        try {
            dao.doAdd(res, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayAdapter<Record> adapter = new ArrayAdapter<>(EndActivity.this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, res);
        listView.setAdapter(adapter);
        System.out.println(res);
        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res.remove(0);
                adapter.notifyDataSetChanged();
            }
        });
    }
}