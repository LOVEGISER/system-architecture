package hello.java.designpattern.clone;

public class Disk implements Cloneable {
    private String ssd;
    private String hhd;

    public Disk(String ssd, String hhd) {
        this.ssd = ssd;
        this.hhd = hhd;
    }

    public Object clone() {
        try {
           return   (Disk)super.clone();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "{" +
                "ssd='" + ssd + '\'' +
                ", hhd='" + hhd + '\'' +
                '}';
    }
}
