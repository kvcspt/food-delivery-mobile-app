package hu.kvcspt.mobilalkfejl_kotprog.models;

public class User {
    private String uid;
    private String email;
    private String name;
    private String phoneNumber;
    private String address;

    public User(String uid, String email, String name, String phoneNumber, String address) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
