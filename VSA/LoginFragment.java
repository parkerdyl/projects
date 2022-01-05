package com2027.cw.dp00405.vsa;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    EditText loginEmail, loginPassword;
    TextView forgotten, none;
    Button buttonConfirm;

    //ArrayList<String[]> accounts = new ArrayList<>();
    String email, password;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        loginEmail = view.findViewById(R.id.loginEmail);
        loginPassword = view.findViewById(R.id.loginPassword);

        buttonConfirm = view.findViewById(R.id.loginConfirm);
        buttonConfirm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        email = loginEmail.getText().toString();
                        password = loginPassword.getText().toString();
                        if (email != null && password != null) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                            DatabaseClass receive = new DatabaseClass(null, LoginFragment.this, null);
                            receive.execute(email + "," + password + "#LOGIN");

                            /*accounts = ((NavigationActivity) getActivity()).getAccounts();
                            if (!accounts.isEmpty()) {
                                for (String[] account : accounts) {
                                    if (account[0].equals(email) && account[3].equals(password)) {
                                        //NavigationActivity nav = new NavigationActivity();
                                        ((NavigationActivity) getActivity()).setEmail(account[0]);
                                        ((NavigationActivity) getActivity()).setAccount(true);
                                        ((NavigationActivity) getActivity()).getSupportActionBar().setTitle("Basket");
                                        ((NavigationActivity) getActivity()).replaceFragment(new BasketFragment(), null, "BasketFragment");
                                        break;
                                    }
                                }
                            }*/
                        }
                        else {
                            Toast.makeText(getActivity(), "Missing username or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        none = view.findViewById(R.id.noAccount);
        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavigationActivity) getActivity()).replaceFragment(new RegisterFragment(), null, "RegisterFragment");
            }
        });

        return view;
    }

    public void setAccount(boolean value, String email, String password, String name, String surname) {
        if (value == true) {
            /*SharedPreferences preferences = getActivity().getSharedPreferences("vsa", 0);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("email", email).commit();
            editor.putString("password", password).commit();
            editor.putString("forename", name).commit();
            editor.putString("surname", surname).commit();*/

            ((NavigationActivity) getActivity()).setAccount(email, password, name, surname);
            ((NavigationActivity) getActivity()).getSupportActionBar().setTitle("Basket");
            ((NavigationActivity) getActivity()).replaceFragment(new BasketFragment(), null, "BasketFragment");
        } else {
            Toast.makeText(getActivity(), "Cannot login", Toast.LENGTH_SHORT).show();
        }
    }


}
