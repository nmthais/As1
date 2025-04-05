package assignment1;

public class EnrollMessage {
    private String enrollMessage;

    EnrollMessage(){
        this.enrollMessage="";
    }
   
    public void setEnrollMessage(String enrollMessage) {
        this.enrollMessage += "<br/>" + enrollMessage ;
    }
    public String getEnrollMessage() {
        return enrollMessage;
    }
    
}
