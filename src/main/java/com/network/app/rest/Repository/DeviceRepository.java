package com.network.app.rest.Repository;

import com.network.app.rest.Models.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}
