package com.example.callender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.callender.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public String fname=null;
    public String str=null;
    public CalendarView calendarView;
    public Button cha_Btn,del_Btn,save_Btn,select_Btn;
    public TextView textView2;
    public EditText contextEditText;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        calendarView=findViewById(R.id.calendarView);

        select_Btn=findViewById(R.id.select_Btn);
        save_Btn=findViewById(R.id.save_Btn);
        del_Btn=findViewById(R.id.del_Btn);
        cha_Btn=findViewById(R.id.cha_Btn);
        textView2=findViewById(R.id.textView2);

        contextEditText=findViewById(R.id.contextEditText);
        //로그인 및 회원가입 엑티비티에서 이름을 받아옴
        Intent intent=getIntent();
        String name=intent.getStringExtra("userName");
        final String userID=intent.getStringExtra("userID");




        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                select_Btn.setVisibility(View.VISIBLE); // 운동선택
                save_Btn.setVisibility(View.VISIBLE); // 저장
                contextEditText.setVisibility(View.VISIBLE); // 내용을 입력하세요
                textView2.setVisibility(View.INVISIBLE); // SET TEXT
                cha_Btn.setVisibility(View.INVISIBLE); // 수정
                del_Btn.setVisibility(View.INVISIBLE); // 삭제


                checkDay(year,month,dayOfMonth,userID);
            }
        });
        final List<String> selecteditems = new ArrayList<>();

        select_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("운동종류를 선택하세요"); //제목
                final String[] versionArray = new String[] {"상체운동","하체운동","맨몸운동","유산소운동"};
                dlg.setIcon(R.drawable.startfit); // 아이콘 설정

                dlg.setSingleChoiceItems(versionArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contextEditText.setText(versionArray[which]);
                    }
                });
              //버튼 클릭시 동작
                dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "확인되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show();
            }
        });

        save_Btn.setOnClickListener(new View.OnClickListener() { // 저장버튼을 눌렀을때
            @Override
            public void onClick(View view) {
                saveDiary(fname);
                str=contextEditText.getText().toString();
                textView2.setText(str); // SET TEXT
                select_Btn.setVisibility(View.INVISIBLE); // 운동선택
                save_Btn.setVisibility(View.INVISIBLE); // 저장
                cha_Btn.setVisibility(View.VISIBLE); // 수정
                del_Btn.setVisibility(View.VISIBLE); // 삭제
                contextEditText.setVisibility(View.INVISIBLE); // 내용을 입력하세요
                textView2.setVisibility(View.VISIBLE); // SET TEXT

            }
        });
    }

    public void  checkDay(int cYear,int cMonth,int cDay,String userID){
        fname=""+userID+cYear+"-"+(cMonth+1)+""+"-"+cDay+".txt";//저장할 파일 이름설정
        FileInputStream fis=null;//FileStream fis 변수

        try{
            fis=openFileInput(fname);

            byte[] fileData=new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            str=new String(fileData);

            contextEditText.setVisibility(View.INVISIBLE); // 내용을 입력하세요
            textView2.setVisibility(View.VISIBLE); // SET TEXT
            textView2.setText(str);


            select_Btn.setVisibility(View.INVISIBLE); // 운동선택
            save_Btn.setVisibility(View.INVISIBLE); // 저장
            cha_Btn.setVisibility(View.VISIBLE); // 수정
            del_Btn.setVisibility(View.VISIBLE); // 삭제

            cha_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contextEditText.setVisibility(View.VISIBLE); // 내용을 입력하세요
                    textView2.setVisibility(View.INVISIBLE); // SET TEXT
                    contextEditText.setText(str);

                    select_Btn.setVisibility(View.VISIBLE); // 운동선택
                    save_Btn.setVisibility(View.VISIBLE); // 저장
                    cha_Btn.setVisibility(View.INVISIBLE); // 수정
                    del_Btn.setVisibility(View.INVISIBLE); // 삭제
                    textView2.setText(contextEditText.getText());
                }

            });
            del_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select_Btn.setVisibility(View.VISIBLE); // 운동선택
                    textView2.setVisibility(View.INVISIBLE); // SET TEXT
                    contextEditText.setText("");
                    contextEditText.setVisibility(View.VISIBLE); // 내용을 입력하세요
                    save_Btn.setVisibility(View.VISIBLE); // 저장
                    cha_Btn.setVisibility(View.INVISIBLE); // 수정
                    del_Btn.setVisibility(View.INVISIBLE); // 삭제
                    removeDiary(fname);
                }
            });
            if(textView2.getText()==null){
                textView2.setVisibility(View.INVISIBLE);// SET TEXT
                contextEditText.setText("");
                select_Btn.setVisibility(View.VISIBLE); // 운동선택
                save_Btn.setVisibility(View.VISIBLE); // 저장
                cha_Btn.setVisibility(View.INVISIBLE); // 수정
                del_Btn.setVisibility(View.INVISIBLE); // 삭제
                contextEditText.setVisibility(View.VISIBLE); // 내용을 입력하세요
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressLint("WrongConstant")
    public void removeDiary(String readDay){
        FileOutputStream fos=null;

        try{
            fos=openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
            String content="";
            fos.write((content).getBytes());
            fos.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressLint("WrongConstant")
    public void saveDiary(String readDay){
        FileOutputStream fos=null;

        try{
            fos=openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
            String content=contextEditText.getText().toString();
            fos.write((content).getBytes());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}