package com.network.app.rest.Models;

import javax.persistence.*;

@Entity
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // unique for each entity
    private long id; // primary key

    @Column
    private String ipAddress;

    @Column
    private long pollingIntervalInSec;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public long getPollingIntervalInSec() {
        return pollingIntervalInSec;
    }

    public void setPollingIntervalInSec(long pollingIntervalInSec) {
        this.pollingIntervalInSec = pollingIntervalInSec;
    }


    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", ipAddress='" + ipAddress + '\'' +
                ", pollingIntervalInSec=" + pollingIntervalInSec +
                '}';
    }
}
