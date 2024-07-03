package com.example.student;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class changepwdActivity extends AppCompatActivity {
    EditText oldpsw,newpsw1,newpsw2;
    DBHelper dbHelper;
    SQLiteDatabase db;
    user user;
    String username;
    Cursor cursor;
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
        setContentView(R.layout.changepwd);
        oldpsw= (EditText) findViewById(R.id.et_oldpassword);
        newpsw1= (EditText) findViewById(R.id.newpassword1);
        newpsw2= (EditText) findViewById(R.id.newpassword2);
        dbHelper = new DBHelper(this);
        intent = getIntent();
        username = intent.getStringExtra("username");
    }
    public void onClick(View view) {
        switch (view.getId()) {
            // 跳转到注册界面
            case R.id.iv_back:
                db = dbHelper.getWritableDatabase();
                cursor = db.query("user", null, "username=?", new String[]{username}, null, null, null);
                while (cursor.moveToNext()) {
                    user = new user();
                    user.setId(cursor.getInt(0));
                    user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                    user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                    user.setPermission(cursor.getString(cursor.getColumnIndex("permission")));
                }
                cursor.close();
                db.close();
                if(user.getPermission().equals("student")){
                    intent =new Intent(this,StuMainActivity.class);
                    intent.putExtra("username",user.getUsername());
                    startActivity(intent);
                }else if(user.getPermission().equals("manager")){
                    intent =new Intent(this,MainActivity.class);
                    intent.putExtra("username",user.getUsername());
                    startActivity(intent);
                }
                break;
            case R.id.bt_xiugai:
                String oldpassword = oldpsw.getText().toString();
                String newpasswor1 = newpsw1.getText().toString();
                String newpasswor2 = newpsw2.getText().toString();
                db = dbHelper.getWritableDatabase();
                cursor = db.query("user", null, "username=?", new String[]{username}, null, null, null);
                while (cursor.moveToNext()) {
                    user = new user();
                    user.setId(cursor.getInt(0));
                    user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                    user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                    user.setPermission(cursor.getString(cursor.getColumnIndex("permission")));
                }
                if (!TextUtils.isEmpty(oldpassword) && !TextUtils.isEmpty(newpasswor1) && !TextUtils.isEmpty(newpasswor2)) {
                    if (oldpassword.equals(user.getPassword())) {
                        if (newpasswor1.equals(newpasswor2)) {
                            if(!newpasswor1.equals(oldpassword)){
                                db = dbHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put("password", newpasswor1);
                                int i = db.update("user", values, "username=?", new String[]{username});
                                if (i == 0) {
                                    Toast.makeText(this, "修改失败", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(this, "修改成功", Toast.LENGTH_LONG).show();
                                    cursor.close();
                                    db.close();
                                    oldpsw.setText(null);
                                    newpsw1.setText(null);
                                    newpsw2.setText(null);

                                    if(user.getPermission().equals("student")){
                                        intent =new Intent(this,StuMainActivity.class);
                                        intent.putExtra("username",username);
                                        startActivity(intent);
                                    }else if(user.getPermission().equals("manager")){
                                        intent =new Intent(this,MainActivity.class);
                                        intent.putExtra("username",username);
                                        startActivity(intent);
                                    }
                                }
                            }else {
                                Toast.makeText(this, "新密码不能和原密码一致！！", Toast.LENGTH_LONG).show();
                                newpsw1.setText(null);
                                newpsw2.setText(null);
                            }

                        }else{
                            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_LONG).show();
                            newpsw1.setText(null);
                            newpsw2.setText(null);
                        }
                    } else {
                        Toast.makeText(this, "原密码错误，请重新输入！！", Toast.LENGTH_LONG).show();
                        oldpsw.setText(null);
                    }
                } else {
                    Toast.makeText(this, "原密码以及新密码不能为空！！", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }
}