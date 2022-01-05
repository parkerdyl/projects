package com2027.cw.dp00405.vsa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RegisterFragment extends Fragment {

    EditText registerEmail;
    EditText registerForename;
    EditText registerSurname;
    EditText registerPassword;
    Button registerConfirm;

    String email = "";
    String forename = "";
    String surname = "";
    String password = "";

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);

        registerEmail = view.findViewById(R.id.registerEmail);
        registerForename = view.findViewById(R.id.registerForename);
        registerSurname = view.findViewById(R.id.registerSurname);
        registerPassword = view.findViewById(R.id.registerPassword);

        registerConfirm = view.findViewById(R.id.registerConfirm);
        registerConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (registerEmail.getText() != null && registerForename.getText() != null && registerSurname.getText() != null && registerPassword.getText() != null) { //doesnt check for duplicate emails
                    email = registerEmail.getText().toString();
                    forename = registerForename.getText().toString();
                    surname = registerSurname.getText().toString();
                    password = registerPassword.getText().toString();
                    //String[] account = {email, forename, surname, password};

                    DatabaseClass receive = new DatabaseClass(null, null, null);
                    receive.execute(email + "," + password + "," + forename + "," + surname + "#REGISTER");

                    //((NavigationActivity) getActivity()).addAccounts(account);
                    ((NavigationActivity) getActivity()).replaceFragment(new LoginFragment(), null, "LoginFragment");
                }
            }
        });

        return view;
    }
}
