package e.mamtanegi.vehicledetection;

public class userlogindata{
    String Username, Createpassword1, Confirmpwd1;
    int Userphoneno1;

    public void userlogindata() {
                                     }

    public userlogindata(String username, int userphoneno, String createpassword, String confirmpwd) {

            this.Username = username;
            this.Userphoneno1 = userphoneno;
            this.Createpassword1 = createpassword;
            this.Confirmpwd1 = confirmpwd;
                                                                                                     }
        public String getUsername() {
            return Username;
        }
        public int getUserphoneno1(){
            return Userphoneno1;
        }
        public String getCreatepassword(){
            return Createpassword1;
        }
        public String getConfirmpwd(){
            return Confirmpwd1;

        }
        public void setConfirmpwd1(String Confirmpwd){
        Confirmpwd1=Confirmpwd;
        }
        public void setCreatepassword(String Createpassword){
            Createpassword1=Createpassword;
        }
        public void setUserphoneno1(int userphoneno){
        Userphoneno1=userphoneno;
        }

    public void setUsername(String username) {
        Username = username;
    }
}
