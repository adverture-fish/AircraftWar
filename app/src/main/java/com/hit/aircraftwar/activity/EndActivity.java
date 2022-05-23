package com.hit.aircraftwar.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

    private String path = "easyRanking.dat";

    private String difficulty;

    private int selectedNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        if("medium".equals(difficulty)){
            path = "mediumRanking.dat";
        }
        else if ("difficult".equals(difficulty)) {
            path = "difficultRanking.dat";
        }
        //名字获取
        AlertDialog.Builder nameDialog = new AlertDialog.Builder(EndActivity.this);
        nameDialog.setTitle("请输入你的名字:");
        final EditText editText = new EditText(EndActivity.this);
        nameDialog.setView(editText);
        nameDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = editText.getText().toString();
                dao = new DaoImpl();
                Date date = new Date();
                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
                Record data = new Record(name, dateFormat.format(date), GameSurfaceView.getScore());
                try {
                    res = dao.getAll(path, EndActivity.this);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                res.add(data);
                try {
                    dao.doAdd(res, path, EndActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ListView listView = (ListView) findViewById(R.id.rankingList);
                ArrayAdapter<Record> adapter = new ArrayAdapter<>(EndActivity.this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, res);
                listView.setAdapter(adapter);
                //获取点击的位置
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectedNumber = position;
                    }
                });
                System.out.println(res);
                Button deleteButton = (Button) findViewById(R.id.deleteButton);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        res.remove(selectedNumber);
                        try {
                            dao.doAdd(res, path, EndActivity.this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
        nameDialog.show();
        // 生成排行榜


    }
}