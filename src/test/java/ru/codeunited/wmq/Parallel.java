package ru.codeunited.wmq;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 06.02.15.
 */
public class Parallel {

    private List<Branch> branches = new ArrayList<>(2);

    public static abstract class Branch implements Runnable {

        public long delay = 0;

        public Branch(long delay) {
            this.delay = delay;
        }

        public Branch() {

        }

        protected abstract void perform() throws Exception;

        @Override
        public void run() {
            try {
                Thread.sleep(delay);
                perform();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void add(Branch branch) {
        branches.add(branch);
    }

    public void cleanUp() {
        branches.clear();
    }

    public void go() throws ExecutionException, InterruptedException {
        final ExecutorService service = Executors.newFixedThreadPool(branches.size());
        final List<Future> futures = new LinkedList<>();
        for (Branch branch : branches) {
            futures.add(
                    service.submit(branch)
            );
        }
        for (Future future : futures) {
            future.get();
        }

        cleanUp();
    }

}
