package com.example.dickyeka.garasibuku;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dickyeka.garasibuku.chat.FirebaseChatMainApp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;

    private ProgressDialog progressDialog;
    private SharedPreferences sp;
    private ImageView propicnavdraw;
    private TextView namaView;
    private Context context;
    private FloatingActionButton fab;
    private int inc;
    private ListView listmenu;

    private static ViewPager mPager;
    public static int currentPage = 0;
    private static final Integer[] headlineberita ={R.drawable.berita1,R.drawable.berita2,R.drawable.berita3,R.drawable.berita4};
    private ArrayList<Integer> headlineberitaarray = new ArrayList<Integer>();
    private static final String judulberita[] ={"berita1","berita2","berita3","berita4"};
    private ArrayList<String> judulberitaarray = new ArrayList<>();

    private RelativeLayout beritaslider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        beritaslider = (RelativeLayout) findViewById(R.id.imageslider);
        init();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        albumList = new ArrayList<Album>();
        adapter = new AlbumsAdapter(NavigationDrawer.this,albumList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(NavigationDrawer.this, 2);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new NavigationDrawer.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        listmenu = (ListView) findViewById(R.id.listviewmenu);
        final String namamenu[] = {"Beli Buku","Jual Buku","Profil Saya","Tentang Kami"};
        int head[] = {R.drawable.belibukuhead,R.drawable.jualbukuhead,R.drawable.profilsayahead,R.drawable.tentangkamihead};
        CustomAdapter adapter = new CustomAdapter(namamenu,head,this);
        listmenu.setAdapter(adapter);

        listmenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=null;
                switch (i){
                    case 0:
                        intent = new Intent(NavigationDrawer.this,BeliBuku.class);
                        break;
                    case 1:
                        intent = new Intent(NavigationDrawer.this,JualBuku.class);
                        break;
                    case 2:
                        String id1 = sp.getString("id","");
                        intent = new Intent(NavigationDrawer.this,Profil.class);
                        intent.putExtra("id", id1);
                        break;
                    case 3:
                        intent = new Intent(NavigationDrawer.this,TentangKami.class);
                        break;
                }
                startActivity(intent);
            }
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        sp = getSharedPreferences("sp",MODE_PRIVATE);
        final String idsession = sp.getString("id","");
        View headerView = navigationView.getHeaderView(0);

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationDrawer.this,JualBuku.class);
                startActivity(intent);
            }
        });

        namaView = (TextView) headerView.findViewById(R.id.textView7);

        final String nama = idsession;
        Toast.makeText(this, nama, Toast.LENGTH_SHORT).show();
        context = NavigationDrawer.this;

        String url = "http://cendolzboy.000webhostapp.com/api/";
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(gson)).build();
        MemberService service = retrofit.create(MemberService.class);

        Call<List<Member>> call = service.getDataMembers(nama);
        call.enqueue(new Callback<List<Member>>() {
            @Override
            public void onResponse(Call<List<Member>> call, Response<List<Member>> response) {
                if(response.isSuccessful()) {
                    propicnavdraw = (ImageView) findViewById(R.id.propicnav);
                    final List<Member> data = response.body();
                    String namanav = new String();
                    String photonav = new String();
                    for (Member m : data) {
                        namanav = m.getName();
                        photonav = m.getPhoto();
                    }
                    namaView.setText(namanav + "!");

                    if (photonav.toString().isEmpty())
                        Picasso.with(context).load(R.drawable.cobaapp).noFade().into(propicnavdraw);
                    else
                        Picasso.with(context).load(photonav).noFade().into(propicnavdraw);
                }else{
                    Toast.makeText(NavigationDrawer.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Member>> call, Throwable throwable) {
                Toast.makeText(NavigationDrawer.this, "Koneksi Gagal", Toast.LENGTH_SHORT).show();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void init() {
        for(int i = 0; i< headlineberita.length; i++)
            headlineberitaarray.add(headlineberita[i]);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapter(NavigationDrawer.this, headlineberitaarray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == headlineberita.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(inc>0){
            finish();
            Intent intent = getIntent();
            startActivity(intent);
        }
        else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // lakukan query disini
                searchView.clearFocus();
                progressDialog = new ProgressDialog(NavigationDrawer.this);
                progressDialog.setMessage("Loading..");
                progressDialog.show();
                albumList.clear();
                prepareAlbums(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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

    private void prepareAlbums(String query) {
        listmenu.setVisibility(View.GONE);
        beritaslider.setVisibility(View.GONE);


        String url = "http://cendolzboy.000webhostapp.com/api/";
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(gson)).build();
        MemberService service = retrofit.create(MemberService.class);

        Call<List<Buku>> call = service.getBukuCari(query);
        call.enqueue(new Callback<List<Buku>>() {
            @Override
            public void onResponse(Call<List<Buku>> call, Response<List<Buku>> response) {
                if(response.isSuccessful()) {
                    final List<Buku> data = response.body();
                    if (data.size() == 0) {
                        Toast.makeText(NavigationDrawer.this, "Buku tidak ditemukan", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {
                        final String idbuku[] = new String[data.size()];
                        final String judul[] = new String[data.size()];
                        final String idmk[] = new String[data.size()];
                        final String harga[] = new String[data.size()];
                        final String foto[] = new String[data.size()];
                        final String nim[] = new String[data.size()];
                        int i = 0;
                        for (Buku b : data) {
                            nim[i] = b.getNIM();
                            idbuku[i] = b.getIdBuku();
                            idmk[i] = b.getIdMk();
                            judul[i] = b.getMataKuliah();
                            harga[i] = b.getHarga();
                            foto[i] = b.getPhoto();
                            Album a = new Album(idmk[i] + " - " + judul[i], harga[i], foto[i]);
                            albumList.add(a);
                            i++;
                        }
                        adapter.notifyDataSetChanged();

                        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(NavigationDrawer.this, recyclerView, new ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                Intent intent = new Intent(NavigationDrawer.this, DetailBuku.class);
                                intent.putExtra("idbuku", idbuku[position]);
                                startActivity(intent);
                            }

                            @Override
                            public void onLongClick(View view, int position) {

                            }
                        }));

                        progressDialog.dismiss();
                        inc++;
                    }
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(NavigationDrawer.this, "Koneksi Gagal, coba lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Buku>> call, Throwable throwable) {
                progressDialog.dismiss();
                Toast.makeText(NavigationDrawer.this, "Koneksi Gagal, coba lagi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this,NavigationDrawer.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            String id1 = sp.getString("id","");
            Intent intent = new Intent(NavigationDrawer.this,Profil.class);
            intent.putExtra("id", id1);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(NavigationDrawer.this,BeliBuku.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(NavigationDrawer.this, JualBuku.class);
            startActivity(intent);
        }else if(id == R.id.nav_about){
            Intent intent = new Intent(NavigationDrawer.this, TentangKami.class);
            startActivity(intent);
        }else if(id == R.id.nav_chat){
            Intent intent = new Intent(NavigationDrawer.this, com.example.dickyeka.garasibuku.chat.ui.activities.LoginActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_send) {
            AlertDialog.Builder warning = new AlertDialog.Builder(this);
            warning.setTitle("Apakah anda yakin akan keluar?");
            warning
                    .setMessage("Klik Ya untuk keluar.")
                    .setIcon(R.drawable.ic_menu_send)
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(NavigationDrawer.this,LoginActivity.class);
                            context.getSharedPreferences("sp",0).edit().clear().commit();
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            warning.create();
            warning.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
