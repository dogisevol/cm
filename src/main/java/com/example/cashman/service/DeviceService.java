package com.example.cashman.service;

import com.example.cashman.repository.dto.DeviceDTO;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface DeviceService {
    int M_FACTOR = 20;
    int TYPE_GREEDY = 1;
    int TYPE_KNAPSACK = 2;
    int TYPE_BOTH = 0;

    Iterable<DeviceDTO> fetchAll();

    DeviceDTO fetchByID(Long id);

    DeviceDTO fetchBySerialNumber(String serialNumber);

    @Transactional
    DeviceDTO addTo(Long id, DeviceDTO toAdd);

    @Transactional
    DeviceDTO withdraw(Long deviceId, BigDecimal amount);

    @Transactional
    DeviceDTO withdraw(Long deviceId, BigDecimal amount, int type);

    @Transactional
    DeviceDTO initializeATM(DeviceDTO atmDTO);

    boolean deactivateATM(Long id);
}
