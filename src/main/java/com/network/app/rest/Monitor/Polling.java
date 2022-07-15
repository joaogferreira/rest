package com.network.app.rest.Monitor;

import com.network.app.rest.Repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class Polling extends Thread{

    private long interval;
    private String address;

    @Autowired
    private DeviceRepository deviceRepository;

    public Polling( long interval, String address, DeviceRepository deviceRepository){
        this.interval = interval;
        this.address = address;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public void run(){

        while( true ){

            /*
            System.out.println("Ping " + this.address + " , Time: " + System.currentTimeMillis() / 1000
                + ", Current db size: " + deviceRepository.count() );
            */
            
            try {
                Thread.sleep( this.interval * 1000 ); // Thread sleep uses milliseconds
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }

    }
}
