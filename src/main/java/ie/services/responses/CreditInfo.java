package ie.services.responses;

public class CreditInfo {
    private long credit;
    public CreditInfo(long credit){
        this.credit = credit;
    }

    public long getCredit() {
        return credit;
    }

    public void setCredit(long credit) {
        this.credit = credit;
    }
}
