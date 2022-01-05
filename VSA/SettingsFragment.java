package com2027.cw.dp00405.vsa;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {

    View view;
    //RelativeLayout relativePay;
    RelativeLayout relativeNFC;
    RelativeLayout relativeBT;
    RelativeLayout relativeCamera;
    RelativeLayout relativeStore;
    RelativeLayout relativeBookmark;
    RelativeLayout relativeClubcard;
    RelativeLayout relativeTill;
    RelativeLayout relativeDelete;
    RelativeLayout relativeLogout;
    RelativeLayout relativeContact;

    View viewAccount;
    View viewBookmark;
    View viewClubcard;
    View viewBlock;

    Switch switchNFC;
    Switch switchBluetooth;
    Switch switchCamera;

    boolean[] switchBool;

    ArrayList<String[]> accounts = new ArrayList<>();

    private static final String PREF_CLASS = "class_vsa";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        switchNFC = view.findViewById(R.id.switchNFC);
        switchBluetooth = view.findViewById(R.id.switchBluetooth);
        switchCamera = view.findViewById(R.id.switchCamera);

        //switchBool = new boolean[]{preferences.getBoolean("nfc", false), preferences.getBoolean("bt", false), preferences.getBoolean("camera", false)};

        switchBool = ((NavigationActivity) getActivity()).getSwitch();
        switchNFC.setChecked(switchBool[0]);
        switchBluetooth.setChecked(switchBool[1]);
        switchCamera.setChecked(switchBool[2]);

        //relativePay = view.findViewById(R.id.relativePay);
        relativeNFC = view.findViewById(R.id.relativeNFC);
        relativeBT = view.findViewById(R.id.relativeBT);
        relativeCamera = view.findViewById(R.id.relativeCamera);
        relativeStore = view.findViewById(R.id.relativeStore);
        relativeBookmark = view.findViewById(R.id.relativeBookmark);
        relativeClubcard = view.findViewById(R.id.relativeClubcard);
        relativeTill = view.findViewById(R.id.relativeTill);
        relativeDelete = view.findViewById(R.id.relativeDelete);
        relativeLogout = view.findViewById(R.id.relativeLogout);
        relativeContact = view.findViewById(R.id.relativeContact);

        viewBlock = view.findViewById(R.id.viewBlock);
        viewAccount = view.findViewById(R.id.viewAccount);
        viewBookmark = view.findViewById(R.id.viewBookmark);
        viewClubcard = view.findViewById(R.id.viewClubcard);

        relativeClubcard.setVisibility(View.GONE);
        viewClubcard.setVisibility(View.GONE);
        if(!((NavigationActivity) getActivity()).getEmail().equals("admin")) {
            viewBlock.setVisibility(View.GONE);
            relativeNFC.setVisibility(View.GONE);
            relativeBT.setVisibility(View.GONE);
            relativeCamera.setVisibility(View.GONE);
            relativeTill.setVisibility(View.GONE);
            if (((NavigationActivity) getActivity()).getEmail().equals("")) {
                relativeBookmark.setVisibility(View.GONE);
                relativeDelete.setVisibility(View.GONE);
                relativeLogout.setVisibility(View.GONE);
                viewAccount.setVisibility(View.GONE);
                viewBookmark.setVisibility(View.GONE);
            }
        } else if (((NavigationActivity) getActivity()).getEmail().equals("admin")) {
            relativeDelete.setVisibility(View.GONE);
        }

        /*relativePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasketFragment frag = (BasketFragment) getActivity().getSupportFragmentManager().findFragmentByTag("BasketFragment");
                frag.purchaseBasket(getActivity());
            }
        });*/

        relativeStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavigationActivity) getActivity()).replaceFragment(new FindStoreFragment(), null, "FindStoreFragment");
            }
        });

        relativeBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!((NavigationActivity) getActivity()).getEmail().equals("")) {
                    ((NavigationActivity) getActivity()).replaceFragment(new BookmarkFragment(), null, "BookmarkFragment");
                } else {
                    Toast.makeText(getActivity(), "Please create or log into an account", Toast.LENGTH_SHORT).show();
                    ((NavigationActivity) getActivity()).replaceFragment(new LoginFragment(), null, "LoginFragment");
                }
            }
        });

        relativeClubcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!((NavigationActivity) getActivity()).getEmail().equals("")) {
                    ((NavigationActivity) getActivity()).replaceFragment(new ClubcardFragment(), null, "ClubcardFragment");
                } else {
                    Toast.makeText(getActivity(), "Please create or log into an account", Toast.LENGTH_SHORT).show();
                    ((NavigationActivity) getActivity()).replaceFragment(new LoginFragment(), null, "LoginFragment");
                }
            }
        });

        relativeTill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TillActivity.class);
                startActivity(intent);
            }
        });

        relativeDelete.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Are you sure you want to delete your account?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //((NavigationActivity) getActivity()).setAccount("", "", "", "");

                                SharedPreferences preferences = getActivity().getSharedPreferences("vsa", 0);
                                SharedPreferences.Editor editor = preferences.edit();

                                editor.putString("content_email", "").commit();
                                editor.putString("content_password", "").commit();
                                editor.putString("content_forename", "").commit();
                                editor.putString("content_surname", "").commit();

                                DatabaseClass receive = new DatabaseClass(null, null, null);
                                receive.execute(((NavigationActivity) getActivity()).getEmail() + "#DELETE");

                                ((NavigationActivity) getActivity()).replaceFragment(new BasketFragment(), null, "BasketFragment");

                                /*int j = 0;
                                accounts = ((NavigationActivity) getActivity()).getAccounts();
                                for (String[] account : accounts) {
                                    if (((NavigationActivity) getActivity()).getEmail() == account[0]) {
                                        ((NavigationActivity) getActivity()).removeAccounts(j);
                                        edit_toolbar.setTitle("Basket");
                                        ((NavigationActivity) getActivity()).replaceFragment(new BasketFragment(), null, "BasketFragment");
                                    }
                                    j += 1;
                                }*/
                            }
                        })
                        .setNeutralButton(android.R.string.cancel, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }));

        relativeLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Are you sure you want to log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                /*SharedPreferences preferences = getActivity().getSharedPreferences("vsa", 0);
                                SharedPreferences.Editor editor = preferences.edit();

                                editor.putString("email", "");
                                editor.putString("password", "");
                                editor.putString("forename", "");
                                editor.putString("surname", "");*/
                                ((NavigationActivity) getActivity()).setAccount("","","","");
                                ((NavigationActivity) getActivity()).replaceFragment(new BasketFragment(), null, "BasketFragment");
                            }
                        })
                        .setNeutralButton(android.R.string.cancel, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        relativeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setData(Uri.parse("mailto:"));
                email.setType("text/plain");
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"dp00405@surrey.ac.uk"});

                try {
                    startActivity(Intent.createChooser(email, "Choose your preferred email application"));
                } catch(Exception e) {
                    e.printStackTrace();
                }

            }
        });

        return view;
    }

   @Override
    public void onPause() {
        super.onPause();

        /*SharedPreferences preferences = getActivity().getSharedPreferences("vsa", 0);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("nfc", switchNFC.isChecked());
        editor.putBoolean("bt", switchBluetooth.isChecked());
        editor.putBoolean("camera", switchCamera.isChecked());*/
        ((NavigationActivity) getActivity()).setSwitch(new boolean[]{switchNFC.isChecked(), switchBluetooth.isChecked(), switchCamera.isChecked()});
    }

    /*@Override
    public void onResume() {
        super.onResume();

        SharedPreferences preferences = getActivity().getSharedPreferences("vsa", 0);

        switchNFC.setChecked(preferences.getBoolean("nfc", false));
        switchBluetooth.setChecked(preferences.getBoolean("bt", false));
        switchCamera.setChecked(preferences.getBoolean("camera", false));
    }*/


}
