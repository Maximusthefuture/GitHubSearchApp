package com.example.maximus.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.maximus.myapplication.model.GitHubModel;
import com.example.maximus.myapplication.pref.QueryPreferences;
import com.example.maximus.myapplication.utils.GithubSearchResponse;
import com.example.maximus.myapplication.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private static List<GitHubModel> items;
    private static RecyclerAdapter adapter;
    private GithubSearchResponse response;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        invalidateOptionsMenu();
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        items = new ArrayList<>();
        adapter = new RecyclerAdapter(items);
        recyclerView.setAdapter(adapter);
        updateItems();

    }

    public void updateItems() {
        String query = QueryPreferences.getStoredQuery(this);
        new GithubQueryTask(query).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                QueryPreferences.setStoredQuery(getApplicationContext(), query);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class GithubQueryTask extends AsyncTask<Void, Void, List<GitHubModel>> {
        private String mQuery;

        public GithubQueryTask(String query) {
            this.mQuery = query;
        }

        @Override
        protected void onPostExecute(List<GitHubModel> gitHubModels) {
            progressBar.setVisibility(View.INVISIBLE);
            if (gitHubModels != null) {
                adapter.setJsonData(gitHubModels);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected List<GitHubModel> doInBackground(Void... voids) {

            URL requestQuery = NetworkUtils.buildUrl(mQuery);

            if (mQuery == null) {
                mQuery = "java";
            }
            try {
                String jsonResponce = NetworkUtils.getResponseFromHttpUrl(requestQuery);
                List<GitHubModel> models = NetworkUtils.parseJSON(MainActivity.this, jsonResponce);

                return models;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }
}
