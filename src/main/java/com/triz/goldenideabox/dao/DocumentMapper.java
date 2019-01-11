package com.triz.goldenideabox.dao;

import com.triz.goldenideabox.model.Document;
import java.util.List;

public interface DocumentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Document record);

    int insertSelective(Document record);

    Document selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Document record);

    int updateByPrimaryKey(Document record);

    int addDocument(Document record);

    Document selectDocumentByID(Integer id);

    int deleteDocumentByID(Integer id);

    List<Document> selectDocumentByPatentID(Integer id);

    List<Document> getDocumentByMd5hex(String md5);
}