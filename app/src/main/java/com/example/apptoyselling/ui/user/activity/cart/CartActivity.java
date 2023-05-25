package com.example.apptoyselling.ui.user.activity.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptoyselling.R;
import com.example.apptoyselling.data.sqlite.SQLiteHelper;
import com.example.apptoyselling.databinding.ActivityCartBinding;
import com.example.apptoyselling.model.Cart;
import com.example.apptoyselling.ui.user.activity.home.HomeActivity;
import com.example.apptoyselling.ui.utils.Utils;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity implements CartAdapter.ICart{
    private ActivityCartBinding binding;
    private SQLiteHelper sqLiteHelper;
    CartAdapter cartAdapter;
    float feeShip = 0f;
    float itemTotal = 0f;
    float total = 0f;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    Dialog dialogDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_cart);
        sqLiteHelper = new SQLiteHelper(this,"toy.db",null,1);
        getDataFromDatabase();
        initRecylerView();
        onClickBack();
        checkData();
        customDataTotal();
    }

    private void onClickBack() {
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, HomeActivity.class));
            }
        });
    }
    private void checkData() {
        if (Utils.cartArrayList.size() == 0){
            binding.txtCartEmpty.setVisibility(View.VISIBLE);
            binding.layout1.setVisibility(View.GONE);
        }else{
            feeShip = 20000f;
            binding.layout1.setVisibility(View.VISIBLE);
            binding.txtCartEmpty.setVisibility(View.GONE);
        }
    }
    @SuppressLint("SetTextI18n")
    private void customDataTotal() {
        itemTotal = 0f;
        for (int i =0; i< Utils.cartArrayList.size()-1; i++){
            itemTotal += Utils.cartArrayList.get(i).getSumPrice();
        }
        total = itemTotal + feeShip;
        binding.txtItemTotal.setText(decimalFormat.format(itemTotal)+"đ");
        binding.txtTotal.setText(decimalFormat.format(total)+"đ");
        binding.txtDelivery.setText(decimalFormat.format(feeShip)+"đ");
    }
    private void initRecylerView() {
        cartAdapter = new CartAdapter(this);
        binding.recylerCart.setLayoutManager(new LinearLayoutManager(this));
        binding.recylerCart.setAdapter(cartAdapter);
    }

    private void getDataFromDatabase() {
        initRecylerView();
        if (Utils.cartArrayList != null){
            Utils.cartArrayList.clear();
        }
        Cursor data = sqLiteHelper.GetData("SELECT * FROM CART");
        while (data.moveToNext()){
            int idCart = data.getInt(1);
            String imageCart = data.getString(2);
            String nameCart = data.getString(3);
            float priceCart = data.getFloat(4);
            String descriptionCart = data.getString(5);
            String originCart = data.getString(6);
            int numberOrder = data.getInt(7);
            float sumPrice = data.getFloat(8);
            Utils.cartArrayList.add(new Cart(idCart,nameCart,imageCart,priceCart,descriptionCart,originCart,numberOrder,sumPrice));
        }
    }

    @Override
    public int getCount() {
        return Utils.cartArrayList.size();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public Cart getData(int position) {
        return Utils.cartArrayList.get(position);
    }

    @Override
    public void onClickDelete(int id) {
        OpenDialog(id);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCLickMinus(int position, int idCart, int numberOder, TextView txtNumberOrderCart, TextView txtSumPriceCart, ImageView btnMinus) {
        if (numberOder < 2){
            btnMinus.setEnabled(false);
        }else{
            int newNumber = numberOder - 1;
            txtNumberOrderCart.setText(String.valueOf(newNumber));
            int numberNow = Utils.cartArrayList.get(position).getNumberOrder();
            float priceNow = Utils.cartArrayList.get(position).getSumPrice();
            Utils.cartArrayList.get(position).setNumberOrder(newNumber);
            float newPrice = (newNumber* priceNow)/ numberNow;
            Utils.cartArrayList.get(position).setSumPrice(newPrice);
            txtSumPriceCart.setText(decimalFormat.format(newPrice)+"đ");
            updateCart(idCart,newNumber,newPrice);
            customDataTotal();

        }
    }

    @Override
    public void onClickPlus(int position, int idCart, int numberOder, TextView txtNumberOrderCart, TextView txtSumPriceCart) {
        int newNumber = numberOder +1;
        txtNumberOrderCart.setText(String.valueOf(newNumber));
        int numberNow = Utils.cartArrayList.get(position).getNumberOrder();
        float priceNow = Utils.cartArrayList.get(position).getSumPrice();
        Utils.cartArrayList.get(position).setNumberOrder(newNumber);
        float newPrice = (newNumber* priceNow)/ numberNow;
        Utils.cartArrayList.get(position).setSumPrice(newPrice);
        txtSumPriceCart.setText(decimalFormat.format(newPrice)+"đ");
        updateCart(idCart,newNumber,newPrice);
        customDataTotal();
    }
    private void updateCart(int id , int newNumber , float newPrice){
        sqLiteHelper.QueryData("UPDATE CART SET NumberOrder = '"+newNumber+"' , SumPrice = '"+newPrice+"' WHERE IdSP = '"+id+"' ");
        getDataFromDatabase();
    }
    private void OpenDialog(int id){
        dialogDelete = new Dialog(this);
        dialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDelete.setContentView(R.layout.dialog_delete);
        Window window = dialogDelete.getWindow();
        if (window==null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtributes = window.getAttributes();
        windowAtributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtributes);
        dialogDelete.setCancelable(true);
        Button dialogOK = dialogDelete.findViewById(R.id.btnOKDelete);
        Button dialogCancel = dialogDelete.findViewById(R.id.btnCancleDelete);
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDelete.dismiss();
            }
        });
        dialogOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteHelper.QueryData("DELETE FROM CART WHERE IdSP = '"+id+"' ");
                getDataFromDatabase();
                customDataTotal();
                checkData();
                Toast.makeText(CartActivity.this,"Xóa thành công !!!",Toast.LENGTH_LONG).show();
                dialogDelete.dismiss();
            }
        });
        dialogDelete.show();
    }
}