package com.umeshraj.creations.roomdata;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.umeshraj.creations.roomdata.roomdb.DataBaseClient;
import com.umeshraj.creations.roomdata.roomdb.Product;

public class AddActivity extends AppCompatActivity {

    private EditText editTextName, editTextDesc, editTextCategory, editTextPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editTextName = findViewById(R.id.editTextName);
        editTextDesc = findViewById(R.id.editTextDesc);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextPrice = findViewById(R.id.editTextPrice);

        Button msave=findViewById(R.id.button_save);

        msave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });

    }

    private void saveProduct() {
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
        class SaveProductDetails extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                Product product = new Product();
                product.setName(sName);
                product.setDesc(sDesc);
                product.setCategory(sCategory);
                product.setPrice(sPrice);
                //adding to database
                DataBaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .productDao()
                        .insert(product);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveProductDetails spd = new SaveProductDetails();
        spd.execute();
    }
}
