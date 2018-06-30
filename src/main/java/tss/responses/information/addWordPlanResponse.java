package tss.responses.information;

import org.jetbrains.annotations.Nls;

public class addWordPlanResponse {
    @Nls
    private final String status;
    private final String wid;
    private final String chinese;
    private final String english;

    public addWordPlanResponse(String status, String wid, String chinese, String english) {
        this.status = status;
        this.wid = wid;
        this.chinese = chinese;
        this.english = english;
    }

    public String getStatus() {
        return status;
    }

    public String getWid() {
        return wid;
    }

    public String getChinese() {
        return chinese;
    }

    public String getEnglish() {
        return english;
    }
}
