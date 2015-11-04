package id.harni.pkm;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import id.harni.help.AndroidMultiPartEntity;
import id.harni.help.AndroidMultiPartEntity.ProgressListener;
import id.harni.help.SessionManager;

public class SetPicActivity extends Activity {

	private static final int PICK_IMAGE = 0;
String urii="";
	private static final int RESULT_LOAD_IMG = 1;
	String ip = "";
	Button btn;
	ImageView iv;
	String id;
	SessionManager session;
	long totalSize = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_pic);
		
		Bundle b = getIntent().getExtras();
		id = b.getString("id");
		
		session = new SessionManager(getApplicationContext());
		HashMap<String, String> user = session.getUserDetails();
		ip = user.get(SessionManager.KEY_IP);

		
		btn= (Button) findViewById(R.id.btnSimpanPic);
		iv=(ImageView) findViewById(R.id.ivPic);
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new UploadFileToServer().execute();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_pic, menu);
		return true;
	}
	
	private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            //progressBar.setProgress(0);
            super.onPreExecute();
        }
 
        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            //progressBar.setVisibility(View.VISIBLE);
 
            // updating progress bar value
            //progressBar.setProgress(progress[0]);
 
            // updating percentage value
            //txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }
 
        @Override
        protected String doInBackground(Void... params) {
            //return uploadFile();
            //return uploadFile(urii);
        	return uploadFile(urii);
        }
        
        public String uploadFile(String sourceFileUri) {
            
            
            String fileName = sourceFileUri;
    
            HttpURLConnection conn = null;
            DataOutputStream dos = null;  
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024; 
            File sourceFile = new File(sourceFileUri); 
             
            if (!sourceFile.isFile()) {
                 
                 //dialog.dismiss(); 
                  
                 //Log.e("uploadFile", "Source File not exist :"
                                     //+uploadFilePath + "" + uploadFileName);
                  
                 runOnUiThread(new Runnable() {
                     public void run() {
                         //messageText.setText("Source File not exist :"
                                 //+uploadFilePath + "" + uploadFileName);
                     }
                 }); 
                  
                 return "19";
              
            }
            else
            {
                int serverResponseCode=0;
				try { 
                      
                       // open a URL connection to the Servlet
                     FileInputStream fileInputStream = new FileInputStream(sourceFile);
                     String urlz="http://" + ip + "/ws_p2km/index.php/servicecontroller/uploadImage";
                     URL url = new URL(urlz);
                      
                     // Open a HTTP  connection to  the URL
                     conn = (HttpURLConnection) url.openConnection(); 
                     conn.setDoInput(true); // Allow Inputs
                     conn.setDoOutput(true); // Allow Outputs
                     conn.setUseCaches(false); // Don't use a Cached Copy
                     conn.setRequestMethod("POST");
                     conn.setRequestProperty("Connection", "Keep-Alive");
                     conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                     conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                     conn.setRequestProperty("uploaded_file", "usaha"+id+".jpg"); 
                      
                     dos = new DataOutputStream(conn.getOutputStream());
            
                     dos.writeBytes(twoHyphens + boundary + lineEnd); 
                     dos.writeBytes("Content-Disposition: form-data; name=\""+"uploaded_file"+"\";filename=\""
                                               + "usaha_"+id+".jpg" + "\"" + lineEnd);
                      
                     dos.writeBytes(lineEnd);
            
                     // create a buffer of  maximum size
                     bytesAvailable = fileInputStream.available(); 
            
                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
                     buffer = new byte[bufferSize];
            
                     // read file and write it into form...
                     bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                        
                     while (bytesRead > 0) {
                          
                       dos.write(buffer, 0, bufferSize);
                       bytesAvailable = fileInputStream.available();
                       bufferSize = Math.min(bytesAvailable, maxBufferSize);
                       bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
                        
                      }
            
                     // send multipart form data necesssary after file data...
                     dos.writeBytes(lineEnd);
                     dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            
                     // Responses from the server (code and message)
                     serverResponseCode = conn.getResponseCode();
                     String serverResponseMessage = conn.getResponseMessage();
                       
                     Log.i("uploadFile", "HTTP Response is : "
                             + serverResponseMessage + ": " + serverResponseCode);
                      
                     if(serverResponseCode == 200){
                          
                         runOnUiThread(new Runnable() {
                              public void run() {
                                   
                                  //String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                  //              +" http://www.androidexample.com/media/uploads/"
                                  //              +uploadFileName;
                                   
                                  //messageText.setText(msg);
                                  Toast.makeText(SetPicActivity.this, "File Upload Complete.", 
                                               Toast.LENGTH_SHORT).show();
                              }
                          });                
                     }    
                      
                     //close the streams //
                     fileInputStream.close();
                     dos.flush();
                     dos.close();
                       
                } catch (MalformedURLException ex) {
                     
                    //dialog.dismiss();  
                    ex.printStackTrace();
                     
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //messageText.setText("MalformedURLException Exception : check script url.");
                            Toast.makeText(SetPicActivity.this, "MalformedURLException", 
                                                                Toast.LENGTH_SHORT).show();
                        }
                    });
                     
                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
                } catch (Exception e) {
                     
                    //dialog.dismiss();  
                    e.printStackTrace();
                     
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //messageText.setText("Got Exception : see logcat ");
                            Toast.makeText(SetPicActivity.this, "Got Exception : see logcat ", 
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.e("Upload file to server Exception", "Exception : "
                                                     + e.getMessage(), e);  
                }
                //dialog.dismiss();       
                return serverResponseCode+""; 
                 
             } // End else block 
           } 
 
        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;
 
            HttpClient httpclient = new DefaultHttpClient();
            String url="http://" + ip + "/ws_p2km/index.php/servicecontroller/uploadImage";
            HttpPost httppost = new HttpPost(url);
 
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new ProgressListener() {
 
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
 
                File sourceFile = new File(urii);
 
                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));
 
                // Extra parameters if you want to pass to server
                entity.addPart("website",
                        new StringBody("www.androidhive.info"));
                entity.addPart("email", new StringBody("abc@gmail.com"));
 
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
 
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
 
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
 
            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
 
            return responseString;
 
        }
 
        @Override
        protected void onPostExecute(String result) {
            //Log.e(TAG, "Response from server: " + result);
            Toast.makeText(getApplicationContext(), result+"", Toast.LENGTH_LONG).show();
            // showing the server response in an alert dialog
            //showAlert(result);
 
            super.onPostExecute(result);
        }
 
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
            	 Uri uri = data.getData();
            	 urii=getRealPathFromURI(uri);
            	 Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                 // Log.d(TAG, String.valueOf(bitmap));
            	 Toast.makeText(this, "URI: "+urii,
                         Toast.LENGTH_LONG).show();
            	 iv.setImageBitmap(bitmap);
 
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
 
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_editImage) {
			 Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
			    getIntent.setType("image/*");

			    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			    pickIntent.setType("image/*");

			    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
			    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

			    startActivityForResult(chooserIntent, RESULT_LOAD_IMG);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private String getRealPathFromURI(Uri contentUri) {
	    String[] proj = { MediaStore.Images.Media.DATA };
	    CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
	    Cursor cursor = loader.loadInBackground();
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    String result = cursor.getString(column_index);
	    cursor.close();
	    return result;
	}
}
