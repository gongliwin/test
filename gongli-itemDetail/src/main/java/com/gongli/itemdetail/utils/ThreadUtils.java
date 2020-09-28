package com.gongli.itemdetail.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {

    private static final ExecutorService es=Executors.newFixedThreadPool(10);


    public static void submit(Runnable runnable){
        es.submit(runnable);

    }

}
