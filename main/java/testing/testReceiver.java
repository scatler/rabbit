package testing;

import apprmq.RMQ;

import java.io.IOException;

public class testReceiver {
    public static void main(String[] args) throws IOException, InterruptedException {
        RMQ r = new RMQ();
        r.addExchange("Ex1","direct");
        r.addQueue("q1",false,false,false);
        r.bindCurrent("rk");
        RMQ.Receiver recv = r.getReceiver();
        while (true) {
            Thread.sleep(1000);
            recv.receive();
        }

    }
}
