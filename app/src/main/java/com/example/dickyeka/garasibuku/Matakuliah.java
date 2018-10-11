package com.example.dickyeka.garasibuku;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by DICKYEKA on 27/04/2017.
 */
public class Matakuliah {

    @SerializedName("idMk")
    @Expose
    private String idMk;
    @SerializedName("idPk")
    @Expose
    private String idPk;
    @SerializedName("mataKuliah")
    @Expose
    private String mataKuliah;
    @SerializedName("jenisMk")
    @Expose
    private String jenisMk;

    public String getIdMk() {
        return idMk;
    }

    public void setIdMk(String idMk) {
        this.idMk = idMk;
    }

    public String getIdPk() {
        return idPk;
    }

    public void setIdPk(String idPk) {
        this.idPk = idPk;
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

}