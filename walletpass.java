package com.example.login;

public class walletpass {
    private String amount,dt;

    public walletpass() {}
    public walletpass(String Amount,String DTIME) {
        this.amount = Amount;
        this.dt=DTIME;
    }

    public String getAmount() {
        return amount;

    }
    public void setAmount(String Amount){
        this.amount=Amount;
    }
    public String getdt() {
        return dt;

    }
    public void setDt(String DTIME){
        this.dt=DTIME;
    }
}

