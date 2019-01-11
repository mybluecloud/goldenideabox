package com.triz.goldenideabox.service.impl;

import com.triz.goldenideabox.model.Document;
import com.triz.goldenideabox.dao.DocumentMapper;
import com.triz.goldenideabox.service.DocumentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("DocumentService")
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentMapper documentMapper;

    @Override
    public int upload(Document document) {
        return documentMapper.addDocument(document);
    }

    @Override
    public Document getDocumentByID(int id) {
        return documentMapper.selectDocumentByID(id);
    }

    @Override
    public int deleteDocument(Document document) {
        return documentMapper.deleteDocumentByID(document.getId());
    }

    @Override
    public List<Document> getDocumentByPatentID(int id) {
        return documentMapper.selectDocumentByPatentID(id);
    }

    @Override
    public List<Document> getDocumentByMd5hex(String md5) {
        return documentMapper.getDocumentByMd5hex(md5);
    }
}
