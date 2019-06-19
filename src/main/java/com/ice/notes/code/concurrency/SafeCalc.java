package com.ice.notes.code.concurrency;

public class SafeCalc {

    static long value = 0L;

    synchronized long get() {
        return value;
    }

    synchronized static void addOne() {
        value += 1;

    }

    private final Object balock = new Object();

    private final Object pwlock = new Object();

    private Integer balance;

    private String password;

    void withdraw(Integer amt) {
        synchronized (this.balock) {
            if(this.balance > amt) {
                balance -= amt;
            }
        }
    }

    Integer getBalance() {
        synchronized (this.balock){
            return balance;
        }
    }

    void updatePW(String pw){
        synchronized (this.pwlock) {
            this.password = pw;
        }
    }

    String getPassword() {
        synchronized (this.pwlock){
            return password;
        }
    }





    public static void main(String[] args) {
        SafeCalc sample = new SafeCalc();
        Thread A = new Thread() {
            @Override
            public void run() {
                sample.addOne();
            }
        };

        Thread B = new Thread() {
            @Override
            public void run() {
                sample.addOne();
            }
        };

        A.start();
        B.start();

    }
}
