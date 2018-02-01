package com.example.sam.model;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.sam.model.adapter.Adapter;
import com.example.sam.model.domain.Car;
import com.example.sam.model.manager.CarApp;
import com.example.sam.model.manager.Manager;
import com.example.sam.model.manager.MyCallback;
import com.example.sam.model.service.WebSocketService;

import java.util.List;

public class EmployeeActivity extends AppCompatActivity implements Adapter.OnItemClickListener, MyCallback {

    private Adapter adapter;

    private View recyclerView;

    private Manager manager;

    private ProgressBar progressBar;

    private WebSocketService socketService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        socketService = new WebSocketService(manager);


        Button addButton = findViewById(R.id.addB);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeActivity.this, AddCarActivity.class);
                startActivityForResult(intent,2);
            }
        });
        progressBar = findViewById(R.id.progress);
        manager = new Manager(getApplication());
        socketService = new WebSocketService(manager);

        recyclerView = findViewById(R.id.car_list);
        assert recyclerView != null;
    }

    @Override
    protected void onResume(){
        System.out.println("on resume employee");
        super.onResume();
        socketService.connect();
        loadCars();
        setupRecyclerView((RecyclerView) recyclerView);
    }

    public void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        //System.out.println("sdsdsfsdfs");
        adapter = new Adapter(this,"employee");
        ((CarApp) getApplication()).db.getCarDao().getCars()
                .observe(this, new Observer<List<Car>>() {
                    @Override
                    public void onChanged(@Nullable List<Car> cars) {
                        //System.out.println(cars);
                        adapter.setData(cars);
                    }
                });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Car car) {
        System.out.println("----idd------"+car);
        //int quantity = ((CarApp) getApplication()).db.getCarDao().findByID(id).getQuantity();
        if(manager.networkConnectivity(getApplicationContext())) {
            manager.deleteCar(car);
        }
        else{
            Snackbar.make(recyclerView,"Please check your internet connection and try again.",Snackbar.LENGTH_INDEFINITE)
                    .setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View view){
                            closeOptionsMenu();
                        }
                    }).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra("name");
                String type = data.getStringExtra("type");
                manager.addCar(name, type,this);
            }
        }
    }

    private boolean loadCars() {
        progressBar.setVisibility(View.VISIBLE);
        boolean connectivity = manager.networkConnectivity(getApplicationContext());
        if(connectivity)
            manager.loadEmployeeCars(progressBar,this);
        return connectivity;
    }

    @Override
    public void showError(String message) {
        progressBar.setVisibility(View.GONE);
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadCars();
                    }
                }).show();

    }

    @Override
    public void clear() {
        adapter.clear();
    }
}
