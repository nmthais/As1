package assignment1;

public class EnrollMessage {
    private boolean isEnroll;
    private String enrollMessage;

    EnrollMessage(){

    }
    public void setisEnroll(boolean isEnroll) {
        this.isEnroll = isEnroll;
    }
    public void setEnrollMessage(String enrollMessage) {
        this.enrollMessage = enrollMessage;
    }
    public String getEnrollMessage() {
        return enrollMessage;
    }
    public boolean getisEnroll() {
        return isEnroll;
    }
}
