package cz.vutbr.fit.maros.dip.service;

import org.json.simple.JSONObject;

public interface FileService {

    void writeRawObjectToFile(JSONObject data);

    void writeDataToCsv(String keys, String values, String fileName);

    void appendDataToCsv(String keys, String values, String fileName, boolean addKeys);

    void appendDataToDataset(String keys, String values, String fileName);

    void appendDataToCompareDataset(String keys, String values, String fileName);

    void createCsv(String keys, String values, String fileName);

    void csvToArff(String path);


}

