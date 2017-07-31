package fbapp.example.com.fbapp.UI;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.builditpossible.tube2droid.Adapters.FacebookAdapter;
import com.builditpossible.tube2droid.ApiInterface.FacebookApi;
import com.builditpossible.tube2droid.BuildConfig;
import com.builditpossible.tube2droid.Fonts.TextView;
import com.builditpossible.tube2droid.Models.Facebook.FBData;
import com.builditpossible.tube2droid.Models.Facebook.FBDatumClass;
import com.builditpossible.tube2droid.R;
import com.facebook.ads.NativeAdsManager;
import com.facebook.appevents.AppEventsLogger;
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

import static com.builditpossible.tube2droid.Config.enableAbout;
import static com.builditpossible.tube2droid.Config.enableAds;
import static com.builditpossible.tube2droid.Config.enableFbUpdates;
import static com.builditpossible.tube2droid.Config.enableShare;
import static com.builditpossible.tube2droid.Config.enableStore;

public class FacebookActivity extends AppCompatActivity {

    RecyclerView fbRecyclerview;
    FacebookAdapter fbAdapter;
    private FacebookApi facebookApi;
    private List<FBDatumClass> fbData;

    private NativeAdsManager mAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        //AdSettings.addTestDevice("db6fe7e23d70c68716cf448f5558b93f");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.fbtoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorSecondary));

        toolbar.post(new Runnable() {
            @Override
            public void run() {
                Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu_icon, null);
                toolbar.setNavigationIcon(d);
                toolbar.setContentInsetStartWithNavigation(0);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.fb_base_url))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        facebookApi = retrofit.create(FacebookApi.class);

        fbRecyclerview = (RecyclerView) findViewById(R.id.fbRecyclerView);
        fbRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));
        fbRecyclerview.setNestedScrollingEnabled(false);

        fbData = new ArrayList<>();
        fbAdapter = new FacebookAdapter(fbData, this, mAds);
        fbRecyclerview.setAdapter(fbAdapter);

        if (enableAds) {
            LoadAds();
        }
        fbDataFetch();
        Navclicks();
        loadProfilePicture();
        MenuToggle();

    }

    public void LoadAds() {
        String placement_id = BuildConfig.fbRecyclerId;
        mAds = new NativeAdsManager(getApplicationContext(), placement_id, 5);
        mAds.loadAds();

    }

    private void Navclicks() {

        Button 1Button = (Button) findViewById(R.id.nav_1);
        Button 2Button = (Button) findViewById(R.id.nav_2);
        Button 3Button = (Button) findViewById(R.id.nav_3);
        Button 4Button = (Button) findViewById(R.id.nav_4);
        Button 5Button = (Button) findViewById(R.id.nav_5);

        youtubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppEventsLogger loggerDownload = AppEventsLogger.newLogger(getApplicationContext());
                loggerDownload.logEvent("nav_1");

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                Intent 1Intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(1Intent);
            }
        });

        FacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppEventsLogger loggerDownload = AppEventsLogger.newLogger(getApplicationContext());
                loggerDownload.logEvent("nav_2");

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                Intent 2Intent = new Intent(getApplicationContext(), 2Activity.class);
                startActivity(2Intent);

            }
        });

        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppEventsLogger loggerDownload = AppEventsLogger.newLogger(getApplicationContext());
                loggerDownload.logEvent("nav_3");

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                Intent 2Button = new Intent(getApplicationContext(), 2Activity.class);
                startActivity(2Button);

            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppEventsLogger loggerDownload = AppEventsLogger.newLogger(getApplicationContext());
                loggerDownload.logEvent("nav_4");

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                Intent 4Button = new Intent(getApplicationContext(), 4Activity.class);
                startActivity(4Button);

            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppEventsLogger loggerDownload = AppEventsLogger.newLogger(getApplicationContext());
                loggerDownload.logEvent("nav_5");

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out " + getResources().getString(R.string.app_name) + " app at: https://play.google.com/store/apps/details?id=" +
                                BuildConfig.APPLICATION_ID);
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                startActivity(Intent.createChooser(shareIntent, "Share using"));

            }
        });

    }

    private void fbDataFetch() {

        fbRecyclerview = (RecyclerView) findViewById(R.id.fbRecyclerView);
        fbRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));
        fbRecyclerview.setNestedScrollingEnabled(false);
        fbRecyclerview.setHasFixedSize(true);
        fbRecyclerview.setItemViewCacheSize(20);

        String fbFields = getResources().getString(R.string.fb_fields);
        String access_token = BuildConfig.fbAccessToaken;

        if (enableAds){

            fbData = new ArrayList<>();
            fbAdapter = new FacebookAdapter(fbData, getApplicationContext(), mAds);
            fbRecyclerview.setAdapter(fbAdapter);

            Call<FBData> fbDataCall = facebookApi.getFbPosts(fbFields, access_token);
            fbDataCall.enqueue(new Callback<FBData>() {
                @Override
                public void onResponse(@NonNull Call<FBData> call, @NonNull Response<FBData> response) {
                    int statusCode = response.code();
                    FBData fbDataresponce = response.body();
                    Log.d("FbData", "onResponse: " + statusCode);
                    if (fbDataresponce != null && fbDataresponce.getFBDatumList() != null) {
                        fbData = fbDataresponce.getFBDatumList();
                        int size = fbData.size();
                        int indexOfLastAd = size - size % 3;
                        for (int i = indexOfLastAd; i > 0; i = i - 3) {
                            fbData.add(i, new FBDatumClass());
                        }
                        fbAdapter = new FacebookAdapter(fbData, FacebookActivity.this, mAds);
                        fbRecyclerview.setAdapter(fbAdapter);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FBData> call, @NonNull Throwable t) {

                }
            });

        }else {
            List<FBDatumClass> fbData = new ArrayList<>();

            final Call<FBData> fbDataCall = facebookApi.getFbPosts(fbFields, access_token);
            fbDataCall.enqueue(new Callback<FBData>() {
                @Override
                public void onResponse(@NonNull Call<FBData> call, @NonNull Response<FBData> response) {
                    int statusCode = response.code();
                    FBData fbDataresponce = response.body();
                    Log.d("FbData", "onResponse: " + statusCode);
                    assert fbDataresponce != null;
                    FacebookAdapter fbAdapter = new FacebookAdapter(
                            fbDataresponce.getFBDatumList(), FacebookActivity.this, mAds);
                    fbRecyclerview.setAdapter(fbAdapter);

                }

                @Override
                public void onFailure(@NonNull Call<FBData> call, @NonNull Throwable t) {

                }
            });

            fbAdapter = new FacebookAdapter(fbData, this, mAds);
            fbRecyclerview.setAdapter(fbAdapter);
        }
    }


    private void loadProfilePicture() {

        ImageView image = (ImageView) findViewById(R.id.fbProfileImage);

        Picasso.with(getApplicationContext())
                .load("https://graph.facebook.com/" + BuildConfig.fbPageId
                        + "/picture?type=large&access_token=" + BuildConfig.fbAccessToaken)
                .fit().into(image);
    }

    public void MenuToggle() {

        RelativeLayout menuItem2 = (RelativeLayout) findViewById(R.id.menuItemFacebook);
        RelativeLayout menuItem3 = (RelativeLayout) findViewById(R.id.menuItem2);
        RelativeLayout menuItem4 = (RelativeLayout) findViewById(R.id.menuItemAbout);
        RelativeLayout menuItem4 = (RelativeLayout) findViewById(R.id.menuItemShare);
        TextView storeSubHeading = (TextView) findViewById(R.id.storeSubHeading);
        TextView aboutSubHeading = (TextView) findViewById(R.id.aboutSubHeading);
        TextView shareSubHeading = (TextView) findViewById(R.id.shareSubHeading);

        if (enableFbUpdates) {
            menuItem2.setVisibility(View.VISIBLE);
        } else {
            menuItem2.setVisibility(View.GONE);
        }

        if (enable2) {
            menuItem3.setVisibility(View.VISIBLE);
            3SubHeading.setVisibility(View.VISIBLE);
        } else {
            menuItem2.setVisibility(View.GONE);
            2SubHeading.setVisibility(View.GONE);
        }

        if (enableAbout) {
            menuItem4.setVisibility(View.VISIBLE);
            4SubHeading.setVisibility(View.VISIBLE);
        } else {
            menuItem4.setVisibility(View.GONE);
            4SubHeading.setVisibility(View.GONE);
        }

        if (enableShare) {
            menuItem5.setVisibility(View.VISIBLE);
            5SubHeading.setVisibility(View.VISIBLE);
        } else {
            menuItem4.setVisibility(View.GONE);
            5SubHeading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

}
