package com.example.android.xchangapp;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements xCurrencyAdapter.ListItemClickListner {

   // private static final String URL = "https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,ETH&tsyms=USD,EUR,NGN,JPY,GBP,AUD,CAD,CHF,CNY,SEK,MXN,NZD,SGD,HKD,NOK,KRW,TRY,INR,RUB,BRL,ZAR,DKK,PLN,TWD,THB";
    private static final String URL = appConstant.BASE_URL +"=" + appConstant.MAJOR_CURR_LIST;
    public static final List<Currency> currLists = new ArrayList<>();
    ProgressBar mProgressBar;
    private RecyclerView recyclerView;
    private xCurrencyAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the recycler and Refresh layout
        swipeRefreshLayout = findViewById(R.id.swipe_to_refresh);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        adapter = new xCurrencyAdapter(currLists,this);

        // Line divider
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // Method to retrieve the Crypto currency data using the volley network library
        retrieveData();


        // Swipe to Refresh
        //swipeRefreshLayout.setColorSchemeColors(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveData();
                Toast.makeText(MainActivity.this, "Data Refreshed...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // This section implements the search functionality for the recycler view
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    // Create and initialize text listerner for the search query
    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }


    private void retrieveData() {

        // Initialize progress bar reference to the view
        mProgressBar = findViewById(R.id.progressBar);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.VISIBLE);


        StringRequest request = new StringRequest(Request.Method.GET,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Dismiss the progressbar once the required list is generated.
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                //Log.d(TAG, response.toString());

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject js_btc_rates = jsonObject.getJSONObject(appConstant.CURR_BTC_DATA.trim());
                    JSONObject js_eth_rates = jsonObject.getJSONObject(appConstant.CURR_ETH_DATA.trim());

                    Iterator<String> btcKeys = js_btc_rates.keys();
                    Iterator<String> ethKeys = js_eth_rates.keys();

                    while (btcKeys.hasNext() && ethKeys.hasNext()) {

                        String btcKey = btcKeys.next();
                        String ethKey = ethKeys.next();

                        Currency xCurrList = new Currency(btcKey, js_eth_rates.getDouble(ethKey), js_btc_rates.getDouble(btcKey));
                        currLists.add(xCurrList);
                    }
                    // set the adapter to the RecyclerView
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error:" + error.toString(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }
}
