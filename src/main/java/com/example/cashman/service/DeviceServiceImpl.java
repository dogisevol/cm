package com.example.cashman.service;

import com.example.cashman.Error;
import com.example.cashman.domain.Banknote;
import com.example.cashman.domain.Coin;
import com.example.cashman.domain.Denomination;
import com.example.cashman.domain.Device;
import com.example.cashman.repository.DenominationRepository;
import com.example.cashman.repository.DeviceRepository;
import com.example.cashman.repository.dto.DeviceDTO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Collections.sort;

@Component("questionService")
@Transactional
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository, DenominationRepository denominationRepository) {
        this.deviceRepository = deviceRepository;
    }


    @Override
    public Iterable<DeviceDTO> fetchAll() {
        return StreamSupport.stream(deviceRepository.fetchAll().spliterator(), false)
                .map(DeviceDTO::new).collect(Collectors.toList());
    }

    @Override
    public DeviceDTO fetchByID(Long id) {
        Device atm = deviceRepository.fetchByID(id);
        return atm == null ? null : new DeviceDTO(atm);
    }

    @Override
    public DeviceDTO fetchBySerialNumber(String serialNumber) {
        Device atm = deviceRepository.fetchBySerialNumber(serialNumber);
        return atm == null ? null : new DeviceDTO(atm);
    }

    @Override
    public DeviceDTO addTo(Long id, DeviceDTO toAdd) {
        Device device = deviceRepository.fetchByID(id);
        DeviceDTO result;
        if (device == null) {
            result = new DeviceDTO(Error.DEVICE_NOT_FOUND_ERROR);
        } else {
            device.getBanknotes().stream().forEach(banknote -> toAdd.getBanknotes().forEach(denominationDTO -> {
                if (denominationDTO.getDenomination().doubleValue() == banknote.getDenomination().doubleValue()){
                    banknote.setCount(banknote.getCount() + denominationDTO.getCount());
                }
            }));
            device.getCoins().stream().forEach(coin -> toAdd.getCoins().forEach(denominationDTO -> {
                if (denominationDTO.getDenomination().doubleValue() == coin.getDenomination().doubleValue()){
                    coin.setCount(coin.getCount() + denominationDTO.getCount());
                }
            }));
            result = new DeviceDTO(deviceRepository.save(device));
        }
        return result;
    }

    @Override
    public DeviceDTO withdraw(Long deviceId, BigDecimal amount) {
        DeviceDTO result;
        Device device = deviceRepository.fetchByID(deviceId);
        if (device != null) {
            if (withdraw(device, amount)) {
                result = new DeviceDTO(deviceRepository.save(device));
            } else {
                result = new DeviceDTO(Error.WITHDRAWAL_ERROR);
            }
        } else {
            result = new DeviceDTO(Error.DEVICE_NOT_FOUND_ERROR);
        }
        return result;
    }

    @Override
    public DeviceDTO initializeATM(DeviceDTO atmDTO) {
        Device atm = deviceRepository.save(atmDTO.toEntity());
        return new DeviceDTO(atm);
    }

    @Override
    public boolean deactivateATM(Long id) {
        Device atm = deviceRepository.fetchByID(id);
        if (atm != null) {
            atm.setCoins(null);
            atm.setBanknotes(null);
            deviceRepository.save(atm);
            return true;
        }
        return false;
    }

    private boolean withdraw(Device device, BigDecimal amount) {
        amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
        final BigDecimal mFactor = new BigDecimal(M_FACTOR);
        int withdrawAmount = amount.multiply(mFactor).intValue();
        List<Banknote> banknotes = new ArrayList(device.getBanknotes());
        List<Coin> coins = new ArrayList(device.getCoins());
        sortDenominations(banknotes);
        sortDenominations(coins);
        List<Denomination> weights = new ArrayList<>();
        for (int i = banknotes.size() - 1; i >= 0; i--) {
            for (int j = 0; j < banknotes.get(i).getCount(); j++) {
                weights.add(banknotes.get(i));
            }
        }
        for (int i = coins.size() - 1; i >= 0; i--) {
            for (int j = 0; j < coins.get(i).getCount(); j++) {
                weights.add(coins.get(i));
            }
        }
        int N = weights.size();
        int[][] A = new int[N][withdrawAmount + 1];
        for (int x = 0; x <= withdrawAmount; x++) {
            A[0][x] = 0;
        }
        for (int i = 1; i < N; i++) {
            int j = 0;
            for (; j <= withdrawAmount; j++) {
                int weight = weights.get(i - 1).getDenomination().multiply(mFactor).setScale(2, BigDecimal.ROUND_HALF_UP).intValue();
                if (weight > j) {
                    A[i][j] = A[i - 1][j];
                } else {
                    if (A[i - 1][j] > A[i - 1][j - weight] + weight) {
                    }
                    A[i][j] = Math.max(A[i - 1][j], A[i - 1][j - weight] + weight);
                }
            }
            if (A[i][withdrawAmount] == withdrawAmount) {
                for (int k = i, m = withdrawAmount; k > 0 && m > 0; k--) {
                    int tempI = A[k][m];
                    int tempI_1 = A[k - 1][m];
                    if ((k == 0 && tempI > 0) || (k > 0 && tempI != tempI_1)) {
                        Denomination denomination = weights.get(k - 1);
                        double weight = weights.get(k - 1).getDenomination().multiply(mFactor).setScale(2, BigDecimal.ROUND_HALF_UP).intValue();
                        m -= weight;
                        denomination.setCount(denomination.getCount() - 1);
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void sortDenominations(List<? extends Denomination> denominations) {
        sort(denominations, (o1, o2) -> {
            if (o1 == null || o1.getDenomination() == null)
                return -1;
            if (o2 == null || o2.getDenomination() == null)
                return 1;
            else return o1.getDenomination().compareTo(o2.getDenomination());
        });
    }


}
