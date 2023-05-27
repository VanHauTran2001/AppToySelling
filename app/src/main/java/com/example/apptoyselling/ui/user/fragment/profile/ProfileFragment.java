package com.example.apptoyselling.ui.user.fragment.profile;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.apptoyselling.R;
import com.example.apptoyselling.data.sqlite.SQLiteHelper;
import com.example.apptoyselling.databinding.FragmentProfileBinding;
import com.example.apptoyselling.model.User;
import com.example.apptoyselling.ui.user.activity.signin.SigninActivity;
import com.example.apptoyselling.ui.utils.Utils;
import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private SQLiteHelper sqLiteHelper;
    private ArrayList<User> userArrayList;
    private int passwordNotVisible = 1;
    Dialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false);
        sqLiteHelper = new SQLiteHelper(getContext(),"toy.db",null,1);
        userArrayList = new ArrayList<>();
        getDataUserFromDb();
        onClickLogout();
        onClickChangeInfor();
        onClickVisibilityPassword();

        return binding.getRoot();
    }
    private void onClickVisibilityPassword() {
        binding.imgShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordNotVisible == 1) {
                    binding.imgShowPass.setImageResource(R.drawable.ic_visibility_off_pass);
                    binding.txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordNotVisible = 0;
                } else {
                    binding.imgShowPass.setImageResource(R.drawable.ic_visibliti_pass);
                    binding.txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordNotVisible = 1;
                }
                binding.txtPassword.setSelection(binding.txtPassword.length());
            }
        });
    }
    private void onClickChangeInfor() {
        binding.btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog();
            }
        });
    }

    private void onClickLogout() {
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SigninActivity.class));
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
                binding.txtName.setText(userArrayList.get(i).getName());
                binding.txtemail.setText(userArrayList.get(i).getEmail());
                binding.txtPhone.setText(userArrayList.get(i).getPhone());
                binding.txtPassword.setText(Editable.Factory.getInstance().newEditable(userArrayList.get(i).getPassWord()));
            }
        }
    }
    private void OpenDialog(){
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_profile);
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
        Button dialogOK = dialog.findViewById(R.id.btnOKEdit);
        Button dialogCancel = dialog.findViewById(R.id.btnCancleEidt);
        EditText edtName = dialog.findViewById(R.id.edtNameEdit);
        EditText edtEmail = dialog.findViewById(R.id.edtEmailEdit);
        EditText edtPhone = dialog.findViewById(R.id.edtPhoneEdit);
        EditText edtPass = dialog.findViewById(R.id.edtPassEdit);
        edtName.setText(Editable.Factory.getInstance().newEditable(binding.txtName.getText().toString()));
        edtEmail.setText(Editable.Factory.getInstance().newEditable(binding.txtemail.getText().toString()));
        edtPhone.setText(Editable.Factory.getInstance().newEditable(binding.txtPhone.getText().toString()));
        edtPass.setText(Editable.Factory.getInstance().newEditable(binding.txtPassword.getText().toString()));
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String phone= edtPhone.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                sqLiteHelper.QueryData("UPDATE USERS SET Name = '"+name+"' , Phone = '"+phone+"', Email = '"+email+"', Password = '"+pass+"' WHERE Id = '"+Utils.idUser+"' ");
                Toast.makeText(getContext(),"Cập nhật thành công !!!",Toast.LENGTH_LONG).show();
                getDataUserFromDb();

                dialog.dismiss();
            }
        });
        dialog.show();
    }
}