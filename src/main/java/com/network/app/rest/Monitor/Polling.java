package com.network.app.rest.Monitor;

import com.network.app.rest.Controller.ControllerApi;
import com.network.app.rest.Entities.Device;
import com.network.app.rest.Repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.zip.DeflaterInputStream;

/**
 * Thread that will check if address is reachable via Ping (ICMP packages)
 * Requires root privileges
 */
public class Polling extends Thread{

    private Device device;
    private long interval;
    private String address;
    private ControllerApi controllerApi;

    private boolean isStopped = false; /* Variable used to stop the thread */

    @Autowired
    private DeviceRepository deviceRepository;

    /**
     * Constructor
     * @param device Correspondent device
     * @param interval Device's interval of polling
     * @param address Device's IP Address
     * @param deviceRepository Repository containing all devices
     * @param controllerApi Controller API (used to update the database)
     */
    public Polling( Device device, long interval, String address, DeviceRepository deviceRepository, ControllerApi controllerApi){
        this.device = device;
        this.interval = interval;
        this.address = address;
        this.deviceRepository = deviceRepository;
        this.controllerApi = controllerApi;
    }

    @Override
    public void run(){

        while( !isStopped ){     /* run while thread is NOT stopped */

            /*
            System.out.println("Ping " + this.address + " , Time: " + System.currentTimeMillis() / 1000
                + ", Current db size: " + deviceRepository.count() );
            */

            InetAddress address = null;
            boolean reachable = false;

            try {
                address = InetAddress.getByName( this.address );

                /* Check if IP is reachable - requires root privileges */
                reachable = address.isReachable(1000);

            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if ( reachable ){
                /* If Address is reachable then Change Device timestamp value in db */
                controllerApi.updateDevice(device.getId(), System.currentTimeMillis() / 1000 );

            }

            try {
                /* Check if IP is reachable every X seconds */
                Thread.sleep( this.interval * 1000 ); /* Thread sleep uses milliseconds */
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        /* Thread is stopped when the device is removed from the network */
        System.out.println(" Thread for device " + this.device.getId() + " was stopped.");

    }

    /**
     * Stop thread by changing the loop variable
     */
    public void stopThread(){
        this.isStopped = true;
    }
}