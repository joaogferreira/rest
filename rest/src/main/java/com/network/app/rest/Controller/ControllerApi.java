package com.network.app.rest.Controller;

import com.network.app.rest.Models.Device;
import com.network.app.rest.Repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.GeneratedValue;
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

    @PostMapping( value = "/add")
    public String addDevice( @RequestBody Device device){
        deviceRepository.save( device );
        return "Added device " + device.toString() + " to the database!";
    }

    @DeleteMapping( value = "/delete/{id}")
    public String removeDevice( @PathVariable long id ){
        Device deviceToBeDeleted = deviceRepository.findById(id).get();
        deviceRepository.delete( deviceToBeDeleted );
        return "Deleted device " + deviceToBeDeleted.toString();
    }
}
