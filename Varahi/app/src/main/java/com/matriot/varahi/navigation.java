package com.matriot.varahi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.matriot.varahi.model.UserSessionManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.UserManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class navigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    UserSessionManager user;
    TextView textView, textView1, textView2;
    ImageView image;
    Audio audio;
    Video video;
    bulletin bulletin;
    temple temple;
    panchang panchang;
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        image = findViewById(R.id.image1);
        textView = findViewById(R.id.textView4);
        textView1 = findViewById(R.id.textView5);
        textView2 = findViewById(R.id.textView6);
        builder = new AlertDialog.Builder(this);


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                Log.d("text", "onClick: Hello");
                user = new UserSessionManager(getApplicationContext());
                user.logoutUser();

                builder.setMessage("Do You want to Logout") .setTitle("Confirm");

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to Logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);

                                // Closing all the Activities
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                // Add new Flag to start new Activity
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                // Staring Login Activity
                                startActivity(i);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Confirm");
                alert.show();






                return true;

        }

        return false;

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_audio:


                audio = new Audio();
                image.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                textView2.setVisibility(View.GONE);
                textView1.setVisibility(View.GONE);
                Log.d("text", "onClick: Hello");
                audio = new Audio();

                transaction = manager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.content_navigation, audio , "audio");
                //transaction.remove(device_frag);
                transaction.commit();

                //fm.beginTransaction().hide(active).show(fragment1).commit();
                //active = fragment1;
                return true;

            case R.id.navigation_video:
                //fm.beginTransaction().hide(active).show(fragment2).commit();
                //active = fragment2;


                image.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                textView2.setVisibility(View.GONE);
                textView1.setVisibility(View.GONE);
                Log.d("text", "onClick: Hello");
                video = new Video();

                transaction = manager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.content_navigation, video , "video");
                //transaction.remove(device_frag);
                transaction.commit();
                return true;

            case R.id.navigation_bulaten:
                //fm.beginTransaction().hide(active).show(fragment3).commit();
                //active = fragment3;
                image.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                textView2.setVisibility(View.GONE);
                textView1.setVisibility(View.GONE);
                Log.d("text", "onClick: Hello");
                bulletin = new bulletin();

                transaction = manager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.content_navigation, bulletin , "bulletin");
                //transaction.remove(device_frag);
                transaction.commit();

                return true;

            case R.id.navigation_temple:
                //fm.beginTransaction().hide(active).show(fragment4).commit();
                //active = fragment4;
                image.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                textView2.setVisibility(View.GONE);
                textView1.setVisibility(View.GONE);
                Log.d("text", "onClick: Hello");
                temple = new temple();

                transaction = manager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.content_navigation, temple , "bulletin");
                //transaction.remove(device_frag);
                transaction.commit();
                return true;

            case R.id.navigation_calender:
                //fm.beginTransaction().hide(active).show(fragment4).commit();
                //active = fragment4;

                image.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                textView2.setVisibility(View.GONE);
                textView1.setVisibility(View.GONE);
                Log.d("text", "onClick: Hello");
                panchang = new panchang();

                transaction = manager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.content_navigation, panchang , "bulletin");
                //transaction.remove(device_frag);
                transaction.commit();
                return true;
        }
        return false;
    }
}
