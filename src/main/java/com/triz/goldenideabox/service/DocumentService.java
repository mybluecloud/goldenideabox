package com.triz.goldenideabox.service;

import com.triz.goldenideabox.model.Document;
import java.util.List;

public interface DocumentService {

    int upload(Document document);

    Document getDocumentByID(int id);

    int deleteDocument(Document document);

    List<Document> getDocumentByPatentID(int id);

    List<Document> getDocumentByMd5hex(String md5);
}
