package com2027.cw.dp00405.vsa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.UUID;

import static android.Manifest.permission.CAMERA;

public class BasketFragment extends Fragment {

    private ArrayList<Integer> images = new ArrayList<>();
    private ArrayList<String> brands = new ArrayList<>();
    private ArrayList<String> models = new ArrayList<>();
    private ArrayList<String> prices = new ArrayList<>();
    private ArrayList<String> descriptions = new ArrayList<>();

    String contents;

    boolean[] switchBool = new boolean[]{false, false, false};
    //boolean[] switchBool;

    TextView textViewCost;
    FloatingActionButton fButtonAdd;

    RecyclerView recyclerView;
    AdapterBasket adapterBasket;
    ItemTouchHelper itemTouchHelper;

    View view;
    BluetoothAdapter bluetoothAdapter;
    NfcAdapter nfcAdapter;
    //PackageManager pm;

    TutorialClass tutorialClass;

    static final int REQUEST_QR = 1;
    static final int REQUEST_NFC = 2;
    static final int REQUEST_BLUETOOTH = 3;
    static final int MESSAGE_RECEIVED = 5;

    private static final String APP_NAME = "VSA";
    private static final UUID MY_UUID = UUID.fromString("78b1e6e5-a50a-4632-a8c8-6496066e96a6");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_basket, container, false);

        ActivityCompat.requestPermissions(getActivity(), new String[]{CAMERA}, getActivity().RESULT_FIRST_USER);

        switchBool = ((NavigationActivity) getActivity()).getSwitch();

        //pm = this.getPackageManager();
        if (!switchBool[1]) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        } else {
            bluetoothAdapter = null;
        }

        if(!switchBool[0]) {
            nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
        } else {
            nfcAdapter = null;
        }

        contents = ((NavigationActivity) getActivity()).getContents();
        Log.e("logartian", contents);
        try {
            if (!contents.equals("")) {
                images = ((NavigationActivity) getActivity()).getImages();
                for (String content : contents.split("#")) {
                    brands.add(content.split(",")[1]);
                    models.add(content.split(",")[2]);
                    prices.add("£" + content.split(",")[3]);
                    descriptions.add(content.split(",")[6]);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            contents = "";
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed to read item", Toast.LENGTH_SHORT).show();
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        textViewCost = view.findViewById(R.id.textValue);
        initRecyclerView();

        fButtonAdd = view.findViewById(R.id.fPhoneAdd);
        fButtonAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
                            Intent intent = new Intent(getActivity(), NFCActivity.class);
                            intent.putExtra("MESSAGE", "");
                            intent.putExtra("ACTION", "READ");
                            startActivityForResult(intent, REQUEST_NFC);
                        } else {
                            if (!switchBool[2]) {
                                Intent intent = new Intent(getActivity(), QRActivity.class);
                                intent.putExtra("DIRECTION", 0);
                                startActivityForResult(intent, REQUEST_QR);
                            } else {
                                Toast.makeText(getActivity(), "The camera module must be enabled", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        return view;
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    Log.e("log_swiped_left", "Swiped left");
                    //delete item
                    removeContents(position);
                    ((NavigationActivity) getActivity()).replaceFragment(new BasketFragment(), null, "BasketFragment");


                    break;
                case ItemTouchHelper.RIGHT:
                    //open webpage or more info
                    ((NavigationActivity) getActivity()).getSupportActionBar().setTitle("Website");
                    WebsiteFragment website = new WebsiteFragment();
                    //Log.e("log_url", descriptions.get(position));
                    website.setURL(descriptions.get(position));
                    ((NavigationActivity) getActivity()).replaceFragment(website, null, "WebsiteFragment");
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            Bitmap icon;
            RectF dest;

            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // Get RecyclerView item from the ViewHolder
                View itemView = viewHolder.itemView;

                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                float width = height / 3;

                Paint p = new Paint();
                if (dX > 0) {
                    // website
                    p.setARGB(255, 0, 0, 255);
                    // Draw Rect with varying right side, equal to displacement dX
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX + itemView.getLeft(),
                            (float) itemView.getBottom(), p);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.website_image);
                    dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, dest, p);
                } else {
                    // delete
                    p.setARGB(255, 255, 0, 0);
                    // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), p);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.bin_image);
                    dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, dest, p);
                }
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void initRecyclerView() {
        double cost = 0;
        for (String price : prices) {
            cost += Double.parseDouble(price.replaceAll("£", ""));
        }
        textViewCost.setText("£" + cost);

        adapterBasket = new AdapterBasket(getActivity(), images, brands, models, prices);
        recyclerView.setAdapter(adapterBasket);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        if (!((NavigationActivity) getActivity()).getTutorial()[0]) {
            tutorialClass = new TutorialClass(getActivity());
            tutorialClass.addToBasket();
        }
    }

    private void addItem(Intent intent) {
        String returned = intent.getStringExtra("CONTENTS");
        try {
            if (!contents.equals("")) {
                contents += "#" + returned;
            } else {
                contents += returned;
            }

            if (returned.split(",")[7].equals("1")) {
                images.add(R.drawable.ic_lock_outline_red_32dp);
            } else {
                images.add(null);
            }
            brands.add(returned.split(",")[1]);
            models.add(returned.split(",")[2]);
            prices.add("£" + returned.split(",")[3]);
            descriptions.add(returned.split(",")[6]);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        if (!((NavigationActivity) getActivity()).getTutorial()[1]) {
            tutorialClass = new TutorialClass(getActivity());
            tutorialClass.swipeItem();
        }
    }

    public void purchaseBasket(final Context context) {
        if (!((NavigationActivity) getActivity()).getTutorial()[2]) {
            tutorialClass = new TutorialClass(getActivity());
            tutorialClass.payForItem();
        } else {
            if (contents.equals("")) {
                Toast.makeText(context, "Please scan an item before attempting transfer",
                        Toast.LENGTH_SHORT).show();
            } else {
                new AlertDialog.Builder(context)
                        .setTitle("Would you like to pay via QR or Bluetooth?")
                        //.setMessage("Pay at Phone or Till")
                        .setPositiveButton("BT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (bluetoothAdapter != null) {
                                    //enable bluetooth if disabled or run activity
                                    if (bluetoothAdapter.isEnabled()) {
                                        Intent intent = new Intent(getActivity(), BluetoothActivity.class);
                                        intent.putExtra("listvalues", contents);
                                        startActivity(intent);
                                    } else {
                                        Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                        startActivityForResult(btIntent, REQUEST_BLUETOOTH);
                                    }
                                } else {
                                    Toast.makeText(context, "Please pay via QR",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("QR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getActivity(), GenerateQRActivity.class);
                                intent.putExtra("listvalues", contents);
                                startActivity(intent);
                            }
                        })
                        .setNeutralButton(android.R.string.cancel, null)
                        .show();
            }
        }
    }

    public void removeContents(Integer value) {
        images.remove(value);

        String[] array = contents.split("#");
        String text = "";
        int i = 0;
        for (String content : array) {
            if (value != i && i != array.length) {
                if (i != 0) {
                    text += "#";
                }
                text += content;
            }
            i++;
        }
        this.contents = text;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_QR) {
            if (resultCode == getActivity().RESULT_OK) {
                addItem(intent);
                initRecyclerView();
            }
        } else if (requestCode == REQUEST_NFC) {
            if (resultCode == getActivity().RESULT_OK) {
                addItem(intent);
                initRecyclerView();
            } else {
                Toast.makeText(getActivity(), "Reading Tag Failed", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_BLUETOOTH) {
            if(resultCode == getActivity().RESULT_OK) {
                Intent intent1 = new Intent(getActivity(), BluetoothActivity.class);
                intent1.putExtra("listvalues", contents);
                startActivity(intent1);
            } else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Bluetooth failed to start", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        ((NavigationActivity) getActivity()).setImage(images);
        ((NavigationActivity) getActivity()).setContents(contents);
    }

    //NEEDS TO SAVE THE INFORMATION IN THE ARRAYLIST WHEN INTENT DESTROYED
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bluetoothAdapter != null) {
            bluetoothAdapter.disable();
        }
    }
}
