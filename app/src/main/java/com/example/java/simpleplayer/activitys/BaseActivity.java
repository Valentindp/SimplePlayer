package com.example.java.simpleplayer.activitys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.java.simpleplayer.R;
import com.example.java.simpleplayer.interfaces.CopyPasteHelper;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    protected void addFragment(Fragment fragment) {
//        FragmentManager manager = getSupportFragmentManager();
//        manager
//                .beginTransaction()
//                .add(R.id.content_navigate, fragment)
//                .addToBackStack(null)
//                .commit();
//
//    }


//    protected void replaceFragment(Fragment fragment) {
//        FragmentManager manager = getSupportFragmentManager();
//        manager
//                .beginTransaction()
//                .replace(R.id.content_navigate, fragment)
//                .commit();
//    }

    protected void addFragment(Fragment fragment) {
        fragmentTransaction(fragmentTransaction -> {
            fragmentTransaction.add(R.id.content_navigate, fragment);
            fragmentTransaction.addToBackStack(null);
        });
    }

    protected void replaceFragment(Fragment fragment) {
        fragmentTransaction(fragmentTransaction -> fragmentTransaction.replace(R.id.content_navigate, fragment));
    }

    protected void fragmentTransaction(CopyPasteHelper<FragmentTransaction> helper) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        helper.block(transaction);
        transaction.commit();
    }
}
