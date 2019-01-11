package com.triz.goldenideabox.dao;

import com.triz.goldenideabox.model.DashBoard;
import java.util.List;

public interface DashBoardMapper {

    List<DashBoard> selectPatentCount(String resUrl);
}
