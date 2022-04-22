package com.example.studenthelpdesk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RegisterCollege2_1 extends AppCompatActivity {
    LinearLayout ll;
    CollegeRegistrationData allData;
    TextView tot,endmsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_college2_1);
        ll=findViewById(R.id.linearL);
        endmsg=findViewById(R.id.msgend);
        tot=findViewById(R.id.no_of_departments);
        allData=RegisterCollege.allData;

        ArrayList<String> dept = allData.getDeptName();
        for(String a:dept)
        {
            CheckBox c=new CheckBox(this);
            c.setText(a);
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    endmsg.setVisibility(View.VISIBLE);
                    if(c.isChecked())
                    {
                        int ct=Integer.parseInt(tot.getText().toString());
                        tot.setText((ct+1) +"");
                    }
                    if(c.isChecked()==false)
                    {
                        int ct=Integer.parseInt(tot.getText().toString());
                        tot.setText((ct-1) +"");
                    }
                }
            });
            ll.addView(c);
        }
        if(allData.getDataDept()!=null)
            showData();
    }
    public void saveAndNext(View v)
    {
        int checkBoxNumber=ll.getChildCount();
        ArrayList<String> dept=new ArrayList<>();
        for(int i=0;i<checkBoxNumber;i++)
        {
            CheckBox cb= (CheckBox) ll.getChildAt(i);
            if(cb.isChecked())
            {
                dept.add(cb.getText().toString());
            }
        }
        if(dept.size()==0)
        {
            Toast.makeText(this,"Select at least 1 department",Toast.LENGTH_LONG).show();
            return;
        }
        allData.setDataDept(dept);
        startActivity(new Intent(RegisterCollege2_1.this,RegisterCollege3.class));
    }
    public void showData()
    {
        ArrayList <String> dataDept=allData.getDataDept();
        for (int i=0; i<ll.getChildCount();i++) {
            CheckBox c= (CheckBox) ll.getChildAt(i);
            if(dataDept.contains(c.getText().toString()))
                c.setChecked(true);
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder saveDetails=new AlertDialog.Builder(this);
        saveDetails.setTitle("ARE YOU SURE?");
        saveDetails.setMessage("All unsaved data will be lost.");
        saveDetails.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RegisterCollege2_1.super.onBackPressed();
            }
        }).setNegativeButton("Save & Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
                saveAndNext(new View(RegisterCollege2_1.this));
            }
        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        saveDetails.create().show();
    }
}