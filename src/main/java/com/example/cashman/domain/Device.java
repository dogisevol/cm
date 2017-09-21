package com.example.cashman.domain;

import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
public class Device implements Serializable {

    private static final long serialVersionUID = -3501514625217380338L;
    @Id
    @SequenceGenerator(name = "device_generator", sequenceName = "device_sequence", initialValue = 100)
    @GeneratedValue(generator = "device_generator")
    private Long id;

    @Column(nullable = false, unique = true)
    private String serialNumber;

    @Column(name = "creation_date", insertable = false, updatable = false, columnDefinition = "timestamp default current_timestamp")
    @org.hibernate.annotations.Generated(value = GenerationTime.INSERT)
    private ZonedDateTime creationDate;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private Set<Banknote> banknotes;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private Set<Coin> coins;

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Banknote> getBanknotes() {
        return banknotes;
    }

    public void setBanknotes(Set<Banknote> banknotes) {
        this.banknotes = banknotes;
    }

    public Set<Coin> getCoins() {
        return coins;
    }

    public void setCoins(Set<Coin> coins) {
        this.coins = coins;
    }
}
