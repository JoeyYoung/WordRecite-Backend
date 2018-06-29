package tss.responses.information;

import org.jetbrains.annotations.Nls;

public class deleteTypeResponse {
    @Nls
    private final String status;

    public deleteTypeResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
