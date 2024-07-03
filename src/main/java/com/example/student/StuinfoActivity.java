package com.example.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class StuinfoActivity extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase db;
    Student student;
    user user;
    Cursor cursor;
    Intent intent;
    String id,pemission;
    Button btn_back;
    boolean useMyTheme=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //读取保存的boolean类型的值，更改主题
        SharedPreferences sp1=getSharedPreferences("theme",MODE_PRIVATE);
        useMyTheme= sp1.getBoolean("myTheme",true);
        if(useMyTheme){
            setTheme(R.style.myTheme);
        }else{
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.studentinfo);
        TextView tvid= (TextView) findViewById(R.id.tv_stu_id);
        TextView tvname= (TextView) findViewById(R.id.tv_stu_name);
        TextView tvage= (TextView) findViewById(R.id.tv_stu_age);
        TextView tvsex= (TextView) findViewById(R.id.tv_stu_sex);
        TextView tvcollage= (TextView) findViewById(R.id.tv_stu_collage);
        TextView tvbanji= (TextView) findViewById(R.id.tv_stu_banji);
        TextView tvadress= (TextView) findViewById(R.id.tv_stu_adress);
        TextView tvphone= (TextView) findViewById(R.id.tv_stu_phone);
        dbHelper = new DBHelper(this);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        pemission=intent.getStringExtra("pemission");
        Toast.makeText(this,pemission,Toast.LENGTH_SHORT).show();

        db = dbHelper.getWritableDatabase();
        cursor = db.query("student", null, "id=?", new String[]{id}, null, null, null);
        student = new Student();
        while (cursor.moveToNext()) {
                student.setId(cursor.getInt(0));
                student.setName(cursor.getString(cursor.getColumnIndex("name")));
                student.setAge(cursor.getInt(cursor.getColumnIndex("age")));
                student.setSex(cursor.getString(cursor.getColumnIndex("sex")));
                student.setCollege(cursor.getString(cursor.getColumnIndex("college")));
                student.setBanji(cursor.getString(cursor.getColumnIndex("banji")));
                student.setAdress(cursor.getString(cursor.getColumnIndex("adress")));
                student.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
        }
            tvid.setText("学号："+student.getId());
            tvname.setText("姓名："+student.getName());
            tvage.setText("年龄："+student.getAge());
            tvsex.setText("性别："+student.getSex());
            tvcollage.setText("所属院系："+student.getCollege());
            tvbanji.setText("班级："+student.getBanji());
            tvadress.setText("家庭住址："+student.getAdress());
            tvphone.setText("联系方式："+student.getPhone());
            cursor.close();
            db.close();
    }
    public void onClick(View v){
        if(pemission.equals("student")){
            intent =new Intent(this,StuMainActivity.class);
            String username= String.valueOf(student.getId());
            intent.putExtra("username",username);
            startActivity(intent);
        }else if(pemission.equals("manager")){
            intent =new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }
}
