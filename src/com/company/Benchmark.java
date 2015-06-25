package com.company;
import java.util.Timer;
import java.util.TimerTask;

public class Benchmark {


    private long tickTime=-1;
    private long ticks=0;
    private long summaryTime=0;
    private boolean isCountingNow=false;
    private String name;

    public Benchmark(String name, boolean enableContinuousLogging) {
        this.name=name;
        if (enableContinuousLogging) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    printLog();
                    ticks = 0;
                    summaryTime = 0;
                }
            }, 0, 10000);
        }
    }

    private String longToString(long l) {
        String subres= String.valueOf(l);
        StringBuilder sb=new StringBuilder();
        for (int i=0; i<subres.length(); i++) {
            sb.append(subres.charAt(i));
            if ((subres.length()-i)%3==1 && i+1!=subres.length()) {
                sb.append('.');
            }
        }
        return sb.toString();
    }

    public void printLog() {
        if (ticks == 0) {
            System.out.println("[Bench] "+name + ": no usages");
        } else {
            System.out.println("[Bench] " + name + ": " + longToString(summaryTime / ticks) +
                    " avg nanos per call, " + (ticks * 1000000000l / summaryTime) +
                    " cps, " + ticks + " usages, " + longToString(summaryTime) + " total time");
        }
    }

    public void tick() {
        if (!isCountingNow) {
            tickTime= System.nanoTime();
            isCountingNow=true;
        } else {
            new Exception("[Bench] Tick called in non - closed benchmark").printStackTrace();
        }
    }

    public void tack() {
        if (isCountingNow) {
            summaryTime+= System.nanoTime()-tickTime;
            ticks++;
            isCountingNow=false;

        } else {
            new Exception("[Bench] Tack called in closed benchmark").printStackTrace();
        }
    }

}
