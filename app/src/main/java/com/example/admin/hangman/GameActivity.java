package com.example.admin.hangman;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.icu.util.TimeUnit;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class GameActivity extends AppCompatActivity {

    int stepCounter = 0;
    final static int MAX_STEPS = 6;
    String currentWord;

    List<WordGroup> wordGroups;
    List<TextView> letters;
    List<Integer> images;
    List<String> words;
    LinearLayout layoutText, layoutLetters;
    ImageView view;

    Integer level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        populateRandomWords();

        //get level
        Bundle bundle = this.getIntent().getExtras();
        level = (Integer) bundle.get("level");

        //initialize group of words
        words = new ArrayList<>();
        for (WordGroup wordGroup : wordGroups){
            if (wordGroup.getLevel().equals(level)){
                words = wordGroup.getWords();
            }
        }

        layoutText = findViewById(R.id.layoutText);
        layoutLetters = findViewById(R.id.layoutLetters);
        view = findViewById(R.id.viewImage);
        view.setImageResource(R.drawable.hangman_1);

        images = new ArrayList<>();
        images.add(R.drawable.hangman_1); // index 0
        images.add(R.drawable.hangman_2); // index 1
        images.add(R.drawable.hangman_3);
        images.add(R.drawable.hangman_4);
        images.add(R.drawable.hangman_5);
        images.add(R.drawable.hangman_6);
        images.add(R.drawable.hangman_7); // index 6

        initializeLayoutText();
    }

    //implementation can be replaced by getting words from database
    //another feature to add is level of word difficulty
    public void populateRandomWords(){

        wordGroups = new ArrayList<>();

        List<String> words = new ArrayList<>();
        words = new ArrayList<>();
        words.add("apple");
        words.add("house");
        words.add("water");
        words.add("spoon");
        words.add("fluff");
        words.add("razor");
        words.add("quilt");
        words.add("prism");
        words.add("pilot");
        words.add("pizza");
        words.add("organ");
        words.add("orbit");
        words.add("image");
        words.add("joint");
        words.add("error");
        words.add("jazz");
        words.add("juju");
        words.add("quiz");
        words.add("jinx");
        words.add("zyme");
        words.add("joke");
        words.add("flux");
        words.add("mock");
        words.add("quad");
        words.add("zeal");
        words.add("hack");
        words.add("wire");
        words.add("mink");
        words.add("chef");
        words.add("club");
        words.add("monday");
        words.add("purple");
        words.add("moment");
        words.add("secret");
        words.add("snitch");
        words.add("energy");
        words.add("spring");
        words.add("pirate");
        words.add("winter");
        words.add("bottle");
        words.add("ginger");
        words.add("melody");
        words.add("dragon");
        words.add("wisdom");

        wordGroups.add(new WordGroup(1, words));
    }

    public void initializeLayoutText() {
        getNextWord();
    }

    public void reset(){
        stepCounter = 0;
        letters = new ArrayList<>();
        layoutText.removeAllViews();
        view.setImageResource(R.drawable.hangman_1);

        int mainLayoutChildCount = layoutLetters.getChildCount();
        for (int x = 0; x < mainLayoutChildCount; x++){
            LinearLayout layout = (LinearLayout)layoutLetters.getChildAt(x);
            int buttonCount = layout.getChildCount();
            for(int y = 0; y < buttonCount; y++){
                View v = layout.getChildAt(y);
                if (v instanceof Button){
                    Button button = (Button)v;
                    button.setEnabled(true);
                    button.setTextColor(getResources().getColor(R.color.font_black));
                    button.setBackgroundTintList(getResources().getColorStateList(R.color.app_color_yellow));
                }
            }
        }
    }

    public void getNextWord(){
        reset();

        //get random word from list
        getRandomWord();
        if (currentWord == null || currentWord.length() == 0){
            return;
        }

        char[] characters = currentWord.toCharArray();
        for (char c : characters){
            //add blank text field
            if (letters.size() > 0){
                TextView space = new TextView(this);
                space.setTextSize(40F);
                space.setText(" ");
                layoutText.addView(space);
            }

            TextView textView = new TextView(this);
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            textView.setTextSize(40F);

            SpannableString content = new SpannableString("-");
            textView.setText(content);

            letters.add(textView);
            layoutText.addView(textView);
        }
        layoutText.refreshDrawableState();
    }

    public void getRandomWord(){
        Random random = new Random();
        int index = random.nextInt(words.size());
        currentWord = words.get(index).toUpperCase();
        words.remove(index); //to remove from next word
    }

    public void buttonCharacterClicked(View view){
        Button button = (Button) view;
        Character buttonChar = button.getText().charAt(0);

        boolean found = false;
        char[] characters = currentWord.toCharArray();
        for (char c : characters){
            if (buttonChar.charValue() == c){
                found = true;

                displayCharacter(buttonChar);
                break;
            }
        }

        if(!found) {
            button.setTextColor(getResources().getColor(R.color.font_red));
            updateView();
        }

        button.setEnabled(false);
        button.setTextColor(getResources().getColor(R.color.font_gray));
        checkWordStatus();
    }

    public void displayCharacter(Character character){
        char[] characters = currentWord.toCharArray();
        for (int x = 0; x < characters.length; x++){
            if (character.charValue() == characters[x]){
                TextView textView = letters.get(x);
                textView.setText("" + character);
            }
        }
    }

    public void updateView(){
        view.setImageResource(images.get(++stepCounter));

        if (stepCounter == MAX_STEPS){
            //Toast.makeText(this, "Oh, noose! Please try again.", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Oh, noose! Please try again.");
            alertDialogBuilder.setPositiveButton("Okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(GameActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    public void checkWordStatus(){
        boolean isComplete = true;
        String pattern = "[A-Z]";
        for (TextView textView : letters){
            if (!textView.getText().toString().matches(pattern)){
                isComplete = false;
                break;
            }
        }

        if (isComplete){
            //Toast.makeText(this, "Hooray! You got it! The word was " + currentWord + ".", Toast.LENGTH_SHORT).show();

            String message = "Hooray! You got it! The word was " + currentWord + ".";
            if (words.size() == 0){
                message = "You're a genius! You have guessed all the words in this version.";
            }
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setPositiveButton("Continue",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //get next word...
                            if (words.size() > 0) {
                                getNextWord();
                            }else{
                                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }
}
