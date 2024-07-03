package com.example.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StuMainActivity extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase db;
    Student student;
    Cursor cursor;
    String stuid;
    String username;
    Intent intent;
    boolean useMyTheme=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp1=getSharedPreferences("theme",MODE_PRIVATE);
        useMyTheme= sp1.getBoolean("myTheme",true);
        if(useMyTheme){
            setTheme(R.style.myTheme);
        }else{
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.stu_mian);
        dbHelper = new DBHelper(this);

        TextView tvname= (TextView) findViewById(R.id.tvname);
        Intent intent=getIntent();
        username=intent.getStringExtra("username");


        db = dbHelper.getWritableDatabase();
        cursor = db.query("student", null, "id=?", new String[]{username}, null, null, null);
        while (cursor.moveToNext()) {
            student = new Student();
            student.setId(cursor.getInt(0));
            student.setName(cursor.getString(cursor.getColumnIndex("name")));
            student.setAge(cursor.getInt(cursor.getColumnIndex("age")));
            student.setSex(cursor.getString(cursor.getColumnIndex("sex")));
            student.setCollege(cursor.getString(cursor.getColumnIndex("college")));
            student.setBanji(cursor.getString(cursor.getColumnIndex("banji")));
            student.setAdress(cursor.getString(cursor.getColumnIndex("adress")));
            student.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
        }
        cursor.close();
        db.close();
        tvname.setText("欢迎您, "+student.getName()+"同学,"+"你可以进行以下操作！");
    }
    public void onClick(View V){
        switch (V.getId()) {
            case R.id.iv_back: //返回登录页面
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_personinfo:
                stuid= String.valueOf(student.getId());
                intent = new Intent(this, StuinfoActivity.class);
                intent.putExtra("id",stuid);
                intent.putExtra("pemission","student");
                startActivity(intent);
                break;
            case R.id.btn_changepwd:
                intent = new Intent(this, changepwdActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
                break;
        }
    }

}
