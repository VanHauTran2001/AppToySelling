package com.example.apptoyselling.ui.admin.qldonhang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import com.example.apptoyselling.R;
import com.example.apptoyselling.data.sqlite.SQLiteHelper;
import com.example.apptoyselling.databinding.ActivityQuanLyDhactivityBinding;
import com.example.apptoyselling.model.DonHang;
import com.example.apptoyselling.ui.user.fragment.donhang.DonHangAdapter;
import com.example.apptoyselling.ui.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class QuanLyDHActivity extends AppCompatActivity implements DonHangAdapter.IDonHang{
    private ActivityQuanLyDhactivityBinding binding;
    private SQLiteHelper sqLiteHelper;
    private ArrayList<DonHang> donHangArrayList;
    private DonHangAdapter adapter;
    float tongTien;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_quan_ly_dhactivity);
        sqLiteHelper = new SQLiteHelper(this,"toy.db",null,1);
        donHangArrayList = new ArrayList<>();
        getDataUserFromDb();
        initRecylerView();
        for (int i=0;i<donHangArrayList.size();i++){
            tongTien += donHangArrayList.get(i).getPriceDH();
        }
        binding.txtTongDoanhThu.setText(decimalFormat.format(tongTien)+"đ");
    }



    private void initRecylerView() {
        adapter = new DonHangAdapter(donHangArrayList,this);
        binding.recylerDonHang.setLayoutManager(new LinearLayoutManager(this));
        binding.recylerDonHang.setAdapter(adapter);
    }

    @SuppressLint("SetTextI18n")
    private void getDataUserFromDb() {
        if (donHangArrayList != null){
            donHangArrayList.clear();
        }
        Cursor data = sqLiteHelper.GetData("SELECT * FROM BILLS ORDER BY Id DESC");
        while (data.moveToNext()) {
            String idDH = data.getString(2);
            String nameDH = data.getString(3);
            String phoneDH = data.getString(4);
            String diaChiDH = data.getString(5);
            float priceDH = data.getFloat(6);
            String statusDH = data.getString(7);
            String date = data.getString(8);
            donHangArrayList.add(new DonHang(idDH,nameDH,phoneDH,diaChiDH,priceDH,statusDH,date));
        }
    }

    @Override
    public void onClickXacNhanDH(String idDH) {
        String status = "Đã xác nhận";
        sqLiteHelper.QueryData("UPDATE BILLS SET StatusDH = '"+ status +"' ");
        getDataUserFromDb();
        initRecylerView();
    }
}