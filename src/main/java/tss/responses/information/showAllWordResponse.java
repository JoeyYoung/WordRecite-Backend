package tss.responses.information;

import java.util.List;

public class showAllWordResponse {
    private final List<String> chineses;
    private final List<String> englishs;
    private final List<String> books;
    private final List<String> types;


    public showAllWordResponse(List<String> chineses, List<String> englishs, List<String> books, List<String> types) {
        this.chineses = chineses;
        this.englishs = englishs;
        this.books = books;
        this.types = types;
    }

    public List<String> getChineses() {
        return chineses;
    }

    public List<String> getEnglishs() {
        return englishs;
    }

    public List<String> getBooks() {
        return books;
    }

    public List<String> getTypes() {
        return types;
    }
}