package cz.vutbr.fit.maros.dip.service;

import cz.vutbr.fit.maros.dip.model.Fixture;
import java.util.List;

public interface DatasetService {

    int initializeDataset();

    int divideDatasets();

    int makeAllPredictions();

    int addIndexesToDataset();

    List<Fixture> getRemainingFixtures();
}

