package com.umeshraj.creations.roomdata;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.umeshraj.creations.roomdata.roomdb.DataBaseClient;
import com.umeshraj.creations.roomdata.roomdb.Product;
import com.umeshraj.creations.roomdata.roomdb.ProductAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton buttonAddProduct = findViewById(R.id.floating_button_add);
        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });


        getTasks();

    }

    private void getTasks() {

        class GetProducts extends AsyncTask<Void,Void, List<Product>>{

            @Override
            protected List<Product> doInBackground(Void... voids) {

                return DataBaseClient.getInstance(getApplicationContext())
                        .getAppDatabase()
                        .productDao()
                        .getAll();
            }

            @Override
            protected void onPostExecute(List<Product> products) {
                super.onPostExecute(products);
                ProductAdapter productAdapter=new ProductAdapter(getApplicationContext(), products);
                recyclerView.setAdapter(productAdapter);
            }
        }

        GetProducts gp=new GetProducts();
        gp.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
