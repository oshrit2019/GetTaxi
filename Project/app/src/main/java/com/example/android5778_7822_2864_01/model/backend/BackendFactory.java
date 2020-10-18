package com.example.android5778_7822_2864_01.model.backend;

import android.content.Context;

/**
 * a class that create a object from kind backend
 */
    public final class BackendFactory {
        static Backend instance = null;
        public static String mode = "Fb";

    /**
     * if instance null create the object else return instance
     *
     * @param context
     * @return instance
     */
    public final static Backend getInstance(Context context) {

        if (mode == "lists") {
            if (instance == null)// if object does not exist
                instance = new com.example.android5778_7822_2864_01.model.dataSource.DatabaseList();
            return instance;
        } else {
            if (mode == "Fb") {
                if (instance == null) {
                    instance = new com.example.android5778_7822_2864_01.model.dataSource.DatabaseFB();
                }
                    return instance;


            }
        }
        return null;
    }

}

