package com.network.app.rest.Controller;

import com.network.app.rest.Entities.Device;
import com.network.app.rest.Monitor.Polling;
import com.network.app.rest.Repository.DeviceRepository;
import org.apache.tomcat.jni.Poll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ControllerApi {

    @Autowired
    private DeviceRepository deviceRepository;

    @GetMapping( value = "/")
    public String welcome(){
        return "hello world";
    }

    @GetMapping( value = "/devices")
    public List<Device> getDevices(){
        return deviceRepository.findAll();
    }

    @GetMapping( value = "/listAddress")
    public String getAllAddresses(){
        List<Device> devices = deviceRepository.findAll();
        ArrayList<String> addressList = new ArrayList<>();

        for( Device device: devices ){
            addressList.add( device.getIpAddress() );
        }

        return addressList.toString();


    }
    @PostMapping( value = "/add")
    public String addDevice( @RequestBody Device device){
        deviceRepository.save( device );

        Polling pollingThread = new Polling( device, device.getPollingIntervalInSec(), device.getIpAddress(),
                deviceRepository, this );
        pollingThread.start();

        return "Added device " + device.toString() + " to the database!"+
                "Current Database size: " + deviceRepository.count();
    }

    @PostMapping( value = "/addList")
    public String addDevices( @RequestBody Device[] devices){
        for (Device device : devices)
        {
            deviceRepository.save( device );

            Polling pollingThread = new Polling( device, device.getPollingIntervalInSec(), device.getIpAddress(),
                    deviceRepository, this);
            pollingThread.start();
        }
        return "Added " + devices.length + " devices to the database! " +
                "Current Database size: " + deviceRepository.count();
    }

    @DeleteMapping( value = "/delete/{id}")
    public String removeDevice( @PathVariable long id ){
        Device deviceToBeDeleted = deviceRepository.findById(id).get();
        deviceRepository.delete( deviceToBeDeleted );
        return "Deleted device " + deviceToBeDeleted.toString() + " Current Database size: " + deviceRepository.count();
    }

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
