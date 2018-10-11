package com.example.dickyeka.garasibuku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.dickyeka.garasibuku.R.id.thumbnail;

/**
 * Created by DICKYEKA on 08/05/2017.
 */

public class FragmentUmum extends Fragment {
    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;
    private ProgressBar load;
    private Button btnref;
    private Fragment newFragment;
    private FragmentTransaction ft;

    public static FragmentUmum newInstance(){
        return new FragmentUmum();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragmentumum, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        albumList = new ArrayList<Album>();
        adapter = new AlbumsAdapter(getActivity(),albumList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        btnref = (Button) rootView.findViewById(R.id.btnrefresh);
        btnref.setVisibility(View.GONE);

        load = (ProgressBar) rootView.findViewById(R.id.progressBar2);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        newFragment = new FragmentUmum();

        prepareAlbums();
        return rootView;
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
        btnref.setVisibility(View.GONE);
        String url = "http://cendolzboy.000webhostapp.com/api/";
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(gson)).build();
        MemberService service = retrofit.create(MemberService.class);

        final String jenis = "Umum";
        Call<List<Buku>> call = service.getBukuKat(jenis);
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
                        idmk[i] = b.getIdMk();
                        judul[i] = b.getMataKuliah();
                        harga[i] = b.getHarga();
                        foto[i] = b.getPhoto();
                        Album a = new Album(idmk[i] + " - " + judul[i], harga[i], foto[i]);
                        albumList.add(a);
                        i++;
                    }
                    adapter.notifyDataSetChanged();
                    load.setVisibility(View.GONE);

                    recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            Intent intent = new Intent(getActivity(), DetailBuku.class);
                            intent.putExtra("idbuku", idbuku[position]);
                            startActivity(intent);
                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));
                }else{
                    btnref.setVisibility(View.VISIBLE);
                    load.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                    btnref.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            load.setVisibility(View.VISIBLE);
                            ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.containerumum, newFragment);
                            ft.addToBackStack(null);
                            ft.commit();
                            btnref.setVisibility(View.GONE);
                            load.setVisibility(View.GONE);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Buku>> call, Throwable throwable) {
                btnref.setVisibility(View.VISIBLE);
                load.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Koneksi Gagal", Toast.LENGTH_SHORT).show();
                btnref.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        load.setVisibility(View.VISIBLE);
                        ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.containerumum, newFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                        btnref.setVisibility(View.GONE);
                        load.setVisibility(View.GONE);
                    }
                });
            }
        });
    }




    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //do your variables initialization
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        //initialize your view
    }


}
