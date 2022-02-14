package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RegisterCollege2_1 extends AppCompatActivity {
    LinearLayout ll;
    CollegeRegistrationData allData;
    TextView tot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_college2_1);
        ll=findViewById(R.id.linearL);
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
}