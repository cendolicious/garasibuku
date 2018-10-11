package com.example.dickyeka.garasibuku;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Buku {

    @SerializedName("idBuku")
    @Expose
    private String idBuku;
    @SerializedName("idMk")
    @Expose
    private String idMk;
    @SerializedName("NIM")
    @Expose
    private String nIM;
    @SerializedName("mataKuliah")
    @Expose
    private String mataKuliah;
    @SerializedName("jenisMk")
    @Expose
    private String jenisMk;
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;
    @SerializedName("harga")
    @Expose
    private String harga;
    @SerializedName("Photo")
    @Expose
    private String photo;
    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdBuku() {
        return idBuku;
    }

    public void setIdBuku(String idBuku) {
        this.idBuku = idBuku;
    }

    public String getIdMk() {
        return idMk;
    }

    public void setIdMk(String idMk) {
        this.idMk = idMk;
    }

    public String getNIM() {
        return nIM;
    }

    public void setNIM(String nIM) {
        this.nIM = nIM;
    }

    public String getMataKuliah() {
        return mataKuliah;
    }

    public void setMataKuliah(String mataKuliah) {
        this.mataKuliah = mataKuliah;
    }

    public String getJenisMk() {
        return jenisMk;
    }

    public void setJenisMk(String jenisMk) {
        this.jenisMk = jenisMk;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}