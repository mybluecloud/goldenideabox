package com.triz.goldenideabox.service;

import com.triz.goldenideabox.model.OperationLog;
import java.util.List;

public interface OperationLogService {

    List<OperationLog> getOperationLog(int cpquery);
}
