package com.example.dickyeka.garasibuku;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity{

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final SharedPreferences sharedPreferences = getSharedPreferences("sp",MODE_PRIVATE);

        final ListView list1 = (ListView) findViewById(R.id.listView);

        String url = "http://cendolzboy.000webhostapp.com/api/";
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(gson)).build();
        MemberService service = retrofit.create(MemberService.class);

        Call<List<Member>> call = service.getMembers();
        call.enqueue(new Callback<List<Member>>() {
            @Override
            public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                if(response.isSuccessful()) {
                    final List<Member> data = response.body();
                    String name[] = new String[data.size()];
                    int i = 0;
                    for (Member m : data) {
                        name[i++] = m.getName();

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_list_item_1, name);
                    list1.setAdapter(adapter);

                    list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(HomeActivity.this, Profil.class);
                            intent.putExtra("id", data.get(position).getId());
                            intent.putExtra("name", data.get(position).getName());
                            intent.putExtra("photo", data.get(position).getPhoto());
                            startActivity(intent);

                        }
                    });
                }else {
                    Toast.makeText(HomeActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Member>> call, Throwable throwable) {
                Toast.makeText(HomeActivity.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
            }

        });



//DELETE
       /* list1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Member member = new Member();
                member.setId(list1.getItemAtPosition(position).toString());
                service.deleteMember(member.getId()).enqueue(new Callback<Member>() {
                    @Override
                    public void onResponse(Call<Member> call, Response<Member> response) {

                    }

                    @Override
                    public void onFailure(Call<Member> call, Throwable throwable) {

                    }
                });
                return false;
            }
        });*/


        Button btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RegistActivity.class);
                startActivity(intent);
            }
        });

    }
}
