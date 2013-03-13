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
	public String DeviceId;
	
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);

		arrayList = new ArrayList<String>();
		//Button Send to 
		final EditText editText = (EditText) findViewById(R.id.editText);
		final Button send = (Button) findViewById(R.id.send_button);
		final ImageButton turnOn = (ImageButton) findViewById(R.id.imageturnon);
		final ImageButton turnOff = (ImageButton) findViewById(R.id.imageturnoff);
		final Button version = (Button) findViewById(R.id.version);
		final Button name = (Button) findViewById(R.id.name);
		final Button unitid = (Button) findViewById(R.id.unitid);
		final Button register =(Button) findViewById(R.id.register);
		

		// relate the listView from java to the one created in xml
		mList = (ListView) findViewById(R.id.list);
		mAdapter = new MyCustomAdapter(this, arrayList);
		mList.setAdapter(mAdapter);
		
		/*
		//Get Device_ID and get log android_id and Device_id
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		Log.d(">>>>",
				"Android ID: "
						+ Secure.getString(getContentResolver(),
								Secure.ANDROID_ID));
		Log.d(">>>>", "Device ID : " + tm.getDeviceId());
		 String DeviceFulfill = "0000000000";
		 String DeviceId = new String(tm.getDeviceId());
		 String repeatDeviceId =  new String(DeviceId);
		 repeatDeviceId += DeviceId;
		 repeatDeviceId = repeatDeviceId+DeviceFulfill;
		*/
		 
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
				//String repeatDeviceId;
				//String message = "R,U";
				//String registerMessage = new String(repeatDeviceId);
				//String combineString = message + registerMessage;
				// add the text in the arrayList
				//StringBuilder sb1 = new StringBuilder("R,U,3537710535716333537710535716330000000000");
				String message = "R,U,3537710535716333537710535716330000000000";
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
           String strValues = new String(values[0]);
           char[] charMessage = strValues.toCharArray();
           
           
           if(charMessage[2] != 'V' && charMessage[2] != 'N' && charMessage[2] != 'U' ){
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
           else{
        	   //in the arrayList we add the messaged received from server
               arrayList.add("Server:"+strValues);
               
               // notify the adapter that the data set has changed. This means that new message received
               // from server was added to the list
               mAdapter.notifyDataSetChanged();
           }

        }
	}
}