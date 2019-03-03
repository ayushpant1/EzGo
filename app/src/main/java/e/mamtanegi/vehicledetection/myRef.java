package e.mamtanegi.vehicledetection;

public class myRef {
        String Username;
        int phoneno;
        String password;
        public myRef(){
        }
        public myRef(String userName, int phoneno, String password){
            this.Username=userName;
            this.phoneno=phoneno;
            this.password=password;
        }

        public String getUsername() {
            return Username;
        }

        public String getPassword() {
            return password;
        }

        public double getPhoneno() {
            return phoneno;
        }

        public void setPhoneno(int phoneno) {
            this.phoneno = phoneno;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setUsername(String userName) {
            Username = userName;
        }


}


