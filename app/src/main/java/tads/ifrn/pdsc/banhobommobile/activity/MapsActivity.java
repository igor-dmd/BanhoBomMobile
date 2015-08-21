package tads.ifrn.pdsc.banhobommobile.activity;

import android.content.Intent;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tads.ifrn.pdsc.banhobommobile.R;
import tads.ifrn.pdsc.banhobommobile.ws.AppController;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    public static final String TAG = AppController.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(-5.883546, -35.167693))
                        .zoom(8).build();

                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));

                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        String urlEstacoes = "http://192.168.0.18:3000/stations.json";

//        boolean status = false;

        JsonArrayRequest req = new JsonArrayRequest(urlEstacoes,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                               JSONObject estacao = (JSONObject) response.get(i);
                                mMap.addMarker(new MarkerOptions().position(new LatLng(estacao.getDouble("latitude"),
                                        estacao.getDouble("longitude")))
                                        .title(estacao.getString("nome")));

                                System.out.print(estacao.getString("nome"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
//                                Toast.makeText(getApplicationContext(),
//                                        "Error: " + e.getMessage(),
//                                        Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(req);

//            if (!status) {
//                mMap.addMarker(new MarkerOptions().position(new LatLng(estacao.getLatitude(), estacao.getLongitude())).title(estacao.getNome())
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//            } else {
//                mMap.addMarker(new MarkerOptions().position(new LatLng(estacao.getLatitude(), estacao.getLongitude())).title(estacao.getNome())
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
//            }


            //Link para o perfil da estacao
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent1 = new Intent(getApplicationContext(), PerfilActivity.class);
                    intent1.putExtra("titulo", marker.getTitle());
                    startActivity(intent1);
                }
            });
        }
    }
