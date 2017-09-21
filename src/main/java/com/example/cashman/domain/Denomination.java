package com.example.cashman.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class Denomination implements Serializable {


    private static final long serialVersionUID = 1292824767348769583L;
    @Id
    @SequenceGenerator(name = "denomination_generator", sequenceName = "denomination_generator")
    @GeneratedValue(generator = "denomination_generator")
    private Long id;

    @Column(nullable = false)
    private BigDecimal denomination;

    @Column(nullable = false)
    private Integer count = 0;

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
}

