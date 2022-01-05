package com2027.cw.dp00405.vsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;

public class TillActivity extends AppCompatActivity {

    Toolbar toolbar;

    String tagData;
    String contents = "";


    NfcAdapter nfcAdapter;

    //Integer bToolbar = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_till);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        toolbar = findViewById(R.id.tillToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Till");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.till_container,
                    new TillFragment(), "TillFragment").commit();
        }
    }

    //======================================================
    // Change fragements

    public void replaceFragment(Fragment fragment, Fragment backstack, String name) {
        //edit_toolbar.getMenu().clear();
        //bToolbar = 0;
        if (backstack == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.till_container, fragment, name).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.till_container, fragment, name).addToBackStack(backstack.getClass().getName()).commit();
        }
    }

    //======================================================
    // Create button in edit_toolbar and deal with presses

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        toolbar.getMenu().clear();
        getMenuInflater().inflate(R.menu.toolbar_nfctag, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.toolbarNFCTag) {
            final EditText editText = new EditText(this);
            editText.setText(tagData);
            new AlertDialog.Builder(this)
                    .setTitle("Enter data values:")
                    .setMessage("If NFC, please place device onto Tag before confirmation")
                    .setView(editText)
                    .setPositiveButton("NFC Tag", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (nfcAdapter != null && nfcAdapter.isEnabled()) {
                                tagData = editText.getText().toString();
                                //write tagdata to tag
                                Intent intent = new Intent(TillActivity.this, NFCActivity.class);
                                intent.putExtra("ACTION", "WRITE");
                                intent.putExtra("MESSAGE", tagData);
                                startActivity(intent);
                            } else {
                                Toast.makeText(TillActivity.this, "Enable NFC", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("QR Sticker", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tagData = editText.getText().toString();
                            Intent intent = new Intent(TillActivity.this, GenerateQRActivity.class);
                            intent.putExtra("listvalues", tagData);
                            startActivity(intent);
                        }
                    })
                    .setNeutralButton(android.R.string.cancel, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }



    //encrypt tag message
    /*private String encrypt(String string) {
        String secret = "";
        ///String encrypted = string * secret;
        return ""; }*/

    //permanant lock of tag
    /*private void lockTag(Tag tag) {
        if (tag == null) {
            return;
        }
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (ndef.canMakeReadOnly()) {
                    ndef.makeReadOnly();
                }
                ndef.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
