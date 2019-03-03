package e.mamtanegi.vehicledetection.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CustomerModel {


    public String userId;
    public String username;
    public String email;
    public String phoneno;
    public String password;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public CustomerModel() {
    }

    public static void writeUsers(String email, String username, String phoneno, String password) {
        CustomerModel customerModel = new CustomerModel();
        customerModel.setEmail(email);
        customerModel.setUsername(username);
        customerModel.setPhoneno(phoneno);
        customerModel.setPassword(password);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("SignupUsers");
        customerModel.setUserId(mDatabase.push().getKey());
        mDatabase.child(customerModel.getUserId()).setValue(customerModel);
        String v = "";


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
