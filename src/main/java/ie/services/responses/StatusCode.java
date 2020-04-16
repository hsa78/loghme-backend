package ie.services.responses;

public class StatusCode {
    private int status;
    public StatusCode(int status){
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
