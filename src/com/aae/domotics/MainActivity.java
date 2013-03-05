package com.aae.domotics;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;
import com.domotics.DomoticsEncryption;

public class MainActivity extends Activity {
	private ListView mList;
	private ArrayList<String> arrayList;
	private MyCustomAdapter mAdapter;
	private TCPClient mTcpClient;
	public static String DeviceId;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);

		arrayList = new ArrayList<String>();
		//Button Send to 
		final EditText editText = (EditText) findViewById(R.id.editText);
		Button send = (Button) findViewById(R.id.send_button);
		final ImageButton turnOn = (ImageButton) findViewById(R.id.imageturnon);
		final ImageButton turnOff = (ImageButton) findViewById(R.id.imageturnoff);
		Button version = (Button) findViewById(R.id.version);
		Button name = (Button) findViewById(R.id.name);
		Button unitid = (Button) findViewById(R.id.unitid);
		Button register =(Button) findViewById(R.id.register);
		

		// relate the listView from java to the one created in xml
		mList = (ListView) findViewById(R.id.list);
		mAdapter = new MyCustomAdapter(this, arrayList);
		mList.setAdapter(mAdapter);

		// connect to the server
		new connectTask().execute("");

		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String message = editText.getText().toString();

				// add the text in the arrayList
				arrayList.add("Client: " + message);

				// sends the message to the server
				if (mTcpClient != null) {
					mTcpClient.sendMessage(message);
				}

				// refresh the list
				mAdapter.notifyDataSetChanged();
				editText.setText("");
			}
		});
		// turn-on the light
		turnOn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String message ="R,O,O,31";
				//Convert String to byte
				//byte[] messageByte = message.getBytes();
				
				// add the text in the arrayList
				arrayList.add("Client: " + message);

				// sends the message to the server
				if (mTcpClient != null) {
					mTcpClient.sendMessage(message);
				}

				// refresh the list
				mAdapter.notifyDataSetChanged();
				editText.setText("");
				
			}
		});
		
		//turn-off the light
		turnOff.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String message ="R,o,O,31";
				//Convert String to byte
				//byte[] messageByte = message.getBytes();
				
				// add the text in the arrayList
				arrayList.add("Client: " + message);

				// sends the message to the server
				if (mTcpClient != null) {
					mTcpClient.sendMessage(message);
				}

				// refresh the list
				mAdapter.notifyDataSetChanged();
				editText.setText("");
				
			}
		});
		
		version.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String message = "E,V";
				// add the text in the arrayList
				arrayList.add("Client: " + message);

				// sends the message to the server
				if (mTcpClient != null) {
					mTcpClient.sendMessage(message);
				}

				// refresh the list
				mAdapter.notifyDataSetChanged();
				editText.setText("");
				
	
			}
		});
		
		name.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String message = "R,N";
				// add the text in the arrayList
				arrayList.add("Client: " + message);

				// sends the message to the server
				if (mTcpClient != null) {
					mTcpClient.sendMessage(message);
				}

				// refresh the list
				mAdapter.notifyDataSetChanged();
				editText.setText("");
				
	
			}
		});
		
		unitid.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				String message = "R,U";
				// add the text in the arrayList
				arrayList.add("Client: " + message);

				// sends the message to the server
				if (mTcpClient != null) {
					mTcpClient.sendMessage(message);
				}

				// refresh the list
				mAdapter.notifyDataSetChanged();
				editText.setText("");
				
	
			}
		});
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				String message = "R,U";
				// add the text in the arrayList
				arrayList.add("Client: " + message);

				// sends the message to the server
				if (mTcpClient != null) {
					mTcpClient.sendMessage(message);
				}

				// refresh the list
				mAdapter.notifyDataSetChanged();
				editText.setText("");
				
	
			}
		});
		
	
		
		
		
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);

		Log.d(">>>>",
				"Android ID: "
						+ Secure.getString(getContentResolver(),
								Secure.ANDROID_ID));
		Log.d(">>>>", "Device ID : " + tm.getDeviceId());
		
		DeviceId = tm.getDeviceId();
		
	}
			

	@Override
	public void onResume() {
		super.onResume();
	}

	public class connectTask extends AsyncTask<String, String, TCPClient> {

		@Override
		protected TCPClient doInBackground(String... message) {
			
			// we create a TCPClient object and
			mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
				@Override
				// here the messageReceived method is implemented
				public void messageReceived(String message) {
					// this method calls the onProgressUpdate
					publishProgress(message);
				}
			});
			mTcpClient.run();

			return null;
		}

		@Override
        protected void onProgressUpdate(String... values) {
           super.onProgressUpdate(values);
           //String strValues = new String(values[0]);
           String strValues = new String(values[0]);
           char[] charMessage = strValues.toCharArray();
            
           if(charMessage[0]=='I'&&charMessage[2]=='V'&&charMessage[4]=='0'&&charMessage[5]=='1'&&charMessage[6]=='0'&&
        	  charMessage[7]=='1'&&charMessage[9]=='0'&&charMessage[10]=='1'&&charMessage[11]=='0'&&charMessage[12]=='0'){
        	   //in the arrayList we add the messaged received from server
               arrayList.add("Server:"+strValues);
               
               // notify the adapter that the data set has changed. This means that new message received
               // from server was added to the list
               mAdapter.notifyDataSetChanged();
               }
           else if(charMessage[0]=='C'&& charMessage[2]=='N'&& charMessage[4]=='0'){
        	   //in the arrayList we add the messaged received from server
               arrayList.add("Server:"+strValues);
               
               // notify the adapter that the data set has changed. This means that new message received
               // from server was added to the list
               mAdapter.notifyDataSetChanged();
           }
           
           else if(charMessage[0]=='C'&& charMessage[2]=='U'&& charMessage[4]=='0'&& charMessage[6]=='0'){
        	   //in the arrayList we add the messaged received from server
               arrayList.add("Server:"+strValues);
               
               // notify the adapter that the data set has changed. This means that new message received
               // from server was added to the list
               mAdapter.notifyDataSetChanged();
           }
           
           else{   
            //Decryption---------Valuse need to decrypt.
            DomoticsEncryption decrypt = new DomoticsEncryption();
            String decryptMsg = strValues+"\r\n";
            byte[] bytesValues = decryptMsg.getBytes();
            byte[] decrypted_msg = decrypt.Decrypt(bytesValues);
            String output_decryption = new String(decrypted_msg);

            //in the arrayList we add the messaged received from server
            arrayList.add("Server:"+ output_decryption);
            
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            mAdapter.notifyDataSetChanged();
           }
            
        }
	}
}