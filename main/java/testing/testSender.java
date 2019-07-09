package testing;
import apprmq.*;

import java.io.IOException;

public class testSender {
    public static void main(String[] args) throws IOException, InterruptedException {
        RMQ r = new RMQ();
        r.addExchange("Ex1","direct");
        r.setRoutingKey("rk");
        RMQ.Sender sender = r.getSender();
        while (true) {

            sender.sendMessage("1");
            Thread.sleep(1000);
        }
    }
}

