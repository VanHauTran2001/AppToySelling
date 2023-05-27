package com.example.apptoyselling.ui.user.activity.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.apptoyselling.R;
import com.example.apptoyselling.data.sqlite.SQLiteHelper;
import com.example.apptoyselling.databinding.ActivityPaymentBinding;
import com.example.apptoyselling.model.User;
import com.example.apptoyselling.ui.user.activity.cart.CartActivity;
import com.example.apptoyselling.ui.user.activity.home.HomeActivity;
import com.example.apptoyselling.ui.utils.Utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PaymentActivity extends AppCompatActivity {
    private ActivityPaymentBinding binding;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    private SQLiteHelper sqLiteHelper;
    private ArrayList<User> userArrayList;
    Dialog dialog;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_payment);
        sqLiteHelper = new SQLiteHelper(this,"toy.db",null,1);
        userArrayList = new ArrayList<>();
        Intent intent = getIntent();
        float pricePay = intent.getFloatExtra("pay",0f);
        binding.txtPricePayment.setText(decimalFormat.format(pricePay)+"đ");
        onClickBack();
        getDataUserFromDb();
        onCLickRadioButton();
        onClickPayment();
    }

    private void onClickPayment() {
        binding.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edtDiaChi = binding.edtAddressPayment.getText().toString().trim();
                if (edtDiaChi.isEmpty()){
                    Toast.makeText(PaymentActivity.this,"Địa chỉ nhận hàng không được để trống !",Toast.LENGTH_SHORT).show();
                }else {
                    OpenDialog();
                }
            }
        });
    }

    private void onCLickRadioButton() {
        binding.radioCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layoutCard.setVisibility(View.VISIBLE);
            }
        });
        binding.radioMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layoutCard.setVisibility(View.GONE);
            }
        });
    }

    private void getDataUserFromDb() {
        if (userArrayList != null){
            userArrayList.clear();
        }
        Cursor data = sqLiteHelper.GetData("SELECT * FROM USERS"); //Lấy ra danh sách tài khoản
        while (data.moveToNext()) {
            int id = data.getInt(0);
            String name = data.getString(1);
            String phone = data.getString(2);
            String email = data.getString(3);
            String password = data.getString(4);
            userArrayList.add(new User(id, name, phone, email, password)); //thêm user vào list
        }
        for (int i=0;i<userArrayList.size();i++){
            if (userArrayList.get(i).getId() == Utils.idUser){
                binding.txtNamePayment.setText(userArrayList.get(i).getName());
                binding.txtPhonePayment.setText(userArrayList.get(i).getPhone());
            }
        }
    }

    private void onClickBack() {
        binding.imgBackPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentActivity.this, CartActivity.class));
            }
        });
    }
    private String date(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }
    private void OpenDialog(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_payment_success);
        Window window = dialog.getWindow();
        if (window==null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtributes = window.getAttributes();
        windowAtributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtributes);
        dialog.setCancelable(true);
        Button dialogOK = dialog.findViewById(R.id.btnOkPayment);
        dialogOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentActivity.this, HomeActivity.class));
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}