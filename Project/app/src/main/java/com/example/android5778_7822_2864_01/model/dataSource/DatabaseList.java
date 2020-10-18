package com.example.android5778_7822_2864_01.model.dataSource;
import com.example.android5778_7822_2864_01.entities.Travel;
import java.util.ArrayList;
import java.util.List;

// a database class that implements the backend in list
public class DatabaseList implements com.example.android5778_7822_2864_01.model.backend.Backend {

    static List<Travel> travels=new ArrayList<Travel>();
    static Long counter=new Long(0);

    /**
     * implement the function add in backend, add travel to list
     * @param t
     * @throws Exception
     */
    @Override
    public void add(Travel t) throws Exception {

        for(Travel travel : travels)// pass on the travels
        {
           if(travel.getId()==t.getId())// check if travel existing in travels
                throw new Exception("the travel in the data base");
        }
        t.setId(counter++);
        travels.add(t);
    }
}
