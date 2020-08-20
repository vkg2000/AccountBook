
package tradersapp;

/**
 *
 * @author VISHAL KUMAR GUPTA
 * vkg10@iitbbs.ac.in
 */
public class CustomerBean {
    private int id;
    private String name;
    private String mobile;
    private String Address;
    private byte[] photo;
    
    public CustomerBean (int id, String name ,String mobile, String Address, byte[] photo ){
   
        this.id=id;
        this.name=name;
        this.mobile=mobile;
        this.Address=Address;
        this.photo=photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
    
    
}



