package com.example.apptoyselling.ui.user.fragment.donhang;

import android.database.Cursor;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apptoyselling.R;
import com.example.apptoyselling.data.sqlite.SQLiteHelper;
import com.example.apptoyselling.databinding.FragmentDonHangBinding;
import com.example.apptoyselling.model.DonHang;
import com.example.apptoyselling.model.User;
import com.example.apptoyselling.ui.utils.Utils;

import java.util.ArrayList;

public class DonHangFragment extends Fragment implements DonHangAdapter.IDonHang{
    private FragmentDonHangBinding binding;
    private SQLiteHelper sqLiteHelper;
    private ArrayList<DonHang> donHangArrayList;
    private DonHangAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_don_hang,container,false);
        sqLiteHelper = new SQLiteHelper(getContext(),"toy.db",null,1);
        donHangArrayList = new ArrayList<>();
        getDataUserFromDb();
        initRecylerView();
        return binding.getRoot();
    }

    private void initRecylerView() {
        adapter = new DonHangAdapter(donHangArrayList,this);
        binding.recylerDonHang.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recylerDonHang.setAdapter(adapter);
    }

    private void getDataUserFromDb() {
        if (donHangArrayList != null){
            donHangArrayList.clear();
        }
        Cursor data = sqLiteHelper.GetData("SELECT * FROM BILLS WHERE IdUser = '" + Utils.idUser+"' ORDER BY Id DESC");
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

    }
}