package hello.java.designpattern.builder;

public class ComputerDirector {
    public Computer constructComputer(ComputerBuilder computerBuilder) {

        computerBuilder.buildemory();
        computerBuilder.buildpu();
        computerBuilder.buildDisk();
        return computerBuilder.buildComputer();
    }
}
