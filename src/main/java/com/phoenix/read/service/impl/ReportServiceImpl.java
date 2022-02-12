package com.phoenix.read.service.impl;

import com.phoenix.read.controller.request.ReportRequest;
import com.phoenix.read.entity.Report;
import com.phoenix.read.mapper.ReportMapper;
import com.phoenix.read.service.ReportService;
import com.phoenix.read.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private TimeUtil timeUtil;
    @Override
    public String report(ReportRequest request,Long userId){
        Report report=new Report(null,userId,request.getPassageId(),request.getReason(),0,timeUtil.getCurrentTimestamp());
        reportMapper.insert(report);
        return "举报成功";
    }
}
