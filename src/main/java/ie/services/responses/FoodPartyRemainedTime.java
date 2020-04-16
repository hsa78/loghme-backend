package ie.services.responses;

public class FoodPartyRemainedTime {
    private int seconds;
    private int minutes;

    public FoodPartyRemainedTime(int seconds, int minutes){
        this.seconds = seconds;
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
