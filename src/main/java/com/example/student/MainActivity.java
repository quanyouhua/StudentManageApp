package com.example.student;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    DBHelper dbHelper;
    List<Student> Studentinfos;
    ListView lv;
    EditText edsearch;
    SQLiteDatabase db;
    RelativeLayout relativeLayout;
    Student student;
    Cursor cursor;
    List<Long> list;
    String stuid;
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
        setContentView(R.layout.main);
        dbHelper = new DBHelper(this);
        initView();
        list = new ArrayList<Long>();
    }

    private void initView() {
        // 初始化控件
        lv= (ListView) findViewById(R.id.lv);
        edsearch= (EditText) findViewById(R.id.edsearch);
        relativeLayout=(RelativeLayout) findViewById(R.id.rl_loginactivity_top);
        // 设置点击事件监听器
        lv.setOnItemClickListener(this);
        lv.setOnItemLongClickListener(this);
        lv.setOnCreateContextMenuListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        load();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TextView tvid=view.findViewById(R.id.tv_stu_id);
            stuid=tvid.getText().toString();
            Intent intent = new Intent(this, StuinfoActivity.class);
            intent.putExtra("id",stuid);
            intent.putExtra("pemission","manager");
            startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView tvid=view.findViewById(R.id.tv_stu_id);
        String id=tvid.getText().toString();
        lv.setTag(id);
        registerForContextMenu(lv);//给listview注册上下文菜单
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        stuid = (String) lv.getTag();
        Intent intent = new Intent();
        switch (item_id) {
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("删除信息提示")
                        .setMessage("确定删除该学生吗?")
                        .setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SQLiteDatabase db =dbHelper.getWritableDatabase();
                                int i = db.delete("student", "id=?", new String[]{stuid});
                                if (i == 0) {
                                    Toast.makeText(MainActivity.this, "数据库没有该数据", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                                }
                                load();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.look:
                // 查看学生信息
                Log.v("abcde", "TestSQLite+++++++look"+student+"");
                intent.putExtra("id",stuid);
                intent.setClass(this, StuinfoActivity.class);
                intent.putExtra("pemission","manager");
                this.startActivity(intent);
                break;
            case R.id.write:
                // 修改学生信息
                intent.putExtra("id", stuid);
                intent.setClass(this, StuupdateActivity.class);
                this.startActivity(intent);
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    class Myadapter extends BaseAdapter{
        @Override
        public int getCount() {
            return Studentinfos.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View layout;
            if(view!=null){
                layout=view;
            }else {
                layout= View.inflate(MainActivity.this, R.layout.listview_item, null);
            }

            ImageView iv = (ImageView) layout.findViewById(R.id.IamgeView_List);
            TextView tv_stu_id = (TextView) layout.findViewById(R.id.tv_stu_id);
            TextView tv_stu_name = (TextView) layout.findViewById(R.id.tv_stu_name);
            TextView tv_stu_sex = (TextView) layout.findViewById(R.id.tv_stu_sex);
            TextView tv_stu_age = (TextView) layout.findViewById(R.id.tv_stu_age);
            TextView tv_stu_collage = (TextView) layout.findViewById(R.id.tv_stu_collage);
            TextView tv_stu_banji = (TextView) layout.findViewById(R.id.tv_stu_banji);
            TextView tv_stu_adress = (TextView) layout.findViewById(R.id.tv_stu_adress);
            TextView tv_stu_phone = (TextView) layout.findViewById(R.id.tv_stu_phone);
            iv.setImageResource(R.drawable.student);
            tv_stu_id.setText(""+Studentinfos.get(i).getId());
            tv_stu_name.setText(Studentinfos.get(i).getName());
            tv_stu_sex.setText(Studentinfos.get(i).getSex());
            tv_stu_age.setText(""+Studentinfos.get(i).getAge());
            tv_stu_collage.setText(Studentinfos.get(i).getCollege());
            tv_stu_banji.setText(Studentinfos.get(i).getBanji());
            tv_stu_adress.setText(Studentinfos.get(i).getAdress());
            tv_stu_phone.setText(Studentinfos.get(i).getPhone());
//            cb.setVisibility(View.GONE);
            tv_stu_id.setTextColor(Color.BLACK);
            tv_stu_name.setTextColor(Color.BLACK);
            tv_stu_sex.setTextColor(Color.BLACK);
            return layout;
        }
    }

    public void onClick(View V) {
        switch (V.getId()) {
            case R.id.iv_back:
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.tv_changepwd:
                Intent intent=getIntent();
                String username= intent.getStringExtra("username");
                Intent intent3 = new Intent(this, changepwdActivity.class);
                intent3.putExtra("username",username);
                startActivity(intent3);
                 break;
            case R.id.btn_searchbyid:
                String id = edsearch.getText().toString();
                if (!TextUtils.isEmpty(id)) {
                    db = dbHelper.getWritableDatabase();
                    Studentinfos = new ArrayList<Student>();
                    cursor = db.query("student", null, "id=?", new String[]{id}, null, null, null);
                    if (cursor.getCount() == 0) {
                        Toast.makeText(this, "没有该id对应的学生！", Toast.LENGTH_SHORT).show();
                    }
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
                        Studentinfos.add(student);
                    }
                    lv.setAdapter(new Myadapter());
                    cursor.close();
                    db.close();
                } else {
                    Toast.makeText(this, "请输入学号后再试！！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_searchbyname:
                String name = edsearch.getText().toString();
                if (!TextUtils.isEmpty(name)) {
                    db = dbHelper.getWritableDatabase();
                    Studentinfos = new ArrayList<Student>();
                    cursor = db.query("student", null, "name=?", new String[]{name}, null, null, null);
                    if (cursor.getCount() == 0) {
                        Toast.makeText(this, "没有该name对应的学生！", Toast.LENGTH_SHORT).show();
                    }
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
                        Studentinfos.add(student);
                    }
                    lv.setAdapter(new Myadapter());
                    cursor.close();
                    db.close();
                } else {
                    Toast.makeText(this, "请输入姓名后再试！！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_searchall:
                db = dbHelper.getWritableDatabase();
                Studentinfos = new ArrayList<Student>();
                cursor = db.query("student", null, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    Student student = new Student();
                    student.setId(cursor.getInt(0));
                    student.setName(cursor.getString(cursor.getColumnIndex("name")));
                    student.setAge(cursor.getInt(cursor.getColumnIndex("age")));
                    student.setSex(cursor.getString(cursor.getColumnIndex("sex")));
                    student.setCollege(cursor.getString(cursor.getColumnIndex("college")));
                    student.setBanji(cursor.getString(cursor.getColumnIndex("banji")));
                    student.setAdress(cursor.getString(cursor.getColumnIndex("adress")));
                    student.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                    Studentinfos.add(student);
                    student = null;
                }
                lv.setAdapter(new Myadapter());
                Toast.makeText(this, "查询成功", Toast.LENGTH_LONG).show();

                db.close();
                break;
            case R.id.btn_add_student:
                Intent intent2 = new Intent(this, StuaddActivity.class);
                startActivity(intent2);
                break;
        }
    }


    // 自定义一个加载数据库中的全部记录到当前页面的无参方法
    public void load() {
        db = dbHelper.getWritableDatabase();
        Studentinfos = new ArrayList<Student>();
        cursor = db.query("student", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Student student = new Student();
            student.setId(cursor.getInt(0));
            student.setName(cursor.getString(cursor.getColumnIndex("name")));
            student.setAge(cursor.getInt(cursor.getColumnIndex("age")));
            student.setSex(cursor.getString(cursor.getColumnIndex("sex")));
            student.setCollege(cursor.getString(cursor.getColumnIndex("college")));
            student.setBanji(cursor.getString(cursor.getColumnIndex("banji")));
            student.setAdress(cursor.getString(cursor.getColumnIndex("adress")));
            student.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            Studentinfos.add(student);
            student = null;
        }
        lv.setAdapter(new Myadapter());
        Toast.makeText(this, "查询成功", Toast.LENGTH_LONG).show();
        db.close();
    }



}
