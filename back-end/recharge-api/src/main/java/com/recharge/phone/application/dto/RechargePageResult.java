package com.recharge.phone.application.dto;

import java.util.List;

public record RechargePageResult(
    List<RechargeResultCommand> content,
    int page,
    int size,
    long totalElements,
    int totalPages) {}
