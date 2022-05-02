package dduwcom.mobile.finalproject.ma01_20190937;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class GoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    final static String TAG = "GoogleMapActivity";
    final static int PERMISSION_REQ_CODE = 100;

    EditText et_placeInfo;
    String placeType;
    private GoogleMap mGoogleMap;
    private MarkerOptions markerOptions;
    private PlacesClient placesClient;
    private LocationManager locationManager;
    LatLng currentLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemap);

        et_placeInfo = findViewById(R.id.et_placeInfo);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        String place = (String) getIntent().getStringExtra("place");
        if (place.equals("library")) {
            placeType = PlaceType.LIBRARY;
        } else if (place.equals("bookstore")) {
            placeType = PlaceType.BOOK_STORE;
        }

        mapLoad(); // 맵 로딩시킴

        Places.initialize(getApplicationContext(), getString(R.string.google_api_key)); // getResouces 생략해도된다
        placesClient = Places.createClient(this);
    }


    /*입력된 유형의 주변 정보를 검색*/
    private void searchStart(String type) {
        new NRPlaces.Builder().listener(placesListener)
                .key(getResources().getString(R.string.google_api_key))
                .latlng(currentLoc.latitude, currentLoc.longitude)
                .radius(1000)
                .type(type)
                .build()
                .execute();
    }


    PlacesListener placesListener = new PlacesListener() {

        @Override
        public void onPlacesFailure(PlacesException e) {}
        @Override
        public void onPlacesStart() {}


        //noman.googlepaleces 가 import되있으면 지워야함(구글 api의 place와 충돌)
        @Override
        public void onPlacesSuccess(final List<noman.googleplaces.Place> places) { //돌리면서 사라질 수 있으므로 final
            Log.d(TAG, "adding Marker");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (noman.googleplaces.Place place : places) {
                        markerOptions.title(place.getName());
                        markerOptions.position(new LatLng(place.getLatitude(), place.getLongitude()));
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        Marker newMarker = mGoogleMap.addMarker(markerOptions);
                        newMarker.setTag(place.getPlaceId());
                        Log.d(TAG, place.getPlaceId());
                    }
                }
            });

        }

        @Override
        public void onPlacesFinished() {}
    };


    @Override
    public void onMapReady(GoogleMap googleMap) { // 맵 로딩시키면 실행됨
        mGoogleMap = googleMap;
        markerOptions = new MarkerOptions();

        Log.d(TAG, "Map ready");

        if (checkPermission()) {
            mGoogleMap.setMyLocationEnabled(true);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            currentLoc = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        } else { // 에러날때 학교 위치로
            currentLoc = new LatLng(37.606320, 127.041808);
        }

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 14));

        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Toast.makeText(GoogleMapActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mGoogleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
                searchStart(placeType);
            }
        });

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String placeId = marker.getTag().toString();
                getPlaceDetail(placeId);
            }
        });
    }

    /*Place ID 의 장소에 대한 세부정보 획득*/
    private void getPlaceDetail(String placeId) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse response) {
                Place place = response.getPlace();
                et_placeInfo.setText(place.getAddress() +" "+ place.getName());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof  ApiException) {
                    ApiException apiException = (ApiException) e;
                    int statusCode = apiException.getStatusCode();
                    Log.e(TAG, "Place Not FOund : "+ e.getMessage());
                }
            }
        });
    }


    /*구글맵을 멤버변수로 로딩*/
    private void mapLoad() {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);      // 매개변수 this: MainActivity 가 OnMapReadyCallback 을 구현하므로
    }



    /* 필요 permission 요청 */
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    public void mapOnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_placeInfo:
                String result = et_placeInfo.getText().toString();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("resultAddress", result);
                setResult(RESULT_OK, resultIntent);
                finish();
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 퍼미션을 획득하였을 경우 맵 로딩 실행
                mapLoad();
            } else {
                // 퍼미션 미획득 시 액티비티 종료
                Toast.makeText(this, "앱 실행을 위해 권한 허용이 필요함", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
