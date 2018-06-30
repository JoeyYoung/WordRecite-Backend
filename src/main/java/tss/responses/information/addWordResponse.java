package tss.responses.information;

import org.jetbrains.annotations.Nls;

public class addWordResponse {
    @Nls
    private final String status;

    private final String chinese;
    private final String english;

    public addWordResponse(String status, String chinese, String english) {
        this.status = status;
        this.chinese = chinese;
        this.english = english;
    }

    public String getStatus() {
        return status;
    }

    public String getChinese() {
        return chinese;
    }

    public String getEnglish() {
        return english;
    }
}
