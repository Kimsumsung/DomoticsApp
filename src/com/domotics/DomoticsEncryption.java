package com.domotics;

public class DomoticsEncryption {
	
	/**
	 * Phone_id=Unit Number, Gateways provide 9 device/Number UDID or Device_id
	 * = Unit_id encr_decr_message = <Mess_TYPE>,<CMD>, <OUT_ID>,<OUT_NR>,[DATA]
	 * CR+LF
	 * 
	 * 
	 * 
	 * Usigned Char UDID[40]= Android Device id has char = 15 must add to 40
	 * char. Shift = UDID[pos]+UDID[0];// UDID = Android Device CharEncryoted =
	 * CharOriginal + Shift(withins ASCII table)
	 * 
	 * @param msg - incoming message
	 * @param phone_id
	 * @return Encrypted message (byte array)
	 */
	
	
	public byte[] Encrypt(byte[] msg, byte phone_id) {
		byte encrypt_message[] = new byte[msg.length + 2];
		// Declares array
		byte[] unit_id = {'1', '2', '3', '4', '5', '6', '7', '8', '9','0',
						  '1', '2', '3', '4', '5', '6', '7', '8', '9','0',
						  '1', '2', '3', '4', '5', '6', '7', '8', '9','0',
						  '1', '2', '3', '4', '5', '6', '7', '8', '9','0'
		};
		
		byte i = 2;
		int shift = 0;
		//msg = {'R', ',', 'O', 'O', '3', '1'}; // Command
																		// Client
																		// Side
		encrypt_message[0] = '1';// phone_id, 9 device
		encrypt_message[1] = ',';

		byte LINE_FEED = '\n';
		while (msg[i - 2] != LINE_FEED) {
			shift = unit_id[i - 1] + unit_id[0];
			encrypt_message[i] = msg[i - 2];
			if ((encrypt_message[i] >= '0' && encrypt_message[i] <= '9')
					|| (encrypt_message[i] >= 'A' && encrypt_message[i] <= 'Z')
					|| (encrypt_message[i] >= 'a' && encrypt_message[i] <= 'z')) {
				while (shift > 0) {
					encrypt_message[i]++;
					if (encrypt_message[i] > '9'
							&& encrypt_message[i] < 'A')
						encrypt_message[i] = 'A';
					else if (encrypt_message[i] > 'Z'
							&& encrypt_message[i] < 'a')
						encrypt_message[i] = 'a';
					else if (encrypt_message[i] > 'z')
						encrypt_message[i] = '0';
					--shift;
				}
			}
			i++;
		}
		encrypt_message[i] = LINE_FEED;
		return encrypt_message;
	}

	/**
	 * 
	 * @param msg
	 * @param phone_id
	 * @return Decrypted message (byte array)
	 */
	public byte[] Decrypt(byte[]in_decrypt) {
		
		
		byte shift; //first char is phone number[1,9], not encrypted
		byte LINE_FEED = '\n';
		byte[] unit_id = {'1', '2', '3', '4', '5', '6', '7', '8', '9','0',
				  		'1', '2', '3', '4', '5', '6', '7', '8', '9','0',
				  		'1', '2', '3', '4', '5', '6', '7', '8', '9','0',
				  		'1', '2', '3', '4', '5', '6', '7', '8', '9','0'};
		byte decrypt_message[] = new byte[in_decrypt.length - 2];
		
		byte i =0;
		
		while(in_decrypt[i+2] != LINE_FEED){
			shift = (byte) (unit_id[i+1]+unit_id[0]);
			decrypt_message[i] = in_decrypt[i+2];
			if((decrypt_message[i] >= '0' && decrypt_message[i] <= '9')|| 
					(decrypt_message[i] >= 'A' && decrypt_message[i] <= 'Z')|| 
					(decrypt_message[i] >= 'a' && decrypt_message[i] <= 'z'))
			{
				while(shift>0){
					decrypt_message[i]--;
					if (decrypt_message[i]<'0')
						decrypt_message[i] = 'z';
					else if (decrypt_message[i] > '9'&& decrypt_message[i] < 'A')
						decrypt_message[i] = '9';
					else if (decrypt_message[i] > 'Z' && decrypt_message[i]<'a'){
						decrypt_message[i] = 'Z';}
					--shift;
				}
			}
			i++;		
		}
		decrypt_message[i] = LINE_FEED;	
		return decrypt_message;
	}

}
