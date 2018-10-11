package com.example.dickyeka.garasibuku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfil extends AppCompatActivity {
    Toolbar toolbar;
    TextView nim;
    EditText colnama,colnohp,colalamat,colbio;
    SharedPreferences sp;
    ImageView colpropictedit;
    ProgressDialog progressDialog;
    Button savebtn;
    String urlPhoto;
    String passsaver;
    double latit,longit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        progressDialog = new ProgressDialog(EditProfil.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        sp = getSharedPreferences("sp",MODE_PRIVATE);
        final String idsession = sp.getString("id","");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_editprofil);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.editprofil);
        final Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);
        nim = (TextView) findViewById(R.id.nim);
        colnama = (EditText) findViewById(R.id.nama);
        colnohp = (EditText) findViewById(R.id.nohp);
        colalamat = (EditText) findViewById(R.id.alamat);
        colbio = (EditText) findViewById(R.id.bio);
        colpropictedit = (ImageView) findViewById(R.id.propictedit);
        savebtn = (Button) findViewById(R.id.save);

        nim.setText("Anda terdaftar dengan NIM "+idsession);

        String url = "http://cendolzboy.000webhostapp.com/api/";
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(gson)).build();
        final MemberService service = retrofit.create(MemberService.class);

        Call<List<Member>> call = service.getDataMembers(idsession);
        call.enqueue(new Callback<List<Member>>() {
            @Override
            public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                if(response.isSuccessful()) {
                    List<Member> data = response.body();
                    String nama = new String();
                    String password = new String();
                    String nohp = new String();
                    String alamat = new String();
                    String bio = new String();
                    String photo = new String();
                    for (Member m : data) {
                        nama = m.getName();
                        password = m.getPassword();
                        nohp = m.getNohp();
                        alamat = m.getAlamat();
                        bio = m.getBio();
                        photo = m.getPhoto();
                        latit = m.getLatit();
                        longit = m.getLongit();
                    }
                    urlPhoto = photo;
                    colnama.setText(nama);
                    colalamat.setText(alamat);
                    colbio.setText(bio);
                    colnohp.setText(nohp);
                    passsaver = password;

                    if (photo.toString().isEmpty())
                        Picasso.with(EditProfil.this).load(R.drawable.defaultpp).noFade().into(colpropictedit);
                    else
                        Picasso.with(EditProfil.this).load(photo).noFade().into(colpropictedit);
                    progressDialog.dismiss();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(EditProfil.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Member>> call, Throwable throwable) {
                progressDialog.dismiss();
                Toast.makeText(EditProfil.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                Call<List<Member>> call1 = service.updateMember(
                        idsession,
                        colnama.getText().toString(),
                        passsaver,
                        colalamat.getText().toString(),
                        colnohp.getText().toString(),
                        colbio.getText().toString(),
                        latit,longit);
                call1.enqueue(new Callback<List<Member>>() {
                    @Override
                    public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(EditProfil.this, "Berhasil di edit", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditProfil.this, NavigationDrawer.class);
                            intent.putExtra("id", idsession);
                            progressDialog.dismiss();
                            startActivity(intent);
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(EditProfil.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Member>> call, Throwable throwable) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfil.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
