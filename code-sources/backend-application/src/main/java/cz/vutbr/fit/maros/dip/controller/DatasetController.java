package cz.vutbr.fit.maros.dip.controller;

import cz.vutbr.fit.maros.dip.results.ResponseWrapper;
import cz.vutbr.fit.maros.dip.service.ComparisonService;
import cz.vutbr.fit.maros.dip.service.DatasetService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/dataset")
public class DatasetController {

    private final DatasetService datasetService;
    private final ComparisonService comparisonService;

    public DatasetController(DatasetService datasetService, ComparisonService comparisonService) {
        this.datasetService = datasetService;
        this.comparisonService = comparisonService;
    }

    @GetMapping(value = "/init")
    public ResponseWrapper<Object> initializeDataset() {
        return new ResponseWrapper<>(datasetService.initializeDataset(), HttpStatus.OK);
    }

    @GetMapping(value = "/divide")
    public ResponseWrapper<Object> divideDatasets() {
        return new ResponseWrapper<>(datasetService.divideDatasets(), HttpStatus.OK);
    }

    @GetMapping(value = "/predict-all")
    public ResponseWrapper<Object> predictAll() {
        return new ResponseWrapper<>(datasetService.makeAllPredictions(), HttpStatus.OK);
    }

    @GetMapping(value = "/stats")
    public ResponseWrapper<Object> addIndexes() {
        return new ResponseWrapper<>(datasetService.addIndexesToDataset(), HttpStatus.OK);
    }

    @GetMapping(value = "/compare")
    public ResponseWrapper<Object> createDataForComparison() {
        return new ResponseWrapper<>(comparisonService.createDataForComparison(), HttpStatus.OK);
    }

}
