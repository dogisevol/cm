package com.example.cashman.service;

import com.example.cashman.repository.dto.DenominationDTO;
import com.example.cashman.repository.dto.DeviceDTO;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;

public interface DeviceService {
    Iterable<DeviceDTO> fetchAll();

    DeviceDTO fetchByID(Long id);

    DeviceDTO fetchBySerialNumber(String serialNumber);

    @Transactional
    DeviceDTO addTo(Long id, Collection<DenominationDTO> toAdd);
    @Transactional
    DeviceDTO withdraw(Long deviceId, BigDecimal amount);
    DeviceDTO initializeATM(DeviceDTO atmDTO);
    boolean deactivateATM(Long id);
}
