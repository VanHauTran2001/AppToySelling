package com.example.apptoyselling.data.api;
import io.reactivex.Observable;
import com.example.apptoyselling.model.SanPhamModel;
import retrofit2.http.GET;

public interface APIService {
    @GET("getsanpham.php")
    Observable<SanPhamModel> getSanPham();
}
