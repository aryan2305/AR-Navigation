package com.example.coderescue.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coderescue.Classes.NetworkConnectivity;
import com.example.coderescue.Fragments.HomeFragment;
import com.example.coderescue.VictimLocationAdapter;
import com.example.coderescue.VictimLocationCardModel;
import com.example.coderescue.R;
import com.example.coderescue.navar.PoiBrowserActivity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class RescueTeamDashboard extends AppCompatActivity {

    Button snd2;
    String disaster_id;
    RecyclerView mRecylcerView;
    VictimLocationAdapter myAdapter;
    Context c;
    double latvic,longivic;
    String latvics,longivics;
    ArrayList<VictimLocationCardModel> models = new ArrayList<>();
    VictimLocationCardModel m;
    private ProgressBar prog;
    int flag;

    public String lat,longi;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    public static String state;
    soup.neumorphism.NeumorphButton button_ar_camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_team_dashboard);
        button_ar_camera = findViewById(R.id.button_ar_camera);
        flag=0;

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        HomeFragment.diss_idd = disaster_id;
        System.out.println(HomeFragment.diss_idd);
        System.out.println("a for apple");
        prog=findViewById(R.id.progressBar2);

        mRecylcerView=findViewById(R.id.recylcerView2);
        c = this;
        mRecylcerView.setLayoutManager(new LinearLayoutManager(this));
        snd2=findViewById(R.id.snd_msg2);

        button_ar_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RescueTeamDashboard.this, PoiBrowserActivity.class);
                startActivity(intent);
            }
        });
    }


    public void button_click2(View view){
        if(!NetworkConnectivity.isInternetAvailable(getApplicationContext())){
        }
        else{
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(RescueTeamDashboard.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
            } else {
                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation(){
        prog.setVisibility(View.VISIBLE);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.getFusedLocationProviderClient(RescueTeamDashboard.this)
                .requestLocationUpdates(locationRequest, new LocationCallback(){
                    @Override

                    public void onLocationResult(LocationResult locationResult){
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(RescueTeamDashboard.this)
                                .removeLocationUpdates(this);
                        if(locationResult != null && locationResult.getLocations().size() > 0){
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            lat = Double.toString(latitude);
                            longi = Double.toString(longitude);

                            Geocoder gcd = new Geocoder(getBaseContext(),
                                    Locale.getDefault());
                            List<Address> addresses;
                            try {
                                addresses = gcd.getFromLocation(latitude,
                                        longitude, 1);
                                if (addresses.size() > 0) {
                                    state = addresses.get(0).getAdminArea();
                                    getVictims();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, Looper.getMainLooper());
    }

    public void getVictims(){
        ArrayList<String> latitudeData = new ArrayList<String>(
                Arrays.asList("31.2907875","28.6326577","28.6331577","28.6230599","28.6320599","28.631975","28.6320624","28.6323719","28.6320356","28.6319763","28.6322245","28.6295538","28.629508","28.6321568","28.6320905","28.6319031","28.6862738","28.6319431"));
        ArrayList<String> longitudeData = new ArrayList<String>(
                Arrays.asList("75.551899","28.6326577","28.6331577","77.0920861","28.6320599","77.0969907","77.0949164","77.0949158","77.0949156","77.0949154","28.6322245","77.0801899","77.0945777","77.0950111","77.0950887","77.0947476","77.2217831","77.0949863"));
        int numVictims = latitudeData.size();
        models.clear();
        for(int i =0;i<numVictims;i++){
                float[] results = new float[1];
                latvic = Double.parseDouble(latitudeData.get(i));
                longivic = Double.parseDouble(longitudeData.get(i));

                Location.distanceBetween(Double.parseDouble(lat), Double.parseDouble(longi),
                        latvic, longivic,
                        results);
                System.out.println(results[0]);
                m = new VictimLocationCardModel();
                m.setLatitude(latitudeData.get(i));
                m.setLongitude(longitudeData.get(i));
                m.setDistance(results[0]);
                m.setTitle(results[0] + " m");
                m.setRescueUsername("abc");
                m.setDescription("Latitude: " + latvic + "\n" + "Longitude: "+ longivic);
                m.setDisaster_id("def");
                models.add(m);
        }
        Collections.sort(models, VictimLocationCardModel.DistSort);
        latvics = models.get(0).getLatitude();
        longivics = models.get(0).getLongitude();
        latvic = Double.parseDouble(latvics);
        longivic = Double.parseDouble(longivics);
        myAdapter=new VictimLocationAdapter(c,models);
        mRecylcerView.setAdapter(myAdapter);
        prog.setVisibility(View.GONE);


    }


}
