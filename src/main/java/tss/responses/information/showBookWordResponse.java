package tss.responses.information;

import java.util.List;

public class showBookWordResponse {
    private final List<String> chineses;
    private final List<String> englishs;

    public showBookWordResponse(List<String> chineses, List<String> englishs) {
        this.chineses = chineses;
        this.englishs = englishs;
    }

    public List<String> getChineses() {
        return chineses;
    }

    public List<String> getEnglishs() {
        return englishs;
    }
}
