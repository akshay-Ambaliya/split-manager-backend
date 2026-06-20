package com.splitManager.splitmanager.service;

import com.splitManager.splitmanager.dto.request.*;
import com.splitManager.splitmanager.dto.response.SplitResponseDTO;

import java.util.List;

public interface SplitService {

    List<SplitResponseDTO> equalSplit(EqualSplitRequestDTO request);

    List<SplitResponseDTO> percentageSplit(PercentageSplitRequestDTO request);

    List<SplitResponseDTO> exactSplit(ExactSplitRequestDTO request);
}