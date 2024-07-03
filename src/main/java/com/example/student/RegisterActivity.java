package com.example.student;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private String realCode;
    private DBHelper dbHelper;
    private Button mBtRegisteractivityRegister;
    private RelativeLayout mRlRegisteractivityTop;
    private ImageView mIvRegisteractivityBack;
    private LinearLayout mLlRegisteractivityBody;
    private EditText mEtRegisteractivityUsername;
    private EditText mEtRegisteractivityPassword1;
    private EditText mEtRegisteractivityPassword2;
    private EditText mEtRegisteractivityPhonecodes;
    private ImageView mIvRegisteractivityShowcode;
    private RelativeLayout mRlRegisteractivityBottom;
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
        setContentView(R.layout.register);
        initView();
        dbHelper = new DBHelper(this);
        //将验证码用图片的形式显示出来
        mIvRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
    }
    private void initView(){
        mBtRegisteractivityRegister= (Button) findViewById(R.id.bt_registeractivity_register);
        mRlRegisteractivityTop = (RelativeLayout) findViewById(R.id.rl_registeractivity_top);
        mIvRegisteractivityBack = (ImageView) findViewById(R.id.iv_registeractivity_back);
        mLlRegisteractivityBody = (LinearLayout) findViewById(R.id.ll_registeractivity_body);
        mEtRegisteractivityUsername = (EditText) findViewById(R.id.et_registeractivity_username);
        mEtRegisteractivityPassword1 = (EditText) findViewById(R.id.et_registeractivity_password1);
        mEtRegisteractivityPassword2 = (EditText) findViewById(R.id.et_registeractivity_password2);
        mEtRegisteractivityPhonecodes = (EditText) findViewById(R.id.et_registeractivity_phoneCodes);
        mIvRegisteractivityShowcode = (ImageView) findViewById(R.id.iv_registeractivity_showCode);
        mRlRegisteractivityBottom = (RelativeLayout) findViewById(R.id.rl_registeractivity_bottom);

        /**
         * 注册页面能点击的就三个地方
         * top处返回箭头、刷新验证码图片、注册按钮
         */
        mIvRegisteractivityBack.setOnClickListener(this);
        mIvRegisteractivityShowcode.setOnClickListener(this);
        mBtRegisteractivityRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_registeractivity_back: //返回登录页面
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.iv_registeractivity_showCode:    //改变随机验证码的生成
                mIvRegisteractivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;
            case R.id.bt_registeractivity_register:    //注册按钮
                //获取用户输入的用户名、密码、验证码
                String username = mEtRegisteractivityUsername.getText().toString().trim();
                String password1 = mEtRegisteractivityPassword1.getText().toString().trim();
                String password2 = mEtRegisteractivityPassword2.getText().toString().trim();
                String phoneCode = mEtRegisteractivityPhonecodes.getText().toString().toLowerCase();
                //注册验证
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password1) && !TextUtils.isEmpty(password2) && !TextUtils.isEmpty(phoneCode) ) {
                    if(password1.equals(password2)) {
                        if (phoneCode.equals(realCode)) {
                            //将用户名和密码加入到数据库中
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("username",username);
                            values.put("password",password1);
                            values.put("permission","manager");
                            long l = db.insert("user", null, values);
                            if (l == -1) {
                                Toast.makeText(this, "注册失败，该id已存在！！"+l,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(this, "验证通过，注册成功！"+ l,Toast.LENGTH_LONG).show();
                                Intent intent2 = new Intent(this, MainActivity.class);
                                startActivity(intent2);
                            }
                        } else {
                            Toast.makeText(this, "验证码错误,注册失败！", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this, "两次密码不一致！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "未完善信息，注册失败！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
