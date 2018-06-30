package tss.responses.information;

import org.jetbrains.annotations.Nls;

public class statusResponse {
    @Nls
    private final String status;

    public statusResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
