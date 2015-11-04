package id.harni.pkm;

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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import id.harni.help.AppController;
import id.harni.help.SessionManager;

public class CustomListAdapterUsaha extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private JSONArray jarr;
    private String ip;
    private String tipe;
    private String id;
    
    SessionManager session;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private String TAG = CustomListAdapterUsaha.class.getSimpleName();
    private String tag_string_req = "tambah_detal_pesanan_req";


    public CustomListAdapterUsaha(Activity activity, JSONArray jarr, String ip, String id) {
    	session = new SessionManager(activity);
    	this.activity = activity;
        this.jarr = jarr;

        this.ip = ip;
        this.id = id;
        
        if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();


        Toast.makeText(activity, jarr.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getCount() {
        return jarr.length();
    }

    @Override
    public Object getItem(int location) {
        try {
            return jarr.get(location);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_usaha, null);



        final TextView usaha = (TextView) convertView.findViewById(R.id.tvItemUsahaUsaha);
        final TextView deskrip = (TextView) convertView.findViewById(R.id.tvUsahaDeskripsipendek);
        //ImageView img = (ImageView) convertView.findViewById(R.id.tvItemUsahaPic);
        NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.tvItemUsahaPic);

        String id_usaha="";
        String usahaa="";
        String deskripsia="";
        JSONObject m = null;
        try {
            m = jarr.getJSONObject(position);
            usahaa=m.getString("usaha").toString();
            deskripsia=m.getString("deskripsi").toString();
            id_usaha=m.getString("id_usaha").toString();
            
            usaha.setText(usahaa);
            deskrip.setText(deskripsia);
            thumbNail.setImageUrl("http://"+ip+"/ws_p2km/res/usaha_"+id_usaha+".jpg",imageLoader);
            //id_menu = m.getString("id_menu").toString();
            //id.setText(id.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String id_usahay=id_usaha;
        final String usahay=usahaa;
        final String deskripsiy=deskripsia;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent i = new Intent(activity.getApplicationContext(), DetailUsahaActivity.class);
              i.putExtra("id", id_usahay);
              activity.startActivity(i);


            }
        });


        if(session.isLoggedIn())
        {
        	convertView.setOnLongClickListener(new OnLongClickListener() {
    			
    			@Override
    			public boolean onLongClick(View v) {
    				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder
                            .setItems(R.array.dialogUsaha, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // The 'which' argument contains the index position
                                    // of the selected item
                                    if(which == 0)
                                    {
                                    	AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        LayoutInflater inflater = activity.getLayoutInflater();
                                        View vDialog = inflater.inflate(R.layout.dialog_tambah_edit_usaha, null);
                                        final EditText nama = (EditText) vDialog.findViewById(R.id.etTEUsaha_Nama);
                                        final EditText deskripsi = (EditText) vDialog.findViewById(R.id.etTEUsaha_deskripsi);
                                        Button btnSimpan = (Button) vDialog.findViewById(R.id.btnTEUsaha);
                                        nama.setText(usahay);
                                        deskripsi.setText(deskripsiy);
                                        
                                        btnSimpan.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                               // finishWithResult();
                                            	editUsahaService(id_usahay, nama.getText().toString(), deskripsi.getText().toString());
                                            	
                                            }
                                        });
                                        builder.setView(vDialog);
                                        builder.show();

                                    }else if(which == 1)
                                    {
                                    	 AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                         builder.setMessage("Yakin akan menghapus pesanan?")
                                                 .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                                     public void onClick(DialogInterface dialog, int id) {
                                                         // FIRE ZE MISSILES!
                                                         hapusUsaha(id_usahay);
                                                     }
                                                 })
                                                 .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                                     public void onClick(DialogInterface dialog, int id) {
                                                         // User cancelled the dialog

                                                     }
                                                 });
                                         // Create the AlertDialog object and return it
                                         builder.show();
                                    }
                                }
                            });
                    builder.show();
    				return false;
    			}
    		});
        }
        

        return convertView;
    }

    public void editUsahaService(String id_usaha,String usaha, String deskripsi)
    {
    	String url_makanan = "http://"+ip+"/ws_p2km/index.php/servicecontroller/edithUsaha?id_usaha="+id_usaha+"&usaha="+usaha+"&deskripsi="+deskripsi;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_makanan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MenuActivity.this, response, Toast.LENGTH_SHORT).show();
                    	activity.finish();
                    	activity.startActivity(activity.getIntent());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "error",Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        //queue.add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    	
    }
    
    public void hapusUsaha(String id)
    {
    	String url_makanan = "http://"+ip+"/ws_p2km/index.php/servicecontroller/hapusUsaha?id_usaha="+id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_makanan,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MenuActivity.this, response, Toast.LENGTH_SHORT).show();
                    	activity.finish();
                    	activity.startActivity(activity.getIntent());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "error",Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        //queue.add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    	
    }
//    private void finishWithResult()
//    {
//        Bundle conData = new Bundle();
//        conData.putString("results", "Thanks Thanks");
//        Intent intent = new Intent();
//        intent.putExtras(conData);
//        activity.setResult(activity.RESULT_OK, intent);
//        activity.finish();
//    }

//    public void tambahDetailOrder(String id_ordr_fk, String id_menu_fk, String quantity, String catatan, String subtotal, String status)
//    {
////        http://localhost/ws_resto/index.php/servicecontroller/tambahDetailOrder?id_order_fk=5&id_menu_fk=1&quantity=3&catatan=tidak%20pedas&subtotal=30000&status=0
//        String url_order_hari_ini = "http://"+ip+"/resto/index.php/servicecontroller/tambahDetailOrder?id_order_fk="+id_ordr_fk+"&id_menu_fk="+id_menu_fk+"&quantity="+quantity+"&catatan="+catatan+"&subtotal="+subtotal+"&status="+status;
//        Toast.makeText(activity, url_order_hari_ini, Toast.LENGTH_SHORT).show();
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_order_hari_ini,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        Toast.makeText(activity, "response : "+response.trim(),Toast.LENGTH_SHORT).show();
//                        if(response.trim().matches("1"))
//                        {
//                            //activity.finish();
//                            //activity.startActivity(activity.getIntent());
//
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(activity, "error",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
//
//        finishWithResult();
//
//    }




}