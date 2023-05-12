package sbu.cs.CalculatePi;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PiCalculator {

    // Using Pi - Machin identity
    class CalculateTask implements Runnable {
        int start;
        int end;
        BigDecimal result;

        public CalculateTask(int start, int end) {
            this.start = start;
            this.end = end;
            result = new BigDecimal(0);
        }

        @Override
        public void run() {
            for(int i = start; i < end; i++) {
                
                BigDecimal firstTerm = BigDecimal.ZERO;
                BigDecimal secondTerm = BigDecimal.ZERO;

                BigDecimal firstDen = new BigDecimal(5);
                firstDen = firstDen.pow(2*i+1);
                BigDecimal firstCoeficient = new BigDecimal((2*i+1)*Math.pow(-1, i));
                firstDen = firstDen.multiply(firstCoeficient);

                firstTerm = (BigDecimal.ONE).divide(firstDen, 10000, RoundingMode.HALF_EVEN);
                firstTerm = firstTerm.multiply(new BigDecimal(4));

                BigDecimal secondDen = new BigDecimal(239);
                secondDen = secondDen.pow(2*i+1);
                BigDecimal secondCoeficient = new BigDecimal((2*i+1)*Math.pow(-1, i));
                secondDen = secondDen.multiply(secondCoeficient);

                secondTerm = (BigDecimal.ONE).divide(secondDen, 10000, RoundingMode.HALF_EVEN);

                BigDecimal finalTerm = firstTerm.subtract(secondTerm);

                result = result.add(finalTerm);
            }
        }

        public BigDecimal getResult(){
            return result;
        }
    }

    /**
     * Calculate pi and represent it as a BigDecimal object with the given floating point number (digits after . )
     * There are several algorithms designed for calculating pi, it's up to you to decide which one to implement.
     Experiment with different algorithms to find accurate results.

     * You must design a multithreaded program to calculate pi. Creating a thread pool is recommended.
     * Create as many classes and threads as you need.
     * Your code must pass all of the test cases provided in the test folder.

     * @param floatingPoint the exact number of digits after the floating point
     * @return pi in string format (the string representation of the BigDecimal object)
     */

    public String calculate(int floatingPoint)
    {
        int maxThreads = 7;
        int terms = 1000;
        int maxValuePerThread = terms / maxThreads;
        int start = 0;
        int end = maxValuePerThread;
        BigDecimal result = new BigDecimal(0);
        ExecutorService pool = Executors.newFixedThreadPool(maxThreads);
        ArrayList<CalculateTask> tasks = new ArrayList<CalculateTask>();
        for(int i = 0; i < maxThreads-1; i++) {
            CalculateTask task = new CalculateTask(start, end);
            tasks.add(task);
            pool.execute(task);
            start = end;
            end = end + maxValuePerThread;
        }

        // Last thread does all the left overs
        end = terms;
        CalculateTask lastTask = new CalculateTask(start, end);
        tasks.add(lastTask);
        pool.execute(lastTask);

        // Finishing all tasks

        pool.shutdown();

        try {
            pool.awaitTermination(10, TimeUnit.SECONDS);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        for(CalculateTask task: tasks) {
            result = result.add(task.getResult());
        }

        result = result.multiply(new BigDecimal(4));

        result = result.setScale(floatingPoint, RoundingMode.DOWN);

        return result.toString();
    }
}
