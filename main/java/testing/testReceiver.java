package testing;

import apprmq.RMQ;

import java.io.IOException;

public class testReceiver {
    public static void main(String[] args) throws IOException, InterruptedException {
        RMQ r = new RMQ();
        RMQ.Receiver recv = r.getReceiver();
        while (true) {
            Thread.sleep(1000);
            recv.receive();
        }

    }
}
