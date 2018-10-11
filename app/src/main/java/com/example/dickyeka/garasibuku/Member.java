package com.example.dickyeka.garasibuku;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DICKYEKA on 27/04/2017.
 */
public class Member {
    @SerializedName("NIM")
    private String id;
    @SerializedName("Nama")
    private String name;
    @SerializedName("Password")
    private String password;
    @SerializedName("Photo")
    private  String photo;
    @SerializedName("Alamat")
    private  String alamat;
    @SerializedName("NoHP")
    private String nohp;
    @SerializedName("Bio")
    private String bio;
    @SerializedName("ProgramKeahlian")
    private String pk;
    @SerializedName("latitude")
    private double latit;
    @SerializedName("longitude")
    private double longit;

    public Member() {
    }

    public Member(String id, String name, String photo, String password, String alamat, String nohp, String bio, String pk, double latit, double longit) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.password = password;
        this.alamat = alamat;
        this.nohp = nohp;
        this.bio = bio;
        this.pk = pk;
        this.latit = latit;
        this.longit = longit;
    }

    public double getLatit() {
        return latit;
    }

    public void setLatit(double latit) {
        this.latit = latit;
    }

    public double getLongit() {
        return longit;
    }

    public void setLongit(double longit) {
        this.longit = longit;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {return photo;}

    public String getPassword() {
        return password;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getNohp() {
        return nohp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(String photo) {this.photo = photo;}

    public void setPassword(String password) {this.password = password;}

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }
}
