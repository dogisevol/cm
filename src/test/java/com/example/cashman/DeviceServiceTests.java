package com.example.cashman;

import com.example.cashman.domain.Banknote;
import com.example.cashman.domain.Coin;
import com.example.cashman.domain.Device;
import com.example.cashman.repository.DeviceRepository;
import com.example.cashman.repository.dto.DenominationDTO;
import com.example.cashman.repository.dto.DeviceDTO;
import com.example.cashman.service.DeviceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceServiceTests {

    @Autowired
    DeviceService deviceService;
    @MockBean
    DeviceRepository deviceRepository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testBaseCase1() {
        withdrawPositive(new double[]{20, 50}, new int[]{10, 10}, new int[]{1, 0}, 20);
    }

    @Test
    public void testBaseCase2() {
        withdrawPositive(new double[]{20, 50}, new int[]{10, 10}, new int[]{2, 0}, 40);
    }

    @Test
    public void testBaseCase3() {
        withdrawPositive(new double[]{20, 50}, new int[]{10, 10}, new int[]{0, 1}, 50);
    }

    @Test
    public void testBaseCase4() {
        withdrawPositive(new double[]{20, 50}, new int[]{10, 10}, new int[]{1, 1}, 70);
    }

    @Test
    public void testBaseCase5() {
        withdrawPositive(new double[]{20, 50}, new int[]{10, 10}, new int[]{4, 0}, 80);
    }

    @Test
    public void testBaseCase6() {
        withdrawPositive(new double[]{20, 50}, new int[]{10, 10}, new int[]{0, 2}, 100);
    }

    @Test
    public void testBaseCase7() {
        withdrawPositive(new double[]{20, 50}, new int[]{10, 10}, new int[]{0, 3}, 150);
    }

    @Test
    public void testBaseCase8() {
        withdrawPositive(new double[]{20, 50}, new int[]{10, 10}, new int[]{3, 0}, 60);
    }

    @Test
    public void testBaseCase9() {
        withdrawPositive(new double[]{20, 50}, new int[]{10, 10}, new int[]{3, 1}, 110);
    }

    @Test
    public void testBaseCase10() {
        withdrawPositive(new double[]{20, 50}, new int[]{8, 3}, new int[]{5, 2}, 200);
    }

    //Extended test cases
    @Test
    public void testExtendedCase11() {
        withdrawPositive(new double[]{5, 10, 20}, new int[]{10, 10, 10}, new int[]{1, 1, 1}, 35);
    }

    //Test cases with coins
    @Test
    public void testWithdrawAllAustralianCurrency1() {
        withdrawPositive(new double[]{0.05, 0.1, 0.2, 0.5, 1, 2, 5, 10, 20, 50, 100},
                new int[]{10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
                new int[]{0, 0, 0, 0, 0, 1, 1, 1, 1, 0}, 35.5);
    }

    @Test
    public void testWithdrawAllAustralianCurrency2() {
        withdrawPositive(new double[]{0.05, 0.1, 0.2, 0.5, 1, 2, 5, 10, 20, 50, 100},
                new int[]{10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
                new int[]{0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0}, 37.7);
    }

    private void withdrawPositive(double[] denominations, int[] counts, int[] expectedWithdrawals, double amount) {
        BigDecimal withdrawalAmount = new BigDecimal(amount);
        Device device = new Device();
        Set<Banknote> banknotes = new HashSet<>();
        Set<Coin> coins = new HashSet<>();
        for (int i = 0; i < denominations.length; i++) {
            if (denominations[i] > 1) {
                banknotes.add(new DenominationDTO(1L, new BigDecimal(denominations[i]), counts[i]).toBanknote(device));
            } else {
                coins.add(new DenominationDTO(1L, new BigDecimal(denominations[i]), counts[i]).toCoin(device));
            }
        }
        device.setBanknotes(banknotes);
        device.setCoins(coins);
        when(deviceRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(device));
        when(deviceRepository.save(device)).thenReturn(device);
        DeviceDTO withdraw = deviceService.withdraw(1L, withdrawalAmount);
        withdraw.getBanknotes().stream().forEach(
                denominationDTO -> {
                    int index = 0;
                    for (int i = 0; i < denominations.length; i++) {
                        if (denominations[i] > 5) {
                            if (denominationDTO.getDenomination().intValue() == denominations[i]) {
                                assert denominationDTO.getCount() == counts[index] - expectedWithdrawals[index];
                            }
                            index++;
                        }
                    }
                }
        );
        withdraw.getCoins().stream().forEach(
                denominationDTO -> {
                    int index = 0;
                    for (int i = 0; i < denominations.length; i++) {
                        if (denominations[i] < 1) {
                            if (denominationDTO.getDenomination().doubleValue() == denominations[i]) {
                                assert denominationDTO.getCount() == counts[index] - expectedWithdrawals[index];
                            }
                            index++;
                        }
                    }
                }
        );
    }

}
