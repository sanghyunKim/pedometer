package com.steven.pedometer.ui.pedometer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.steven.pedometer.R;

public class PedometerActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pedometer);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new PedometerFragment();
            transaction.add(R.id.fragment_container, fragment)
                    .commit();
        }

        PedometerContract.Presenter presenter = new PedometerPresenter((PedometerContract.View)fragment);
        ((PedometerContract.View)fragment).setPresenter(presenter);
    }
}
