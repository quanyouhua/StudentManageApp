package com.example.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener  {
    DBHelper dbHelper;
    List<user> userinfos;
    private TextView mTvLoginactivityRegister;
    private RelativeLayout mRlLoginactivityTop;
    private EditText mEtLoginactivityUsername;
    private EditText mEtLoginactivityPassword;
    private LinearLayout mLlLoginactivityTwo;
    private Button mBtLoginactivityLogin;
    private CheckBox cb1;
    String permission;
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
        setContentView(R.layout.login);
        initView();
        dbHelper = new DBHelper(this);
    }

    private void initView() {
        // 初始化控件
        mBtLoginactivityLogin = (Button) findViewById(R.id.bt_loginactivity_login);
        mTvLoginactivityRegister = (TextView) findViewById(R.id.tv_loginactivity_register);
        mRlLoginactivityTop = (RelativeLayout) findViewById(R.id.rl_loginactivity_top);
        mEtLoginactivityUsername = (EditText) findViewById(R.id.et_loginactivity_username);
        mEtLoginactivityPassword = (EditText) findViewById(R.id.et_loginactivity_password);
        mLlLoginactivityTwo = (LinearLayout) findViewById(R.id.ll_loginactivity_two);
        cb1= (CheckBox) findViewById(R.id.cb1);
        //SharedPreferences实现记住密码
        SharedPreferences sp=getSharedPreferences("userinfo",MODE_PRIVATE);
        boolean isremember=sp.getBoolean("isremember",false);
        if(isremember){
            String username=sp.getString("username",null);
            String password =sp.getString("password",null);
            mEtLoginactivityUsername.setText(username);
            mEtLoginactivityPassword.setText(password);
        }
        // 设置点击事件监听器
        mBtLoginactivityLogin.setOnClickListener(this);
        mTvLoginactivityRegister.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 跳转到注册界面
            case R.id.tv_loginactivity_register:
                Intent intent1 = new Intent(this, RegisterActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.changetheme:
                useMyTheme = !useMyTheme;//true
                recreate(); //重启画面
                SharedPreferences sp1=getSharedPreferences("theme",MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sp1.edit();
                editor1.putBoolean("myTheme",useMyTheme);
                editor1.commit();
                break;
            case R.id.iv_loginactivity_back:
                finish();
                System.exit(0);
                break;
            case R.id.bt_loginactivity_login:
                String username = mEtLoginactivityUsername.getText().toString().trim();
                String password = mEtLoginactivityPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
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
                    Log.e("abcde","用户名："+username);
                    Log.e("abcde","密码："+password);
                    boolean match = false;
                    for (int i = 0; i < userinfos.size(); i++) {
                        user ad = userinfos.get(i);
                        if (username.equals(ad.getUsername()) && password.equals(ad.getPassword())) {
                            match = true;
                            permission=ad.getPermission();
                            break;
                        } else {
                            match = false;
                        }
                    }
                    if (match) {
                       SharedPreferences sp= getSharedPreferences("userinfo",MODE_PRIVATE);
                        SharedPreferences.Editor editor= sp.edit();
                        editor.putString("username",username);
                        editor.putString("password",password);
                        if(cb1.isChecked()){
                            editor.putBoolean("isremember",true);
                        }else {
                            editor.clear();
                        }
                        editor.commit();

                        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                        if(permission.equals("manager")){
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.putExtra("username",username);
                            startActivity(intent);
                            finish();//销毁此Activity
                        }else if(permission.equals("student")){
                            Intent intent = new Intent(this, StuMainActivity.class);
                            intent.putExtra("username",username);
                            startActivity(intent);
                            finish();//销毁此Activity
                        }
                    } else {
                        Toast.makeText(this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入你的用户名或密码", Toast.LENGTH_SHORT).show();
                }
                    break;
                }
        }
}
