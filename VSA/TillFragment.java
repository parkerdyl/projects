package com2027.cw.dp00405.vsa;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TillFragment extends Fragment {

    Button buttonQR, buttonNFC;
    TextView textTill;
    String contents;
    ArrayList<Boolean> isTrue = new ArrayList<>();

    BluetoothAdapter bluetoothAdapter;

    static final int REQUEST_QR = 1;
    static final int REQUEST_NFC = 2;
    static final int REQUEST_BLUETOOTH = 3;
    static final int TRANSFER_BLUETOOTH = 4;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_till, container, false);

        buttonQR = view.findViewById(R.id.bTillQR);
        buttonQR.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), QRActivity.class);
                        intent.putExtra("DIRECTION", 1);
                        startActivityForResult(intent, REQUEST_QR);
                    }
                });

        buttonNFC = view.findViewById(R.id.bTillOther);
        buttonNFC.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (bluetoothAdapter != null) {
                            //enable bluetooth if disabled or run activity
                            if (bluetoothAdapter.isEnabled()) {
                                Intent intent = new Intent(getActivity(), BluetoothActivity.class);
                                intent.putExtra("listvalues", "till");
                                startActivityForResult(intent, TRANSFER_BLUETOOTH);
                            } else {
                                Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(btIntent, REQUEST_BLUETOOTH);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please pay via Till", Toast.LENGTH_SHORT).show();
                        }
                }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_QR) {
            if (resultCode == getActivity().RESULT_OK) {
                contents = intent.getStringExtra("CONTENTS");
                textTill = view.findViewById(R.id.textTill);
                //only puts first of list into textView
                textTill.setText(contents);

                ((TillActivity) getActivity()).replaceFragment(new ConfirmFragment().newInstance(contents), new TillFragment(), "ConfirmFragment");
            }
        } else if (requestCode == REQUEST_BLUETOOTH) {
            if (resultCode == getActivity().RESULT_OK) {
                Intent intent1 = new Intent(getActivity(), BluetoothActivity.class);
                intent1.putExtra("listvalues", "till");
                startActivityForResult(intent1, TRANSFER_BLUETOOTH);
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Failed to turn on bluetooth", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == TRANSFER_BLUETOOTH) {
            Log.e("log_runningbt", "It be runnnig fam");
            if (resultCode == getActivity().RESULT_OK) {
                contents = intent.getStringExtra("CONTENTS");
                ((TillActivity) getActivity()).replaceFragment(new ConfirmFragment().newInstance(contents), new TillFragment(), "ConfirmFragment");
            }
        }
    }
}
