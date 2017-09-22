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
        assert withdrawPositive(new double[]{20, 50}, new int[]{10, 10}, new int[]{1, 0}, 20, true, true);
    }

    @Test
    public void testBaseCase2() {
        assert withdrawPositive(new double[]{20, 50}, new int[]{10, 10}, new int[]{2, 0}, 40, true, true);
    }

    @Test
    public void testBaseCase3() {
        assert withdrawPositive(new double[]{20, 50}, new int[]{10, 10}, new int[]{0, 1}, 50, true, true);
    }

    @Test
    public void testBaseCase4() {
        assert withdrawPositive(new double[]{20, 50}, new int[]{10, 10}, new int[]{1, 1}, 70, true, true);
    }

    @Test
    public void testBaseCase5() {
        assert withdrawPositive(new double[]{20, 50}, new int[]{10, 10}, new int[]{4, 0}, 80, true, true);
    }

    @Test
    public void testBaseCase6() {
        assert withdrawPositive(new double[]{20, 50}, new int[]{10, 10}, new int[]{0, 2}, 100, true, true);
    }

    @Test
    public void testBaseCase7() {
        assert withdrawPositive(new double[]{20, 50}, new int[]{10, 10}, new int[]{0, 3}, 150, true, true);
    }

    @Test
    public void testBaseCase8() {
        assert withdrawPositive(new double[]{20, 50}, new int[]{10, 10}, new int[]{3, 0}, 60, true, true);
    }

    @Test
    public void testBaseCase9() {
        assert withdrawPositive(new double[]{20, 50}, new int[]{10, 10}, new int[]{3, 1}, 110, true, true);
    }

    @Test
    public void testBaseCase10() {
        assert withdrawPositive(new double[]{20, 50}, new int[]{8, 3}, new int[]{5, 2}, 200, true, true);
    }

    //Extended test cases
    @Test
    public void testExtendedCase11() {
        assert withdrawPositive(new double[]{5, 10, 20}, new int[]{10, 10, 10}, new int[]{1, 1, 1}, 35, true, true);
    }

    //Test cases with coins
    @Test
    public void testWithdrawAllAustralianCurrency1() {
        assert withdrawPositive(new double[]{0.05, 0.1, 0.2, 0.5, 1, 2, 5, 10, 20, 50, 100},
                new int[]{10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
                new int[]{0, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0}, 35.5, true, true);
    }

    @Test
    public void testWithdrawAllAustralianCurrency2() {
        assert withdrawPositive(new double[]{0.05, 0.1, 0.2, 0.5, 1, 2, 5, 10, 20, 50, 100},
                new int[]{10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
                new int[]{0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0}, 37.7, true, true);
    }

    @Test
    public void testWithdraw() {
        assert withdrawPositive(new double[]{0.05, 0.1, 0.2, 0.5},
                new int[]{2, 2, 2, 2},
                new int[]{1, 1, 0, 1}, 0.65, true, true);
    }

    @Test
    public void testWithdrawAllAustralianCurrency3() {
        assert withdrawPositive(
                new double[]{0.05, 0.1, 0.2, 0.5, 1, 2, 5, 10, 20, 50, 100},
                new int[]{10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
                new int[]{1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0}, 35.75, true, true);
    }

    private boolean withdrawPositive(double[] denominations, int[] counts, int[] expectedWithdrawals, double amount, boolean passGreedy, boolean passKnapsack) {
        boolean result;
        boolean greedy = withdrawPositive(denominations, counts, expectedWithdrawals, amount, DeviceService.TYPE_GREEDY);
        result = passGreedy ? greedy : !greedy;
        boolean knapsack = withdrawPositive(denominations, counts, expectedWithdrawals, amount, DeviceService.TYPE_KNAPSACK);
        result &= passKnapsack ? knapsack : !knapsack;
        return result;
    }

    private boolean withdrawPositive(double[] denominations, int[] counts, int[] expectedWithdrawals, double amount, int type) {
        boolean[] result = new boolean[]{true};
        BigDecimal withdrawalAmount = new BigDecimal(amount);
        Device device = new Device();
        Set<Banknote> banknotes = new HashSet<>();
        Set<Coin> coins = new HashSet<>();
        for (int i = 0; i < denominations.length; i++) {
            if (denominations[i] > 2) {
                banknotes.add(new DenominationDTO(1L, new BigDecimal(denominations[i]), counts[i]).toBanknote(device));
            } else {
                coins.add(new DenominationDTO(1L, new BigDecimal(denominations[i]), counts[i]).toCoin(device));
            }
        }
        device.setBanknotes(banknotes);
        device.setCoins(coins);
        when(deviceRepository.fetchByID(anyLong())).thenReturn(device);
        when(deviceRepository.save(device)).thenReturn(device);
        DeviceDTO withdraw = deviceService.withdraw(1L, withdrawalAmount, type);
        assert withdraw.getErrors().size() == 0;
        withdraw.getBanknotes().stream().forEach(
                denominationDTO -> {
                    for (int i = 0; i < denominations.length; i++) {
                        if (denominations[i] > 2) {
                            if (denominationDTO.getDenomination().doubleValue() == denominations[i]) {
                                result[0] = result[0] && denominationDTO.getCount().intValue() == counts[i] - expectedWithdrawals[i];
                            }
                        }
                    }
                }
        );
        withdraw.getCoins().stream().forEach(
                denominationDTO -> {
                    for (int i = 0; i < denominations.length; i++) {
                        if (denominations[i] <= 2) {
                            if (denominationDTO.getDenomination().doubleValue() == denominations[i]) {
                                result[0] = result[0] && denominationDTO.getCount().intValue() == counts[i] - expectedWithdrawals[i];
                            }
                        }
                    }
                }
        );
        return result[0];
    }

}
