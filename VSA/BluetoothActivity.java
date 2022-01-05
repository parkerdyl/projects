package com2027.cw.dp00405.vsa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends Activity {

    ListView listBT;
    SwipeRefreshLayout refreshLayout;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice[] bluetoothDevices;

    String contents;
    ArrayList<Boolean> isTrue = new ArrayList<>();

    TransferData transferData;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int CONNECTION_FAILURE = 4;
    static final int MESSAGE_RECEIVED = 5;

    int REQUEST_BLUETOOTH = 1;

    private static final String APP_NAME = "VSA";
    private static final UUID MY_UUID = UUID.fromString("78b1e6e5-a50a-4632-a8c8-6496066e96a6");

    //need to consider devices that have connected already, wont be shown in listview

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        contents = intent.getStringExtra("listvalues");

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enable, REQUEST_BLUETOOTH);
        }


        if (!contents.equals("till")) {
            setContentView(R.layout.activity_bluetooth_phone);
            displayMetrics();

            listBT = findViewById(R.id.listBT);
            listBT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!contents.equals("till")) {
                        ClientThread clientThread = new ClientThread(bluetoothDevices[i]);
                        clientThread.start();
                    }
                }
            });

            refreshLayout = findViewById(R.id.refreshLayout);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    listDevices();

                    refreshLayout.setRefreshing(false);
                }
            });

            listDevices();
        } else {
            setContentView(R.layout.activity_bluetooth_till);
            displayMetrics();

            Toast.makeText(this, "TILL", Toast.LENGTH_SHORT).show();

            ServerThread serverThread = new ServerThread();
            serverThread.start();
        }
    }

    public void listDevices() {
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        String[] devicenum = new String[devices.size()];
        bluetoothDevices = new BluetoothDevice[devices.size()];

        int i = 0;
        if (devices.size() > 0) {
            for (BluetoothDevice device : devices) {
                bluetoothDevices[i] = device;
                devicenum[i] = device.getName();
                i++;
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, devicenum);
            listBT.setAdapter(arrayAdapter);
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case STATE_CONNECTED:
                    Toast.makeText(BluetoothActivity.this, "CONNECTED", Toast.LENGTH_SHORT).show();
                    try {
                        transferData.write(contents.getBytes());

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    break;
                case MESSAGE_RECEIVED:
                    if (contents.equals("till")) {
                        byte[] read = (byte[]) message.obj;
                        String msg = new String(read, 0, message.arg1);
                        Toast.makeText(BluetoothActivity.this, msg, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        intent.putExtra("CONTENTS", msg);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    break;
            }
            return true;
        }
    });

    private class ServerThread extends Thread {

        private BluetoothServerSocket serverSocket;

        public ServerThread() {
            try {
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME, MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            BluetoothSocket socket = null;

            while (socket == null) {
                try {
                    //Message message = Message.obtain();
                    //message.what = STATE_CONNECTING;
                    //handler.sendMessage(message);

                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    //Message message = Message.obtain();
                    //message.what = CONNECTION_FAILURE;
                    //handler.sendMessage(message);
                }

                if (socket != null) {
                    //Message message = Message.obtain();
                    //message.what = STATE_CONNECTED;
                    //handler.sendMessage(message);

                    transferData = new TransferData(socket);
                    transferData.start();
                    //try {
                    //    socket.close();
                    //} catch (IOException e) {
                    //    e.printStackTrace();
                    //}
                    break;
                }

            }
        }
    }

    private class ClientThread extends Thread {

        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientThread (BluetoothDevice dev) {
            device = dev;

            try {
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                socket.connect();
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                handler.sendMessage(message);

                transferData = new TransferData(socket);
                transferData.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class TransferData extends Thread {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        private TransferData (BluetoothSocket socket) {
            bluetoothSocket = socket;
            InputStream tempIn = null;
            OutputStream tempOut = null;

            try {
                tempIn = bluetoothSocket.getInputStream();
                tempOut = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream = tempIn;
            outputStream = tempOut;

        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = inputStream.read(buffer);
                    handler.obtainMessage(MESSAGE_RECEIVED, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write (byte[] bytes) {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
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

    @Override
    public void onDestroy() {
        super.onDestroy();



    }

}
