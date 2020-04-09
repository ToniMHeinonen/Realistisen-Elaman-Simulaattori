package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.SaveSystemPreferences;

public class ScenarioActivity extends ParentActivity {
    private String scenario;
    private int scenarioID;
    private SaveSystemPreferences saveSystem;
    private TextView questionTextView;
    private List<String> colors;
    private ArrayList<Integer> userAnswers;
    private final int CORRECT = 100;
    private final int WRONG = 0;
    private final int SEMICORRECT = 50;
    private ImageView background, character, face;
    private Drawable characterStart, characterEnd, faceStart, faceEnd;
    private ColorDrawable emptyDrawable = new ColorDrawable(Color.TRANSPARENT);
    private ImageView answeredImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load font theme
        getTheme().applyStyle(GlobalPrefs.loadFontStyle().getResId(), true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario);
        userAnswers = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            scenario = extras.getString("scenario");
            scenarioID = extras.getInt("scenarioID");
            setupStart();
            updateImages();
            setupAnswers();
        }
    }

    private void setupStart() {
        saveSystem = new SaveSystemPreferences(this);
        saveSystem.setCurrentScenario(scenario);
        questionTextView = findViewById(R.id.question);
        questionTextView.setText(saveSystem.getQuestionFromScenario());
        colors = saveSystem.getColorsInString();
        background = findViewById(R.id.scenarioBackground);
        character = findViewById(R.id.scenarioCharacter);
        face = findViewById(R.id.scenarioFace);
    }

    private void setupAnswers() {
        final ListView list = findViewById(R.id.answers);
        List<String> answersList = saveSystem.getAnswersList();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.scenario_adapter, R.id.choiceText, answersList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                // Add user's answer to list and set arrayadapter non-clickable.
                addUserAnswerToList(colors.get(position));
                list.setEnabled(false);

                answeredImageView = view.findViewById(R.id.choiceBg);
                answeredImageView.setImageResource(getResources()
                        .getIdentifier(String.valueOf(colors.get(position)),
                                "drawable", getApplicationContext().
                                        getPackageName()));

                String previousCharacter = saveSystem.getPersonPicture();
                String previousFace = saveSystem.getFacePicture();
                String clickedItem = (String) list.getItemAtPosition(position);
                saveSystem.nextScene(clickedItem);
                checkPersonTransition(previousCharacter, previousFace);

                // Pause 1 second, then do the run-method.
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Set default background-color for button.
                        answeredImageView.setImageResource(R.drawable.button_default);

                        // If the question was last from the scenario, go to GameOverActivity
                        // and send user's answers. Else update the questions in arrayadapter.
                        if (saveSystem.endOfScenario()) {
                            scenarioFinished();
                        } else {
                            colors = saveSystem.getColorsInString();
                            questionTextView.setText(saveSystem.getQuestionFromScenario());
                            arrayAdapter.clear();
                            arrayAdapter.addAll(saveSystem.getAnswersList());
                            arrayAdapter.notifyDataSetChanged();
                            updateImages();
                            list.setEnabled(true);
                        }
                    }
                }, 1000);
            }
        });
    }

    private void scenarioFinished() {
        Intent intent = new Intent(getApplicationContext(), GameOverActivity.class);
        intent.putExtra("scenario", saveSystem.getCurrentScenario());
        intent.putExtra("scenarioID", scenarioID);
        intent.putExtra("userAnswers", userAnswers);
        startActivity(intent);
    }

    /**
     * Add 100% if player answered correctly, 50% if answer was "acceptable" and
     * 0% if answer was wrong.
     * @param color color of the answer
     */
    private void addUserAnswerToList(String color) {
        switch (color) {
            case "green":
                userAnswers.add(CORRECT);
                break;
            case "yellow":
                userAnswers.add(SEMICORRECT);
                break;
            case "red":
                userAnswers.add(WRONG);
                break;
        }
    }

    /**
     * Update images from json-file.
     */
    private void updateImages() {
        background.setImageResource(getResources()
                .getIdentifier(saveSystem.getBackgroundPicture(),
        "drawable", getApplicationContext()
                .getPackageName()));

        if (saveSystem.getPersonPicture().equals("null")) {
            character.setImageResource(android.R.color.transparent);
            face.setImageResource(android.R.color.transparent);
        } else {
            character.setImageResource(getResources()
                    .getIdentifier(saveSystem.getPersonPicture(),
            "drawable", getApplicationContext()
                    .getPackageName()));
            face.setImageResource(getResources()
                    .getIdentifier(saveSystem.getFacePicture(),
            "drawable", getApplicationContext()
                    .getPackageName()));
        }
    }

    private void checkPersonTransition(String previousCharacter, String previousFace) {
        try {
            if (saveSystem.getPersonPicture() != null) {
                if (!previousCharacter.equals(saveSystem.getPersonPicture())) {
                    // from "null" to "character_"
                    if (previousCharacter.equals("null") && !saveSystem.getPersonPicture().equals("null")) {
                        characterStart = emptyDrawable;
                        characterEnd = ContextCompat.getDrawable(getApplicationContext(), getResources()
                                .getIdentifier(saveSystem.getPersonPicture(),
                                        "drawable", getApplicationContext()
                                                .getPackageName()));
                    // from "character_" to "null"
                    } else if (saveSystem.getPersonPicture().equals("null") && !previousCharacter.equals("null")) {
                        characterStart = ContextCompat.getDrawable(getApplicationContext(), getResources()
                                .getIdentifier(previousCharacter,
                                        "drawable", getApplicationContext()
                                                .getPackageName()));
                        characterEnd = emptyDrawable;
                        Debug.print("SCENARIO", "CHECKPERSON", "CHAR TO NULL", 1);
                    // from "character_" to "character_"
                    } else {
                        characterStart = ContextCompat.getDrawable(getApplicationContext(), getResources()
                                .getIdentifier(previousCharacter,
                                        "drawable", getApplicationContext()
                                                .getPackageName()));
                        characterEnd = ContextCompat.getDrawable(getApplicationContext(), getResources()
                                .getIdentifier(saveSystem.getPersonPicture(),
                                        "drawable", getApplicationContext()
                                                .getPackageName()));
                    }

                    Drawable[] characterDrawables = new Drawable[2];
                    characterDrawables[0] = characterStart;
                    characterDrawables[1] = characterEnd;
                    TransitionDrawable transitionDrawable = new TransitionDrawable(characterDrawables);
                    transitionDrawable.setCrossFadeEnabled(true);
                    character.setImageDrawable(transitionDrawable);
                    transitionDrawable.startTransition(1000);
                    checkFaceTransition(previousFace);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkFaceTransition(String previousFace) {
        try {
            // from "null" to "face_"
            if (previousFace.equals("null") && !saveSystem.getFacePicture().equals("null")) {
                faceStart = emptyDrawable;
                faceEnd = ContextCompat.getDrawable(getApplicationContext(), getResources()
                        .getIdentifier(saveSystem.getFacePicture(),
                                "drawable", getApplicationContext()
                                        .getPackageName()));
                // from "face_" to "null"
            } else if (saveSystem.getFacePicture().equals("null") && !previousFace.equals("null")) {
                faceStart = ContextCompat.getDrawable(getApplicationContext(), getResources()
                        .getIdentifier(previousFace,
                                "drawable", getApplicationContext()
                                        .getPackageName()));
                faceEnd = emptyDrawable;
                // from "face_" to "face_"
            } else {
                faceStart = ContextCompat.getDrawable(getApplicationContext(), getResources()
                        .getIdentifier(previousFace,
                                "drawable", getApplicationContext()
                                        .getPackageName()));
                faceEnd = ContextCompat.getDrawable(getApplicationContext(), getResources()
                        .getIdentifier(saveSystem.getFacePicture(),
                                "drawable", getApplicationContext()
                                        .getPackageName()));
            }

            Drawable[] faceDrawables = new Drawable[2];
            faceDrawables[0] = faceStart;
            faceDrawables[1] = faceEnd;
            TransitionDrawable faceTransition = new TransitionDrawable(faceDrawables);
            faceTransition.setCrossFadeEnabled(true);
            face.setImageDrawable(faceTransition);
            faceTransition.startTransition(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
