package com2027.cw.dp00405.vsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;

public class NavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    TextView textHeader;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    BottomNavigationView navigationView;

    Integer bToolbar = 1;

    boolean switchNFC;
    boolean switchBluetooth;
    boolean switchCamera;

    boolean addToBasket;
    boolean swipeItem;
    boolean payForItem;

    private ArrayList<Integer> images = new ArrayList<>();
    private String contents = "";
    private ArrayList<String> account = new ArrayList<String>(Arrays.asList("", "", "", "")); //{email, password, forename, surname}

    private static final String PREF_CLASS = "class_vsa";
    private static final String PREF_SWITCH_NFC = "switch_nfc";
    private static final String PREF_SWITCH_BT = "switch_bt";
    private static final String PREF_SWITCH_CAMERA = "switch_camera";
    private static final String PREF_CONTENT_EMAIL = "content_email";
    private static final String PREF_CONTENT_PASSWORD = "content_password";
    private static final String PREF_CONTENT_FORENAME = "content_forename";
    private static final String PREF_CONTENT_SURNAME = "content_surname";
    private static final String PREF_TUTORIAL_ADD = "tutorial_add";
    private static final String PREF_TUTORIAL_SWIPE = "tutorial_swipe";
    private static final String PREF_TUTORIAL_PAY = "tutorial_pay";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.phoneToolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setOnNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            onResume();
            //replaceFragment(new BasketFragment(), null, "BasketFragment");
            navigationView.setSelectedItemId(R.id.nav_basket);
            //getSupportActionBar().setTitle("Basket");
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
            //        new BasketFragment(), "BasketFragment").commit();
        }
    }

    public void replaceFragment(Fragment fragment, Fragment backstack, String name) {
        bToolbar = 0;
        if (name.equals("LoginFragment")) {
            getSupportActionBar().setTitle("Login");
        } else if (name.equals("RegisterFragment")) {
            getSupportActionBar().setTitle("Register");
        } else if (name.equals("ProfileFragment")) {
            getSupportActionBar().setTitle("Profile");
            bToolbar = 3;
            //navigationView.setSelectedItemId(R.id.nav_account);
        } else if (name.equals("ProfileEditFragment")) {
            bToolbar = 5;
            getSupportActionBar().setTitle("Edit Profile");
            //navigationView.setSelectedItemId(R.id.nav_basket);
        } else if (name.equals("BasketFragment")) {
            bToolbar = 1;
            getSupportActionBar().setTitle("Basket");
            //navigationView.setSelectedItemId(R.id.nav_basket);
        } else if (name.equals("WebsiteFragment")) {
            if (!getEmail().equals("")) {
                bToolbar = 2;
            }
            getSupportActionBar().setTitle("Website");
            //navigationView.setSelectedItemId(R.id.nav_website);
        } else if (name.equals("SettingsFragment")) {
            getSupportActionBar().setTitle("Settings");
            //navigationView.setSelectedItemId(R.id.nav_settings);
        } else if (name.equals("BookmarkFragment")) {
            getSupportActionBar().setTitle("Bookmarks");
        } else if (name.equals("ClubcardFragment")) {
            bToolbar = 7;
            getSupportActionBar().setTitle("Clubcards");
        } else if (name.equals("FindStoreFragment")) {
            getSupportActionBar().setTitle("Store Finder");
        }

        onCreateOptionsMenu(toolbar.getMenu());
        if (backstack == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, name).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, name).addToBackStack(backstack.getClass().getName()).commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //navigationView.setOnNavigationItemSelectedListener(this);
        switch (menuItem.getItemId()) {
            case R.id.nav_basket:
                replaceFragment(new BasketFragment(), null, "BasketFragment");
                //navigationView.setSelectedItemId(R.id.nav_basket);
                break;
            case R.id.nav_account:
                //if (!this.account.get(0).equals("")) {
                if (!getEmail().equals("")) {
                    replaceFragment(new ProfileFragment(), null, "ProfileFragment");
                    //navigationView.setSelectedItemId(R.id.nav_account);
                } else{
                    replaceFragment(new LoginFragment(), null, "LoginFragment");
                    //navigationView.setSelectedItemId(R.id.nav_account);
                }
                break;
            case R.id.nav_website:
                replaceFragment(new WebsiteFragment(), null, "WebsiteFragment");
                //navigationView.setSelectedItemId(R.id.nav_website);
                break;
            case R.id.nav_settings:
                replaceFragment(new SettingsFragment(), null, "SettingsFragment");
                //navigationView.setSelectedItemId(R.id.nav_settings);
                break;
        }
        return true;
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        menu.clear();
        if (bToolbar == 1) {
            getMenuInflater().inflate(R.menu.toolbar_payment, menu);
        } else if (bToolbar == 2) {
            getMenuInflater().inflate(R.menu.toolbar_favourite, menu);
        } else if (bToolbar == 3) {
            getMenuInflater().inflate(R.menu.toolbar_edit, menu);
        } else if (bToolbar == 5) {
            getMenuInflater().inflate(R.menu.toolbar_check, menu);
        } else if (bToolbar == 7) {
            getMenuInflater().inflate(R.menu.toolbar_add, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.toolbarPayment) {
            BasketFragment frag = (BasketFragment) getSupportFragmentManager().findFragmentByTag("BasketFragment");
            frag.purchaseBasket(this);
        } else if (id == R.id.toolbarFavourite) {
            WebsiteFragment frag1 = (WebsiteFragment) getSupportFragmentManager().findFragmentByTag("WebsiteFragment");
            frag1.bookmarkPage(this, getEmail());
        } else if (id == R.id.toolbarEdit) {
            replaceFragment(new ProfileEditFragment(), null, "ProfileEditFragment");
        } else if(id == R.id.toolbarCheck) {
            ProfileEditFragment frag2 = (ProfileEditFragment) getSupportFragmentManager().findFragmentByTag("ProfileEditFragment");
            frag2.editProfile();
        } else if (id == R.id.toolbarAdd) {
            ClubcardFragment frag3 = (ClubcardFragment) getSupportFragmentManager().findFragmentByTag("ClubcardFragment");
            frag3.addClubcard(this);
        }

        return super.onOptionsItemSelected(item);
    }

   @Override
    public void onPause() {
        super.onPause();

        SharedPreferences preferences = getSharedPreferences(PREF_CLASS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();

        editor.putString(PREF_CONTENT_EMAIL, this.account.get(0)).commit();
        editor.putString(PREF_CONTENT_PASSWORD, this.account.get(1)).commit();
        editor.putString(PREF_CONTENT_FORENAME, this.account.get(2)).commit();
        editor.putString(PREF_CONTENT_SURNAME, this.account.get(3)).commit();

        editor.putBoolean(PREF_SWITCH_NFC, this.switchNFC).commit();
        editor.putBoolean(PREF_SWITCH_BT, this.switchBluetooth).commit();
        editor.putBoolean(PREF_SWITCH_CAMERA, this.switchCamera).commit();

        Log.e("log_tutorial_values", String.valueOf(addToBasket) + " " + String.valueOf(swipeItem) + " " + String.valueOf(payForItem));
        editor.putBoolean(PREF_TUTORIAL_ADD, this.addToBasket).commit();
        editor.putBoolean(PREF_TUTORIAL_SWIPE, this.swipeItem).commit();
        editor.putBoolean(PREF_TUTORIAL_PAY, this.payForItem).commit();

        String image = "";
        for (Integer imageInt : images) {
            if (image.equals("")) {
                image += imageInt;
            } else {
                image += "," + imageInt;
            }
        }
        editor.putString("images", image).commit();
        Log.e("log_contentspref", contents);
        editor.putString("contents", contents).commit();

    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences(PREF_CLASS, MODE_PRIVATE);

        String email = preferences.getString(PREF_CONTENT_EMAIL, "");
        String pass = preferences.getString(PREF_CONTENT_PASSWORD, "");
        String forename = preferences.getString(PREF_CONTENT_FORENAME, "");
        String surname = preferences.getString(PREF_CONTENT_SURNAME, "");

        this.account = new ArrayList<String>(Arrays.asList(email, pass, forename, surname));

        this.switchNFC = preferences.getBoolean(PREF_SWITCH_NFC, false);
        this.switchBluetooth = preferences.getBoolean(PREF_SWITCH_BT, false);
        this.switchCamera = preferences.getBoolean(PREF_SWITCH_CAMERA, false);

        this.addToBasket = preferences.getBoolean(PREF_TUTORIAL_ADD, false);
        this.swipeItem = preferences.getBoolean(PREF_TUTORIAL_SWIPE, false);
        this.payForItem = preferences.getBoolean(PREF_TUTORIAL_PAY, false);

        Log.e("log_tutorial_resume", String.valueOf(addToBasket) + " " + String.valueOf(swipeItem) + " " + String.valueOf(payForItem));

        String images = preferences.getString("images", "");
        if (!images.equals("")) {
            for (String image : images.split(",")) {
                this.images.add(Integer.parseInt(image));
            }
        }
        contents = preferences.getString("contents", "");
        Log.e("log_contentspref11", contents);
    }

    public void setAccount(String value1, String value2, String value3, String value4) {
        this.account.set(0, value1);
        this.account.set(1, value2);
        this.account.set(2, value3);
        this.account.set(3, value4);
    }

    public ArrayList<String> getAccount() {return this.account;}

    public String getEmail() {
        return this.account.get(0);
    }

    public void setContents(String value) {this.contents = value;}

    public String getContents() {return this.contents;}

    public void setImage(ArrayList<Integer> values) {this.images = values;}

    public ArrayList<Integer> getImages() {return images;}

    public void setSwitch(boolean[] switchBool) {
        this.switchNFC = switchBool[0];
        this.switchBluetooth = switchBool[1];
        this.switchCamera = switchBool[2];
    }

    public boolean[] getSwitch() {return new boolean[]{switchNFC, switchBluetooth, switchCamera}; }

    public void setTutorial(boolean[] setTutorial) {
        this.addToBasket = setTutorial[0];
        this.swipeItem = setTutorial[1];
        this.payForItem = setTutorial[2];
    }

    public boolean[] getTutorial() {
        return new boolean[]{addToBasket, swipeItem, payForItem};
    }

}
