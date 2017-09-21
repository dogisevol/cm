package com.example.cashman.service;

import com.example.cashman.repository.dto.DeviceDTO;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface DeviceService {
    int M_FACTOR = 20;

    Iterable<DeviceDTO> fetchAll();

    DeviceDTO fetchByID(Long id);

    DeviceDTO fetchBySerialNumber(String serialNumber);

    @Transactional
    DeviceDTO addTo(Long id, DeviceDTO toAdd);

    @Transactional
    DeviceDTO withdraw(Long deviceId, BigDecimal amount);

    @Transactional
    DeviceDTO initializeATM(DeviceDTO atmDTO);

    boolean deactivateATM(Long id);
}
