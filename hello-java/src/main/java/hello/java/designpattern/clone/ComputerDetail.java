package hello.java.designpattern.clone;

public class ComputerDetail implements Cloneable {
    private String cpu;
    private String memory;
    private Disk disk;
    public ComputerDetail(String cpu, String memory, Disk disk) {
        this.cpu = cpu;
        this.memory = memory;
        this.disk = disk;
    }

    public Object clone() {
        try {
            ComputerDetail computerDetail =  (ComputerDetail)super.clone();
            computerDetail.disk = (Disk) this.disk.clone();
            return computerDetail;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "ComputerDetail{" +
                "cpu='" + cpu + '\'' +
                ", memory='" + memory + '\'' +
                ", disk=" + disk.toString() +
                '}';
    }
}
