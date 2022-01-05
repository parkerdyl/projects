package com2027.cw.dp00405.vsa;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ConfirmFragment extends Fragment {

    Button buttonCard;
    Button buttonCash;
    Button buttonGift;


    RecyclerView recyclerTill;

    AdapterBasket adapterBasket;

    String contents;
    ArrayList<Integer> images = new ArrayList<>();
    ArrayList<Boolean> isTrue;
    ArrayList<String> brands = new ArrayList<>();
    ArrayList<String> models = new ArrayList<>();
    ArrayList<String> prices = new ArrayList<>();
    ArrayList<String> descriptions = new ArrayList<>();

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_confirm, container, false);

        recyclerTill = view.findViewById(R.id.recyclerTill);
        initRecyclerView();

        buttonCard = view.findViewById(R.id.tillCard);
        buttonCard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                buttonPress();
            }
        });

        buttonCash = view.findViewById(R.id.tillCash);
        buttonCash.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                buttonPress();
            }
        });

        buttonGift = view.findViewById(R.id.tillGift);
        buttonGift.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                buttonPress();
            }
        });

        return view;
    }

    static ConfirmFragment newInstance(String values) {
        ConfirmFragment confirmFragment = new ConfirmFragment();

        Bundle args = new Bundle();
        args.putString("contents", values);
        confirmFragment.setArguments(args);

        return confirmFragment;
    }

    private void initRecyclerView() {
        contents = getArguments().getString("contents", "");
        if (!contents.equals("")) {
            for (String content : contents.split("#")) {
                Log.e("log_confirm", content.split(",")[1] + " ### " + content.split(",")[2] + " ### " + content.split(",")[3] + " ### " + content.split(",")[6]);
                brands.add(content.split(",")[1]);
                models.add(content.split(",")[2]);
                prices.add("£" + content.split(",")[3]);
                descriptions.add(content.split(",")[6]);
            }
        }

        adapterBasket = new AdapterBasket(getActivity(), null, brands, models, prices);
        recyclerTill.setAdapter(adapterBasket);
        recyclerTill.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void buttonPress() {
        Toast.makeText(getActivity(), "Payment Accepted", Toast.LENGTH_SHORT).show();
        DatabaseClass database = new DatabaseClass(ConfirmFragment.this, null, null);
        database.execute(contents + "#RECEIVE");
    }

    public void setIsTrue(ArrayList<Boolean> arrayList) {
        this.isTrue = arrayList;
        for (Boolean bool : isTrue) {
            if (bool) {
                images.add(R.drawable.ic_lock_open_green_32dp);
            } else {
                images.add(R.drawable.ic_lock_outline_red_32dp);
            }
        }
        adapterBasket = new AdapterBasket(getActivity(), images, brands, models, prices);
        recyclerTill.setAdapter(adapterBasket);
        recyclerTill.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

}
