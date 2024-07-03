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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class StuupdateActivity extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase db;
    Student student;
    Cursor cursor;
    String id;
    Button btn_update;
    EditText stuid,stuname,stuage,stuphone,stuadress;
    Spinner spcollege,spbanji;
    RadioButton radiobutton;
    RadioGroup rdgroup;
    ArrayAdapter<String> banjiAdapter;
    ArrayAdapter<String>  collegeAdapter;
    String college,banji;
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
        setContentView(R.layout.updatestudent);
        dbHelper = new DBHelper(this);
        initView();

        setSpinner();
        chuangzhi();
    }

    private void initView() {
        // 初始化控件
         stuid= (EditText) findViewById(R.id.stuid);
        stuname= (EditText) findViewById(R.id.stuname);
        stuage= (EditText) findViewById(R.id.stuage);
         rdgroup= (RadioGroup) findViewById(R.id.rdgroup);
        spcollege= (Spinner) findViewById(R.id.spcollege);
        spbanji= (Spinner) findViewById(R.id.spbanji);
        stuadress= (EditText) findViewById(R.id.stuadress);
        stuphone= (EditText) findViewById(R.id.stuphone);
        //设置监听
        rdgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            }
        });
    }
    public void setSpinner() {
        //绑定适配器和值
        collegeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, collegelist);
        spcollege.setAdapter(collegeAdapter);
        spcollege.setSelection(1,true);
        //绑定适配器和值
        banjiAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jisuanji);
        spbanji.setAdapter(banjiAdapter);
        spbanji.setSelection(1,true);

        spcollege.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                college=adapterView.getItemAtPosition(i).toString();
                if(college.equals("计算机与信息技术学院")){
                    banjiAdapter = new ArrayAdapter<String>(StuupdateActivity.this,android.R.layout.simple_spinner_item, jisuanji);
                    spbanji.setAdapter(banjiAdapter);
                }else if (college.equals("软件学院")){
                    banjiAdapter = new ArrayAdapter<String>(StuupdateActivity.this,android.R.layout.simple_spinner_item, ruanjian);
                    spbanji.setAdapter(banjiAdapter);
                }else if(college.equals("石油工程学院")){
                    banjiAdapter = new ArrayAdapter<String>(StuupdateActivity.this,android.R.layout.simple_spinner_item, shiyou);
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

    private void chuangzhi(){
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
//      Toast.makeText(this,"id"+id,Toast.LENGTH_SHORT).show();
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
        cursor.close();
        db.close();
        stuid.setText(""+student.getId());
        stuid.setEnabled(false);
        stuname.setText(student.getName());
        stuage.setText(""+student.getAge());
        stuadress.setText(student.getAdress());
        stuphone.setText(student.getPhone());
        String sex= student.getSex();
        if(sex.equals("男")){
            rdgroup.check(R.id.rdman);
        }else {
            rdgroup.check(R.id.rdwoman);
        }
        college= student.getCollege();
        banji= student.getBanji();
        Toast.makeText(this,college+banji,Toast.LENGTH_SHORT).show();
        if(college.equals("计算机与信息技术院")){
            spcollege.setSelection(1,true);
            if(banji.equals("计科181班")){
                spbanji.setSelection(1,true);
            }else if(banji.equals("计科182班")){
                spbanji.setSelection(2,true);
            }else if(banji.equals("计科183班")){
                spbanji.setSelection(3,true);
            }
        }else if(college.equals("软件学院")){
            spcollege.setSelection(2,true);
            if(banji.equals("软工181班")){
                spbanji.setSelection(1,true);
            }else if(banji.equals("软工182班")){
                spbanji.setSelection(2,true);
            }else if(banji.equals("软工183班")){
                spbanji.setSelection(3,true);
            }
        } if(college.equals("石油工程学院")){
            spcollege.setSelection(3,true);
            if(banji.equals("油工181班")){
                spbanji.setSelection(1,true);
            }else if(banji.equals("油工182班")){
                spbanji.setSelection(2,true);
            }else if(banji.equals("油工183班")){
                spbanji.setSelection(3,true);
            }
        }
//        if(banji.equals("计科181班") || banji.equals("油工181班") || banji.equals("软工181班")){
//            banjiAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ruanjian);
//            spbanji.setAdapter(banjiAdapter);
//            spbanji.setSelection(1,true);
//        }else if(banji.equals("计科182班") || banji.equals("油工182班") || banji.equals("软工182班")){
//            banjiAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ruanjian);
//            spbanji.setAdapter(banjiAdapter);
//            spbanji.setSelection(2,true);
//        }else if(banji.equals("计科183班") || banji.equals("油工183班") || banji.equals("软工183班")){
//            banjiAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ruanjian);
//            spbanji.setAdapter(banjiAdapter);
//            spbanji.setSelection(3,true);
//        }
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_update:
                radiobutton=(RadioButton)findViewById(rdgroup.getCheckedRadioButtonId());
                String name=stuname.getText().toString();
                String age= stuage.getText().toString();
                String phone=stuphone.getText().toString();
                String adress=stuadress.getText().toString();
                int length1=age.length();
                int length2=phone.length();
                if(!TextUtils.isEmpty(adress) && !TextUtils.isEmpty(name)&& !TextUtils.isEmpty(age)  && !TextUtils.isEmpty(phone)){
                    if(length1>0 && length1<3){
                        if(!(college.equals("请选择学院") || banji.equals("请选择班级"))){
                            if(length2==11){
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put("name",stuname.getText().toString());
                                values.put("age",stuage.getText().toString());
                                values.put("sex",radiobutton.getText().toString());
                                values.put("college",college);
                                values.put("banji",banji);
                                values.put("phone",stuphone.getText().toString());
                                values.put("adress",stuadress.getText().toString());
                                int i = db.update("student", values, "id=?", new String[]{id});
                                if (i == 0) {
                                    Toast.makeText(this, "修改失败", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(this, "修改成功", Toast.LENGTH_LONG).show();
                                    Intent intent2 = new Intent(this, MainActivity.class);
                                    startActivity(intent2);
                                }
                            }else {
                                Toast.makeText(this, "手机号位数不正确，应为13位！！！", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(this, "请选择学院或班级后再试！！！", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(this, "年龄超出范围，请输入0-99之间的数字！！！", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(this, "姓名，年龄，电话，地址不能为空！！！", Toast.LENGTH_LONG).show();
                }

//                finish();
                break;
            case R.id.iv_back:
            Intent intent1 = new Intent(this, MainActivity.class);
            startActivity(intent1);
            break;
        }
    }
}
