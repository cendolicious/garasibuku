package com.example.dickyeka.garasibuku;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener,LocationListener{

    private GoogleApiClient gApi;
    private Location lokasiAktif;
    LocationRequest mintaLokasi;
    double latitude,longitude;
    Button ambillokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_regist);

        if(gApi==null){
            gApi = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }




        final AlphaAnimation buttonClick = new AlphaAnimation(1F,0.8F);

        TextView grsbk = (TextView) findViewById(R.id.textView3);
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/csThin.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/csRegular.ttf");
        grsbk.setTypeface(typeface1);

        final EditText name = (EditText) findViewById(R.id.editText5);
        final EditText pass = (EditText) findViewById(R.id.password);
        final EditText nim = (EditText) findViewById(R.id.editText3);
        final EditText alamat = (EditText) findViewById(R.id.editText6);
        final EditText nohp = (EditText) findViewById(R.id.editText7);
        final EditText passmatch = (EditText) findViewById(R.id.passwordmatch);
        final EditText bio = (EditText) findViewById(R.id.bio);
        ambillokasi = (Button) findViewById(R.id.mockloc);

        nim.setFilters(new InputFilter[]{
                new InputFilter.AllCaps(),
                new InputFilter.LengthFilter(9)});

        Button save = (Button) findViewById(R.id.button3);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://cendolzboy.000webhostapp.com/api/";
                Gson gson = new GsonBuilder().create();
                Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(gson)).build();
                MemberService service = retrofit.create(MemberService.class);

                v.startAnimation(buttonClick);
                String awalnim = nim.getText().toString().substring(0, 1);
                if (nim.getText().toString().isEmpty() ||
                        pass.getText().toString().isEmpty() ||
                        passmatch.getText().toString().isEmpty() ||
                        name.getText().toString().isEmpty() ||
                        alamat.getText().toString().isEmpty() ||
                        nohp.getText().toString().isEmpty()) {
                    Toast.makeText(RegistActivity.this, "Lengkapi form pendaftaran anda", Toast.LENGTH_SHORT).show();

                } else if (!pass.getText().toString().equals(passmatch.getText().toString())) {
                    Toast.makeText(RegistActivity.this, "Konfirmasi password tidak cocok", Toast.LENGTH_SHORT).show();
                } else if(!awalnim.equals("J") || nim.getText().toString().length() != 9){
                    Toast.makeText(RegistActivity.this, "NIM tidak valid", Toast.LENGTH_SHORT).show();
                } else if(latitude==0 && longitude==0){
                    Toast.makeText(RegistActivity.this, "Tekan tombol AMBIL LOKASI", Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog progressDialog;
                    progressDialog = new ProgressDialog(RegistActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Creating your account..");
                    progressDialog.show();

                    String nim1 = nim.getText().toString();
                    String name1 = name.getText().toString();
                    String pass1 = pass.getText().toString();
                    String alamat1 = alamat.getText().toString();
                    String nohp1 = nohp.getText().toString();
                    String bio1 = bio.getText().toString();

                    Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);
                    String encryptedpass = encryption.encryptOrNull(pass1);

                    Call<Member> call = service.insertMember(nim1, name1, encryptedpass, alamat1, nohp1, bio1, latitude, longitude);
                    call.enqueue(new Callback<Member>() {
                        @Override
                        public void onResponse(Call<Member> call, Response<Member> response) {
                            if(response.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Regist Berhasil", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(RegistActivity.this, "Koneksi Gagal/NIM sudah terdaftar, coba lagi", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Member> call, Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Koneksi Gagal/NIM sudah terdaftar, coba lagi", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart(){
        gApi.connect();
        super.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
        LocationServices.FusedLocationApi.removeLocationUpdates(gApi,this);
        gApi.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(RegistActivity.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return;
        }

        mintaLokasi = new LocationRequest();
        mintaLokasi.setInterval(10000);
        mintaLokasi.setFastestInterval(10000);
        mintaLokasi.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        final Location lok = LocationServices.FusedLocationApi.getLastLocation(gApi);


            ambillokasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(lok == null){
                        Toast.makeText(RegistActivity.this, "Nyalakan layanan lokasi anda.", Toast.LENGTH_SHORT).show();
                    }else {
                    latitude = lok.getLatitude();
                    longitude = lok.getLongitude();
                    Toast.makeText(RegistActivity.this, "Lokasi Tersimpan! Silahkan lanjutkan registrasi", Toast.LENGTH_SHORT).show();
                }
                }
            });



    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("Koneksi","Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Gagal Koneksi:"+connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        lokasiAktif = location;
        Toast.makeText(this, "Lokasi berubah:Lat="+String.valueOf(lokasiAktif.getLatitude())+",Longitude="+String.valueOf(lokasiAktif.getLongitude()), Toast.LENGTH_SHORT).show();
    }
}
