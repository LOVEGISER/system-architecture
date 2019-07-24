package com.alex.vertx;

import io.vertx.core.AbstractVerticle;

/**
 * url :https://vertxchina.github.io/vertx-translation-chinese/start/FAQ.html
 *
 * 怎样正确理解Vert.x机制？
 * 答：Vert.x其实就是建立了一个Verticle内部的线程安全机制，让用户可以排除多线程并发冲突的干扰，专注于业务逻辑上的实现，用了Vert.x，您就不用操心多线程和并发的问题了。Verticle内部代码，除非声明Verticle是Worker Verticle，否则Verticle内部环境全部都是线程安全的，不会出现多个线程同时访问同一个Verticle内部代码的情况。
 *
 * 请注意：一般情况下，用了Vert.x的Verticle之后，原则上synchronized，Lock，volatile，static对象，java.util.concurrent, HashTable, Vector, Thread, Runnable, Callable, Executor, Task, ExecutorService等这些并发和线程相关的东西就不再需要使用了，可以由Verticle全面接管，如果您不得不在Vert.x代码中使用上述内容，则多少暗示着您的设计或者使用Vert.x的姿势出现了问题，建议再斟酌商榷一下。
 *
 * 问：Verticle对象和处理器（Handler）是什么关系？Vert.x如何保证Verticle内部线程安全？
 * 答：Verticle对象往往包含有一个或者多个处理器（Handler），在Java代码中，后者经常是以Lambda也就是匿名函数的形式出现，比如：
 *
 * vertx.createHttpServer().requestHandler(req->{
 *     //blablabla
 * }).listen(8080);
 * Java中，Lambda本身也是一个对象，是一个@FunctionalInterface的对象，Verticle对象中包含了一个或者多个处理器（Handler）对象，比如上述例子中MyVerticle中就包含有两个handler。在Vert.x中，完成Verticle的部署之后，真正调用处理逻辑的入口往往是处理器（Handler），Vert.x保证同一个普通Verticle（也就是EventLoop Verticle，非Worker Verticle）内部的所有处理器（Handler）都只会由同一个EventLoop线程调用，由此保证Verticle内部的线程安全。所以我们可以放心地在Verticle内部声明各种线程不安全的属性变量，并在handler中分享他们，比如：
 *访问 http://localhost:8080 就会使计数器加1，访问 http://localhost:8081 将会看到具体的计数。同理，也可以将i替换成HashMap等线程不安全对象，不需要使用ConcurrentHashMap或HashTable，可在Verticle内部安全使用。
 *
 * Vert.x的Handler内部是atomic/原子操作，Verticle内部是thread safe/线程安全的，Verticle之间传递的数据是immutable/不可改变的。
 *
 * 一个vert.x实例/进程内有多个Eventloop和Worker线程，每个线程会部署多个Verticle对象并对应执行Verticle内的Handler，每个Verticle内有多个Handler，普通Verticle会跟Eventloop绑定，而Worker Verticle对象则会被Worker线程所共享，会依次顺序访问，但不会并发同时访问，如果声明为Multiple Threaded Worker Verticle则没有此限制，需要开发者手工处理并发冲突，我们并不推荐这类操作。
 *
 */
public class VerticleThreadSafe extends AbstractVerticle {
    int i = 0;//属性变量

    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(req->{
            i++;
        req.response().end();//要关闭请求，否则连接很快会被占满
        }).listen(8080);

        vertx.createHttpServer().requestHandler(req->{
            System.out.println(i);
        req.response().end(""+i);
        }).listen(8081);
    }
}