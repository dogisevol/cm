package com.example.cashman.repository.dto;


import com.example.cashman.Error;
import com.example.cashman.domain.Denomination;
import com.example.cashman.domain.Device;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DeviceDTO {
    private Long id;
    private String serialNumber;
    private Collection<DenominationDTO> banknotes = new ArrayList<>();
    private Collection<DenominationDTO> coins = new ArrayList<>();
    private List<String> errors = new ArrayList<>();

    public DeviceDTO() {
    }

    public DeviceDTO(Error error) {
        addError(error.toString());
    }

    public DeviceDTO(Long id, Collection<DenominationDTO> banknotes, Collection<DenominationDTO> coins) {
        this.setId(id);
        this.setBanknotes(banknotes);
        this.setCoins(coins);
    }

    public DeviceDTO(String serialNumber, Collection<DenominationDTO> banknotes, Collection<DenominationDTO> coins) {
        this.setSerialNumber(serialNumber);
        this.setBanknotes(banknotes);
        this.setCoins(coins);
    }

    public DeviceDTO(Device atm) {
        this.setId(atm.getId());
        this.setSerialNumber(atm.getSerialNumber());
        for (Denomination denomination : atm.getBanknotes()) {
            getBanknotes().add(new DenominationDTO(denomination));
        }
        for (Denomination denomination : atm.getCoins()) {
            getCoins().add(new DenominationDTO(denomination));
        }
    }

    public Device toEntity() {
        Device device = new Device();
        device.setId(this.getId());
        device.setSerialNumber(this.getSerialNumber());
        device.setBanknotes(getBanknotes().stream().map(denominationDTO -> denominationDTO.toBanknote(device)).collect(Collectors.toSet()));
        device.setCoins(getCoins().stream().map(denominationDTO -> denominationDTO.toCoin(device)).collect(Collectors.toSet()));
        return device;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void addError(String error) {
        errors.add(error);
    }

    public boolean hasErrors() {
        return this.errors.size() > 0;
    }

    public Collection<DenominationDTO> getCoins() {
        return coins;
    }

    public void setCoins(Collection<DenominationDTO> coins) {
        this.coins = coins;
    }

    public Collection<DenominationDTO> getBanknotes() {
        return banknotes;
    }

    public void setBanknotes(Collection<DenominationDTO> banknotes) {
        this.banknotes = banknotes;
    }
}
