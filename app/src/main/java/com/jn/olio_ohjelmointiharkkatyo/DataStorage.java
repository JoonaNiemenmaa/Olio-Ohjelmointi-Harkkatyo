package com.jn.olio_ohjelmointiharkkatyo;

public class DataStorage {
    private static DataStorage data_storage;
    private MunicipalityData municipality;
    private DataStorage() {}
    public static DataStorage getInstance() {
        if (data_storage == null) {
            data_storage = new DataStorage();
        }
        return data_storage;
    }

    public MunicipalityData getMunicipality() {
        return municipality;
    }

    public void setMunicipality(MunicipalityData municipality) {
        this.municipality = municipality;
    }
}
