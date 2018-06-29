package tss.responses.information;

import org.jetbrains.annotations.Nls;

public class unOrderBookResponse {
    @Nls
    private final String status;

    public unOrderBookResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
