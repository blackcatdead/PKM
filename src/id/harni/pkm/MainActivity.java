package id.harni.pkm;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import id.harni.help.SessionManager;

public class MainActivity extends Activity {

	Button btnUsaha;
	Button btnLogin;
	Button btnPkm;
	TextView tvLoginSebagai;
	SessionManager session;
	String id = "0";
	Activity ac;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ac=this;
		
		btnUsaha = (Button) findViewById(R.id.btnMain_usaha);
		btnLogin = (Button) findViewById(R.id.btnMain_loginlogout);
		btnPkm = (Button) findViewById(R.id.btnMain_pkm);
		tvLoginSebagai = (TextView) findViewById(R.id.tvMain_loginsebagai);

		session = new SessionManager(getApplicationContext());
		HashMap<String, String> user = session.getUserDetails();
		tvLoginSebagai.setText("Login Sebagai : " + user.get(SessionManager.KEY_NAME));
		id = user.get(SessionManager.KEY_ID);
		if (session.isLoggedIn()) {
			btnLogin.setText("LOG OUT");
			tvLoginSebagai.setVisibility(View.VISIBLE);
		} else {
			tvLoginSebagai.setVisibility(View.GONE);
			btnLogin.setText("LOG IN");
		}

		btnUsaha.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), UsahaActivity.class);
				i.putExtra("id", id);
				startActivity(i);
			}
		});

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (session.isLoggedIn()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(ac);
					builder.setMessage("Yakin ingin logout?")
							.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// FIRE ZE MISSILES!
							session.logoutUser();
							finish();
	                    	startActivity(getIntent());
						}
					}).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog
							
						}
					});
					// Create the AlertDialog object and return it
					builder.show();
				}

				else {
					Intent i = new Intent(getApplicationContext(), LoginActivity.class);
					startActivity(i);
				}
				
			}
		});

		btnPkm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), PKMActivity.class);
				startActivity(i);
			}
		});
	}

}
