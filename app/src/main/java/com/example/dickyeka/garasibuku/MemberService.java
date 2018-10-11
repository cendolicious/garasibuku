package com.example.dickyeka.garasibuku;

import android.provider.CalendarContract;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by DICKYEKA on 27/04/2017.
 */
public interface MemberService {

    @Multipart
    @POST("imagefolder/index.php")
    Call<UploadObject> uploadFile(@Part MultipartBody.Part file, @Part("name") RequestBody name);

    @FormUrlEncoded
    @POST("Buku/deletebuku")
    Call<Buku> deleteBuku(
            @Query("idBuku") String idBuku
    );

    @GET("User/login")
    Call<List<Member>> login(
            @Query("nim") String id,
            @Query("password") String password
    );

    @GET("User/datamem")
    Call<List<Member>> getDataMembers(
            @Query("nim") String id
    );

    @GET("User")
    Call<List<Member>> getMembers();

    @GET("Buku")
    Call<List<Buku>> getBuku();

    @GET("Buku/databukusearch")
    Call<List<Buku>> getBukuCari(
            @Query("cari") String cari
    );

    @GET("Buku/kategori")
    Call<List<Buku>> getBukuKat(
            @Query("jenisMk") String jenis
    );

    @GET("Buku/databuku")
    Call<List<Buku>> getDataBuku(
            @Query("idBuku") String idbuku
    );

    @GET("Buku/databukunim")
    Call<List<Buku>> getDataBukuNim(
            @Query("nim") String id
    );

    @GET("Matakuliah/kategori")
    Call<List<Matakuliah>> getMatkulKat(
            @Query("jenisMk") String jenis
    );

    @FormUrlEncoded
    @POST("User/edituser")
    Call<List<Member>> updateMember(
            @Field("nim") String id,
            @Field("nama") String name,
            @Field("password") String password,
            @Field("alamat") String alamat,
            @Field("nohp") String nohp,
            @Field("bio") String bio,
            @Field("latitude") double latit,
            @Field("longitude") double longit
    );

    @FormUrlEncoded
    @POST("Buku/editbuku")
    Call<List<Buku>> updateBuku(
            @Field("idBuku") String idbuku,
            @Field("idmk") String idmk,
            @Field("nim") String nim,
            @Field("matakuliah") String matakuliah,
            @Field("jenismk") String jenismk,
            @Field("deskripsi") String deskripsi,
            @Field("harga") String harga,
            @Field("photo") String photo,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("User")
    Call<Member> insertMember(
            @Field("nim") String id,
            @Field("nama") String name,
            @Field("password") String password,
            @Field("alamat") String alamat,
            @Field("nohp") String nohp,
            @Field("bio") String bio,
            @Field("latitude") double latit,
            @Field("longitude") double longit
    );

    @FormUrlEncoded
    @POST("Buku")
    Call<Buku> insertBuku(
            @Field("idmk") String id,
            @Field("nim") String nim,
            @Field("deskripsi") String deskripsi,
            @Field("harga") String harga
    );

    @PUT("User")
    @FormUrlEncoded
    Call<Member> updateMember(
            @Field("nim") String id,
            @Field("nama") String name,
            @Field("password") String password,
            @Field("alamat") String alamat,
            @Field("nohp") String nohp,
            @Field("bio") String bio,
            @Field("photo") String photo
    );
}
