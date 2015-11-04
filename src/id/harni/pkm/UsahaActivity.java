package id.harni.pkm;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import id.harni.help.AppController;
import id.harni.help.SessionManager;

public class UsahaActivity extends Activity {


    SessionManager session;
    private String TAG = UsahaActivity.class.getSimpleName();
    Activity ac;
    private String tag_string_req = "menu_req";
    private CustomListAdapterUsaha adapter;
    String ip="";
    String id="0";
    String tipe="";
    ListView lv_usaha;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usaha);
		
		session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        ip = user.get(SessionManager.KEY_IP);

        
        Bundle b = getIntent().getExtras();
        id = b.getString("id");
        lv_usaha = (ListView) findViewById(R.id.list_usaha);
        ac=this;
        
        String url_makanan = "http://"+ip+"/ws_p2km/index.php/servicecontroller/dataUsaha";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_makanan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MenuActivity.this, response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray jarr= new JSONArray(response.toString());
                            adapter = new CustomListAdapterUsaha(ac, jarr,ip,id);
                            lv_usaha.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UsahaActivity.this, "error",Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        //queue.add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if(session.isLoggedIn())
			getMenuInflater().inflate(R.menu.usaha, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_tambah_usaha) {
			showDialogTambahUsaha();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void showDialogTambahUsaha()
	{
		
		AlertDialog.Builder builder = new AlertDialog.Builder(ac);
        LayoutInflater inflater = ac.getLayoutInflater();
        View vDialog = inflater.inflate(R.layout.dialog_tambah_edit_usaha, null);
        final EditText nama = (EditText) vDialog.findViewById(R.id.etTEUsaha_Nama);
        final EditText deskripsi = (EditText) vDialog.findViewById(R.id.etTEUsaha_deskripsi);
        Button btnSimpan = (Button) vDialog.findViewById(R.id.btnTEUsaha);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // finishWithResult();
            	tambahUsahaService(nama.getText().toString(), deskripsi.getText().toString());
            	
            }
        });
        builder.setView(vDialog);
        builder.show();
	}
	
	public void tambahUsahaService(String usaha, String deskripsi)
	{
		
		String url_makanan = "http://"+ip+"/ws_p2km/index.php/servicecontroller/tambahUsaha?usaha="+usaha.replace(" ", "%20")+"&deskripsi="+deskripsi.replace(" ", "%20");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_makanan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MenuActivity.this, response, Toast.LENGTH_SHORT).show();
                        if(response.trim().length()!=0)
                        {
                        	Toast.makeText(UsahaActivity.this, "Sukses",Toast.LENGTH_SHORT).show();
                        	ac.finish();
                        	ac.startActivity(ac.getIntent());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UsahaActivity.this, "error",Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        //queue.add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
	}
	
	
}
