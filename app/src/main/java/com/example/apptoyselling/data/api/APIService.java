package com.example.apptoyselling.data.api;
import io.reactivex.Observable;

import com.example.apptoyselling.model.MessageModel;
import com.example.apptoyselling.model.SanPhamModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {
    @GET("getsanpham.php")
    Observable<SanPhamModel> getSanPham();

    @POST("insertsp.php")
    @FormUrlEncoded
    Observable<MessageModel> insertSP(
            @Field("tenSP") String tenSP,
            @Field("hinhAnh") String hinhAnh,
            @Field("moTa") String moTa,
            @Field("giaTien") float giaTien,
            @Field("thuongHieu") String thuongHieu
    );

    @Multipart
    @POST("upload.php")
    Call<MessageModel> uploadFile(@Part MultipartBody.Part file);

    @POST("xoa.php")
    @FormUrlEncoded
    Observable<SanPhamModel> xoaSanPham(
            @Field("id") int id
    );

    @POST("update.php")
    @FormUrlEncoded
    Observable<MessageModel> updateSP(
            @Field("tenSP") String tenSP,
            @Field("hinhAnh") String hinhAnh,
            @Field("moTa") String moTa,
            @Field("giaTien") float giaTien,
            @Field("thuongHieu") String thuongHieu,
            @Field("id") int id
    );

}

