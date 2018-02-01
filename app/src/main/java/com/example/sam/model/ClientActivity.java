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

public class ClientActivity extends AppCompatActivity implements Adapter.OnItemClickListener,MyCallback {

    private Adapter adapter;

    private View recyclerView;

    ProgressBar progressBar;

    private Manager manager;

    private WebSocketService socketService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        Button purchasedButton = (Button) findViewById(R.id.purchasedButton);
        purchasedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientActivity.this, PurchasedCarsActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        manager = new Manager(getApplication());
        socketService = new WebSocketService(manager);

        progressBar = findViewById(R.id.progress);

        recyclerView = findViewById(R.id.event_list);
        assert recyclerView != null;
    }

    @Override
    public void showError(String error) {
        progressBar.setVisibility(View.GONE);
        Snackbar.make(recyclerView, error, Snackbar.LENGTH_INDEFINITE)
                .setAction("DISMISS", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        closeOptionsMenu();
                    }
                }).show();
    }

    @Override
    public void clear() {
        adapter.clear();
    }

    @Override
    protected void onResume() {
        System.out.println("on resume");
        super.onResume();
        socketService.connect();
        loadEvents();
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void loadEvents() {
        progressBar.setVisibility(View.VISIBLE);
        boolean connectivity = manager.networkConnectivity(getApplicationContext());
        if (connectivity) {
            manager.loadEvents(progressBar, this);
        } else {
            progressBar.setVisibility(View.GONE);
            Snackbar.make(recyclerView, "Please check your internet connection and try again.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            closeOptionsMenu();
                        }
                    }).show();
        }
    }

    public void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new Adapter(this, "client");
        ((CarApp) getApplication()).db.getCarDao().getCars()
                .observe(this, new Observer<List<Car>>() {
                    @Override
                    public void onChanged(@Nullable List<Car> cars) {
                        adapter.setData(cars);
                    }
                });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                manager.deleteAllLocal();
            }
        }
    }

    @Override
    public void onItemClick(Car car) {
        System.out.println("----idd------" + car);
        //int quantity = ((CarApp) getApplication()).db.getCarDao().findByID(id).getQuantity();
        if (manager.networkConnectivity(getApplicationContext())) {
            manager.buyCar(car, this);
            Intent intent = new Intent(ClientActivity.this, PurchasedCarsActivity.class);
            startActivity(intent);
        } else {
            Snackbar.make(recyclerView, "Please check your internet connection and try again.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            closeOptionsMenu();
                        }
                    }).show();
        }
    }

    @Override
    public void onStop() {
        ((CarApp) getApplication()).db.getCarDao().deleteCars();
        socketService.close("");
        super.onStop();
    }

    public void refresh(View view) {
        progressBar.setVisibility(View.VISIBLE);
        manager.loadEvents(progressBar, this);
    }
}