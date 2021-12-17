package com.example.talktalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.talktalk.Adapter.FragmentsAdapter;
import com.example.talktalk.databinding.ActivityMainBinding;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView (binding.getRoot());
//        getSupportActionBar().hide();
//        ActionBar bar = getActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#6c7ed6")));
        mAuth = FirebaseAuth.getInstance();

        //Calling our viewPager and Adding our tableLayout
        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager ()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);

    }

    // Adding our Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //LayoutInflater takes XML files that define a layout,converts them into View objects.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu); //fst arg R.menu.menu : where we hav the menu
                                            //sec arg menu : our menu
        return super.onCreateOptionsMenu(menu);
    }



    //Up to this Point, nothing happens if we select an item from the menu
    //Let's change that !
    //Adding Functionality to each item
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.settings:
                Toast.makeText(this,"Settings is clicked",Toast.LENGTH_SHORT).show();
                break;

            case R.id.groupChat:
                Toast.makeText(this,"GroupChat is clicked",Toast.LENGTH_SHORT).show();
                break;

            case R.id.logout:
                //predefined meth
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}