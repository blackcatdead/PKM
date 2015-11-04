package id.harni.pkm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import id.harni.help.AppController;
import id.harni.help.SessionManager;

public class DetailUsahaActivity extends Activity {

	SessionManager session;
	private String TAG = DetailUsahaActivity.class.getSimpleName();
	Activity ac;
	private String tag_string_req = "menu_req";
	String ip = "";
	String id = "0";
	TextView deskripsi;
	TextView usaha;
	private CustomListAdapterDetailUsaha adapter;
	ListView lv;
	NetworkImageView iv;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	String[] kegiatans = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_usaha);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		session = new SessionManager(getApplicationContext());
		HashMap<String, String> user = session.getUserDetails();
		ip = user.get(SessionManager.KEY_IP);

		Bundle b = getIntent().getExtras();
		id = b.getString("id");
		ac = this;

		final ListView lv = (ListView) findViewById(R.id.list_kegiatan);

		LayoutInflater inflater = getLayoutInflater();
		ViewGroup header = (ViewGroup) inflater.inflate(R.layout.head_detail_usaha, null, false);
		lv.addHeaderView(header);
		deskripsi = (TextView) header.findViewById(R.id.tvDetailUsaha_deskripsi);
		usaha = (TextView) header.findViewById(R.id.tvDetailUsaha_usaha);
		iv = (NetworkImageView) header.findViewById(R.id.ivDetailUsaha);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(DetailUsahaActivity.this, "clicked", Toast.LENGTH_SHORT).show();

			}
		});
		String url_makanan = "http://" + ip + "/ws_p2km/index.php/servicecontroller/dataUsaha?id_usaha=" + id;

		StringRequest stringRequest = new StringRequest(Request.Method.GET, url_makanan,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// Toast.makeText(MenuActivity.this, response,
						// Toast.LENGTH_SHORT).show();
						try {
							
							JSONArray jarr= new JSONArray(response.toString());
                      

							JSONObject job = jarr.getJSONObject(0);
							String usahaa = job.getString("usaha");
							String deskripsia = job.getString("deskripsi");
							String img = job.getString("logo");
							String tgl_buat = job.getString("datetime_created");
							String logo = job.getString("logo");
							setTitle(usahaa);
							usaha.setText(usahaa);
							deskripsi.setText(deskripsia);
							iv.setImageUrl("http://" + ip + "/ws_p2km/res/usaha_" + id + ".jpg", imageLoader);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(DetailUsahaActivity.this, "error", Toast.LENGTH_SHORT).show();
						
					}
				});
		// Add the request to the RequestQueue.
		// queue.add(stringRequest);
		AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), SetPicActivity.class);
				i.putExtra("id", id);
				startActivity(i);
			}
		});
		// final String[] values = new String[];
		final List<String> values = new ArrayList<String>();

		String url_kegiatan = "http://" + ip + "/ws_p2km/index.php/servicecontroller/dataKegiatan?id_usaha_fk=" + id;

		StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url_kegiatan,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						
						Toast.makeText(ac, response.trim(), Toast.LENGTH_SHORT).show();
						try {
							JSONArray jarr = new JSONArray(response.trim().toString());
							
                            adapter = new CustomListAdapterDetailUsaha(ac, jarr,ip,id);
                            lv.setAdapter(adapter);
                            
//							kegiatans = new String[jarr.length()];
//							for (int i = 0; i < jarr.length(); i++) {
//								JSONObject job = jarr.getJSONObject(i);
//								Toast.makeText(ac, job.getString("kegiatan"), Toast.LENGTH_SHORT).show();
//								kegiatans[i] = job.getString("kegiatan");
//							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
//						ArrayAdapter<String> adapter = new ArrayAdapter<String>(ac, android.R.layout.simple_list_item_1,
//								android.R.id.text1, kegiatans);
//
//						// Assign adapter to ListView
//
//						lv.setAdapter(adapter);
						
						
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(DetailUsahaActivity.this, "error", Toast.LENGTH_SHORT).show();
					}
				});
		// Add the request to the RequestQueue.
		// queue.add(stringRequest);
		AppController.getInstance().addToRequestQueue(stringRequest2, tag_string_req);

		
		
//		lv.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent i = new Intent(getApplicationContext(), KegiatanActivity.class);
//				startActivity(i);
//			}
//		});

		// Define a new Adapter
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Forth - the Array of data

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (session.isLoggedIn())
			getMenuInflater().inflate(R.menu.detail_usaha, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_tambahedit_kegiatan) {
			showDialogTambahUsaha();
			return true;
			
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void showDialogTambahUsaha()
	{
		
		AlertDialog.Builder builder = new AlertDialog.Builder(ac);
        LayoutInflater inflater = ac.getLayoutInflater();
        View vDialog = inflater.inflate(R.layout.dialog_tambah_edit_kegiatan, null);
        final EditText nama = (EditText) vDialog.findViewById(R.id.etTEUkegiatan_Nama);
        final EditText deskripsi = (EditText) vDialog.findViewById(R.id.etTEUkegiatan_deskripsi);
        Button btnSimpan = (Button) vDialog.findViewById(R.id.btnSimpanKegiatan);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // finishWithResult();
            	tambahKegiatanService(nama.getText().toString(), deskripsi.getText().toString(),id);
            	
            }
        });
        builder.setView(vDialog);
        builder.show();
	}
	
	public void tambahKegiatanService(String kegiatan, String deskripsi, String id)
	{
		
		String url_makanan = "http://"+ip+"/ws_p2km/index.php/servicecontroller/tambahKegiatan?kegiatan="+kegiatan.replace(" ", "%20")+"&deskripsi="+deskripsi.replace(" ", "%20")+"&id_usaha_fk="+id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_makanan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MenuActivity.this, response, Toast.LENGTH_SHORT).show();
                        if(response.trim().length()!=0)
                        {
                        	Toast.makeText(DetailUsahaActivity.this, "Sukses",Toast.LENGTH_SHORT).show();
                        	ac.finish();
                        	ac.startActivity(ac.getIntent());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailUsahaActivity.this, "error",Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        //queue.add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
	}
	
	
}
