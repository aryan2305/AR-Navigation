package com.example.coderescue.navar;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.beyondar.android.view.OnClickBeyondarObjectListener;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.example.coderescue.Activities.BeyondARWorld;
import com.example.coderescue.Activities.CameraWithGoogleMapsActivity;
import com.example.coderescue.Fragments.HomeFragment;
import com.example.coderescue.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.SphericalUtil;
import com.example.coderescue.navar.ar.ArBeyondarGLSurfaceView;
import com.example.coderescue.navar.ar.ArFragmentSupport;
import com.example.coderescue.navar.ar.OnTouchBeyondarViewListenerMod;
import com.example.coderescue.navar.network.PlaceResponse;
import com.example.coderescue.navar.network.PoiResponse;
import com.example.coderescue.navar.network.RetrofitInterface;
import com.example.coderescue.navar.network.poi.Result;
import com.example.coderescue.navar.utils.UtilsCheck;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class PoiBrowserActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks
    ,GoogleApiClient.OnConnectionFailedListener,OnClickBeyondarObjectListener,
        OnTouchBeyondarViewListenerMod{

    private final static String TAG="PoiBrowserActivity";

    private TextView textView;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LayoutInflater layoutInflater;
    private ArFragmentSupport arFragmentSupport;
    private World world;

    @BindView(R.id.poi_place_detail)
    CardView poi_cardview;
    @BindView(R.id.poi_place_close_btn)
    ImageButton poi_cardview_close_btn;
    @BindView(R.id.poi_place_name)
    TextView poi_place_name;
    @BindView(R.id.poi_place_address)
    TextView poi_place_addr;
    @BindView(R.id.poi_place_image)
    ImageView poi_place_image;
    @BindView(R.id.poi_place_ar_direction)
    Button poi_place_ar_btn;
    @BindView(R.id.poi_place_maps_direction)
    Button poi_place_maps_btn;
    @BindView(R.id.poi_brwoser_progress)
    ProgressBar poi_browser_progress;
//    @BindView(R.id.seekBar)
//    SeekBar seekbar;
    @BindView(R.id.seekbar_cardview)
    CardView seekbar_cardview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_browser);
        ButterKnife.bind(this);

        seekbar_cardview.setVisibility(View.GONE);
        poi_browser_progress.setVisibility(View.GONE);
        poi_cardview.setVisibility(View.GONE);

        if(!UtilsCheck.isNetworkConnected(this)){
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.poi_layout),
                    "Turn Internet On", Snackbar.LENGTH_SHORT);
            mySnackbar.show();
        }

        arFragmentSupport = (ArFragmentSupport) getSupportFragmentManager().findFragmentById(R.id.poi_cam_fragment);
        arFragmentSupport.setOnClickBeyondarObjectListener(this);
        arFragmentSupport.setOnTouchBeyondarViewListener(this);


        textView=(TextView) findViewById(R.id.loading_text);

        Set_googleApiClient(); //Sets the GoogleApiClient

        poi_cardview_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekbar_cardview.setVisibility(View.VISIBLE);
                poi_cardview.setVisibility(View.GONE);
                poi_place_image.setImageResource(android.R.color.transparent);
                poi_place_name.setText(" ");
                poi_place_addr.setText(" ");
            }
        });

    }

    void Poi_list_call(int radius){
        poi_browser_progress.setVisibility(View.VISIBLE);

        BeyondARWorld.getVictims();
                    Log.d("Correct", "yayy yayy yayya yayy");

                    poi_browser_progress.setVisibility(View.GONE);
                    seekbar_cardview.setVisibility(View.VISIBLE);

                    List<GeoObject> poiResult=BeyondARWorld.listVictims;
                    Configure_AR(poiResult);
    }

    void Poi_details_call(String placeid){

        poi_browser_progress.setVisibility(View.VISIBLE);

                String[] arrOfStr = placeid.split("_");
                String victimLatitude = arrOfStr[0];
                String victimLongitude = arrOfStr[1];

                seekbar_cardview.setVisibility(View.GONE);
                poi_cardview.setVisibility(View.VISIBLE);
                poi_browser_progress.setVisibility(View.GONE);


                poi_place_name.setText("OPTIONS:");
                poi_place_addr.setText(victimLatitude + " " + victimLongitude);
Log.d(TAG, placeid);


            poi_place_maps_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent;
                        try{
                            Uri.Builder builder = new Uri.Builder();
                            builder.scheme("http")
                                    .authority("maps.google.com")
                                    .appendPath("maps")
                                    .appendQueryParameter("saddr", mLastLocation.getLatitude()+","+mLastLocation.getLongitude())
                                    .appendQueryParameter("daddr", Double.parseDouble( victimLatitude)+","+
                                            Double.parseDouble(victimLongitude));

                            intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse( builder.build().toString()));

                                        Context context = PoiBrowserActivity.this
                                                .getApplicationContext();
                                        CharSequence text = "Opening Maps to guid to the location!";
                                        int duration = Toast.LENGTH_LONG;

                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();

                            startActivity(intent);
                            finish();
                        }catch (Exception e){
                            Log.d(TAG, "onClick: mapNav Exception caught");
                            Toast.makeText(PoiBrowserActivity.this, "Unable to Open Maps Navigation", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                poi_place_ar_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(PoiBrowserActivity.this,ArCamActivity.class);

                        try {
                            intent.putExtra("SRC", "Current Location");
                            intent.putExtra("DEST",  victimLatitude+","+
                                    victimLongitude);
                            intent.putExtra("SRCLATLNG",  mLastLocation.getLatitude()+","+mLastLocation.getLongitude());
                            intent.putExtra("DESTLATLNG", victimLatitude+","+
                                    victimLongitude);
                            startActivity(intent);
                            finish();
                        }catch (NullPointerException npe){
                            Log.d(TAG, "onClick: The IntentExtras are Empty");
                        }
                    }
                });

            }

    public class PoiPhotoAsync extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            poi_place_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            poi_place_image.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];

            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

    private void Configure_AR(List<GeoObject> pois){

        layoutInflater=getLayoutInflater();

        world=new World(getApplicationContext());
        world.setGeoPosition(mLastLocation.getLatitude(),mLastLocation.getLongitude());

        System.out.println(mLastLocation.getLatitude() + " " + mLastLocation.getLongitude());
        world.setDefaultImage(R.drawable.ar_sphere_default);

        arFragmentSupport.getGLSurfaceView().setPullCloserDistance(0);

        GeoObject geoObjects[]=new GeoObject[pois.size()];

        for(int i=0;i<pois.size();i++) {
            GeoObject poiGeoObj = new GeoObject(1000 * (i + 1));
//            poiGeoObj = pois.get(i);
            poiGeoObj.setGeoPosition(pois.get(i).getLatitude(),
                    pois.get(i).getLongitude());
            poiGeoObj.setName( poiGeoObj.getLatitude() + "_" + poiGeoObj.getLongitude() + "_" + poiGeoObj.getId() );
//
            Bitmap snapshot = null;
            View view = getLayoutInflater().inflate(R.layout.poi_container, null);
            TextView name = (TextView) view.findViewById(R.id.poi_container_name);
            TextView dist = (TextView) view.findViewById(R.id.poi_container_dist);
            ImageView icon = (ImageView) view.findViewById(R.id.poi_container_icon);

            name.setText(pois.get(i).getName());
            String distance = String.valueOf((SphericalUtil.computeDistanceBetween(
                    new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()),
                    new LatLng(pois.get(i).getLatitude(),
                            pois.get(i).getLongitude()))) / 1000);
            String d = distance + " KM";
            dist.setText(d);
            Log.d(TAG, "hey bunny " + d);

            String type = getResources().getString(R.string.restaurant);
            icon.setImageResource(R.drawable.map_icon);

            view.setDrawingCacheEnabled(false);
            view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
            try {
                //  Paint paint = new Paint(ANTI_ALIAS_FLAG);
//                paint.setTextSize(textSize);
//                paint.setColor(textColor);
                //paint.setTextAlign(Paint.Align.LEFT);
//                float baseline = -paint.ascent(); // ascent() is negative
//                int width = (int) (paint.measureText(pois.get(i).getName()) + 0.5f); // round
//                int height = (int) (baseline + paint.descent() + 0.5f);

                view.measure(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                snapshot = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight()
                        , Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(snapshot);
                view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
                view.draw(canvas);

                //canvas.drawBitmap(snapshot);
                //snapshot = Bitmap.createBitmap(view.getDrawingCache(),10,10,200,100); // You can tell how to crop the snapshot and whatever in this method
            }  finally {
                view.setDrawingCacheEnabled(false);
            }

            String uri = saveToInternalStorage(snapshot, pois.get(i).getId() + ".png");
            //icon.setImageURI(Uri.parse(uri));
            poiGeoObj.setImageUri(uri);
            world.addBeyondarObject(poiGeoObj);
            Log.d(TAG, "hello honey bunny " + i + " " + poiGeoObj.getId());

        }

                Log.d(TAG, "no of objects in the world "  + mLastLocation.getLatitude()+ " " + mLastLocation.getLongitude());
        Log.d(TAG, "no of objects in the world "  + world.getBeyondarObjectLists().size());
        Log.d(TAG, "no of objects also in the world "  + world.getBeyondarObjectLists().get(0).size() );
        for( BeyondarObject i1 : world.getBeyondarObjectLists().get(0)){
            Log.d(TAG, "hurray  "  +  i1.getName() );

        }

        textView.setVisibility(View.INVISIBLE);

        // ... and send it to the fragment
        arFragmentSupport.setWorld(world);
 }

    private String saveToInternalStorage(Bitmap bitmapImage,String name){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,name);

        Log.d(TAG, "saveToInternalStorage: PATH:"+mypath.toString());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.toString();
    }

    public String getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path).toString();
    }

    public Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    private void Set_googleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        else {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (mLastLocation != null) {
                try {
                    Poi_list_call(900);
                }catch (Exception e){
                    Log.d(TAG, "onCreate: Intent Error");
                }
            }
        }

    }

    @Override
    public void onClickBeyondarObject(ArrayList<BeyondarObject> beyondarObjects) {
        if (beyondarObjects.size() > 0) {
            Poi_details_call(beyondarObjects.get(0).getName());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onTouchBeyondarView(MotionEvent event, ArBeyondarGLSurfaceView var2) {

        float x = event.getX();
        float y = event.getY();

        ArrayList<BeyondarObject> geoObjects = new ArrayList<BeyondarObject>();

        // This method call is better to don't do it in the UI thread!
        // This method is also available in the BeyondarFragment
        var2.getBeyondarObjectsOnScreenCoordinates(x, y, geoObjects);

        String textEvent = "";
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                textEvent = "Event type ACTION_DOWN: ";
                break;
            case MotionEvent.ACTION_UP:
                textEvent = "Event type ACTION_UP: ";
                break;
            case MotionEvent.ACTION_MOVE:
                textEvent = "Event type ACTION_MOVE: ";
                break;
            default:
                break;
        }

        Iterator<BeyondarObject> iterator = geoObjects.iterator();
        while (iterator.hasNext()) {
            BeyondarObject geoObject = iterator.next();
            textEvent = textEvent + " " + geoObject.getName();
            Log.d(TAG, "onTouchBeyondarView: ATTENTION !!! "+textEvent);
        }
    }
}
