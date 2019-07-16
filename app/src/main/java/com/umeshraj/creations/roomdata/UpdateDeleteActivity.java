package com.umeshraj.creations.roomdata;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.umeshraj.creations.roomdata.roomdb.DataBaseClient;
import com.umeshraj.creations.roomdata.roomdb.Product;

public class UpdateDeleteActivity extends AppCompatActivity {

    private EditText editTextName, editTextDesc, editTextCategory, editTextPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);

        editTextName = findViewById(R.id.editTextName);
        editTextDesc = findViewById(R.id.editTextDesc);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextPrice = findViewById(R.id.editTextPrice);

        final Product product = (Product) getIntent().getSerializableExtra("product");

        loadproduct(product);

        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateproduct(product);
            }
        });

        findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDeleteActivity.this);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteProduct(product);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
            }
        });


    }

    private void loadproduct(Product product) {
        editTextName.setText(product.getName());
        editTextDesc.setText(product.getDesc());
        editTextCategory.setText(product.getCategory());
        editTextPrice.setText(product.getPrice());
    }

    private void updateproduct(final Product product) {
        final String sName = editTextName.getText().toString().trim();
        final String sDesc = editTextDesc.getText().toString().trim();
        final String sCategory = editTextCategory.getText().toString().trim();
        final String sPrice = editTextPrice.getText().toString().trim();

        if (sName.isEmpty()) {
            editTextName.setError("Name required");
            editTextName.requestFocus();
            return;
        }

        if (sDesc.isEmpty()) {
            editTextDesc.setError("Desc required");
            editTextDesc.requestFocus();
            return;
        }

        if (sCategory.isEmpty()) {
            editTextCategory.setError("Finish by required");
            editTextCategory.requestFocus();
            return;
        }
        if (sPrice.isEmpty()) {
            editTextPrice.setError("Price required");
            editTextPrice.requestFocus();
        }
        class UpdateProductDetails extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                product.setName(sName);
                product.setDesc(sDesc);
                product.setCategory(sCategory);
                product.setPrice(sPrice);
                //adding to database
                DataBaseClient.getInstance(getApplicationContext())
                        .getAppDatabase()
                        .productDao()
                        .update(product);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Updated    ", Toast.LENGTH_LONG).show();
            }
        }

        UpdateProductDetails upd = new UpdateProductDetails();
        upd.execute();
    }

    private void deleteProduct(final Product product) {
        class DeleteProduct extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DataBaseClient.getInstance(getApplicationContext())
                        .getAppDatabase()
                        .productDao()
                        .delete(product);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(UpdateDeleteActivity.this, MainActivity.class));
            }
        }

        DeleteProduct dp = new DeleteProduct();
        dp.execute();

    }
}
