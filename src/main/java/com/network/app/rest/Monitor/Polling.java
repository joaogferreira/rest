package com.network.app.rest.Monitor;

public class Polling extends Thread{

    private long interval;
    private String address;

    public Polling( long interval, String address ){
        this.interval = interval;
        this.address = address;
    }

    @Override
    public void run(){

        while( true ){

            System.out.println("Ping " + this.address + " , Time: " + System.currentTimeMillis() / 1000 );

            try {
                Thread.sleep( this.interval * 1000 ); // Thread sleep uses milliseconds
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }

    }
}
