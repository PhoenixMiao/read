package com.phoenix.read.service;

import com.phoenix.read.controller.request.ReportRequest;

public interface ReportService {
    String report(ReportRequest request,Long userId);
}
