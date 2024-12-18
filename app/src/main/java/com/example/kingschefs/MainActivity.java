package com.example.kingschefs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.kingschefs.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity{

    ActivityMainBinding binding;

    HomeFragment homeFragment = new HomeFragment();
    OrdersFragment ordersFragment = new OrdersFragment();
    HistoryFragment historyFragment = new HistoryFragment();
    Fragment activeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayout, historyFragment, "HISTORY")
                .hide(historyFragment)
                .add(R.id.frameLayout, ordersFragment, "ORDERS")
                .hide(ordersFragment)
                .add(R.id.frameLayout, homeFragment, "HOME")
                .commit();
        activeFragment = homeFragment;
        setContentView(binding.getRoot()) ;
            ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, v.getPaddingBottom());
                return insets;
            });


        binding.bottomNavigationView.setOnItemSelectedListener(item->{
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                switchFragment(homeFragment);
            } else if (itemId == R.id.orders) {
                switchFragment(ordersFragment);
            } else if (itemId == R.id.history) {
                switchFragment(historyFragment);
            }
            return true;
        });
        Button button = findViewById(R.id.LogOutButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Login.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                finish();
            }
        });

    }

    private void switchFragment(Fragment fragment) {
        if(activeFragment == fragment) return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(activeFragment).show(fragment).commit();
        activeFragment = fragment;
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager =  getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

}