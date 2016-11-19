package com.errorstation.tracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rubayet on 19-Nov-16.
 */

public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    String nw = String.valueOf(countWords(message));

                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
                    Log.d("SmsReceiver","Word: "+nw);

                    information(message);


                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "senderNum: "+ senderNum + ", message: " + message, duration);
                    toast.show();

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }

    private void information(String message) {

        List<String> msgs = Arrays.asList(message.split(","));
        for(int i=0;i<msgs.size();i++)
        {
            Log.d("SmsReceiver","Information ["+String.valueOf(i)+"] = "+msgs.get(i));
        }
        List<String> msgs2 = Arrays.asList(msgs.get(0).split(":"));
        for(int i=0;i<msgs2.size();i++)
        {
            Log.d("SmsReceiver","Information2 ["+String.valueOf(i)+"] = "+msgs2.get(i));
        }
        List<String> msgs3 = Arrays.asList(msgs.get(3).split("on"));
        for(int i=0;i<msgs3.size();i++)
        {
            Log.d("SmsReceiver","Information3 ["+String.valueOf(i)+"] = "+msgs3.get(i));
        }

    }

    public static int countWords(String s){

        int wordCount = 0;

        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }

}