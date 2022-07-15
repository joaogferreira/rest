package com.network.app.rest.Monitor;

import com.network.app.rest.Controller.ControllerApi;
import com.network.app.rest.Entities.Device;
import com.network.app.rest.Repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.zip.DeflaterInputStream;

public class Polling extends Thread{

    private Device device;
    private long interval;
    private String address;
    private ControllerApi controllerApi;

    @Autowired
    private DeviceRepository deviceRepository;

    public Polling( Device device, long interval, String address, DeviceRepository deviceRepository, ControllerApi controllerApi){
        this.device = device;
        this.interval = interval;
        this.address = address;
        this.deviceRepository = deviceRepository;
        this.controllerApi = controllerApi;
    }

    @Override
    public void run(){

        while( true ){

            /*
            System.out.println("Ping " + this.address + " , Time: " + System.currentTimeMillis() / 1000
                + ", Current db size: " + deviceRepository.count() );
            */

            InetAddress address = null;
            boolean reachable = false;

            try {
                address = InetAddress.getByName( this.address );
                reachable = address.isReachable(1000); // requires root privileges
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if ( reachable ){
                // Change value in db
                controllerApi.updateDevice(device.getId(), System.currentTimeMillis() / 1000 );

            }

            try {
                Thread.sleep( this.interval * 1000 ); // Thread sleep uses milliseconds
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
