package com.example.sam.model.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sam.model.R;
import com.example.sam.model.domain.Car;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 31.01.2018.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    List<Car> cars;
    private String type;

    OnItemClickListener listener;

    public Adapter( OnItemClickListener listener, String type) {
        this.cars = new ArrayList<Car>();
        this.listener = listener;
        this.type = type;
    }

//    public MyAdapter(String type) {
//        cars = new ArrayList<>();
//        this.type = type;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(type.equals("purchased")){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.purchased_list, parent, false);
        }

        else if(type.equals("client")){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list, parent, false);
        }
        else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.employee_list, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.car = cars.get(position);
        holder.nameView.setText(cars.get(position).getName());
        holder.typeView.setText(cars.get(position).getType());
        holder.quantityView.setText(String.valueOf(cars.get(position).getQuantity()));
        if (type.equals("employee")) {
            holder.status.setText(cars.get(position).getStatus());
        }
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public void setData(List<Car> cars){
        this.cars = cars;
        //System.out.println("ADAPTER:" + this.cars);
        notifyDataSetChanged();
    }

    public void clear() {
        cars.clear();
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Car car);
    }

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    View mView;
    final TextView nameView;
    final TextView typeView;
    final TextView quantityView;
    final TextView status;
    final Button button;

    Car car;

    public ViewHolder(View view) {
        super(view);
        mView = view;
        nameView = view.findViewById(R.id.car_name);
        typeView = view.findViewById(R.id.car_type);
        quantityView = view.findViewById(R.id.car_quantity);
        button = view.findViewById(R.id.button2);
        if (type.equals("employee")) {
            status = view.findViewById(R.id.status);
        } else {
            status = null;
        }
        //mView.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        System.out.println("onClick " + getAdapterPosition() + " ");
        listener.onItemClick(cars.get(getAdapterPosition()));
    }

    @Override
    public String toString() {
        return super.toString() + " '" + typeView.getText() + "'";
    }
}
}

