package com.network.app.rest.Controller;

import com.network.app.rest.Entities.Device;
import com.network.app.rest.Monitor.Polling;
import com.network.app.rest.Repository.DeviceRepository;
import org.apache.tomcat.jni.Poll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ControllerApi {

    private HashMap<Long, Polling> pollingThreads = new HashMap<>();

    @Autowired
    private DeviceRepository deviceRepository;

    /**
     * List all devices in the network
     * @return a list with all the devices in the network
     */
    @GetMapping( value = "/devices")
    public List<Device> getDevices(){
        return deviceRepository.findAll();
    }

    /**
     * List all devices IP addresses
     * Returns a String containing all devices IP addresses
     * Initially it starts by getting all the devices on the network.
     * It then iterates over those devices and extracts the IPs, saving them in a list.
     * @return all devices IP addresses
     */
    @GetMapping( value = "/listAddress")
    public String getAllAddresses(){
        List<Device> devices = deviceRepository.findAll(); /* Get all devices */
        Set<String> addressList = new HashSet<>(); /* no duplicates */

        for( Device device: devices ){
            addressList.add( device.getIpAddress() ); /* Get device IP and save it */
        }

        return addressList.toString();

    }

    /**
     * Add a device to the network
     * Initially, the device is added to the database.
     * Then, a Polling thread is created and that thread
     * will be responsible for recording the timestamps related to the connectivity test (Ping)
     * @param device a device to be added
     * @return a String showing that the device was added successfully
     */
    @PostMapping( value = "/add")
    public String addDevice( @RequestBody Device device){
        deviceRepository.save( device ); /* save device in database */

        /* start the thread responsible for the ping command */
        Polling pollingThread = new Polling( device, device.getPollingIntervalInSec(), device.getIpAddress(),
                deviceRepository, this );
        pollingThread.start();

        this.pollingThreads.put( device.getId(), pollingThread);

        return "Added device " + device.toString() + " to the database!"+
                "Current Database size: " + deviceRepository.count();
    }

    /**
     * Add multiple devices to the network
     * Initially, it iterates over the list of devices passed as an argument and, individually,
     * adds each one of them to the database, also creating the thread responsible for checking the availability
     * of the associated address.
     * @param devices a list of devices to be added
     * @return a String showing that the devices were added successfully
     */
    @PostMapping( value = "/addList")
    public String addDevices( @RequestBody Device[] devices){
        for (Device device : devices)
        {
            deviceRepository.save( device ); /* save device in database */

            /* start the thread responsible for the ping command */
            Polling pollingThread = new Polling( device, device.getPollingIntervalInSec(), device.getIpAddress(),
                    deviceRepository, this);
            pollingThread.start();

            this.pollingThreads.put( device.getId(), pollingThread);
        }
        return "Added " + devices.length + " devices to the database! " +
                "Current Database size: " + deviceRepository.count();
    }

    /**
     * Delete a device from the network
     * Initially, the device is found in the database.
     * Then, the device is deleted from the database.
     * Finally, the correspondent thread is stopped.
     * @param id id of the device to be deleted
     * @return a String showing that the device was deleted successfully
     */
    @DeleteMapping( value = "/delete/{id}")
    public String removeDevice( @PathVariable long id ){

        try {
            Device deviceToBeDeleted = deviceRepository.findById(id).get(); /* find device in the database */

            deviceRepository.delete( deviceToBeDeleted ); /* remove device from the database */

            /* TO DO - STOP THE THREAD */
            Polling pollingThread = this.pollingThreads.get( id );
            pollingThread.stopThread();

            this.pollingThreads.remove( id );

            return "Deleted device " + deviceToBeDeleted.toString() + " Current Database size: " + deviceRepository.count();
        } catch( NoSuchElementException e){
            return "No device found!";
        }
    }

    /**
     * Update last success communication timestamp
     * Firstly, the device is found in the database.
     * Then, we keep all the old values, except the last success communication timestamp.
     * The old last success communication timestamp is replaced with the new one.
     * @param id id of the device to be updated
     * @param newLastSuccessCommTimestamp new timestamp to insert in the database
     * @return a String showing that the device was successfully updated
     */
    public String updateDevice(long id, long newLastSuccessCommTimestamp){
        Device deviceToBeUpdated = deviceRepository.findById(id).get();
        deviceToBeUpdated.setId( deviceToBeUpdated.getId() );
        deviceToBeUpdated.setIpAddress( deviceToBeUpdated.getIpAddress() );
        deviceToBeUpdated.setPollingIntervalInSec( deviceToBeUpdated.getPollingIntervalInSec() );
        deviceToBeUpdated.setLastSuccessCommTimestamp( newLastSuccessCommTimestamp );
        deviceRepository.save( deviceToBeUpdated );

        return "Device " + id + " updated!";
    }
}