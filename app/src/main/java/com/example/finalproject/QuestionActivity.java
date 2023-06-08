package com.example.finalproject;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class QuestionActivity extends AppCompatActivity {

    private JSONArray questions;
    private int currentQuestionIndex = 0;
    private int correctAnswersCount = 0;

    private TextView questionTextView;
    private RadioButton[] answerButtons;
    private Button nextButton;
    private ImageView questionImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Button backToCategories = findViewById(R.id.backToCategories);
        questionTextView = findViewById(R.id.questionTextView);
        questionImageView = findViewById(R.id.questionImageView);


        answerButtons = new RadioButton[4];
        answerButtons[0] = findViewById(R.id.answer1);
        answerButtons[1] = findViewById(R.id.answer2);
        answerButtons[2] = findViewById(R.id.answer3);
        answerButtons[3] = findViewById(R.id.answer4);

        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerAndLoadNextQuestion();
            }
        });
        backToCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionActivity.this, CategorySelectionActivity.class);
                startActivity(intent);
            }
        });

        String category = getIntent().getStringExtra("category");
        questions = getQuestionsFromJson(category);

        loadQuestion();
    }

    private void loadQuestion() {
        try {
            questionImageView.setVisibility(View.GONE);
            JSONObject question = questions.getJSONObject(currentQuestionIndex);
            questionTextView.setText(question.getString("question"));

            JSONArray answers = question.getJSONArray("answers");
            for (int i = 0; i < answerButtons.length; i++) {
                answerButtons[i].setText(answers.getString(i));
            }
            String imageFileName = "";
            try {
                imageFileName = question.getString("image");
            } catch (JSONException e) {
                imageFileName = "";
            }

            if (!imageFileName.isEmpty()) {
                loadImageFromFileName(imageFileName);
            }
        }

            catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void loadImageFromFileName(String imageFileName) {
        if (!imageFileName.isEmpty()) {
            int resourceId = getResources().getIdentifier(imageFileName, "drawable", getPackageName());
            if (resourceId != 0) {
                Drawable imageDrawable = getResources().getDrawable(resourceId);
                questionImageView.setImageDrawable(imageDrawable);
                questionImageView.setVisibility(View.VISIBLE);
            }
        } else {
            questionImageView.setVisibility(View.GONE);
        }
    }



        private void checkAnswerAndLoadNextQuestion() {
        try {
            JSONObject question = questions.getJSONObject(currentQuestionIndex);
            String correctAnswer = question.getString("correct_answer");

            RadioGroup radioGroup = findViewById(R.id.answerRadioGroup);
            int selectedButtonId = radioGroup.getCheckedRadioButtonId();
            RadioButton selectedButton = findViewById(selectedButtonId);
            String userAnswer = selectedButton.getText().toString();
            if (userAnswer.equals(correctAnswer)) {
                correctAnswersCount++;
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Incorrect, the correct answer was " + correctAnswer, Toast.LENGTH_SHORT).show();
            }

            currentQuestionIndex++;
            if (currentQuestionIndex < questions.length()) {
                loadQuestion();
            } else {
                Intent intent = new Intent(QuestionActivity.this, ResultsActivity.class);
                intent.putExtra("category", getIntent().getStringExtra("category"));
                intent.putExtra("correctAnswersCount", correctAnswersCount);
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONArray getQuestionsFromJson(String category) {
        JSONArray categoryQuestions = null;

        try {
            InputStream is = getAssets().open("categories.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                if (obj.getString("name").equals(category)) {
                    categoryQuestions = obj.getJSONArray("questions");
                    break;
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return categoryQuestions;
    }
}

