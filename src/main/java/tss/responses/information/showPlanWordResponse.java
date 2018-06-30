package tss.responses.information;

import java.util.List;

public class showPlanWordResponse {
    private final List<String> chineses;
    private final List<String> englishs;
    private final List<String> status;

    public showPlanWordResponse(List<String> chineses, List<String> englishs, List<String> status) {
        this.chineses = chineses;
        this.englishs = englishs;
        this.status = status;
    }

    public List<String> getChineses() {
        return chineses;
    }

    public List<String> getEnglishs() {
        return englishs;
    }

    public List<String> getStatus() {
        return status;
    }
}
