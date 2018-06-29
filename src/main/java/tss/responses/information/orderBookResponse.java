package tss.responses.information;

import org.jetbrains.annotations.Nls;

public class orderBookResponse {
    @Nls
    private final String status;

    private final String userName;
    private final String bookName;

    public orderBookResponse(String status, String userName, String bookName) {
        this.status = status;
        this.userName = userName;
        this.bookName = bookName;
    }

    public String getStatus() {
        return status;
    }

    public String getUserName() {
        return userName;
    }

    public String getBookName() {
        return bookName;
    }
}
