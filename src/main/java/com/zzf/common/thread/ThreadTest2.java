package com.zzf.common.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest2 {
	private int j;
    private Lock lock = new ReentrantLock();
    public static void main(String[] args) {
         ThreadTest2 tt = new ThreadTest2();
         for(int i=0;i<2;i++){
             new Thread(tt.new Adder()).start();
             new Thread(tt.new Subtractor()).start();
         }
    }

    private class Subtractor implements Runnable{
         public void run() {
             while(true){
                  /*synchronized (ThreadTest.this) {              
                       System.out.println("j--=" + j--);
                       //这里抛异常了，锁能释放吗？
                  }*/
                  lock.lock();
                  try{
                       System.out.println("j--=" + j--);
                  }finally{
                       lock.unlock();
                  }
             }
         }
    }

    private class Adder implements Runnable{
         public void run() {
             while(true) {
                  /*synchronized (ThreadTest.this) {
                  System.out.println("j++=" + j++); 
                  }*/
                  lock.lock();
                  try{
                       System.out.println("j++=" + j++);
                  }finally{
                       lock.unlock();
                  }                  
             }             
         }
    }

}
