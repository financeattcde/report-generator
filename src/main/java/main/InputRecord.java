package main;

public class InputRecord {

    private String donorName;

    String getDonationAmt() {
        return "$"+donationAmt;
    }

    public void setDonationAmt(String donationAmt) {
        this.donationAmt = donationAmt;
    }

    private String donationAmt;

    String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    InputRecord(String donorName, String donationAmt){
        this.donationAmt = donationAmt;
        this.donorName  = donorName;
    }
}
