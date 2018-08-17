package com.example.maximus.myapplication.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.maximus.myapplication.R;
import com.example.maximus.myapplication.RecyclerAdapter;
import com.example.maximus.myapplication.activity.MainActivity;
import com.example.maximus.myapplication.model.GitHubModel;
import com.example.maximus.myapplication.pref.QueryPreferences;
import com.example.maximus.myapplication.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.MenuItemCompat.getActionView;


public class SearchRepoFragment extends Fragment implements  LoaderManager.LoaderCallbacks<List<GitHubModel>>,
        SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = SearchRepoFragment.class.getSimpleName();
    private static final int LOADER_ID = 1;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
    private RecyclerView recyclerView;
    private static List<GitHubModel> items;
    private static RecyclerAdapter adapter;
    private ProgressBar progressBar;
    private ImageView bookmark;
    private TextView errorMessage;
    SharedPreferences sharedPreferences;
    private BottomNavigationView bottomNavigationView;

    public SearchRepoFragment() {
        // Required empty public constructor
    }


    //TODO Add in menu spinner, that can choose what kind of sort we do. Instead of SettingsFragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search_repo, container, false);
        initializeItems(rootView);
        items = new ArrayList<>();
        adapter = new RecyclerAdapter(items);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateItems();
        hideBottomNavigation();
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
        Log.i(TAG, "onCreateView: ");
        return rootView;
    }


    public void initializeItems(View rootView) {
        progressBar = rootView.findViewById(R.id.progress_bar);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        bookmark = rootView.findViewById(R.id.bookmark_icon);
        errorMessage = rootView.findViewById(R.id.tv_error_message);
        bottomNavigationView = rootView.findViewById(R.id.bottom_navigation);
    }

    private String load(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString(getString(R.string.sort_github_key), getString(R.string.sort_by_stars));

    }



    public void updateItems() {
        String query = QueryPreferences.getStoredQuery(getActivity());
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(query);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.i(TAG, "onCreate: ");
    }

    //TODO Need to reshresh data when back in search tab
    @Override
    public void onStart() {
        super.onStart();
        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            refreshData();
        }
        Log.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated: ");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: ");
    }



    public void hideBottomNavigation() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    ((MainActivity)getActivity()).setNavigationVisibility(true);
                }

                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    ((MainActivity)getActivity()).setNavigationVisibility(false);
                } else if (dy < 0) {
                    ((MainActivity)getActivity()).setNavigationVisibility(true);
                }
            }


        });
    }

    //TODO Refresh search and load a query
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);

        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                QueryPreferences.setStoredQuery(getContext(), query);
                updateItems();
                searchView.clearFocus();
                searchItem.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;

    }

    public void refreshData() {
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<List<GitHubModel>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<List<GitHubModel>>(getActivity()) {
            List<GitHubModel> gitHubModels = null;
            private String mQuery;

            @Override
            protected void onStartLoading() {
                if (gitHubModels != null) {
                    deliverResult(gitHubModels);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(@Nullable List<GitHubModel> data) {
                this.gitHubModels = data;
                super.deliverResult(data);
            }

            @Nullable
            @Override
            public List<GitHubModel> loadInBackground() {
                List<GitHubModel> models = new ArrayList<>();
                String query = QueryPreferences.getStoredQuery(getActivity());
                String sorting = load(sharedPreferences);
                URL requestQuery = NetworkUtils.buildUrl(query, sorting);

                if (mQuery == null) {
                    mQuery = "java";
                }
                try {
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(requestQuery);
                    NetworkUtils.parseJson(models, jsonResponse);

                    return models;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<GitHubModel>> loader, List<GitHubModel> data) {
        progressBar.setVisibility(View.INVISIBLE);
        adapter.setJsonData(data);
        if (data == null) {
            showErrorMessage();
        } else {
            showData();
        }
    }

    private void showData() {
        errorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<GitHubModel>> loader) {

    }

}
