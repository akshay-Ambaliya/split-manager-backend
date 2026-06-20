package com.splitManager.splitmanager.split;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SplitStrategyFactory {

    private final Map<String, SplitStrategy> strategies;

    public SplitStrategyFactory(Map<String, SplitStrategy> strategies) {
        this.strategies = strategies;
    }

    public SplitStrategy getStrategy(String type) {
        return strategies.get(type.toUpperCase());
    }
}