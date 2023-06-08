package com.example.finalproject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CategorySelectionActivity extends AppCompatActivity {

    ArrayList<String> categories;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        listView = findViewById(R.id.listView);
        categories = getCategoriesFromJson();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CategorySelectionActivity.this, QuestionActivity.class);
                String selectedCategory = categories.get(position);
                intent.putExtra("category", selectedCategory);
                startActivity(intent);
            }
        });
    }

    public ArrayList<String> getCategoriesFromJson() {
        ArrayList<String> categoryList = new ArrayList<>();
        String json;

        try {
            InputStream is = getAssets().open("categories.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                categoryList.add(obj.getString("name"));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return categoryList;
    }
}
