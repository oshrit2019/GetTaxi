package com.example.android5778_7822_2864_01.control;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.android5778_7822_2864_01.R;
import com.example.android5778_7822_2864_01.entities.Travel;
import com.example.android5778_7822_2864_01.model.backend.Backend;
import com.example.android5778_7822_2864_01.model.backend.BackendFactory;
import com.example.android5778_7822_2864_01.model.dataSource.DatabaseList;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.isNaN;


public class ActivityTravel extends AppCompatActivity implements View.OnClickListener {
    public static final String ID_OF_TRAVEL="idOfTravel";

    static Long counter;
    final Backend backend1 = BackendFactory.getInstance(this);
    LocationListener locationListener;
    LocationManager locationManager;
     EditText source;

    /**
     * on create activity travel
     * @param savedInstanceState
     */
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        source = (EditText) findViewById(R.id.editTextSource);
        try {
            // listener to location of source
            locationListener = new LocationListener()
            {
                public void onLocationChanged(Location location)
                {
                    // Called when a new location is found by the network location provider.

                    source.setText(getPlace(location));

                    // Remove the listener you previously added
                    //  locationManager.removeUpdates(locationListener);
                }
                public void onStatusChanged(String provider, int status, Bundle extras)
                {
                }

                public void onProviderEnabled(String provider)
                {
                }

                public void onProviderDisabled(String provider)
                {
                }
            };
            Button add = findViewById(R.id.imageButtonAdd);
            add.setOnClickListener(this);
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
          // check permissions of the location
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        catch (Exception e)
        {
            Toast.makeText(ActivityTravel.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * click on add butoon , add travel to firebase
     * @param v
     */
    @Override
    public void onClick(View v) {
        try {
            EditText userNameField = (EditText) findViewById(R.id.editTextName);
            EditText userPhoneField = (EditText) findViewById(R.id.editTextPhone);
            EditText userEmailField = (EditText) findViewById(R.id.editTextEmail);
            EditText userSourceField = (EditText) findViewById(R.id.editTextSource);
            EditText userDestinationField = (EditText) findViewById(R.id.editTextDestination);
            //check input validity
            if (Validate(userNameField, userPhoneField, userEmailField,userSourceField ,userDestinationField)) {
                final String name = (userNameField.getText().toString());
                final String phone = userPhoneField.getText().toString();
                final String email = userEmailField.getText().toString();
                final String source = userSourceField.getText().toString();
                final String destination = userDestinationField.getText().toString();

                //asyncTask to add to firebase
                new AsyncTask<Context, Void, Void>() {
                                            @Override
                                            protected Void doInBackground(Context... contexts) {
                                                Travel travel1 = new Travel();

                                                travel1.setName(name);
                                                travel1.setPhone(phone);
                                                travel1.setEmail(email);
                                                travel1.setSource(source);
                                                travel1.setDestination(destination);
                                           /*     counter=loadDataAndUpdate();
                                                travel1.setId(counter);
                                                counter++;*/
                                                try {
                                                    backend1.add(travel1);
                                                //    saveData(counter);
                                               } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                return null;
                                            }
                                        }.execute();

                                        userNameField.setText("");
                                        userPhoneField.setText("");
                                        userEmailField.setText("");
                                  //      userSourceField.setText("");
                                        userDestinationField.setText("");
                                    }







        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * check input validity of mail
     * @param email
     * @param userEmailField
     * @return
     */
    private boolean isValidMail(String email,EditText userEmailField) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();
        // if the mail invalid
        if (!check) {
            userEmailField.setError("Not Valid Email");
        }

        return check;
    }

    /**
     * check input validity of mobile
     * @param phone
     * @param userPhoneField
     * @return
     */
    private boolean isValidMobile(String phone,  EditText userPhoneField) {
        boolean check = false;
        // if there are no letters in number phone
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            // if length of number phone smaller from 6 or bigger from 13
            if (phone.length() < 6 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;
                userPhoneField.setError("Not Valid Number");
            }
            else {
                check = true;
            }
        } else {
            check = false;
            userPhoneField.setError("Not Valid Number");
        }
        return check;
    }

    /**
     * check validity of address
     * @param editTextAddress
     * @return
     */
    public boolean isAddress(EditText editTextAddress)
    {
        // check file not empty
        if(editTextAddress.getText().length()>0){
            try{
                Geocoder gc = new Geocoder(this);
                if (gc.isPresent()){
                    List<Address> list = gc.getFromLocationName(editTextAddress.getText().toString(),1);
                    Address address=list.get(0);
                    double lat =address.getLatitude();
                    double lng= address.getLongitude();
                    Location locationA = new Location("A");
                    locationA.setLatitude(lat);
                    locationA.setLongitude(lng);
                }
            }
            catch (Exception e)
            {
                editTextAddress.setError("Invalid Address.");
                return false;
            }
        }
        else
            return false;
        return true;
    }

    /**
     * check input validity to name, phone, email, source, destination
     * @param userNameField
     * @param userPhoneField
     * @param userEmailField
     * @param userSourceField
     * @param userDestinationField
     * @return
     */
    public boolean Validate(EditText userNameField, EditText userPhoneField,
                        EditText userEmailField,EditText userSourceField, EditText userDestinationField){
        if(userNameField.getText().toString().length()==0||userPhoneField.getText().toString().length()==0|| userEmailField.getText().toString().length()==0
                || userSourceField.getText().toString().length()==0 ||userDestinationField.getText().toString().length()==0)
        {
        Toast.makeText(ActivityTravel.this, "You have empty fields!", Toast.LENGTH_LONG).show();
        return false;

        }
        boolean flag=true;
        if(!isValidMail(userEmailField.getText().toString(),userEmailField)) flag=false;
        if(!isValidMobile(userPhoneField.getText().toString(),userPhoneField)) flag=false;
        if(!isAddress(userSourceField)) flag=false;
        if(!isAddress(userDestinationField)) flag=false;
    return flag;


        }

    /**
     * get place in string of location
     * @param location
     * @return
     */
    public String getPlace(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                String cityName = addresses.get(0).getAddressLine(0);
                return cityName + "\n";
            }

            return "no place: \n ("+location.getLongitude()+" , "+location.getLatitude()+")";
        }
        catch(
                IOException e)

        {
            e.printStackTrace();
        }
        return "IOException ...";
    }
}

