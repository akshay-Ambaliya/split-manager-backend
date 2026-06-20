package com.splitManager.splitmanager.split;

import com.splitManager.splitmanager.dto.request.SplitRequestDTO;
import com.splitManager.splitmanager.entity.ExpenseSplit;

import java.util.List;

public interface SplitStrategy {

    List<ExpenseSplit> calculateSplits(SplitRequestDTO request);
}