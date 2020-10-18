package com.example.android5778_7822_2864.model.dataSource;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

import com.example.android5778_7822_2864.entities.Driver;
import com.example.android5778_7822_2864.entities.Travel;
import com.example.android5778_7822_2864.entities.Travel.TravelStatus;
import com.example.android5778_7822_2864.model.backend.Backend;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * this class is the Data base that use in fire base
 */
public class DatabaseFB implements Backend {



    public interface NotifyDataChange<T> {

        void onDataChanged(T obj);

        void onFailure(Exception exception);
    }
    static Long counter = new Long(5);
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference myRefTravel = database.getReference("myTravel");
    static DatabaseReference myRefDriver = database.getReference("myDrivers");
      static List<Travel> travelList = new ArrayList<Travel>();
      static List<Driver> driverList = new ArrayList<Driver>();
   //set up listener
    private static ChildEventListener travelRefChildEventListener;
    private static ChildEventListener driverRefChildEventListener;
    private static ChildEventListener serviceListener;
   //set up lists
    public  List<Travel> getTravelList(){return travelList;}
    public  List<Driver> getDriverList(){return driverList;}

    /**
     * constructor
     */
    public DatabaseFB() {

        notifyToTravelList(new NotifyDataChange<List<Travel>>() {
            @Override
            public void onDataChanged(List<Travel> obj) {
            }
            @Override
            public void onFailure(Exception exp) {
            }
        });
        notifyToDriverList(new NotifyDataChange<List<Driver>>() {
            @Override
            public void onDataChanged(List<Driver> obj) {
            }
            @Override
            public void onFailure(Exception exp) {
            }
        });
    }

    /**
     * @param notifyDataChange
     */
    public static void notifyToTravelList(final NotifyDataChange<List<Travel>> notifyDataChange)//Travel listener
    {
        if (notifyDataChange != null) {
           if (travelRefChildEventListener != null) {
               if (serviceListener != null) //when service listener is null
               {
                   notifyDataChange.onFailure(new Exception("first unNotify ClientRequest list"));
                   return;
               } else {
                   serviceListener = new ChildEventListener() {
                       @Override
                       public void onChildAdded(DataSnapshot dataSnapshot, String s)//when child added
                       {
                           notifyDataChange.onDataChanged(travelList);
                       }

                       @Override
                       public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                       }

                       @Override
                       public void onChildRemoved(DataSnapshot dataSnapshot) {
                       }

                       @Override
                       public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {
                       }
                   };
                   myRefTravel.addChildEventListener(serviceListener);
                   return;
               }

           }
            travelList.clear();
            travelRefChildEventListener = new ChildEventListener() //travel listener
            {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s)//add to list when child added
                {
                   Travel travel = dataSnapshot.getValue(Travel.class);
                    travelList.add(travel);
                    notifyDataChange.onDataChanged(travelList);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) //when child change update the list
                {
                    Travel travel = dataSnapshot.getValue(Travel.class);
                    Long id = Long.parseLong(dataSnapshot.getKey());
                    travel.setId(id);
                    for (int i = 0; i < travelList.size(); i++) {
                        if (travelList.get(i).getId().equals(id)) {
                            travelList.set(i, travel);
                            break;
                        }
                    }
                    notifyDataChange.onDataChanged(travelList);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot)//when child remove update the list
                {
                    Travel travel = dataSnapshot.getValue(Travel.class);
                    Long id = Long.parseLong(dataSnapshot.getKey());
                    travel.setId(id);
                    for (int i = 0; i < travelList.size(); i++) {
                        if (travelList.get(i).getId().equals(id)) {
                            travelList.remove(i);
                            break;
                        }
                    }
                    notifyDataChange.onDataChanged(travelList);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };
            myRefTravel.addChildEventListener(travelRefChildEventListener);
        }
    }

    /**
     *stop the notify
     */
    public static void stopNotifyToTravelList() {
        if (travelRefChildEventListener != null) {
            myRefTravel.removeEventListener(travelRefChildEventListener);
            travelRefChildEventListener = null;
        }
      /* myRefTravel.addChildEventListener(travelRefChildEventListener);
        myRefTravel.removeEventListener(travelRefChildEventListener);*/

    }

    /**
     *notify to driver list
     * @param notifyDataChange
     */
    public static void notifyToDriverList(final NotifyDataChange<List<Driver>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (driverRefChildEventListener != null)
            {
                notifyDataChange.onFailure(new Exception("first unNotify Driver List"));
                return;
            }
            driverList.clear();
            driverRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Driver driver = dataSnapshot.getValue(Driver.class);

                    driverList.add(driver);
                    notifyDataChange.onDataChanged(driverList);
                }

                @Override
               //when child added we insert to list
                public void onChildChanged(DataSnapshot dataSnapshot, String s)
                {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    Long id = Long.parseLong(dataSnapshot.getKey());
                    driver.setId(id);
                    for (int i = 0; i < driverList.size(); i++) {
                        if (driverList.get(i).getId().equals(id)) {
                            driverList.set(i, driver);
                            break;

                        }
                    }
                    notifyDataChange.onDataChanged(driverList);
                }

                @Override
                //when child Removed we update to list
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    Long id = Long.parseLong(dataSnapshot.getKey());
                    driver.setId(id);
                    for (int i = 0; i < driverList.size(); i++) {
                        if (driverList.get(i).getId().equals(id)) {
                            driverList.remove(i);
                            break;

                        }
                    }
                    notifyDataChange.onDataChanged(driverList);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };
            myRefDriver.addChildEventListener(driverRefChildEventListener);
        }
    }

    public static void stopNotifyToDriverList() {
        if (driverRefChildEventListener != null) {
            myRefDriver.removeEventListener(driverRefChildEventListener);
            driverRefChildEventListener = null;
        }
  /*    myRefDriver.addChildEventListener(driverRefChildEventListener);
        myRefDriver.removeEventListener(driverRefChildEventListener);*/

    }
    /**
     * add
     * implement the function add in backend, add driver to firebase
     *
     * @param driver
     * @throws Exception
     */
    @Override
    public void addDriver(final Driver driver, final Action action) throws Exception {

        new AsyncTask<Context, Void, Void>() {
            @Override
            protected Void doInBackground(Context... contexts) {
                try {
                    myRefDriver.child(driver.getId().toString()).setValue(driver);
                    return null;
                }
                catch (Exception ex) {
                    return null;
                }
            }
        }.execute();
    }


    /**
     *return all travels
     * @param id
     * @return
     */
    public List<Travel> getAllMyTravels(Long id){

        List<Travel> allMyTravel= new ArrayList<Travel>();
        List<Travel> travels=getTravelList();
        for (int i=0;i<travels.size();i++)//go over the list
        {
            if(travels.get(i).getIdOfDriver().equals(id)) {
                allMyTravel.add(travels.get(i));
            }
        }
        return allMyTravel;
    }

    /**
     *get all the name of drivers
     * @return
     */
    public List<String> getAllNameDriver(){
        List<String> driverName= new ArrayList<String >();
        for (Driver driver: getDriverList())//go over the ist of drivers
        {
            driverName.add(driver.getFirstName()+" "+driver.getLastName());
        }
        return driverName;
    }

    /**
     * Get all the trips that were not taken
     * @return
     */
    public List<Travel> getTravelNotTaken(){

        List<Travel> travelNotTaken= new ArrayList<Travel>();
        for (Travel travel: getTravelList())//go over the list
        {
            if(travel.getMyTravelStatus()==(TravelStatus.FREE)) {
                travelNotTaken.add(travel);
            }
        }
        return travelNotTaken;
    }

    /**
     * Get all the trips that complected
     * @param idOfDriver
     * @return
     */
    public List<Travel> getTravelEnd(Long idOfDriver) {
        List<Travel> travelEnd = new ArrayList<Travel>();
        for (Travel travel : getTravelList()) {
            if (travel.getMyTravelStatus()==(TravelStatus.FINISHED) && travel.getIdOfDriver().equals(idOfDriver)) {
                travelEnd.add(travel);
            }
        }
        return travelEnd;
    }

    /**
     * get all Travels  that not token in city
     * @param city
     * @param context
     * @return
     */
    public List<Travel> getTravelNotTokenInCity(String city,Context context){
        List<Travel> travelNotTokenInCity= new ArrayList<Travel>();
        try {
        for (Travel travel: getTravelList())//go over travels list
        {
            String cityOfTravel=travel.getDestination();
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> address=geocoder.getFromLocationName(cityOfTravel,1);
            String cityName = address.get(0).getLocality();;
            if(cityName.equals(city)&&travel.getMyTravelStatus()==(TravelStatus.FREE)) {
                travelNotTokenInCity.add(travel);
            }
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return travelNotTokenInCity;
    }


    /**
     *Receive all trips located at a certain distance from the driver's location
     * @param destination
     * @param location
     * @param context
     * @return
     * @throws IOException
     */
    public List<Travel> getTravelNotTokenDestination(float destination,String location,Context context) throws IOException {


        List<Travel> travels=new ArrayList<Travel>();
        for(int i=0;i<travelList.size();i++)
        {
            String source=travelList.get(i).getSource().toString();
            float dis=distance(source,location,context);
            if(dis<=destination)
            {
                travels.add(travelList.get(i));
            }
        }

        return  travels;
    }

    /**
     * Receive all travel made on a certain date
     * @param date
     * @param idOfDriver
     * @return
     * @throws ParseException
     */
    public List<Travel> getTravelInDate(String date,Long idOfDriver) throws ParseException {

        List<Travel> travelInDate= new ArrayList<Travel>();
        DateFormat formatDate = new SimpleDateFormat("EEE MMM d HH:mm:ss zz yy");
        DateFormat fornatd1=new SimpleDateFormat("dd.MM.yyyy");
        for (Travel travel: getTravelList())
        {
            try {
                String dateOfTravel = travel.getFirstHour().toString();
                //Date date1 = fornatd1.parse(dateOfTravel);

                if ((dateOfTravel).equals(date)&&idOfDriver.equals(travel.getIdOfDriver())){
                    travelInDate.add(travel);
                }
            }
            catch (Exception ex)
            {
                System.out.println(ex.toString());
            }
        }
        return travelInDate;
    }

    /**
     * Get all travel by payment
     * @param pay
     * @param context
     * @param idOfDriver
     * @return
     */
    public List<Travel> getTravelPay(int pay,Context context,Long idOfDriver){

        List<Travel> travelsPay=new ArrayList<Travel>();
        for(int i=0;i<travelList.size();i++) {
            try {

                String source=travelList.get(i).getSource();
                String destination=travelList.get(i).getDestination();
                float dis = distance(source, destination, context);
                if(dis*7<=pay&&idOfDriver.equals(travelList.get(i).getIdOfDriver())||dis*7<=pay&& idOfDriver<0)
                {
                    travelsPay.add(travelList.get(i));
                }
            }
            catch (Exception ex)
            {

            }
        }
        return travelsPay;
    }

    /**
     * the driver take travel and update the fire base
     * @param travel
     * @param idOfDriver
     */
    public void takeTravel(Travel travel, long idOfDriver)
    {
        travel.setIdOfDriver(idOfDriver);
        travel.setMyTravelStatus(Travel.TravelStatus.IN_THERAPY);
        DateFormat fornatd1=new SimpleDateFormat("dd.MM.yyyy");
        Date date=new Date();
        travel.setFirstHour(fornatd1.format(date));
        myRefTravel.child(travel.getId().toString()).setValue(travel);

    }

    /**
     * the driver finish travel and update the fire base
     * @param travel
     */
    public void finishTravel(Travel travel)
    {
        travel.setMyTravelStatus(TravelStatus.FINISHED);
        DateFormat fornatd1=new SimpleDateFormat("dd.MM.yyyy");
        Date date=new Date();
        travel.setLastHour(fornatd1.format(date));
        //travel.setLastHour(Calendar.getInstance().getTime().toString());
        myRefTravel.child(travel.getId().toString()).setValue(travel);
    }

    /**
     *the function calculate the distance between 2 addresss
     * @param source
     * @param destination
     * @param context
     * @return
     * @throws IOException
     */
    public float distance(String source, String destination, Context context) throws IOException{
        Geocoder geocoder = new Geocoder(context);
        List<Address> address=geocoder.getFromLocationName(source,1);
        double x1 = address.get(0).getLatitude();
        double y1 = address.get(0).getLongitude();

        Location location1 = new Location("A");
        location1.setLatitude(x1);
        location1.setLongitude(y1);


        List<Address> address2=geocoder.getFromLocationName(destination,1);
        double x = address2.get(0).getLatitude();
        double y = address2.get(0).getLongitude();
        Location locationOfDriver = new Location("B");
        locationOfDriver.setLatitude(x);
        locationOfDriver.setLongitude(y);

        float distance = locationOfDriver.distanceTo(location1);
        return distance/1000;
    }
}



