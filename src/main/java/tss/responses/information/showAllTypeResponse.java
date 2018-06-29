package tss.responses.information;

import java.util.List;

public class showAllTypeResponse {
    private final List<String> names;
    private final List<String> descrs;
    private final List<String> bookNum;

    public showAllTypeResponse(List<String> names, List<String> descrs, List<String> bookNum) {
        this.names = names;
        this.descrs = descrs;
        this.bookNum = bookNum;
    }

    public List<String> getNames() {
        return names;
    }

    public List<String> getDescrs() {
        return descrs;
    }

    public List<String> getBookNum() {
        return bookNum;
    }
}
