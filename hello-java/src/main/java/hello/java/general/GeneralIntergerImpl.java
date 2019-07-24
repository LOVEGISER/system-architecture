package hello.java.general;

import java.util.Random;

public class GeneralIntergerImpl implements IGeneral<Integer>{
    @Override
    public Integer getId() {
        Random random = new Random(100);
        return random.nextInt();
    }
    public static void main(String[] args) {
        GeneralIntergerImpl gen = new GeneralIntergerImpl();
        System.out.println(gen.getId());
    }
}
