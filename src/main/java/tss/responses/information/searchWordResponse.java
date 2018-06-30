package tss.responses.information;

import org.jetbrains.annotations.Nls;

import java.util.List;

public class searchWordResponse {
    @Nls
    private final String status;

    private final List<String> chineses;
    private final List<String> englishs;
    private final List<String> books;
    private final List<String> types;

    public searchWordResponse(String status, List<String> chineses, List<String> englishs, List<String> books, List<String> types) {
        this.status = status;
        this.chineses = chineses;
        this.englishs = englishs;
        this.books = books;
        this.types = types;
    }

    public String getStatus() {
        return status;
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
