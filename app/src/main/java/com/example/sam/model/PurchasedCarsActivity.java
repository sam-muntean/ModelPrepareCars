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
import android.widget.Toast;

import com.example.sam.model.adapter.Adapter;
import com.example.sam.model.domain.Car;
import com.example.sam.model.domain.PurchasedCar;
import com.example.sam.model.manager.CarApp;
import com.example.sam.model.manager.Manager;
import com.example.sam.model.manager.MyCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PurchasedCarsActivity extends AppCompatActivity implements Adapter.OnItemClickListener,MyCallback {

    private Adapter adapter;
    private View recyclerView;
    private Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_cars);

        Button purchasedButton = findViewById(R.id.deleteButton);
        purchasedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        manager = new Manager(getApplication());

        recyclerView = findViewById(R.id.event_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new Adapter(this,"purchased");
        ((CarApp) getApplication()).db.getPCarDao().getCars()
                .observe(this, new Observer<List<PurchasedCar>>() {
                    @Override
                    public void onChanged(@Nullable List<PurchasedCar> cars) {
                        List<Car> newCars = new ArrayList<>();
                        for ( PurchasedCar c: cars){
                            newCars.add(new Car(c.getCarId(),c.getName(),c.getQuantity(),c.getType(),c.getStatus()));
                        }
                        adapter.setData(newCars);
                    }
                });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Car car) {
        System.out.println("----idd------"+car.getId());
        if(manager.networkConnectivity(getApplicationContext())) {
            PurchasedCar p = findById(car.getId());
            if(p != null){
                Calendar calendar = new GregorianCalendar(/* remember about timezone! */);
                calendar.setTime(p.getDate());
                calendar.add(Calendar.DATE, 30);
                Date newDate;
                newDate = calendar.getTime();
                System.out.println(newDate);
                if(newDate.after(new Date())){
                    System.out.println("accepted");
                    manager.returnCar(p);
                }
                else{
                    Toast.makeText(getApplicationContext(),"You can't return this car anymore!!", Toast.LENGTH_LONG).show();
                }
            }
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
    public void showError(String message) {
        //progressBar.setVisibility(View.GONE);
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void clear() {}

    private PurchasedCar findById(int id){
        List<PurchasedCar> pc = ((CarApp)getApplication()).db.getPCarDao().getNormalCars();
        for( PurchasedCar p:pc ){
            if (p.getCarId() == id)
                return p;
        }
        return null;
    }
}
