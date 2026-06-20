package com.splitManager.splitmanager.split;

import com.splitManager.splitmanager.dto.request.SplitRequestDTO;
import com.splitManager.splitmanager.entity.ExpenseSplit;
import com.splitManager.splitmanager.entity.User;
import com.splitManager.splitmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component("PERCENTAGE")
@RequiredArgsConstructor
public class PercentageSplitStrategy implements SplitStrategy {

    private final UserRepository userRepository;

    @Override
    public List<ExpenseSplit> calculateSplits(SplitRequestDTO request) {

        BigDecimal total = request.getTotalAmount();

        List<ExpenseSplit> result = new ArrayList<>();

        for (var user : request.getSplits()) {

            BigDecimal amount = total
                    .multiply(user.getPercentage())
                    .divide(BigDecimal.valueOf(100));

            User temp = userRepository.findById(user.getUserId()).orElseThrow(()->new RuntimeException("User not found"));


            result.add(
                    ExpenseSplit.builder()
                            .user(temp)
                            .amount(amount)
                            .percentage(user.getPercentage())
                            .build()
            );
        }

        return result;
    }
}