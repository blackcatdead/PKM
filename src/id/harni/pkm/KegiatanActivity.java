package id.harni.pkm;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import id.harni.help.AppController;
import id.harni.help.SessionManager;

public class KegiatanActivity extends Activity {

	SessionManager session;
	private String TAG = DetailUsahaActivity.class.getSimpleName();
	Activity ac;
	private String tag_string_req = "menu_req";
	 private CustomListAdapterKegiatan adapter;
	String ip = "";
	String id = "0";
	TextView deskripsi;
	TextView usaha;
	GridView gv;
	NetworkImageView iv;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kegiatan);
		
		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		session = new SessionManager(getApplicationContext());
		HashMap<String, String> user = session.getUserDetails();
		ip = user.get(SessionManager.KEY_IP);

		Bundle b = getIntent().getExtras();
		id = b.getString("id");
		ac = this;
		
		gv = (GridView) findViewById(R.id.gridView1);
		
		String url_makanan = "http://"+ip+"/ws_p2km/index.php/servicecontroller/datadokumentasi";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_makanan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MenuActivity.this, response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray jarr= new JSONArray(response.toString());
                            adapter = new CustomListAdapterKegiatan(ac, jarr,ip,id);
                            gv.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(KegiatanActivity.this, "error",Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        //queue.add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kegiatan, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
