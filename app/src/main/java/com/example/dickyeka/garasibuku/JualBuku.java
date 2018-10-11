package com.example.dickyeka.garasibuku;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JualBuku extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    Spinner jenis,judul;
    ArrayAdapter<String> adp1,adp2;
    List<String> l1,l2;
    int pos;
    Button jualbuku,btnUpload;
    RadioGroup harga;
    RadioButton selektor;
    EditText hargacustom,deskripsibuku;
    SharedPreferences sp;
    ProgressDialog progressDialog;
    String mediaPath;

    private static final String TAG = JualBuku.class.getSimpleName();
    private static final int REQUEST_GALLERY_CODE = 200;
    private static final int READ_REQUEST_CODE = 300;
    private static final String SERVER_PATH = "http://cendolzboy.000webhostapp.com/api/";
    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jual_buku);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_tiga);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.jualbuku);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences("sp",MODE_PRIVATE);
        final String idsession = sp.getString("id","");


        l1 = new ArrayList<String>();
        l1.add("Umum");
        l1.add("Khusus");

        btnUpload = (Button) findViewById(R.id.pick_img);
        jenis = (Spinner) findViewById(R.id.jenisbuku);
        judul = (Spinner) findViewById(R.id.judul);
        jualbuku = (Button) findViewById(R.id.button5);
        harga = (RadioGroup) findViewById(R.id.rg_harga);
        hargacustom = (EditText) findViewById(R.id.hargacustom);
        deskripsibuku = (EditText) findViewById(R.id.deskripsi);
        final String deskripsibukuString = deskripsibuku.getText().toString();

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                openGalleryIntent.setType("image/*");
                startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE);
            }
        });

        hargacustom.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});


        adp1 = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,l1);
        adp1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        jenis.setAdapter(adp1);

        jenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
                add();
            }

            private void add(){
                switch (pos){
                    case 0:
                        l2 = new ArrayList<String>();
                        l2.add("DIP100 - Pendidikan Agama Islam");
                        l2.add("DIP109 - Bahasa Indonesia");
                        adp2 = new ArrayAdapter<String>(JualBuku.this,android.R.layout.simple_dropdown_item_1line,l2);
                        judul.setAdapter(adp2);
                        break;
                    case 1:
                        l2 = new ArrayList<String>();
                        l2.add("KMN100 - Pengantar Komunikasi");
                        l2.add("KMN101 - Teknik Wawancara");
                        adp2 = new ArrayAdapter<String>(JualBuku.this,android.R.layout.simple_dropdown_item_1line,l2);
                        judul.setAdapter(adp2);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(deskripsibuku.isFocused()){
            Toast.makeText(this, "tes", Toast.LENGTH_SHORT).show();
        }

        jualbuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(JualBuku.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Posting your book..");

                int selectedHarga = harga.getCheckedRadioButtonId();
                String hargaasli;
                selektor = (RadioButton) findViewById(selectedHarga);
                if (selektor.getText().toString().equals("Rp. 0"))
                    hargaasli = "0";
                else
                    hargaasli = hargacustom.getText().toString();

                String pilihanjudul = judul.getSelectedItem().toString().substring(0, 6);
                    progressDialog.show();
                    String url = "http://cendolzboy.000webhostapp.com/api/";
                    Gson gson = new GsonBuilder().create();
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(gson)).build();
                    MemberService service = retrofit.create(MemberService.class);


                    Call<Buku> call = service.insertBuku(pilihanjudul, idsession, deskripsibuku.getText().toString(), hargaasli);
                    call.enqueue(new Callback<Buku>() {
                        @Override
                        public void onResponse(Call<Buku> call, Response<Buku> response) {
                            if(response.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(JualBuku.this, "Buku berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(JualBuku.this, NavigationDrawer.class);
                                startActivity(intent);
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(JualBuku.this, "Koneksi Gagal, coba Lagi", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Buku> call, Throwable throwable) {
                            progressDialog.dismiss();
                            Toast.makeText(JualBuku.this, "Koneksi Gagal, coba Lagi", Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, JualBuku.this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_GALLERY_CODE && resultCode == Activity.RESULT_OK){
            uri = data.getData();
            if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String filePath = getRealPathFromURIPath(uri, JualBuku.this);
                File file = new File(filePath);
                Log.d(TAG, "Filename " + file.getName());
                //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(SERVER_PATH)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                MemberService uploadImage = retrofit.create(MemberService.class);
                Call<UploadObject> fileUpload = uploadImage.uploadFile(fileToUpload, filename);
                fileUpload.enqueue(new Callback<UploadObject>() {
                    @Override
                    public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(JualBuku.this, "Gambar Buku Berhasil Diupload!", Toast.LENGTH_LONG).show();
                            Toast.makeText(JualBuku.this, "Success " + response.body().getSuccess(), Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(JualBuku.this, "Gagal Upload", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<UploadObject> call, Throwable t) {
                        Log.d(TAG, "Error " + t.getMessage());
                    }
                });
            }else{
                EasyPermissions.requestPermissions(this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }
    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

        if(uri != null){
            String filePath = getRealPathFromURIPath(uri, JualBuku.this);
            File file = new File(filePath);
            RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
            RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SERVER_PATH)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MemberService uploadImage = retrofit.create(MemberService.class);
            Call<UploadObject> fileUpload = uploadImage.uploadFile(fileToUpload, filename);
            fileUpload.enqueue(new Callback<UploadObject>() {
                @Override
                public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
                    Toast.makeText(JualBuku.this, "Success " + response.message(), Toast.LENGTH_LONG).show();
                    Toast.makeText(JualBuku.this, "Success " + response.body().toString(), Toast.LENGTH_LONG).show();
                }
                @Override
                public void onFailure(Call<UploadObject> call, Throwable t) {
                    Log.d(TAG, "Error " + t.getMessage());
                }
            });
        }
    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        progressDialog.dismiss();
        Log.d(TAG, "Permission has been denied");
    }
}
