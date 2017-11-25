package com.pandian.samuvel.shoppinglist.Activities;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pandian.samuvel.shoppinglist.Fragments.MealsFragment;
import com.pandian.samuvel.shoppinglist.Fragments.ShoppingListFragment;
import com.pandian.samuvel.shoppinglist.Helper;
import com.pandian.samuvel.shoppinglist.Model.ShoppingList;
import com.pandian.samuvel.shoppinglist.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    FloatingActionButton fabButton;
    EditText userInputCreateList;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = findViewById(R.id.mainActivityToolBar);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        fabButton = findViewById(R.id.fab);

        //ref = FirebaseDatabase.getInstance().getReference().child("activeList");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Shop");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        LayoutInflater inflater = LayoutInflater.from(this);
        View promptView = inflater.inflate(R.layout.create_list_prompt,null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(promptView);

        userInputCreateList = promptView.findViewById(R.id.createListEt);
        builder.setPositiveButton("Create",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String listName = userInputCreateList.getText().toString();
                        if(!listName.equals("")) {
                            //ref.setValue(listName);
                            HashMap<String,Long> timeStampCreated = new HashMap<>();
                            timeStampCreated.put("timeStamp",System.currentTimeMillis());
                            Helper.createShoppingList(listName,timeStampCreated);
                            //ShoppingList shoppingList = new ShoppingList(listName,"Anonymous",timeStampCreated);
                            //Helper.addShoppingList(shoppingList);
                            userInputCreateList.setText("");
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Enter something!",Toast.LENGTH_SHORT).show();

                    }
                });
        final AlertDialog dialog = builder.create();
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ShoppingListFragment(),"ShoppingList");
        adapter.addFragment(new MealsFragment(),"Meals");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
        public void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }
    }



}