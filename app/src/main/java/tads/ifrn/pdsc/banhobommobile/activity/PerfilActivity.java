package tads.ifrn.pdsc.banhobommobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tads.ifrn.pdsc.banhobommobile.R;
import tads.ifrn.pdsc.banhobommobile.ws.AppController;

public class PerfilActivity extends ActionBarActivity {

    private Bundle extras;

    public static final String TAG = AppController.class.getSimpleName();

    private TextView textViewStatus;

    private ImageView imgViewStatus;

    private TextView codigoEstacaoPerfil;

    private TextView tituloPraia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        extras = getIntent().getExtras();

        //Mudar titulo do perfil da estacao dinamicamente
        tituloPraia = new TextView(this);
        tituloPraia = (TextView) findViewById(R.id.tituloEstacao);
        String titulo;
        if (savedInstanceState == null) {
            if (extras == null) {
                titulo = null;
            }
            else {
                titulo = extras.getString("tituloPraia");
            }
        } else {
            titulo = extras.getString("tituloPraia");
        }
        tituloPraia.setText(titulo);

        //Mudar nome do codigo da estacao
        codigoEstacaoPerfil = new TextView(this);
        codigoEstacaoPerfil = (TextView) findViewById(R.id.codigoEstacao);
        String codigoEstacao;
        if (savedInstanceState == null) {
            if (extras == null) {
                codigoEstacao = null;
            }
            else {
                codigoEstacao = extras.getString("codigoEstacao");
            }
        } else {
            codigoEstacao = extras.getString("codigoEstacao");
        }
        codigoEstacaoPerfil.setText(codigoEstacao);

        //Mudar texto do status
        textViewStatus = new TextView(this);
        textViewStatus = (TextView) findViewById(R.id.textoStatus);

        //Mudar imagem do status
        imgViewStatus = new ImageView(this);
        imgViewStatus = (ImageView) findViewById(R.id.statusIcon);


        //Mudar imagem do perfil
        ImageView img = new ImageView(this);
        img = (ImageView) findViewById(R.id.imgTitulo);

        //Picasso.with(this).load("http://farm1.staticflickr.com/618/20781168511_1a9606033e_b.jpg").into(img);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(true)
                .build();

        Picasso.with(this)
                .load("http://farm1.staticflickr.com/618/20781168511_1a9606033e_b.jpg")
                .transform(transformation)
                .into(img);

        this.setarInformacoes();
    }

    public void setarInformacoes() {
        String urlColetas = "http://env-4818724.jelasticlw.com.br/banhobom3/rest/cliente/coletasMobile";

        JsonArrayRequest req = new JsonArrayRequest(urlColetas,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject coleta = (JSONObject) response.get(i);
                                if (coleta.getString("codigoEstacao").equals(extras.getString("codigoEstacao"))) {
                                    if (coleta.getBoolean("status")) {
                                        Drawable mDrawable = getResources().getDrawable(R.drawable.ok50);
                                        imgViewStatus.setImageDrawable(mDrawable);

                                        String textoStatus = "Esta localidade está apta para banho!";
                                        textViewStatus.setText(textoStatus);
                                    }
                                    else {
                                        Drawable mDrawable = getResources().getDrawable(R.drawable.cancel50);
                                        imgViewStatus.setImageDrawable(mDrawable);

                                        String textoStatus = "Esta localidade NÃO está apta para banho!";
                                        textViewStatus.setText(textoStatus);

                                    }
                                }
//                                if (estacao.getBoolean("status")) {
//                                    mMap.addMarker(new MarkerOptions().position(new LatLng(estacao.getDouble("latitude"),
//                                            estacao.getDouble("longitude")))
//                                            .title(estacao.getString("praia"))
//                                            .snippet(estacao.getString("codigo"))
//                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//                                }
//                                else {
//                                    mMap.addMarker(new MarkerOptions().position(new LatLng(estacao.getDouble("latitude"),
//                                            estacao.getDouble("longitude")))
//                                            .title(estacao.getString("codigo"))
//                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                                }

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_info) {
            Intent intent1 = new Intent(getApplicationContext(), InfoActivity.class);
            intent1.putExtra("codigoEstacao", codigoEstacaoPerfil.getText());
            startActivity(intent1);
        }
        if (id == R.id.action_historico) {
            Intent intent1 = new Intent(getApplicationContext(), HistoricoActivity.class);
            intent1.putExtra("codigoEstacao", codigoEstacaoPerfil.getText());
            intent1.putExtra("tituloPraia", tituloPraia.getText());
            startActivity(intent1);
        }

        return super.onOptionsItemSelected(item);
    }
}
