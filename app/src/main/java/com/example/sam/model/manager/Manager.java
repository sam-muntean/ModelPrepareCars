package com.example.sam.model.manager;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.widget.ProgressBar;

import com.example.sam.model.domain.PurchasedCar;
import com.example.sam.model.service.CarService;
import com.example.sam.model.domain.Car;
import com.example.sam.model.service.ServiceFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Sam on 31.01.2018.
 */

public class Manager {

    private CarApp app;
    private CarService service;

    public Manager(Application application) {
        this.app = (CarApp) application;
        service = ServiceFactory.createRetrofitService(CarService.class, CarService.SERVICE_ENDPOINT);

    }

    public Manager() {
//        this.app = (CarApp) application;
//        service = ServiceFactory.createRetrofitService(CarService.class, CarService.SERVICE_ENDPOINT);

    }

    public boolean networkConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void loadEvents(final ProgressBar progressBar, final MyCallback callback) {
        System.out.println("loaaaaaaaaadddddddd");
        service.getCarsClient()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Car>>() {
                    @Override
                    public void onCompleted() {
                        Timber.v("Car Service completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        app.db.getCarDao().deleteCars();
                        Timber.e(e, "Error while loading the events");
                        callback.showError("Not able to retrieve the data.");
                    }

                    @Override
                    public void onNext(final List<Car> cars) {
                        System.out.println(cars);
                        app.db.getCarDao().deleteCars();
                        app.db.getCarDao().addCars(cars);
                        Timber.v("Cars persisted");
                    }
                });
    }

    public void loadEmployeeCars(final ProgressBar progressBar, final MyCallback callback) {
        service.getCarsEmployee()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Car>>() {

                    @Override
                    public void onCompleted() {
                        Timber.v("Car Service completed");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the events");
                        callback.showError("Not able to retrieve the data.");
                    }

                    @Override
                    public void onNext(final List<Car> cars) {
                        new Thread(new Runnable() {
                            public void run() {
                                app.db.getCarDao().deleteCars();
                                System.out.println(cars);
                                app.db.getCarDao().addCars(cars);
                            }
                        }).start();
                        Timber.v("Cars persisted");
                    }
                });
    }

    public void buyCar(final Car car, final MyCallback callback){
        service.buyCar(car)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Car>() {
                    @Override
                    public void onCompleted() {
                        addDataLocally(new Car(car.getId(),car.getName(),1,car.getType(),car.getStatus()));
                        Timber.v("Car Service completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            Timber.e(e, "Error while loading the cars");
                            callback.showError(((HttpException) e).response().errorBody().string());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(final Car car2) {
                        new Thread(new Runnable() {
                            public void run() {
                                //app.db.getCarDao().deleteCars();
                                System.out.println("new car:" + car2 );
                                app.db.getCarDao().updateCar(car2);
                            }
                        }).start();
                        Timber.v("Cars persisted");
                    }
                });
    }

    public void deleteCar(final Car car){
        service.deleteCar(car)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Car>() {
                    @Override
                    public void onCompleted() {
                        Timber.v("Car Service completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the events");
                    }

                    @Override
                    public void onNext(final Car car2) {
                        new Thread(new Runnable() {
                            public void run() {
                                //app.db.getCarDao().deleteCars();
                                System.out.println("new car:" + car2 );
                                app.db.getCarDao().deleteCar(car2);
                            }
                        }).start();
                        Timber.v("Cars persisted");
                    }
                });
    }

    public void returnCar(final PurchasedCar car){
        service.returnCar(new Car(car.getCarId(),car.getName(),car.getQuantity(),car.getType(),car.getStatus()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Car>() {
                    @Override
                    public void onCompleted() {
                        deleteDataLocally(car);
                        Timber.v("Car Service completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the events");
                    }

                    @Override
                    public void onNext(final Car car2) {
                        new Thread(new Runnable() {
                            public void run() {
                                //app.db.getCarDao().deleteCars();
                                System.out.println("new car:" + car2 );
                                app.db.getCarDao().updateCar(car2);
                            }
                        }).start();
                        Timber.v("Cars persisted");
                    }
                });
    }

    public void deleteAllLocal(){
        new Thread(new Runnable() {
            public void run() {
                //app.db.getCarDao().deleteCars();
                app.db.getPCarDao().deleteAll();
            }
        }).start();
        Timber.v("All local cars deleted");
    }

    public void addCar(final String name, final String type, final MyCallback myCallback){
        service.addCar(new Car(0,name,0,type,""))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Car>() {
                    @Override
                    public void onCompleted() {
                        Timber.v("Car Service completed");
                        myCallback.clear();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Error while loading the events");
                        myCallback.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(final Car car2) {
                        new Thread(new Runnable() {
                            public void run() {
                                //app.db.getCarDao().deleteCars();
                                System.out.println("new car:" + car2 );
                                app.db.getCarDao().addCar(car2);
                            }
                        }).start();
                        Timber.v("Car persisted");
                    }
                });
    }

    public void addNewCar(final Car car) {
        new Thread(new Runnable() {
            public void run () {
                //app.db.getCarDao().deleteCars();
                System.out.println("new car:" + car);
                app.db.getCarDao().addCar(car);
            }
        }).start();
    }

    private void addDataLocally(final Car car) {
        final PurchasedCar p = new PurchasedCar(0,car.getId(),car.getName(),car.getQuantity(),car.getType(),car.getStatus(), new Date());
        System.out.println(p);
        new Thread(new Runnable() {
            @TargetApi(Build.VERSION_CODES.O)
            public void run() {
                app.db.getPCarDao().addCar(p);
            }
        }).start();
    }

    private void deleteDataLocally(final PurchasedCar car) {
        new Thread(new Runnable() {
            public void run() {
                app.db.getPCarDao().deleteCar(car);
            }
        }).start();
    }
}
