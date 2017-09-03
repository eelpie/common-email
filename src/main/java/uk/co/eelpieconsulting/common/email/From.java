package uk.co.eelpieconsulting.common.email;

public class From {

    final private String address;
    final private String name;

    public From(String address) {
        this.address = address;
        this.name = null;
    }

    public From(String address, String name) {
        this.address = address;
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "From{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
