package com.example.apptoyselling.ui.user.fragment.home;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.apptoyselling.R;
import com.example.apptoyselling.data.api.APIService;
import com.example.apptoyselling.data.api.RetrofitClient;
import com.example.apptoyselling.databinding.FragmentHomeBinding;
import com.example.apptoyselling.model.SanPham;
import com.example.apptoyselling.ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment{
    private FragmentHomeBinding binding;
    SanPhamAdapter sanPhamAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    APIService apiService;
    ArrayList<SanPham> sanPhamList;
    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        apiService = RetrofitClient.getInstance().create(APIService.class);
        khoitao();
        if (isConnectedInternet(getContext())){
            if (Utils.listSPModel != null){
                sanPhamList = (ArrayList<SanPham>) Utils.listSPModel.getResult();
                isLoading.setValue(false);
            }else {
                getSanPham();
            }
        }
        isLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.progressBar.setVisibility(View.VISIBLE);
                }else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recylerSanPham.setVisibility(View.VISIBLE);
                }
            }
        });
        initRecylerView();
        onClickSearch();
        return binding.getRoot();
    }

    private void onClickSearch() {
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sanPhamAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void khoitao() {
        sanPhamList = new ArrayList<>();
    }

    private void getSanPham() {
        isLoading.setValue(true);
        compositeDisposable.add(apiService.getSanPham()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamModel -> {
                            isLoading.setValue(false);
                            if (sanPhamModel.isSuccess()){
                                sanPhamList = (ArrayList<SanPham>) sanPhamModel.getResult();
                                Utils.listSPModel = sanPhamModel;
                                initRecylerView();
                            }
                        },
                        throwable -> {
                           Toast.makeText(getContext(),throwable.toString(),Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void initRecylerView() {
        sanPhamAdapter = new SanPhamAdapter(getContext(),sanPhamList);
        binding.recylerSanPham.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.recylerSanPham.setAdapter(sanPhamAdapter);
    }
    //Kiem tra ket noi internet
    private boolean isConnectedInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkWifi!=null && networkWifi.isConnected() || networkMobile!=null && networkMobile.isConnected()){
            return true;
        }else {
            return false;
        }

    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}