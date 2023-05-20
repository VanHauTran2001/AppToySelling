package com.example.apptoyselling.ui.user.activity.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.apptoyselling.R;
import com.example.apptoyselling.databinding.ActivityCartBinding;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_cart);
    }
}