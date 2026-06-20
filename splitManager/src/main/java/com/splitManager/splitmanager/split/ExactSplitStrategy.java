package com.splitManager.splitmanager.split;

import com.splitManager.splitmanager.dto.request.SplitRequestDTO;
import com.splitManager.splitmanager.entity.ExpenseSplit;
import com.splitManager.splitmanager.entity.User;
import com.splitManager.splitmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("EXACT")
@RequiredArgsConstructor
public class ExactSplitStrategy implements SplitStrategy {

    private final UserRepository userRepository;

    @Override
    public List<ExpenseSplit> calculateSplits(SplitRequestDTO request) {

        List<ExpenseSplit> result = new ArrayList<>();

        for (var user : request.getSplits()) {

            User temp = userRepository.findById(user.getUserId()).orElseThrow(()->new RuntimeException("User not found"));

            result.add(
                    ExpenseSplit.builder()
                            .user(temp)
                            .amount(user.getAmount())
                            .build()
            );
        }

        return result;
    }
}