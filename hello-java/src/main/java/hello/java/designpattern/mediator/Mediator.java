package hello.java.designpattern.mediator;

public abstract class Mediator {

        protected Colleague colleagueTenant;
        protected Colleague colleagueLandlord;

        public Mediator(Colleague colleagueTenant, Colleague colleagueLandlord) {
            this.colleagueTenant = colleagueTenant;
            this.colleagueLandlord = colleagueLandlord;
        }

        public abstract boolean notifyColleagueTenant(String message);
        public abstract boolean notifyColleagueLandlord(String message);
}


