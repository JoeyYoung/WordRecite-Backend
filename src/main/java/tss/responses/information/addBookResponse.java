package tss.responses.information;

import org.jetbrains.annotations.Nls;

public class addBookResponse {
    @Nls
    private final String status;

    private final String bid;

    private final String bname;

    public addBookResponse(String status, String bid, String bname) {
        this.status = status;
        this.bid = bid;
        this.bname = bname;
    }

    public String getStatus() {
        return status;
    }

    public String getBid() {
        return bid;
    }

    public String getBname() {
        return bname;
    }
}
