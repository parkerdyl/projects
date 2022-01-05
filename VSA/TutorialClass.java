package com2027.cw.dp00405.vsa;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;

public class TutorialClass {

    Context context;

    boolean[] setTutorial = new boolean[]{false, false, false}; //add, swipe, pay

    public TutorialClass(Context context) {
        this.context = context;
        setTutorial = ((NavigationActivity) context).getTutorial();
    }

    public void addToBasket() {
        View root = RelativeLayout.inflate(context, R.layout.tutorial_add, null);
        new AlertDialog.Builder(context)
                .setTitle("Adding an Item to the Basket:")
                .setView(root)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setTutorial[0] = true;
                        Log.e("log_tutorial_add", String.valueOf(setTutorial[0]) + " " + String.valueOf(setTutorial[1]) + " " + String.valueOf(setTutorial[2]));
                        ((NavigationActivity) context).setTutorial(setTutorial);
                    }
                })
                .show();
    }

    public void swipeItem() {
        View root = RelativeLayout.inflate(context, R.layout.tutorial_swipe, null);
        new AlertDialog.Builder(context)
                .setTitle("Deleting an Item, or Visiting its Webpage:")
                .setView(root)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setTutorial[1] = true;
                        Log.e("log_tutorial_swipe", String.valueOf(setTutorial[0]) + " " + String.valueOf(setTutorial[1]) + " " + String.valueOf(setTutorial[2]));
                        ((NavigationActivity) context).setTutorial(setTutorial);
                    }
                })
                .show();
    }

    public void payForItem() {
        View root = RelativeLayout.inflate(context, R.layout.tutorial_pay, null);
        new AlertDialog.Builder(context)
                .setTitle("Paying for your Basket:")
                .setView(root)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setTutorial[2] = true;
                        Log.e("log_tutorial_pay", String.valueOf(setTutorial[0]) + " " + String.valueOf(setTutorial[1]) + " " + String.valueOf(setTutorial[2]));
                        ((NavigationActivity) context).setTutorial(setTutorial);
                    }
                })
                .show();
    }

}
