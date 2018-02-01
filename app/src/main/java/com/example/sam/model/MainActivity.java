package com.example.sam.model;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.Snackbar;

import com.example.sam.model.manager.Manager;

public class MainActivity extends AppCompatActivity {

    Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = new Manager();
    }

    public void toClient(View view){
        Intent intent = new Intent(MainActivity.this, ClientActivity.class);
        startActivity(intent);
    }

    public void toEmployee(View view){
        if(manager.networkConnectivity(getApplicationContext())){
            Intent intent = new Intent(MainActivity.this, EmployeeActivity.class);
            startActivity(intent);}
        else{
            Snackbar.make(view,"Please check your internet connection and try again.",Snackbar.LENGTH_INDEFINITE)
                    .setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View view){
                            closeOptionsMenu();
                        }
                    }).show();
        }
    }
}
