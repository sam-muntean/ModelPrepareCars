package com.example.sam.model.service;

import com.example.sam.model.domain.Car;

import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

import retrofit2.http.GET;

/**
 * Created by Sam on 31.01.2018.
 */

public interface CarService {

    String SERVICE_ENDPOINT = "http://192.168.43.97:4000";

    @GET("cars")
    Observable<List<Car>> getCarsClient();

    @GET("all")
    Observable<List<Car>> getCarsEmployee();

    @POST("buyCar")
    Observable<Car> buyCar(@Body Car c);

    @POST("returnCar")
    Observable<Car> returnCar(@Body Car c);

    @POST("addCar")
    Observable<Car> addCar(@Body Car c);


    @POST("removeCar")
    Observable<Car> deleteCar(@Body Car c);

}

