package com.example.maximus.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.maximus.myapplication.fragment.BookmarksFragment;
import com.example.maximus.myapplication.R;
import com.example.maximus.myapplication.RecyclerAdapter;
import com.example.maximus.myapplication.ViewPagerAdapter;
import com.example.maximus.myapplication.fragment.RecentFragment;
import com.example.maximus.myapplication.fragment.SearchRepoFragment;
import com.example.maximus.myapplication.model.GitHubModel;
import com.example.maximus.myapplication.pref.QueryPreferences;
import com.example.maximus.myapplication.pref.SettingsFragment;
import com.example.maximus.myapplication.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        invalidateOptionsMenu();
//        hideBottomNavigation();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.viewpager);
        bottomNavigationSelector();
        setupViewPager(viewPager);
        setViewPager();
        disableShiftMode(bottomNavigationView);

    }

    public void setNavigationVisibility(boolean visible) {
        if (bottomNavigationView.isShown() && !visible) {
            bottomNavigationView.setVisibility(View.GONE);
        } else if (!bottomNavigationView.isShown() && visible) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("RestrictedApi")
    public void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
        Log.e("BNVHelper", "Unable to get shift mode field", e);
    } catch (IllegalAccessException e) {
        Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }




//    //TODO
//    public void clickBookmark(View view) {
//        bookmark = view.findViewById(R.id.bookmark_icon);
//        bookmark.setImageDrawable(getResources().getDrawable(R.drawable.outline_bookmark_border_full_24));
//        Toast.makeText(this, "Bookmark added", Toast.LENGTH_SHORT).show();
//    }





    //TODO
    public void bottomNavigationSelector() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.recent_repositories:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.bookmark:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.search :
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.settings:
                        viewPager.setCurrentItem(3);
                        break;


                }
                return false;
            }
        });
    }

    /*TODO:Add ViewPager*/
    public void setViewPager() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        bookmarksFragment = new BookmarksFragment();
        adapter.addFragment(new RecentFragment());
        adapter.addFragment(new BookmarksFragment());
        adapter.addFragment(new SearchRepoFragment());
        adapter.addFragment(new SettingsFragment());
        viewPager.setAdapter(adapter);
    }


}
