package tads.ifrn.pdsc.banhobommobile.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tads.ifrn.pdsc.banhobommobile.R;
import tads.ifrn.pdsc.banhobommobile.ws.AppController;

public class HistoricoActivity extends ActionBarActivity {

    public static final String TAG = AppController.class.getSimpleName();

    private Bundle extras;

    ListView listView;

    private TextView codigoEstacaoPerfil;

    private TextView tituloPraia;

    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        extras = getIntent().getExtras();


        //Mudar titulo do perfil da estacao dinamicamente
        tituloPraia = new TextView(this);
        tituloPraia = (TextView) findViewById(R.id.tituloEstacaoHist);
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
        codigoEstacaoPerfil = (TextView) findViewById(R.id.codigoEstacaoHist);
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

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.listViewHist);

        List<String> listaHistorico = new ArrayList<String>();
        listaHistorico.add("");

        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listaHistorico);

        listView.setAdapter(mAdapter);

        //Mudar imagem do perfil
        ImageView img = new ImageView(this);
        img = (ImageView) findViewById(R.id.imgTituloHist);

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
                        List<String> listaHistorico = new ArrayList<String>();
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject coleta = (JSONObject) response.get(i);
                                if (coleta.getString("codigoEstacao").equals(extras.getString("codigoEstacao"))) {
                                    // Defined Array values to show in ListView
                                    String status = "";
                                    if (coleta.getBoolean("status")) {
                                        status = "Própria";
                                    } else {
                                        status = "Imprópria";
                                    }

                                    String str = ("Data: " + coleta.getString("data")
                                                                + " - Status: "
                                                                + status);

                                    mAdapter.add(str);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        mAdapter.notifyDataSetChanged();
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

        return super.onOptionsItemSelected(item);
    }
}
