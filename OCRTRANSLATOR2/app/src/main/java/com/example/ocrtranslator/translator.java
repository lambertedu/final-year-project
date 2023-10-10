package com.example.ocrtranslator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class translator extends AppCompatActivity {
    private EditText sourcelanguageEt;
    private EditText destinationlanguageTv;
    private MaterialButton sourcelanguagechosebtn;
    private MaterialButton destinationlanguagechosebtn;
    private MaterialButton translatebtn;

    private TranslatorOptions translatorOptions;

    private Translator translator;

    private ProgressDialog progressDialog;

    private ArrayList<ModelLanguage> languageArrayList;

    private static final String TAG = "MAIN_TAG";

    private String sourceLanguageCode = "en";
    private String sourceLanguageTitle = "English";
    private String destinationLanguagueCode = "fr";
    private String destinationLanguagueTitle = "French";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);
        sourcelanguageEt = findViewById(R.id.sourcelanguageEt);
        destinationlanguageTv = findViewById(R.id.destinationlanguageTv);
        sourcelanguagechosebtn = findViewById(R.id.sourcelanguagechosebtn);
        destinationlanguagechosebtn = findViewById(R.id.destinationlanguagechosebtn);
        translatebtn = findViewById(R.id.translatebtn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);


        loadAvailablelanguages();


//Handle source language
        sourcelanguagechosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sourceLanguageChose();

            }
        });
        //Handle destination language
        destinationlanguagechosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destinationLanguageChose();

            }
        });
        //Handle translation
        translatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();

            }
        });

    }

    //source languages code
    private String sourceLanguageText ="";
    private void validateData() {
        sourceLanguageText = sourcelanguageEt.getText().toString().trim();
        Log.d(TAG, "validateData: sourceLanguageText: "+sourceLanguageText);

        if (sourceLanguageText.isEmpty()){
            Toast.makeText(this, "Enter text to translate", Toast.LENGTH_SHORT).show();
        }
        else {
            startTranslations();
        }
    }

    //download language model
    private void startTranslations() {
        progressDialog.setMessage("Processing language model...");
        progressDialog.show();

        translatorOptions = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguageCode)
                .setTargetLanguage(destinationLanguagueCode)
                .build();
        translator = Translation.getClient(translatorOptions);


        // network condition
        DownloadConditions downloadConditions = new DownloadConditions.Builder()
                .build();

        //translating
        translator.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: model ready, starting translating...");
                        progressDialog.setMessage("Translating....");

                        translator.translate(sourceLanguageText)

                                //if translation is successful
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String translatedText) {
                                        Log.d(TAG, "onSuccess: translatedText: "+translatedText);
                                        progressDialog.dismiss();
                                        destinationlanguageTv.setText(translatedText);

                                    }
                                })

                                //if translation fail
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Log.d(TAG, "onFailure: ",e);
                                        Toast.makeText(translator.this, "Failed to translate due to "+e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }
                })

                //if the language model fail to download
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onFailure: ",e);
                        Toast.makeText(translator.this, "Failed to ready model due to "+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    //popup menu to select source language
    private void sourceLanguageChose(){

        PopupMenu popupMenu = new PopupMenu(this,sourcelanguagechosebtn);

        for (int i=0; i<languageArrayList.size(); i++){
            popupMenu.getMenu().add(Menu.NONE,i,i, languageArrayList.get(i).languageTitle);
        }
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int position = item.getItemId();

                sourceLanguageCode = languageArrayList.get(position).languageCode;
                sourceLanguageTitle = languageArrayList.get(position).languageTitle;

                sourcelanguagechosebtn.setText(sourceLanguageTitle);
                sourcelanguageEt.setHint("Enter "+sourceLanguageTitle);

                //show in logs
                Log.d(TAG, "onMenuItemClick: sourceLanguageCode: "+sourceLanguageCode);
                Log.d(TAG, "onMenuItemClick: sourceLanguageTitle: "+sourceLanguageTitle);

                return false;
            }
        });

    }

    //popup menu to select destination language
    private void destinationLanguageChose(){

        PopupMenu popupMenu = new PopupMenu(this,destinationlanguagechosebtn);

        for (int i=0; i<languageArrayList.size();i++){

            popupMenu.getMenu().add(Menu.NONE, i, i, languageArrayList.get(i).getLanguageTitle());

        }
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int position = item.getItemId();

                destinationLanguagueCode = languageArrayList.get(position).languageCode;
                destinationLanguagueTitle = languageArrayList.get(position).languageTitle;

                destinationlanguagechosebtn.setText(destinationLanguagueTitle);

                Log.d(TAG, "onMenuItemClick: destinationLanguageCode: "+destinationLanguagueCode);
                Log.d(TAG, "onMenuItemClick: destinationLanguageTitle: "+destinationLanguagueTitle);

                return false;
            }
        });
    }

    //load all available languages
    private void loadAvailablelanguages() {
        languageArrayList = new ArrayList<>();

        List<String > languageCodeList = TranslateLanguage.getAllLanguages();
        for (String languageCode: languageCodeList){
            String languageTitle = new Locale(languageCode).getDisplayLanguage();

            Log.d(TAG, "loadAvailablelanguages: "+languageCode);
            Log.d(TAG, "loadAvailablelanguages: "+languageTitle);

            ModelLanguage modelLanguage = new ModelLanguage(languageCode,languageTitle);
            languageArrayList.add(modelLanguage);
        }
    }
}