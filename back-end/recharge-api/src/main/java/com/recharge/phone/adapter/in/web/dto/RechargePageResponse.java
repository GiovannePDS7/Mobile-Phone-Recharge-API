package com.recharge.phone.adapter.in.web.dto;

import java.util.List;

public record RechargePageResponse(
    List<RechargeResponse> content,
    int page,
    int size,
    long totalElements,
    int totalPages) {}
