package com.example.dickyeka.garasibuku;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profil extends AppCompatActivity {

    private Context context;
    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;
    private ProgressDialog progressDialog;
    Button telp,alamat,btnref;
    TextView nama,bio,pk,belumadabuku;
    ImageView editprofil,propic;
    SharedPreferences sp;
    RelativeLayout container;
    boolean udahedit;
    double latit,longit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        albumList = new ArrayList<Album>();
        adapter = new AlbumsAdapter(this, albumList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Profil.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        btnref = (Button) findViewById(R.id.btnrefreshprofil);
        btnref.setVisibility(View.GONE);
        container = (RelativeLayout) findViewById(R.id.containerprofil);

        prepareAlbums();


        progressDialog = new ProgressDialog(Profil.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/csMediumItalic.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/csRegular.ttf");
        nama = (TextView) findViewById(R.id.nama);
        bio = (TextView) findViewById(R.id.bio);
        telp = (Button) findViewById(R.id.notelp);
        alamat = (Button) findViewById(R.id.alamat);
        pk = (Button) findViewById(R.id.pk);
        editprofil = (ImageView) findViewById(R.id.edit);
        editprofil.setVisibility(View.VISIBLE);
        belumadabuku = (TextView) findViewById(R.id.belumadabuku);
        belumadabuku.setVisibility(View.GONE);
        propic = (ImageView) findViewById(R.id.propict);

        alamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profil.this,MapsActivity.class);
                intent.putExtra("latit",latit);
                intent.putExtra("longit",longit);
                intent.putExtra("nama",nama.getText().toString());
                intent.putExtra("namabuku","");
                startActivity(intent);
            }
        });

        editprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profil.this,EditProfil.class);
                startActivity(intent);
            }
        });

        sp = getSharedPreferences("sp",MODE_PRIVATE);
        final String idsession = sp.getString("id","");

        final String id = getIntent().getStringExtra("id");

        String url = "http://cendolzboy.000webhostapp.com/api/";
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(gson)).build();
        final MemberService service = retrofit.create(MemberService.class);

        Call<List<Member>> call = service.getDataMembers(id);
        call.enqueue(new Callback<List<Member>>() {
            @Override
            public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                if(response.isSuccessful()) {
                    final List<Member> data = response.body();
                    String nimnav = new String();
                    String namanav = new String();
                    String telpnav = new String();
                    String alamatnav = new String();
                    String bionav = new String();
                    String pknav = new String();
                    String photonav = new String();
                    for (Member m : data) {
                        nimnav = m.getId();
                        namanav = m.getName();
                        telpnav = m.getNohp();
                        alamatnav = m.getAlamat();
                        bionav = m.getBio();
                        pknav = m.getPk();
                        photonav = m.getPhoto();
                        latit = m.getLatit();
                        longit = m.getLongit();
                    }
                    nama.setText(namanav);
                    telp.setText(telpnav);
                    final String telpasli = telpnav;
                    alamat.setText(alamatnav);
                    pk.setText(pknav);
                    final String nobio = "Belum ada Bio";
                    if (bionav.isEmpty() || bionav == null) {
                        bionav = nobio;
                    }
                    if (!nimnav.equals(idsession))
                        editprofil.setVisibility(View.GONE);
                    if (photonav.toString().isEmpty())
                        Picasso.with(Profil.this).load(R.drawable.cobaapp).noFade().into(propic);
                    else
                        Picasso.with(Profil.this).load(photonav).noFade().into(propic);
                    bio.setText("\"" + bionav + "\"");


                    telp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = null;
                            intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telpasli));
                            startActivity(intent);
                        }
                    });
                }else {
                    progressDialog.dismiss();
                    container.setVisibility(View.GONE);
                    btnref.setVisibility(View.VISIBLE);
                    Toast.makeText(Profil.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Profil.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private void prepareAlbums() {
        String url = "http://cendolzboy.000webhostapp.com/api/";
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(gson)).build();
        final MemberService service = retrofit.create(MemberService.class);

        final String idbukuasli[] = new String[1000];
        final String id = getIntent().getStringExtra("id");
        Call<List<Buku>> call = service.getDataBukuNim(id);
        call.enqueue(new Callback<List<Buku>>() {
            @Override
            public void onResponse(Call<List<Buku>> call, Response<List<Buku>> response) {
                if(response.isSuccessful()) {
                    final List<Buku> data = response.body();
                    final String idbuku[] = new String[data.size()];
                    final String idmk[] = new String[data.size()];
                    final String judul[] = new String[data.size()];
                    final String harga[] = new String[data.size()];
                    final String foto[] = new String[data.size()];
                    int i = 0;
                    for (Buku b : data) {
                        idbuku[i] = b.getIdBuku();
                        idbukuasli[i] = idbuku[i];
                        idmk[i] = b.getIdMk();
                        judul[i] = b.getMataKuliah();
                        harga[i] = b.getHarga();
                        foto[i] = b.getPhoto();
                        Album a = new Album(idmk[i] + " - " + judul[i], harga[i], foto[i]);
                        albumList.add(a);
                        i++;
                    }
                    if (data.size() == 0) {
                        belumadabuku.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }else {
                    progressDialog.dismiss();
                    container.setVisibility(View.GONE);
                    btnref.setVisibility(View.VISIBLE);
                    Toast.makeText(Profil.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Profil.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
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

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(Profil.this,DetailBuku.class);
                intent.putExtra("idbuku",idbukuasli[position]);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

}
