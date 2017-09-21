package com.example.cashman.repository.dto;


import com.example.cashman.domain.Banknote;
import com.example.cashman.domain.Coin;
import com.example.cashman.domain.Denomination;
import com.example.cashman.domain.Device;

import java.math.BigDecimal;

public class DenominationDTO implements Comparable<DenominationDTO> {
    private BigDecimal denomination;
    private Integer count;
    private Long id;

    public DenominationDTO() {

    }

    public DenominationDTO(Long id, BigDecimal denomination, Integer count) {
        this(denomination, count);
        this.id = id;

    }

    public DenominationDTO(BigDecimal denomination, Integer count) {
        this.denomination = denomination;
        this.count = count;
    }

    public DenominationDTO(Denomination denomination) {
        this.denomination = denomination.getDenomination();
        this.count = denomination.getCount();
        this.id = denomination.getId();
    }

    public Denomination toEntity(Denomination denomination) {
        denomination.setCount(this.count);
        denomination.setId(this.id);
        denomination.setDenomination(this.denomination);
        return denomination;
    }

    public BigDecimal getDenomination() {
        return denomination;
    }

    public void setDenomination(BigDecimal denomination) {
        this.denomination = denomination;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int compareTo(DenominationDTO o) {
        if (this.getDenomination() == null)
            return -1;
        if (o == null || o.getDenomination() == null)
            return 1;
        else return this.getDenomination().compareTo(o.getDenomination());
    }

    public Coin toCoin(Device device) {
        Coin coin = (Coin) toEntity(new Coin());
        coin.setDevice(device);
        return coin;
    }


    public Banknote toBanknote(Device device) {
        Banknote banknote = (Banknote) toEntity(new Banknote());
        banknote.setDevice(device);
        return banknote;
    }
}

