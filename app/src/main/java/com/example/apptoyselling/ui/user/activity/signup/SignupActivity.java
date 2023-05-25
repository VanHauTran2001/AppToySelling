package com.example.apptoyselling.ui.user.activity.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.apptoyselling.R;
import com.example.apptoyselling.databinding.ActivitySignupBinding;
import com.example.apptoyselling.model.User;
import com.example.apptoyselling.data.sqlite.SQLiteHelper;
import com.example.apptoyselling.ui.user.activity.signin.SigninActivity;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private SQLiteHelper sqLiteHelper;
    private ArrayList<User> userArrayList;
    private boolean check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup);
        userArrayList = new ArrayList<>();
        binding.btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, SigninActivity.class));
            }
        });
        binding.btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.edtName.getText().toString().trim();
                String email = binding.edtEmailSignup.getText().toString().trim();
                String password = binding.edtPasswordSignup.getText().toString().trim();
                String phone = binding.edtPhone.getText().toString().trim();
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()){
                    Toast.makeText(SignupActivity.this,"Dữ liệu không được để trống !",Toast.LENGTH_SHORT).show();
                }else {
                    sqLiteHelper = new SQLiteHelper(getApplicationContext(),"toy.db",null,1);
                    userArrayList.clear();
                    Cursor data = sqLiteHelper.GetData("SELECT * FROM USERS"); //lấy ra danh sách tài khoản
                    while (data.moveToNext()){
                        int id = data.getInt(0);
                        String nameLogin = data.getString(1);
                        String phoneLogin = data.getString(2);
                        String emailLogin = data.getString(3);
                        String passwordLogin = data.getString(4);
                        userArrayList.add(new User(id,nameLogin,phoneLogin,emailLogin,passwordLogin));
                        for (int i=0;i<userArrayList.size();i++){
                            if (userArrayList.get(i).getEmail().equals(email)){ //kiểm tra điều kiện nếu tài khoản trùng
                                check = true;
                            }else {
                                check = false;
                            }
                        }
                    }
                    if (!check){
                        sqLiteHelper.QueryData("INSERT INTO USERS VALUES(null,'" + name + "','" + phone + "','" + email + "','"+password+"')"); //thêm tài khoản vào database
                        Toast.makeText(SignupActivity.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, SigninActivity.class));
                    }else {
                        Toast.makeText(SignupActivity.this,"Tài khoản đã tồn tại !",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
}