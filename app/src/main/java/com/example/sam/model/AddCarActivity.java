package com.example.sam.model;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddCarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
    }

    public void addCar(View view) {
        final String name = ((EditText) findViewById(R.id.name)).getText().toString();
        final String type = ((EditText) findViewById(R.id.type)).getText().toString();
        Intent intent = new Intent();
        intent.putExtra("name",name);
        intent.putExtra("type",type);

        setResult(RESULT_OK,intent);
        finish();
    }
}
