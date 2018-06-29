package tss.responses.information;

import java.util.List;

public class showAllBookResponse {
    private final List<String> names;
    private final List<String> words;
    private final List<String> types;
    private final List<String> descrs;
    private final List<String> dates;

    public showAllBookResponse(List<String> names, List<String> words, List<String> types, List<String> descrs, List<String> dates) {
        this.names = names;
        this.words = words;
        this.types = types;
        this.descrs = descrs;
        this.dates = dates;
    }

    public List<String> getNames() {
        return names;
    }

    public List<String> getWords() {
        return words;
    }

    public List<String> getTypes() {
        return types;
    }

    public List<String> getDescrs() {
        return descrs;
    }

    public List<String> getDates() {
        return dates;
    }
}
