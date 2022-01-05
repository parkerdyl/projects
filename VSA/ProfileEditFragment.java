package com2027.cw.dp00405.vsa;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ProfileEditFragment extends Fragment {

    TextView textEmail;
    EditText editForename;
    EditText editSurname;
    EditText editPassword;

    String email;
    ArrayList<String> account = new ArrayList<>();

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        textEmail = view.findViewById(R.id.textEmail2);
        editForename = view.findViewById(R.id.editForename);
        editSurname = view.findViewById(R.id.editSurname);
        editPassword = view.findViewById(R.id.editPassword);

        email = ((NavigationActivity) getActivity()).getEmail();
        account = ((NavigationActivity) getActivity()).getAccount();

        textEmail.setText(email);
        editForename.setText(account.get(2));
        editSurname.setText(account.get(3));
        editPassword.setText(account.get(1));

        return view;
    }

    public void editProfile() {
        DatabaseClass receive = new DatabaseClass(null, null, null);
        receive.execute(email + "," + editPassword.getText().toString() + "," + editForename.getText().toString() + "," + editSurname.getText().toString() + "#UPDATE");

        ((NavigationActivity) getActivity()).setAccount(email, editPassword.getText().toString(), editForename.getText().toString(), editSurname.getText().toString());
        ((NavigationActivity) getActivity()).replaceFragment(new ProfileFragment(), null, "ProfileFragment");
    }

}
