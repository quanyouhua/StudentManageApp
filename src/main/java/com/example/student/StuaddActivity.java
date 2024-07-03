package com.example.student;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class StuaddActivity extends AppCompatActivity {
    DBHelper dbHelper;
    EditText stuid,stuname,stuage,stuphone,stuadress;
    Spinner spcollege,spbanji;
    RadioButton radiobutton;
    RadioGroup rdgroup;
    SQLiteDatabase db;
    ContentValues values;
    ArrayAdapter<String>  banjiAdapter;
    ArrayAdapter<String>  collegeAdapter;
    List<user> userinfos;
    String college="请选择学院",banji="请选择班级";
    String[] collegelist = new String[] {"请选择学院","计算机与信息技术学院","软件学院","石油工程学院"};
    String[] jisuanji = new String[] {"请选择班级","计科181班","计科182班","计科183班"};
    String[] shiyou = new String[] {"请选择班级","油工181班","油工182班","油工183班"};
    String[] ruanjian = new String[] {"请选择班级","软工181班","软工182班","软工183班"};
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
        setContentView(R.layout.addstudent);
        initView();
        dbHelper = new DBHelper(this);
        setSpinner();
    }
    private void initView() {
        // 初始化控件
        stuid= (EditText) findViewById(R.id.stuid);
        stuname= (EditText) findViewById(R.id.stuname);
        stuage= (EditText) findViewById(R.id.stuage);
        stuphone= (EditText) findViewById(R.id.stuphone);
        stuadress= (EditText) findViewById(R.id.stuadress);
        rdgroup=(RadioGroup)findViewById(R.id.rdgroup);
        rdgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            }
        });
    }
    public void setSpinner() {
        spcollege= (Spinner) findViewById(R.id.spcollege);
        spbanji= (Spinner) findViewById(R.id.spbanji);
        //绑定适配器和值
        collegeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, collegelist);
        spcollege.setAdapter(collegeAdapter);
        spcollege.setSelection(0,true);
        //绑定适配器和值
        banjiAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jisuanji);
        spbanji.setAdapter(banjiAdapter);
        spbanji.setSelection(0,true);
        spcollege.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                college=adapterView.getItemAtPosition(i).toString();
                if(college.equals("计算机与信息技术学院")){
                    banjiAdapter = new ArrayAdapter<String>(StuaddActivity.this,android.R.layout.simple_spinner_item, jisuanji);
                    spbanji.setAdapter(banjiAdapter);
                }else if (college.equals("软件学院")){
                    banjiAdapter = new ArrayAdapter<String>(StuaddActivity.this,android.R.layout.simple_spinner_item, ruanjian);
                    spbanji.setAdapter(banjiAdapter);
                }else if(college.equals("石油工程学院")){
                    banjiAdapter = new ArrayAdapter<String>(StuaddActivity.this,android.R.layout.simple_spinner_item, shiyou);
                    spbanji.setAdapter(banjiAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spbanji.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                banji=adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_loginactivity_back: //返回主页面
                    Intent intent1 = new Intent(this, MainActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.btn_add_student:
                    radiobutton=(RadioButton)findViewById(rdgroup.getCheckedRadioButtonId());
                    String id=stuid.getText().toString();
                    int length=id.length();
                    String name=stuname.getText().toString();
                    String sex=radiobutton.getText().toString();
                    String age= stuage.getText().toString();
                    int length1=age.length();
                    String phone=stuphone.getText().toString();
                    int length2=phone.length();
                    String adress=stuadress.getText().toString();
                    if(!TextUtils.isEmpty(id) && !TextUtils.isEmpty(name)&& !TextUtils.isEmpty(age)  && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(adress)){
                      if( length> 0 && length<10){
                          if(length1>0 && length1<3){
                              if(!(college.equals("请选择学院") || banji.equals("请选择班级"))){
                                  if(length2==11){
                                      if(matchId(id)){
                                          db = dbHelper.getWritableDatabase();
                                          values = new ContentValues();
                                          values.put("id",id);
                                          values.put("name",name);
                                          values.put("sex",sex);
                                          values.put("age",age);
                                          values.put("phone",phone);
                                          values.put("college",college);
                                          values.put("banji",banji);
                                          values.put("adress",adress);
                                          long l = db.insert("student", null, values);
                                          if (l == -1) {
                                              Toast.makeText(this, "添加失败,该id已存在!!", Toast.LENGTH_SHORT).show();
                                          }else {
                                              Toast.makeText(this, "添加成功" +"新增id为：" +l, Toast.LENGTH_LONG).show();
                                              db = dbHelper.getWritableDatabase();
                                              values = new ContentValues();
                                              values.put("username",id);
                                              values.put("password",id);
                                              values.put("permission","student");
                                              long r = db.insert("user", null, values);
                                              if (r== -1) {
                                                  Toast.makeText(this, "该id在用户表中已存在！"+l,Toast.LENGTH_SHORT).show();
                                              }else {
                                                  Toast.makeText(this, "插入用户表成功！"+l,Toast.LENGTH_SHORT).show();
                                              }
                                              Intent intent2 = new Intent(this, MainActivity.class);
                                              startActivity(intent2);
                                          }
                                      }else {
                                          Toast.makeText(this, "该id在用户表中已存在！！",Toast.LENGTH_SHORT).show();
                                          stuid.setText(null);
                                      }
                                  }else {
                                      Toast.makeText(this, "手机号位数不正确，应为13位！！！", Toast.LENGTH_SHORT).show();
                                  }

                              }else {
                                  Toast.makeText(this, "请选择学院和班级后再试！！！", Toast.LENGTH_SHORT).show();
                              }
                          }else {
                              Toast.makeText(this, "年龄超出范围，请输入0-99之间的数字！！！", Toast.LENGTH_SHORT).show();
                          }

                      }else {
                          Toast.makeText(this, "id号超出范围，请输入0-999999999之间的数字！！！", Toast.LENGTH_SHORT).show();
                          stuid.setText(null);
                      }
                    }else {
                        Toast.makeText(this, "还有信息未输入，请输入后再试", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_add_reset:
                    stuid.setText(null);
                    stuname.setText(null);
                    stuage.setText(null);
                    stuphone.setText(null);
                    stuadress.setText(null);
                    break;
            }
    }
    //将输入的id与用户表中的用户名作比对
    public boolean matchId(String id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        userinfos = new ArrayList<user>();
        Cursor cursor = db.query("user", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            user ad = new user();
            ad.setId(cursor.getInt(0));
            ad.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            ad.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            ad.setPermission(cursor.getString(cursor.getColumnIndex("permission")));
            userinfos.add(ad);
            ad =null;
        }
        boolean match=true;
        for (int i = 0; i < userinfos.size(); i++) {
            user ad = userinfos.get(i);
            if (id.equals(ad.getUsername())) {
                match = false;
                break;
            } else {
                match = true;
            }
        }
        return  match;
    }
}
