package e.mamtanegi.vehicledetection.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverModel {

    public String userId;
    public String username;
    public String email;
    public String phoneno;
    public String password;
    public String idProof;
    public String vehicleRegistrationNo;
    public String vehicleType;
    public String license;

    public DriverModel() {
    }

    public static void writeDriverDetails(String email, String username, String phoneno, String password, String idProof, String vehicleRegistrationNo, String vehicleType, String license) {
        DriverModel driverModel = new DriverModel();
        driverModel.setEmail(email);
        driverModel.setUsername(username);
        driverModel.setPhoneno(phoneno);
        driverModel.setPassword(password);
        driverModel.setIdProof(idProof);
        driverModel.setVehicleRegistrationNo(vehicleRegistrationNo);
        driverModel.setVehicleType(vehicleType);
        driverModel.setLicense(license);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("SignupOwners");
        driverModel.setUserId(mDatabase.push().getKey());
        mDatabase.child(driverModel.getUserId()).setValue(driverModel);
        String v = "";


    }

    public String getIdProof() {
        return idProof;
    }

    public void setIdProof(String idProof) {
        this.idProof = idProof;
    }

    public String getVehicleRegistrationNo() {
        return vehicleRegistrationNo;
    }

    public void setVehicleRegistrationNo(String vehicleRegistrationNo) {
        this.vehicleRegistrationNo = vehicleRegistrationNo;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}


