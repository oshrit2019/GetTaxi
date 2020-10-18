package com.example.android5778_7822_2864_01.model.dataSource;

import android.content.SharedPreferences;

import com.example.android5778_7822_2864_01.entities.Travel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DatabaseFB implements com.example.android5778_7822_2864_01.model.backend.Backend {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("myTravel");

    /**
     * add travel to firebase
     * @param t
     * @throws Exception
     */
    @Override
    public void add(Travel t) throws Exception {
        try {
             myRef.push().setValue(t);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


}
