package fbapp.example.com.fbapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.facebook.ads.AdSettings;
import com.facebook.ads.NativeAdsManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fbapp.example.com.fbapp.Adapter.FacebookAdapter;
import fbapp.example.com.fbapp.Interface.FacebookApi;
import fbapp.example.com.fbapp.Modules.FBData;
import fbapp.example.com.fbapp.Modules.FBDatumClass;
import fbapp.example.com.fbapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FacebookActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView fbRecyclerview;
    FacebookAdapter fbAdapter;
    private FacebookApi facebookApi;

    private NativeAdsManager mAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.fb_base_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        facebookApi = retrofit.create(FacebookApi.class);

        LoadAds();
        fbDataFetch();
        loadProfilePicture();
    }


    public void LoadAds() {
        String placement_id = getResources().getString(R.string.app_name);
        mAds = new NativeAdsManager(getApplicationContext(), placement_id, 5);
        mAds.loadAds();
    }

    private void fbDataFetch() {

        fbRecyclerview = (RecyclerView) findViewById(R.id.fbRecyclerView);
        fbRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));
        fbRecyclerview.setNestedScrollingEnabled(false);

        String fbFields = getResources().getString(R.string.fb_fields);
        String access_token = getResources().getString(R.string.access_token);
        List<FBDatumClass> fbData = new ArrayList<>();

        Call<FBData> fbDataCall = facebookApi.getFbPosts(fbFields, access_token);
        fbDataCall.enqueue(new Callback<FBData>() {
            @Override
            public void onResponse(Call<FBData> call, Response<FBData> response) {
                int statusCode = response.code();
                FBData fbDataresponce = response.body();
                Log.d("FbData", "onResponse: " + statusCode);
                FacebookAdapter fbAdapter = new FacebookAdapter(
                        fbDataresponce.getFBDatumList(), FacebookActivity.this, mAds);
                fbRecyclerview.setAdapter(fbAdapter);

            }

            @Override
            public void onFailure(Call<FBData> call, Throwable t) {

            }
        });

        fbAdapter = new FacebookAdapter(fbData, getApplicationContext(), mAds);
        fbRecyclerview.setAdapter(fbAdapter);

    }

    private void loadProfilePicture() {

        ImageView image = (ImageView) findViewById(R.id.fbProfileImage);

        Picasso.with(FacebookActivity.this)
                .load("https://graph.facebook.com/19292868552/picture?type=large&access_token="
                        + getResources().getString(R.string.access_token))
                .into(image);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_facebook) {
             new Intent(getApplicationContext(), FacebookActivity.class);
        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
