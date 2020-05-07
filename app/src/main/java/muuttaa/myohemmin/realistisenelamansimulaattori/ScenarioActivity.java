package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import muuttaa.myohemmin.realistisenelamansimulaattori.data.SaveSystemPreferences;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.GlobalPrefs;

public class ScenarioActivity extends ParentActivity {
    private String scenario;
    private SaveSystemPreferences saveSystem;
    private TextView questionTextView;
    private List<String> colors;
    private ArrayList<Integer> userAnswers;
    private final int CORRECT = 100;
    private final int WRONG = 0;
    private final int SEMICORRECT = 50;
    private ImageView background, character, face, fore;
    private ImageView answeredImageView;
    private RelativeLayout scenarioLayout;
    private ListView list;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load font theme
        getTheme().applyStyle(GlobalPrefs.loadFontStyle().getResId(), true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario);
        userAnswers = new ArrayList<>();
        scenarioLayout = findViewById(R.id.scenarioLayout);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            scenario = extras.getString("scenario");
            setupStart();
            updateImages();
            setupAnswers();
        }
    }

    /**
     * Called when answer is clicked.
     */
    private void buttonClickedAnimation() {
        try {
            scenarioLayout.animate().translationX(0 - scenarioLayout.getWidth()).setDuration(600)
                    .withEndAction(this::afterButtonClickedAnimation).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Called, when buttons have moved outside of the screen.
     */
    private void afterButtonClickedAnimation() {
        // Set default background-color for button.
        answeredImageView.setImageResource(R.drawable.button_default);
        scenarioLayout.setX(scenarioLayout.getWidth());

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
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                scenarioLayout.animate().translationX(0).setDuration(600).start();
                list.setEnabled(true);
            }, 50);
        }
    }

    /**
     * Define variables.
     */
    private void setupStart() {
        saveSystem = new SaveSystemPreferences(this);
        saveSystem.setCurrentScenario(scenario);
        questionTextView = findViewById(R.id.question);
        questionTextView.setText(saveSystem.getQuestionFromScenario());
        colors = saveSystem.getColorsInString();
        background = findViewById(R.id.scenarioBackground);
        character = findViewById(R.id.scenarioCharacter);
        face = findViewById(R.id.scenarioFace);
        fore = findViewById(R.id.scenarioFore);
        loadSounds(R.raw.correct, R.raw.wrong);
    }

    /**
     * Setup answers in listview.
     */
    private void setupAnswers() {
        list = findViewById(R.id.answers);
        List<String> answersList = saveSystem.getAnswersList();
        arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.scenario_adapter, R.id.choiceText, answersList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            // Add user's answer to list and set arrayadapter non-clickable.
            addUserAnswerToList(colors.get(position));
            list.setEnabled(false);

            answeredImageView = view.findViewById(R.id.choiceBg);
            answeredImageView.setImageResource(getResources()
                    .getIdentifier(String.valueOf(colors.get(position)),
                            "drawable", getApplicationContext().
                                    getPackageName()));

            String clickedItem = (String) list.getItemAtPosition(position);
            saveSystem.nextScene(clickedItem);

            // Pause 1 second, then do the run-method.
            Handler handler = new Handler();
            handler.postDelayed(this::buttonClickedAnimation, 500);
        });
    }

    /**
     * Called, after last scene.
     */
    private void scenarioFinished() {
        Intent intent = new Intent(getApplicationContext(), GameOverActivity.class);
        intent.putExtra("scenario", saveSystem.getCurrentScenario());
        intent.putExtra("userAnswers", userAnswers);
        startActivity(intent);
        finish();
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
                playSound(R.raw.correct);
                break;
            case "yellow":
                userAnswers.add(SEMICORRECT);
                playSound(R.raw.correct);
                break;
            case "red":
                userAnswers.add(WRONG);
                playSound(R.raw.wrong);
                break;
        }
    }

    /**
     * Update images from json-file.
     */
    private void updateImages() {
        // If this is null, then scene name is wrong
        if (saveSystem.getBackgroundPicture() == null) {
            showSceneNullError();
            return;
        }

        background.setImageResource(getResources()
                .getIdentifier(saveSystem.getBackgroundPicture(),
        "drawable", getApplicationContext()
                .getPackageName()));

        if (saveSystem.getForegroundPicture().equals("null")) {
            fore.setImageResource(android.R.color.transparent);
        } else {
            fore.setImageResource(getResources()
                    .getIdentifier(saveSystem.getForegroundPicture(),
                            "drawable", getApplicationContext()
                                    .getPackageName()));
        }

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

    /**
     * Shows scene is null error in ChooseScenarioActivity.
     */
    private void showSceneNullError() {
        Intent i = new Intent(this, ChooseScenarioActivity.class);
        i.putExtra("null_scene", true);
        startActivity(i);
        finish();
    }
}
