package hello.java.designpattern.state;

public class Context {
    private AbstractState state;

    public Context(AbstractState state){
        this.state = state;
    }

    public void setState(AbstractState state){
        this.state = state;
    }

    public AbstractState getState(){
        return state;
    }
    public void action()
    {
       this.state.action(this);
    }
}
