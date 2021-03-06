package id.harni.pkm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import id.harni.help.AppController;
import id.harni.help.SessionManager;

public class CustomListAdapterDetailUsaha extends BaseAdapter{
	private Activity activity;
	private LayoutInflater inflater;
	private JSONArray jarr;
	private String ip;
	private String tipe;
	private String id;
	SessionManager session;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	private String TAG = CustomListAdapterKegiatan.class.getSimpleName();
	private String tag_string_req = "tambah_detal_pesanan_req";

	public CustomListAdapterDetailUsaha(Activity activity, JSONArray jarr, String ip, String id) {
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
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.item_kegiatan, null);

		
		// ImageView img = (ImageView)
		// convertView.findViewById(R.id.tvItemUsahaPic);
		TextView kegiatan = (TextView) convertView.findViewById(R.id.item_kegiatan);
		
		String id_kegiatan = "";
		String keg = "";
		String deskripsia = "";
		JSONObject m = null;
		try {
			m = jarr.getJSONObject(position);
			id_kegiatan = m.getString("id_kegiatan").toString();

			keg = m.getString("kegiatan").toString();
			kegiatan.setText(keg);
			
			
			//thumbNail.setImageUrl("http://" + ip + "/ws_p2km/res/datadokumentasi" + id_dokumentasi + ".jpg", imageLoader);
			// id_menu = m.getString("id_menu").toString();
			// id.setText(id.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		final String id_keg=id_kegiatan;
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent i = new Intent(activity.getApplicationContext(), KegiatanActivity.class);
	             
				i.putExtra("id", id_keg);
	              activity.startActivity(i);
				
			}
		});

		return convertView;
	}

}
