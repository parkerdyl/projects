package com2027.cw.dp00405.vsa;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class ClubcardFragment extends Fragment {

    View view;

    EditText editText;
    EditText editText1;
    EditText editText2;

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_clubcard, container, false);

        recyclerView = view.findViewById(R.id.recyclerClub);

        //DatabaseClass receive = new DatabaseClass(null, null, null, ClubcardFragment.this);
        //receive.execute(((NavigationActivity) getActivity()).getEmail() + "#CLUBGET");

        return view;
    }

    public void addClubcard(Context context) {
        View root = RelativeLayout.inflate(context, R.layout.add_clubcard, null);
        editText = root.findViewById(R.id.editClub);
        editText1 = root.findViewById(R.id.editClub2);
        editText2 = root.findViewById(R.id.editClub3);
        //textView.setText(webView.getUrl());
        new AlertDialog.Builder(context)
                .setTitle("Enter data values:")
                .setMessage("Please enter a Brand, Name and Serial Number. Exactly as seen on your clubcard.")
                .setView(root)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //DatabaseClass receive = new DatabaseClass(null, null, null);
                        //receive.execute(editText.getText().toString() + "," + editText1.getText().toString() + "," + editText2.getText().toString() + "#CLUBSET");
                    }
                })
                .setNeutralButton(android.R.string.cancel, null)
                .show();
    }

}
