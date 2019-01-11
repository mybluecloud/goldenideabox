package com.triz.goldenideabox.service.impl;

import com.triz.goldenideabox.dao.OperationLogMapper;
import com.triz.goldenideabox.model.OperationLog;
import com.triz.goldenideabox.service.OperationLogService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("OperationLogService")
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;


    @Override
    public List<OperationLog> getOperationLog(int cpquery) {
        return operationLogMapper.selectByCategory(cpquery);
    }
}
