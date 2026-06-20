package com.splitManager.splitmanager.controller;

import com.splitManager.splitmanager.dto.request.*;
import com.splitManager.splitmanager.dto.response.ApiResponse;
import com.splitManager.splitmanager.dto.response.SplitResponseDTO;
import com.splitManager.splitmanager.service.SplitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/split")
@RequiredArgsConstructor
public class SplitController {

    private final SplitService splitService;

    @PostMapping("/equal")
    public ApiResponse<List<SplitResponseDTO>> equalSplit(
            @RequestBody EqualSplitRequestDTO request) {

        return ApiResponse.success(
                "Equal split calculated successfully",
                splitService.equalSplit(request)
        );
    }

    @PostMapping("/percentage")
    public ApiResponse<List<SplitResponseDTO>> percentageSplit(
            @RequestBody PercentageSplitRequestDTO request) {

        return ApiResponse.success(
                "Percentage split calculated successfully",
                splitService.percentageSplit(request)
        );
    }

    @PostMapping("/exact")
    public ApiResponse<List<SplitResponseDTO>> exactSplit(
            @RequestBody ExactSplitRequestDTO request) {

        return ApiResponse.success(
                "Exact split calculated successfully",
                splitService.exactSplit(request)
        );
    }
}