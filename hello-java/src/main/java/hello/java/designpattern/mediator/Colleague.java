package hello.java.designpattern.mediator;
public abstract  class Colleague {
    protected Mediator mediator;
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }
    public abstract boolean operation(String message);
}
