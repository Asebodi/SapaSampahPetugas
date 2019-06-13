package id.sapasampah.petugas;

public class Finished {
    private String username, fullAddr, district, city, province, postal;

    public Finished() {}

    public Finished(String username, String fullAddr, String district, String city, String province, String postal) {
        this.username = username;
        this.fullAddr = fullAddr;
        this.district = district;
        this.city = city;
        this.province = province;
        this.postal = postal;
    }

    public String getUsername() {
        return username;
    }

    public String getFullAddr() {
        return fullAddr;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getPostal() {
        return postal;
    }
}
