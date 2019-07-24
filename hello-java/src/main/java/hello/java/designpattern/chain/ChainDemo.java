package hello.java.designpattern.chain;

import java.util.logging.Handler;

public class ChainDemo {
    public static void main(String[] args) {
        AuthHandler  authHandler= new AuthHandler("auth");
        BusinessHandler  businessHandler= new BusinessHandler("business");
        ResponseHandler  responseHandler= new ResponseHandler("response");
        authHandler.setHandler(businessHandler);
        businessHandler.setHandler(responseHandler);
        authHandler.operator();
    }
}
