package com.splitManager.splitmanager.split;

import com.splitManager.splitmanager.dto.request.SplitRequestDTO;
import com.splitManager.splitmanager.entity.ExpenseSplit;
import com.splitManager.splitmanager.entity.User;
import com.splitManager.splitmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component("EQUAL")
@RequiredArgsConstructor
public class EqualSplitStrategy implements SplitStrategy {

    private final UserRepository userRepository;

    @Override
    public List<ExpenseSplit> calculateSplits(SplitRequestDTO request) {

        int size = request.getSplits().size();

        BigDecimal share = request.getTotalAmount()
                .divide(BigDecimal.valueOf(size), 2, RoundingMode.HALF_UP);

        List<ExpenseSplit> result = new ArrayList<>();

        for (var user : request.getSplits()) {

            User temp = userRepository.findById(user.getUserId()).orElseThrow(()->new RuntimeException("User not found"));

            ExpenseSplit split = ExpenseSplit.builder()
                    .user(temp)
                    .amount(share)
                    .build();

            result.add(split);
        }

        return result;
    }
}