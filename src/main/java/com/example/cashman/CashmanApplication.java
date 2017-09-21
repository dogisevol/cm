package com.example.cashman;

import com.example.cashman.domain.Banknote;
import com.example.cashman.domain.Coin;
import com.example.cashman.domain.Device;
import com.example.cashman.repository.DeviceRepository;
import com.example.cashman.repository.dto.DenominationDTO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class CashmanApplication {

    @Autowired
    private DeviceRepository deviceRepository;

    public static void main(String[] args) {
        SpringApplication.run(CashmanApplication.class, args);
    }

    //Initialize device on start
    @Bean
    InitializingBean sendDatabase() {
        return () -> {
            Device device = new Device();
            device.setSerialNumber("Default device");
            //In production probably load from device cassette in some manner
            Set<Banknote> banknotes = new HashSet<>();
            Set<Coin> coins = new HashSet<>();
            device.setCoins(coins);
            device.setBanknotes(banknotes);
            Coin coin = new DenominationDTO(new BigDecimal(0.05), 100).toCoin(device);
            coin.setDevice(device);
            coins.add(coin);
            coin = new DenominationDTO(new BigDecimal(0.1), 100).toCoin(device);
            coin.setDevice(device);
            coins.add(coin);
            coin = new DenominationDTO(new BigDecimal(0.2), 100).toCoin(device);
            coin.setDevice(device);
            coins.add(coin);
            coin = new DenominationDTO(new BigDecimal(0.5), 100).toCoin(device);
            coin.setDevice(device);
            coins.add(coin);
            coin = new DenominationDTO(new BigDecimal(1), 100).toCoin(device);
            coin.setDevice(device);
            coins.add(coin);
            coin = new DenominationDTO(new BigDecimal(2), 100).toCoin(device);
            coin.setDevice(device);
            coins.add(coin);

            Banknote banknote = new DenominationDTO(new BigDecimal(5), 100).toBanknote(device);
            banknote.setDevice(device);
            banknotes.add(banknote);
            banknote = new DenominationDTO(new BigDecimal(10), 100).toBanknote(device);
            banknote.setDevice(device);
            banknotes.add(banknote);
            banknote = new DenominationDTO(new BigDecimal(20), 100).toBanknote(device);
            banknote.setDevice(device);
            banknotes.add(banknote);
            banknote = new DenominationDTO(new BigDecimal(50), 100).toBanknote(device);
            banknote.setDevice(device);
            banknotes.add(banknote);
            banknote = new DenominationDTO(new BigDecimal(100), 100).toBanknote(device);
            banknote.setDevice(device);
            banknotes.add(banknote);

            deviceRepository.save(device);
        };
    }
}
