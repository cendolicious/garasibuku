package com.example.dickyeka.garasibuku;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailBuku extends AppCompatActivity {
    Context context;
    Toolbar toolbar;
    TextView judulbukulayar,hargabuku,deskripsibuku;
    SharedPreferences sp;
    ImageView editbuku,pictbuku;
    Button btnnamaowner,btnnohp,btnalamat,btnterjual,btnref;
    RelativeLayout container;
    String idbuku,namaowner,judulbuku;
    double latit,longit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_buku);

        container = (RelativeLayout) findViewById(R.id.containerdetailbuku);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(DetailBuku.this);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        editbuku = (ImageView) findViewById(R.id.edit);
        editbuku.setVisibility(View.VISIBLE);
        judulbukulayar = (TextView) findViewById(R.id.judulbuku);
        hargabuku = (TextView) findViewById(R.id.hargabuku);
        deskripsibuku = (TextView) findViewById(R.id.deskripsi);
        btnnamaowner = (Button) findViewById(R.id.namaowner);
        btnnohp = (Button) findViewById(R.id.notelp);
        btnalamat = (Button) findViewById(R.id.alamat);
        pictbuku = (ImageView) findViewById(R.id.pict);
        btnterjual = (Button) findViewById(R.id.terjual);
        btnref = (Button) findViewById(R.id.btnrefreshdetailbuku);
        btnref.setVisibility(View.GONE);

        sp = getSharedPreferences("sp",MODE_PRIVATE);
        final String idsession = sp.getString("id","");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detailbuku);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idbuku = getIntent().getStringExtra("idbuku");

        btnalamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailBuku.this,MapsActivity.class);
                intent.putExtra("latit",latit);
                intent.putExtra("longit",longit);
                intent.putExtra("namabuku",judulbuku);
                intent.putExtra("nama",namaowner);
                startActivity(intent);
            }
        });

        String url = "http://cendolzboy.000webhostapp.com/api/";
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(gson)).build();
        final MemberService service = retrofit.create(MemberService.class);

        Call<List<Buku>> call = service.getDataBuku(idbuku);
        call.enqueue(new Callback<List<Buku>>() {
            @Override
            public void onResponse(Call<List<Buku>> call, Response<List<Buku>> response) {
                if (response.isSuccessful()) {
                    final List<Buku> data = response.body();
                    String idbukuu = new String();
                    String idMk = new String();
                    String nim = new String();
                    String jenismk = new String();
                    String deskripsi = new String();
                    String harga = new String();
                    String photo = new String();
                    String status = new String();
                    for (Buku b : data) {
                        idbukuu = b.getIdBuku();
                        idMk = b.getIdMk();
                        judulbuku = b.getMataKuliah();
                        nim = b.getNIM();
                        jenismk = b.getJenisMk();
                        deskripsi = b.getDeskripsi();
                        harga = b.getHarga();
                        photo = b.getPhoto();
                        status = b.getStatus();
                    }
                    final String idbukuasli = idbukuu;
                    final String idMkasli = idMk;
                    final String judulbukuasli = judulbuku;
                    final String nimasli = nim;
                    final String jenismkasli = jenismk;
                    final String deskripsiasli = deskripsi;
                    final String hargaasli = harga;
                    final String photoasli = photo;
                    final String statusasli = status;

                    Call<List<Member>> call2 = service.getDataMembers(nim);
                    call2.enqueue(new Callback<List<Member>>() {
                        @Override
                        public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                            if(response.isSuccessful()) {
                                final List<Member> data = response.body();
                                String nimowner = new String();
                                String nohp = new String();
                                String alamat = new String();
                                for (Member m : data) {
                                    nimowner = m.getId();
                                    namaowner = m.getName();
                                    nohp = m.getNohp();
                                    alamat = m.getAlamat();
                                    latit = m.getLatit();
                                    longit = m.getLongit();
                                }
                                btnnamaowner.setText("Dijual oleh " + namaowner);
                                btnnohp.setText(nohp);
                                final String nohpasli = nohp;
                                btnnohp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = null;
                                        intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + nohpasli));
                                        startActivity(intent);
                                    }
                                });
                                btnalamat.setText(alamat);
                                final String statusbaru = "Terjual";
                                if(nimowner.equals(idsession)){
                                    btnterjual.setVisibility(View.VISIBLE);
                                    btnterjual.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            progressDialog.show();
                                            Call<List<Buku>> call1 = service.updateBuku(
                                                    idbukuasli,idMkasli,nimasli,judulbukuasli,jenismkasli,deskripsiasli,
                                                    hargaasli,photoasli,statusbaru
                                            );
                                            call1.enqueue(new Callback<List<Buku>>() {
                                                @Override
                                                public void onResponse(Call<List<Buku>> call, Response<List<Buku>> response) {
                                                    if(response.isSuccessful()) {
                                                        Toast.makeText(DetailBuku.this, "Berhasil terjual", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(DetailBuku.this, NavigationDrawer.class);
                                                        progressDialog.dismiss();
                                                        startActivity(intent);
                                                    }else{
                                                        progressDialog.dismiss();
                                                        Toast.makeText(DetailBuku.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<List<Buku>> call, Throwable throwable) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(DetailBuku.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                                }else{
                                    progressDialog.dismiss();
                                    btnterjual.setVisibility(View.GONE);
                                }

                                progressDialog.dismiss();
                            }else{
                                progressDialog.dismiss();
                                btnterjual.setVisibility(View.GONE);
                                container.setVisibility(View.GONE);
                                btnref.setVisibility(View.VISIBLE);
                                Toast.makeText(DetailBuku.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                                btnref.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Member>> call, Throwable throwable) {
                            progressDialog.dismiss();
                            container.setVisibility(View.GONE);
                            btnref.setVisibility(View.VISIBLE);
                            Toast.makeText(DetailBuku.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                            btnref.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            });
                        }
                    });

                    btnnamaowner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(DetailBuku.this, Profil.class);
                            intent.putExtra("id", nimasli);
                            startActivity(intent);
                        }
                    });

                    if (photo.toString().isEmpty())
                        Picasso.with(context).load(R.drawable.defaultpict).noFade().into(pictbuku);
                    else
                        Picasso.with(context).load(photo).noFade().into(pictbuku);

                    getSupportActionBar().setTitle(judulbuku);
                    judulbukulayar.setText(idMk + " - " + judulbuku);
                    hargabuku.setText("Rp. " + harga);
                    deskripsibuku.setText("\"" + deskripsi + "\"");
                    if (!nim.equals(idsession))
                        editbuku.setVisibility(View.GONE);
                    Toast.makeText(DetailBuku.this, judulbuku, Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    container.setVisibility(View.GONE);
                    btnref.setVisibility(View.VISIBLE);
                    Toast.makeText(DetailBuku.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                    btnref.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Buku>> call, Throwable throwable) {
                progressDialog.dismiss();
                container.setVisibility(View.GONE);
                btnref.setVisibility(View.VISIBLE);
                Toast.makeText(DetailBuku.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                btnref.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
            }
        });

    }
}
