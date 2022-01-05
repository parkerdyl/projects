package com2027.cw.dp00405.vsa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;

public class NFCActivity extends AppCompatActivity {

    public static final String TAG = NFCActivity.class.getSimpleName();

    String writeMessage;
    String action;
    String message = "Failed to discover tag";
    //private boolean isDialogDisplayed = false;
    private boolean isWrite = false;

    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        writeMessage = intent.getStringExtra("MESSAGE");
        action = intent.getStringExtra("ACTION");

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (action.equals("WRITE") && !writeMessage.equals("")) {
            setContentView(R.layout.activity_write_tag);
            displayMetrics();
            isWrite = true;
        } else if (action.equals("READ") && writeMessage.equals("")) {
            setContentView(R.layout.activity_read_tag);
            displayMetrics();
        }
    }

    public void writeToNfc(Ndef ndef, String message){
        //writeMessage.setText(getString(R.string.message_write_progress));
        if (ndef != null) {

            try {
                ndef.connect();
                NdefRecord mimeRecord = NdefRecord.createMime("text/plain", message.getBytes(Charset.forName("US-ASCII")));
                ndef.writeNdefMessage(new NdefMessage(mimeRecord));
                ndef.close();
                //Write Successful
                //writeMessage.setText(getString(R.string.message_write_success));
            } catch (IOException | FormatException e) {
                e.printStackTrace();
               // writeMessage.setText(getString(R.string.message_write_error));
            }
        }
    }

    public String readFromNFC(Ndef ndef) {
        String message = "Failed to read item";
        try {
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            message = new String(ndefMessage.getRecords()[0].getPayload());

            Log.d(TAG, "readFromNFC: " + message);

            //readMessage.setText(message);
            ndef.close();
        } catch (IOException | FormatException e) {
            e.printStackTrace();
        }

        return message;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(nfcAdapter!= null)
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(nfcAdapter!= null)
            nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Log.d(TAG, "onNewIntent: " + intent.getAction());

        if(tag != null) {
            //Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_SHORT).show();
            Ndef ndef = Ndef.get(tag);

            if (isWrite) {
                writeToNfc(ndef, writeMessage);

                finish();
            } else {
                try {
                    message = readFromNFC(ndef);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                Intent intent1 = new Intent();
                intent1.putExtra("CONTENTS", message);
                setResult(RESULT_OK, intent1);
                finish();
            }
        }
    }

    public void displayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * 0.7), (int) (height * 0.7));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

}
