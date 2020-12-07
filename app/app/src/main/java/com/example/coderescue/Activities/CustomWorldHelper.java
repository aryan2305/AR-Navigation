package com.example.coderescue.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;

import com.example.coderescue.Fragments.HomeFragment;
import com.example.coderescue.R;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import java.util.ArrayList;
import java.util.List;


@SuppressLint("SdCardPath")
public class CustomWorldHelper {
    public static final int LIST_TYPE_EXAMPLE_1 = 1;
    public static List<GeoObject> listDisasters =  new ArrayList<GeoObject>();
    public static String lat;
    public static String longi;

    public static World sharedWorld;

    private static void setCurrentLocation(){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        lat = "28.4982534";
        longi = "77.2290081";
    }



    public static World generateObjects(Context context) throws InterruptedException {
        if (sharedWorld != null) {
            return sharedWorld;
        }
        sharedWorld = new World(context);
        Thread.sleep(5000);
        Log.d("Correct", "hello" + listDisasters.size());

        // The user can set the default bitmap. This is useful if you are
        // loading images form Internet and the connection get lost
        sharedWorld.setDefaultImage(R.drawable.beyondar_default_unknow_icon);

        // User position (you can change it using the GPS listeners form Android
        // API)
        sharedWorld.setGeoPosition(41.90533734214473d, 2.565848038959814d);
        sharedWorld.setGeoPosition(28.6318541d, 77.0950067d);

//        List<GeoObject> listVictims = getVictims();

////         Create an object with an image in the app resources.
//        GeoObject go1 = new GeoObject(1l);
//        go1.setGeoPosition(41.90523339794433d, 2.565036406654116d);
//        go1.setGeoPosition(28.6319541d, 77.0950067d);
//
//        go1.setImageResource(R.drawable.creature_1);
//        go1.setName("Creature 1");
//
//        // Is it also possible to load the image asynchronously form internet
//        GeoObject go2 = new GeoObject(2l);
//        go2.setGeoPosition(41.90518966360719d, 2.56582424468222d);
//        go2.setGeoPosition(28.6317541d, 77.0952067d);
//        go2.setImageUri("http://beyondar.github.io/beyondar/images/logo_512.png");
//        go2.setImageUri("https://d2c7ipcroan06u.cloudfront.net/wp-content/uploads/2020/05/tanmay-bhat-2-edited-696x392.jpg");
//        go2.setName("Online image");
//
//        GeoObject go5 = new GeoObject(5l);
//        go5.setGeoPosition(41.90553066234138d, 2.565777906882577d);
//        go5.setImageResource(R.drawable.creature_5);
//        go5.setGeoPosition(28.6318541d, 77.0951067d);
//        go5.setName("Creature 5");
//
//        GeoObject go6 = new GeoObject(6l);
//        go6.setGeoPosition(41.90596218466268d, 2.565250806050688d);
//        go6.setImageResource(R.drawable.creature_6);
//        go6.setGeoPosition(28.6318641d, 77.0951167d);
//        go6.setName("Creature 6");
//
//        GeoObject go7 = new GeoObject(7l);
//        go7.setGeoPosition(41.90581776104766d, 2.565932313852319d);
//        go7.setImageResource(R.drawable.creature_2);
//        go7.setGeoPosition(28.6319341d, 77.0954067d);
//        go7.setName("Creature 2");
//
//        GeoObject go8 = new GeoObject(8l);
//        go8.setGeoPosition(41.90534261025682d, 2.566164369775198d);
//        go8.setImageResource(R.drawable.rectangle);
//        go8.setName("Object 8");
//
//        GeoObject go9 = new GeoObject(9l);
//        go9.setGeoPosition(41.90530734214473d, 2.565808038959814d);
//        go9.setImageResource(R.drawable.creature_4);
//        go9.setName("Creature 4");
//
//        GeoObject go10 = new GeoObject(10l);
//        go10.setGeoPosition(42.006667d, 2.705d);
//        go10.setImageResource(R.drawable.object_stuff);
//        go10.setName("Far away");

        // Add the GeoObjects to the world
//        sharedWorld.addBeyondarObject(go1);
//        sharedWorld.addBeyondarObject(go2, LIST_TYPE_EXAMPLE_1);
//        sharedWorld.addBeyondarObject(go5);
//        sharedWorld.addBeyondarObject(go6);
//        sharedWorld.addBeyondarObject(go7);
//        sharedWorld.addBeyondarObject(go8);
//        sharedWorld.addBeyondarObject(go9);
//        sharedWorld.addBeyondarObject(go10);
        for( GeoObject temp : listDisasters  ){
            sharedWorld.addBeyondarObject(temp,LIST_TYPE_EXAMPLE_1);
            Log.d("Correct", "h" + listDisasters.size());
            Log.d("Correct", temp.getImageUri());
        }

        return sharedWorld;
    }

//
//    public static World generateObjects(Context context) {
//        if (sharedWorld != null) {
//            return sharedWorld;
//        }
//        sharedWorld = new World(context);
//
//        // The user can set the default bitmap. This is useful if you are
//        // loading images form Internet and the connection get lost
//        sharedWorld.setDefaultImage(R.drawable.beyondar_default_unknow_icon);
//
//        // User position (you can change it using the GPS listeners form Android
//        // API)
//        sharedWorld.setGeoPosition(41.90533734214473d, 2.565848038959814d);
//        sharedWorld.setGeoPosition(28.6319541d, 77.0950067d);
//
//        // Create an object with an image in the app resources.
////        GeoObject go1 = new GeoObject(1l);
////        go1.setGeoPosition(41.90523339794433d, 2.565036406654116d);
////        go1.setImageResource(R.drawable.creature_1);
////        go1.setName("Creature 1");
////
////        // Is it also possible to load the image asynchronously form internet
////        GeoObject go2 = new GeoObject(2l);
////        go2.setGeoPosition(41.90518966360719d, 2.56582424468222d);
////        go2.setImageUri("http://beyondar.github.io/beyondar/images/logo_512.png");
////        go2.setName("Online image");
////
////        GeoObject go5 = new GeoObject(5l);
////        go5.setGeoPosition(41.90553066234138d, 2.565777906882577d);
////        go5.setImageResource(R.drawable.creature_5);
////        go5.setName("Creature 5");
////
////        GeoObject go6 = new GeoObject(6l);
////        go6.setGeoPosition(41.90596218466268d, 2.565250806050688d);
////        go6.setImageResource(R.drawable.creature_6);
////        go6.setName("Creature 6");
////
////        GeoObject go7 = new GeoObject(7l);
////        go7.setGeoPosition(41.90581776104766d, 2.565932313852319d);
////        go7.setImageResource(R.drawable.creature_2);
////        go7.setName("Creature 2");
////
////        GeoObject go8 = new GeoObject(8l);
////        go8.setGeoPosition(41.90534261025682d, 2.566164369775198d);
////        go8.setImageResource(R.drawable.rectangle);
////        go8.setName("Object 8");
////
////        GeoObject go9 = new GeoObject(9l);
////        go9.setGeoPosition(41.90530734214473d, 2.565808038959814d);
////        go9.setImageResource(R.drawable.creature_4);
////        go9.setName("Creature 4");
////
////
////        GeoObject go10 = new GeoObject(10l);
////        go10.setGeoPosition(42.006667d, 2.705d);
////        go10.setGeoPosition(28.6319541d, 77.0950067d);
////
////        go10.setImageResource(R.drawable.object_stuff);
////        go10.setName("Far away");
//
//        GeoObject go1 = new GeoObject(1l);
//        go1.setGeoPosition(41.90523339794433d, 2.565036406654116d);
//        go1.setGeoPosition(28.6319541d, 77.0950067d);
//
//        go1.setImageResource(R.drawable.creature_1);
//        go1.setName("Creature 1");
//
//        // Is it also possible to load the image asynchronously form internet
//        GeoObject go2 = new GeoObject(2l);
//        go2.setGeoPosition(41.90518966360719d, 2.56582424468222d);
//        go2.setGeoPosition(28.63195410000000d, 77.09500670000000d);
//
//        go2.setImageUri("http://beyondar.github.io/beyondar/images/logo_512.png");
//        go2.setImageUri("https://d2c7ipcroan06u.cloudfront.net/wp-content/uploads/2020/05/tanmay-bhat-2-edited-696x392.jpg");
//        go2.setName("Online image");
//
//        GeoObject go5 = new GeoObject(5l);
//        go5.setGeoPosition(41.90553066234138d, 2.565777906882577d);
//        go5.setImageResource(R.drawable.creature_5);
//        go5.setGeoPosition(28.6318541d, 77.0951067d);
//        go5.setName("Creature 5");
//
//        GeoObject go6 = new GeoObject(6l);
//        go6.setGeoPosition(41.90596218466268d, 2.565250806050688d);
//        go6.setImageResource(R.drawable.creature_6);
//        go6.setGeoPosition(28.6318641d, 77.0951167d);
//        go6.setName("Creature 6");
//
//        GeoObject go7 = new GeoObject(7l);
//        go7.setGeoPosition(41.90581776104766d, 2.565932313852319d);
//        go7.setImageResource(R.drawable.creature_2);
//        go7.setGeoPosition(28.6319341d, 77.0954067d);
//        go7.setName("Creature 2");
//
//        GeoObject go8 = new GeoObject(8l);
//        go8.setGeoPosition(41.90534261025682d, 2.566164369775198d);
//        go8.setImageResource(R.drawable.rectangle);
//        go8.setName("Object 8");
//
//        GeoObject go9 = new GeoObject(9l);
//        go9.setGeoPosition(41.90530734214473d, 2.565808038959814d);
//        go9.setImageResource(R.drawable.creature_4);
//        go9.setName("Creature 4");
//
//        GeoObject go10 = new GeoObject(10l);
//        go10.setGeoPosition(42.006667d, 2.705d);
//        go10.setImageResource(R.drawable.object_stuff);
//        go10.setName("Far away");
//
//
//        // Add the GeoObjects to the world
//        sharedWorld.addBeyondarObject(go1);
//        sharedWorld.addBeyondarObject(go2, LIST_TYPE_EXAMPLE_1);
//        sharedWorld.addBeyondarObject(go5);
//        sharedWorld.addBeyondarObject(go6);
//        sharedWorld.addBeyondarObject(go7);
//        sharedWorld.addBeyondarObject(go8);
//        sharedWorld.addBeyondarObject(go9);
//        sharedWorld.addBeyondarObject(go10);
//
//        return sharedWorld;
//    }

}