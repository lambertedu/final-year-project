package com.example.ocrtranslator;

public class ModelLanguage {


    String languageCode;
    String languageTitle;

    public ModelLanguage(String languageCode, String languageTitle){
        this.languageCode = languageCode;
        this.languageTitle = languageTitle;
    }


    //    Getter String
    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageTitle() {
        return languageTitle;
    }

    public void setLanguageTitle(String languageTitle) {
        this.languageTitle = languageTitle;
    }

}
