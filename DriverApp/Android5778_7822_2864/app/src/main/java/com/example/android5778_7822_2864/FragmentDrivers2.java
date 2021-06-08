package com.example.android5778_7822_2864;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android5778_7822_2864.entities.Driver;
import com.example.android5778_7822_2864.model.backend.BackendFactory;
import com.example.android5778_7822_2864.model.dataSource.DatabaseFB;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDrivers2 extends Fragment {

    public List<Driver> drivers=new ArrayList<Driver>();
    private RecyclerView recyclerViewOfDrivers1;
    public FragmentDrivers2() {
        // Required empty public constructor
    }

    /**
     * on create view in fragment of drivers
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_drivers2, container, false);
        recyclerViewOfDrivers1 = view.findViewById(R.id.recyclerViewOfDrivers2);
        recyclerViewOfDrivers1.setHasFixedSize(true);
        recyclerViewOfDrivers1.setLayoutManager(new LinearLayoutManager(getActivity()));

        drivers= BackendFactory.getInstance(getActivity()).getDriverList();
        recyclerViewOfDrivers1.setAdapter(new FragmentDrivers2.DriverRecycleViewAdapter());
        return view;
    }
    public void onDestroy() {
        DatabaseFB.stopNotifyToTravelList();
        super.onDestroy();
    }


    /**
     * Driver Recycle View Adapter
     */
    public class DriverRecycleViewAdapter extends RecyclerView.Adapter<FragmentDrivers2.DriverRecycleViewAdapter.DriverViewHolder>

    {
        @NonNull
        @Override
        /**
         * on Create View Holder
         */
        public FragmentDrivers2.DriverRecycleViewAdapter.DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(getActivity()).inflate(R.layout.activity_row__item, parent, false);
            return  new DriverRecycleViewAdapter.DriverViewHolder(v);
        }

        /**
         * the items in the recycle view
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
            Driver driver = drivers.get(position);

            holder.nameTextView.setText(driver.getFirstName());

            holder.phoneTextView.setText(driver.getPhone());
        }

        @Override
        public int getItemCount() {
            return drivers.size();        }

        /**
         * Driver View Holder
         */
        public class DriverViewHolder extends RecyclerView.ViewHolder {
            TextView nameTextView;
            TextView phoneTextView;


            DriverViewHolder(View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.nameTextView);
                phoneTextView = itemView.findViewById(R.id.phoneTextView);

                itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {


                    }
                });
            }
        }
    }

}
