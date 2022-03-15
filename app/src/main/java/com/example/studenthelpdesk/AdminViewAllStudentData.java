package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class AdminViewAllStudentData extends AppCompatActivity {
    AutoCompleteTextView sortin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_all_student_data);
        sortin=findViewById(R.id.sortin);
        String[] sortinList={"Ascending Order","Descending Order"}; //Ascending = 0 and descending =1
        ArrayAdapter spinnerList=new ArrayAdapter(this,android.R.layout.simple_spinner_item,sortinList);
        sortin.setAdapter(spinnerList);

    }
    public void filters(View v){
        startActivity(new Intent(AdminViewAllStudentData.this, AdminViewAllStudentDataFilters.class));
    }
}