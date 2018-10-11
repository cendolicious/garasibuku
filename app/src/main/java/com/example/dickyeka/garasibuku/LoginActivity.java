package com.example.dickyeka.garasibuku;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobapphome.mahencryptorlib.MAHEncryptor;

import java.security.SecureRandom;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.key;
import static android.R.attr.value;

public class LoginActivity extends Activity {
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        final ProgressBar spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        final TextView daftar = (TextView) findViewById(R.id.textView5);
        daftar.setPaintFlags(daftar.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        daftar.setText("Daftar yuk!");

        TextView grsbk = (TextView) findViewById(R.id.textView3);

        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/csThin.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/csRegular.ttf");
        grsbk.setTypeface(typeface1);

        final EditText nim = (EditText) findViewById(R.id.editText);
        final EditText pass = (EditText) findViewById(R.id.editText2);
        Button login = (Button) findViewById(R.id.button);
        nim.setTypeface(typeface2);
        pass.setTypeface(typeface2);
        login.setTypeface(typeface1);
        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
        nim.setFilters(new InputFilter[]{
                new InputFilter.AllCaps(),
                new InputFilter.LengthFilter(9)});

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                if (nim.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Username/Password belum terisi", Toast.LENGTH_SHORT);
                    toast.show();
                    if (nim.getText().toString().isEmpty() && pass.getText().toString().isEmpty()) {
                        nim.setError("NIM Belum Terisi");
                        pass.setError("Password Belum Terisi");
                    } else if (pass.getText().toString().isEmpty()) {
                        pass.setError("Password Belum Terisi");
                    } else if (nim.getText().toString().isEmpty()) {
                        nim.setError("NIM Belum Terisi");
                    }
                } else {
                    String url = "http://cendolzboy.000webhostapp.com/api/";
                    Gson gson = new GsonBuilder().create();
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(gson)).build();
                    MemberService service = retrofit.create(MemberService.class);

                    final String nim1 = nim.getText().toString();
                    final String pass1 = pass.getText().toString();

                    spinner.setVisibility(View.VISIBLE);

                    Call<List<Member>> call = service.getDataMembers(nim1);
                    call.enqueue(new Callback<List<Member>>() {
                        @Override
                        public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                            if (response.isSuccessful()) {
                                final List<Member> data = response.body();
                                String nimnav = new String();
                                String passnav = new String();

                                int i = 0;
                                for (Member m : data) {
                                    nimnav = m.getId();
                                    passnav = m.getPassword();
                                }

                                Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);
                                String decryptedpass = encryption.decryptOrNull(passnav);

                                if (nimnav.equals(nim.getText().toString()) && pass1.equals(decryptedpass)) {
                                    SharedPreferences.Editor editor = getSharedPreferences("sp", MODE_PRIVATE).edit();
                                    editor.putString("id", nim1);
                                    editor.putBoolean("status", true);
                                    editor.commit();

                                    Toast.makeText(LoginActivity.this, "Decrypted Pass : "+decryptedpass, Toast.LENGTH_SHORT).show();
                                    Toast toast = Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT);
                                    toast.show();
                                    Intent intent1 = new Intent(LoginActivity.this, NavigationDrawer.class);
                                    intent1.putExtra("id", nim1);

                                    startActivity(intent1);

                                    //finish();
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Username/Password salah", Toast.LENGTH_SHORT);
                                    toast.show();
                                    spinner.setVisibility(View.GONE);
                                }
                            } else {
                                spinner.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Koneksi Gagal, coba lagi", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Member>> call, Throwable throwable) {
                            Toast.makeText(LoginActivity.this, "Koneksi Gagal, coba lagi", Toast.LENGTH_SHORT).show();
                            spinner.setVisibility(View.GONE);
                        }
                    });

                }
            }
        });



        /*Call<List<Member>> call = service.getPass(nim1);
        call.enqueue(new Callback<List<Member>>() {
            @Override
            public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                List<Member> data = response.body();
                Member m = new Member();
                String passasli = m.getPassword();
                if (pass1.equals(passasli)) {
                    Intent intent1 = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent1);
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_LONG);
                } else {
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Password Salah", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<List<Member>> call, Throwable throwable) {

            }
        });*/

        /*login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent1);
            }
        });*/

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent2);
            }
        });
    }


}
