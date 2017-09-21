package com.example.cashman.repository;

import com.example.cashman.domain.Device;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface DeviceRepository extends CrudRepository<Device, Long> {
    @Query(value = "SELECT DISTINCT d FROM Device d LEFT JOIN FETCH d.banknotes LEFT JOIN FETCH d.coins ")
    Collection<Device> fetchAll();

    @Query(value = "SELECT DISTINCT d FROM Device d LEFT JOIN FETCH d.banknotes LEFT JOIN FETCH d.coins WHERE d.id = :id")
    Device fetchByID(Long id);

    @Query(value = "SELECT DISTINCT d FROM Device d LEFT JOIN FETCH d.banknotes LEFT JOIN FETCH d.coins WHERE d.serialNumber = :serialNumber")
    Device fetchBySerialNumber(String serialNumber);
}
