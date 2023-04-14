package com.example.login;

public class model {
    private String Name,MobileNumber,Gender;

    public model() {}
    public model(String name,String mobileNumber,String gender) {
        this.Name = name;
        this.MobileNumber=mobileNumber;
        this.Gender=gender;
    }

    public String getName() {
        return Name;

    }
    public void setName(String name){
        this.Name=name;
    }
    public String getMobileNumber() {
        return MobileNumber;

    }
    public void setDt(String mobileNumber){
        this.MobileNumber=mobileNumber;
    }
    public String getGender() {
        return Gender;

    }
    public void setGender(String gender){
        this.Gender=gender;
    }
}
