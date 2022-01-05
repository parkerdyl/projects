package com2027.cw.dp00405.vsa;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    TextView textEmail, textForename, textSurname, textPassword;

    String email;
    ArrayList<String> account = new ArrayList<>();
    //String pass;
    //String forename;
    //String surname;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        textEmail = view.findViewById(R.id.textEmail);
        textForename = view.findViewById(R.id.textForename);
        textSurname = view.findViewById(R.id.textSurname);
        textPassword = view.findViewById(R.id.textPassword);

        email = ((NavigationActivity) getActivity()).getEmail();
        account = ((NavigationActivity) getActivity()).getAccount();

        textEmail.setText(email);
        textForename.setText(account.get(2));
        textSurname.setText(account.get(3));
        textPassword.setText(account.get(1));

        return view;
    }

    /*@Override
    public void onPause() {
        super.onPause();

        SharedPreferences preferences = getActivity().getSharedPreferences("vsa", 0);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("email", email).commit();
        editor.putString("password", pass).commit();
        editor.putString("forename", forename).commit();
        editor.putString("surname", surname).commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences preferences = getActivity().getSharedPreferences("vsa", 0);

        email = preferences.getString("email", "");
        pass = preferences.getString("password", "");
        forename = preferences.getString("forename", "");
        surname = preferences.getString("surname", "");
    }
*/
}
