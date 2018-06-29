package tss.responses.information;

import org.jetbrains.annotations.Nls;

public class addTypeResponse {
    @Nls
    private final String status;

    private final String id;
    private final String name;

    public addTypeResponse(String status, String id, String name) {
        this.status = status;
        this.id = id;
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
