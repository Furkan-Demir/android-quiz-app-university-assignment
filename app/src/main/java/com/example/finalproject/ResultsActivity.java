package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        TextView resultTextView = findViewById(R.id.resultTextView);
        Button restartButton = findViewById(R.id.restartButton);

        String category = getIntent().getStringExtra("category");
        int correctAnswersCount = getIntent().getIntExtra("correctAnswersCount", 0);

        resultTextView.setText("Quiz completed in category " + category + "! You answered " + correctAnswersCount + " questions correctly.");
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultsActivity.this, CategorySelectionActivity.class));
            }
        });
    }
}
