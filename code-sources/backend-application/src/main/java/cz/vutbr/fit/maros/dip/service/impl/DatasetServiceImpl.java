package cz.vutbr.fit.maros.dip.service.impl;

import cz.vutbr.fit.maros.dip.constants.ApiConstants;
import cz.vutbr.fit.maros.dip.exception.CustomException;
import cz.vutbr.fit.maros.dip.model.Fixture;
import cz.vutbr.fit.maros.dip.service.DatasetService;
import cz.vutbr.fit.maros.dip.service.FileService;
import cz.vutbr.fit.maros.dip.service.PlayerService;
import cz.vutbr.fit.maros.dip.service.TeamService;
import cz.vutbr.fit.maros.dip.util.DatasetUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.timeseries.WekaForecaster;
import weka.core.Instances;

@Service
public class DatasetServiceImpl implements DatasetService {

    private static final Logger LOG = LoggerFactory.getLogger(DatasetServiceImpl.class);
    private final FileService fileService;
    private final TeamService teamService;
    private final PlayerService playerService;

    public DatasetServiceImpl(FileService fileService, TeamService teamService, PlayerService playerService) {
        this.fileService = fileService;
        this.teamService = teamService;
        this.playerService = playerService;
    }

    public int initializeDataset() {

        String[] keys = {"total_points", "bps", "creativity", "threat", "influence", "ict_index", "minutes", "value",
            "goals_scored", "assists", "yellow_cards", "red_cards", "goals_conceded", "saves", "was_home", "opponent_team" };

        String url = "players/";

        List<String> currentSeasonPlayers = DatasetUtils.getCurrentSeasonPlayers();
        List<Fixture> remainingFixtures = getRemainingFixtures();

        try {
            Path path = Paths.get(ApiConstants.DATASET_URL);
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new CustomException("Couldn't delete file for writing data.", e);
        }
        LOG.info("Start of initializing dataset.");
        for (int year = 1; year < 5; year++) {
            LOG.info("Initializing dataset: year " + year + ".");
            String path = DatasetUtils.getFilePath(year);
            File file = new File(path);
            String[] directories = file.list((current, name) -> new File(current, name).isDirectory());

            if (directories != null) {
                for (final String directory : directories) {
                    int[] minutes = new int[38];
                    int lastYearIndex = 0;
                    String newName = DatasetUtils.getNewName(directory);
                    if (currentSeasonPlayers.contains(newName)) {
                        StringBuilder sb = new StringBuilder();
                        BufferedReader br;
                        try {
                            br = new BufferedReader(new FileReader(path + directory + "/gw.csv"));
                            String header = br.readLine();
                            Integer[] indexes = DatasetUtils.getIndexes(header, keys);
                            String filteredHeader = "gw_index," + DatasetUtils.filterLine(header, indexes);
                            String line;

                            int index;
                            File existingFile = new File(ApiConstants.DATASET_URL + url + newName + ".csv");
                            if (existingFile.exists()) {
                                BufferedReader br2 = new BufferedReader(new FileReader(existingFile));
                                String lastLine = "";
                                String currentLine = "";
                                while ((currentLine = br2.readLine()) != null) {
                                    lastLine = currentLine;
                                }
                                String[] split = lastLine.split(",");
                                index = Integer.parseInt(split[0]) + 1;
                            } else {
                                index = 1;
                            }

                            while ((line = br.readLine()) != null) {

                                String filteredLine = index + "," + DatasetUtils.filterLine(line, indexes);
                                sb.append(filteredLine).append("\n");
                                minutes[lastYearIndex] = Integer.parseInt(filteredLine.split(",")[7]);
                                index++;
                                lastYearIndex++;
                            }

                            if (year == 4) {
                                String[] names = DatasetUtils.getNewName(directory).split("_");
                                Integer teamId = teamService.getTeamByPlayerName(names[0], names[1]);
                                List<Fixture> teamFixtures = filterFixtures(teamId, remainingFixtures);
                                for (Fixture fixture : teamFixtures) {
                                    StringBuilder sb2 = new StringBuilder();
                                    String[] split = filteredHeader.split(",");
                                    sb2.append(index).append(",");
                                    for (final String s : split) {
                                        if (Objects.equals(s, "opponent_team")) {
                                            Integer home = fixture.getHomeTeam();
                                            Integer away = fixture.getAwayTeam();

                                            if (home.equals(teamId)) {
                                                sb2.append(away).append("\n");
                                            } else {
                                                sb2.append(home).append("\n");
                                            }
                                        } else if (Objects.equals(s, "minutes")) {
                                            int minCount = countMinutes(minutes, lastYearIndex);
                                            minutes[lastYearIndex] = minCount;
                                            lastYearIndex++;
                                            String min = minCount + ",";
                                            sb2.append(min);
                                        } else if (!Objects.equals(s, "gw_index")) {
                                            sb2.append("?,");
                                        }
                                    }
                                    sb.append(sb2.toString());
                                    index++;
                                }
                            }

                            fileService.appendDataToDataset(filteredHeader, sb.toString(), url + newName + ".csv");

                        } catch (FileNotFoundException e) {
                            throw new CustomException("File " + path + directory + " does not exist, please generate gw file first.");
                        } catch (IOException e) {
                            throw new CustomException("Cannot read from file " + path + directory + ".");
                        }
                    }
                }
            }
        }
        LOG.info("Finished initializing dataset.");
        return 0;
    }

    private int countMinutes(int[] minutes, int index) {
        int count;
        if (index <= 6) {
            count = Arrays.stream(minutes).sum();
            return count / index;
        } else {
            count = minutes[index - 1] + minutes[index - 2] + minutes[index - 3] + minutes[index - 4] + minutes[index - 5] + minutes[index - 6];
            return count / 6;
        }
    }

    public int divideDatasets() {

        String basePath = "dataset/players/";
        final int MAX_LAG = 8;
        LOG.info("Start of dividing dataset.");
        try (Stream<Path> walk = Files.walk(Paths.get(basePath), 1)) {

            List<String> result = walk
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::toString).collect(Collectors.toList());

            for (String playerFile : result) {
                Path path = Paths.get(playerFile);
                String playerName = path.getFileName().toString().substring(0, path.getFileName().toString().lastIndexOf("."));
                String filePath = basePath + playerName + ".csv";
                String trainPath = basePath + "train/" + playerName + ".csv";
                String primePath = basePath + "prime/" + playerName + ".csv";
                String futurePath = basePath + "future/" + playerName + ".csv";
                String trainBasePath = basePath + "train/" + playerName;
                String primeBasePath = basePath + "prime/" + playerName;
                String futureBasePath = basePath + "future/" + playerName;

                try {
                    Files.deleteIfExists(Paths.get(trainPath));
                    Files.deleteIfExists(Paths.get(primePath));
                    Files.deleteIfExists(Paths.get(futurePath));
                } catch (IOException e) {
                    throw new CustomException("Couldn't delete file for writing data.", e);
                }
                LOG.info("Deleted previous datasets.");

                List<String> lines = new ArrayList<>();
                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(filePath));
                    String header = br.readLine();
                    String line;
                    while ((line = br.readLine()) != null) {
                        lines.add(line);
                    }

                    List<String> trainingSet = new ArrayList<>();
                    List<String> primingSet = new ArrayList<>();
                    List<String> futureSet = new ArrayList<>();

                    // fake data to make same arff headers
                    trainingSet.add("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,False,0\n");
                    trainingSet.add("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,True,0\n");
                    primingSet.add("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,False,0\n");
                    primingSet.add("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,True,0\n");
                    futureSet.add("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,False,0\n");
                    futureSet.add("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,True,0\n");

                    lines.forEach(elem -> {
                        String[] split = elem.split(",");
                        if (!Objects.equals(split[1], "?")) {
                            trainingSet.add(elem);
                        } else {
                            futureSet.add(elem);
                        }
                    });
                    int primingSize = trainingSet.size() - MAX_LAG;
                    primingSet.addAll(trainingSet.subList((primingSize) < 0 ? trainingSet.size() : primingSize, trainingSet.size()));
                    StringBuilder trainingSetSb = new StringBuilder();
                    trainingSet.forEach(item -> trainingSetSb.append(item).append("\n"));
                    StringBuilder primingSetSb = new StringBuilder();
                    primingSet.forEach(item -> primingSetSb.append(item).append("\n"));
                    StringBuilder futureSetSb = new StringBuilder();
                    futureSet.forEach(item -> futureSetSb.append(item).append("\n"));

                    fileService.createCsv(header, trainingSetSb.toString(), trainPath);
                    fileService.createCsv(header, primingSetSb.toString(), primePath);
                    fileService.createCsv(header, futureSetSb.toString(), futurePath);
                    fileService.csvToArff(trainBasePath);
                    fileService.csvToArff(primeBasePath);
                    fileService.csvToArff(futureBasePath);

                } catch (IOException e) {
                    throw new CustomException("Cannot read from file " + filePath + ".");
                }
            }

        } catch (IOException e) {
            throw new CustomException("Cannot read files from directory " + basePath + ".");
        }
        LOG.info("Finished dividing dataset.");
        return 0;
    }

    public int makeAllPredictions() {

        String basePath = "dataset/players/";
        final int PREDICTIONS = 3;
        final int MAX_LAG = 8;
        StringBuilder stringBuilder1 = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        StringBuilder stringBuilder3 = new StringBuilder();
        String keys = "player_name,predicted_points";

        List<String> result;
        try (Stream<Path> walk = Files.walk(Paths.get(basePath), 1)) {
            result = walk
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new CustomException("Cannot read files from directory " + basePath + ".");
        }

        LOG.info("Started making predictions.");
        for (String playerFile : result) {
            Path path = Paths.get(playerFile);
            String playerName = path.getFileName().toString().substring(0, path.getFileName().toString().lastIndexOf("."));
            LOG.info("Making predictions for player: " + playerName);

            String trainPath = basePath + "train/" + playerName + ".csv";
            String primePath = basePath + "prime/" + playerName + ".csv";
            String futurePath = basePath + "future/" + playerName + ".csv";
            String trainBasePath = basePath + "train/" + playerName;
            String primeBasePath = basePath + "prime/" + playerName;
            String futureBasePath = basePath + "future/" + playerName;

            try {
                File trainFile = new File(trainBasePath + ".arff");
                File primeFile = new File(primeBasePath + ".arff");
                File futureFile = new File(futureBasePath + ".arff");
                Instances trainData = new Instances(new BufferedReader(new FileReader(trainFile)));
                Instances primeData = new Instances(new BufferedReader(new FileReader(primeFile)));
                Instances futureData = new Instances(new BufferedReader(new FileReader(futureFile)));

                // it's important to iterate from last to first, because when we remove
                // an instance, the rest shifts by one position.
                for (int i = futureData.numInstances() - 1; i >= 0; i--) {
                    if (i <= 1) {
                        trainData.delete(i);
                        primeData.delete(i);
                        futureData.delete(i);
                    }
                }

                if (trainData.numInstances() <= MAX_LAG) {
                    stringBuilder1.append(playerName).append(",").append("?").append("\n");
                    stringBuilder2.append(playerName).append(",").append("?").append("\n");
                    stringBuilder3.append(playerName).append(",").append("?").append("\n");
                } else {
                    for (int i = 1; i <= PREDICTIONS; i++) {
                        WekaForecaster forecaster = new WekaForecaster();
                        forecaster.setFieldsToForecast("total_points");
                        forecaster.setBaseForecaster(new MultilayerPerceptron());
                        forecaster.getTSLagMaker().setTimeStampField("gw_index");
                        forecaster.getTSLagMaker().setMinLag(1);
                        forecaster.getTSLagMaker().setMaxLag(MAX_LAG);
                        forecaster.getTSLagMaker().setAdjustForTrends(true);
                        forecaster.getTSLagMaker().setRemoveLeadingInstancesWithUnknownLagValues(true);
                        forecaster.setOverlayFields("opponent_team");
                        forecaster.setOverlayFields("minutes");

                        forecaster.buildForecaster(trainData, System.out);

                        forecaster.primeForecaster(primeData);
                        List<List<NumericPrediction>> forecast = forecaster.forecast(i, futureData, System.out);
                        for (int j = 0; j < i; j++) {
                            List<NumericPrediction> predsAtStep = forecast.get(j);
                            for (NumericPrediction predForTarget : predsAtStep) {
                                double scaledPrediction = scalePrediction(predForTarget.predicted());
                                switch (i) {
                                    case 1:
                                        stringBuilder1.append(playerName).append(",").append(Math.floor(scaledPrediction * 100) / 100).append("\n");
                                        break;
                                    case 2:
                                        stringBuilder2.append(playerName).append(",").append(Math.floor(scaledPrediction * 100) / 100).append("\n");
                                        break;
                                    case 3:
                                        stringBuilder3.append(playerName).append(",").append(Math.floor(scaledPrediction * 100) / 100).append("\n");
                                        break;
                                    default:
                                        throw new IllegalStateException("Unexpected value: " + i);
                                }
                            }
                        }
                    }
                }


            } catch (IOException e) {
                throw new CustomException("Cannot read from one of the files: " + trainPath + ", " + primePath + ", " + futurePath + ".", e);
            } catch (Exception e) {
                throw new CustomException("Cannot perform forecast", e);
            }
        }

        LOG.info("Finished making predictions.");
        fileService.createCsv(keys, stringBuilder1.toString(), basePath + "predictions/1gw.csv");
        fileService.createCsv(keys, stringBuilder2.toString(), basePath + "predictions/2gw.csv");
        fileService.createCsv(keys, stringBuilder3.toString(), basePath + "predictions/3gw.csv");
        return 0;
    }

    private double scalePrediction(double pred) {
        double result = Math.abs(pred);
        if (result <= 10) {
            result *= 1.0;
        } else if (result <= 20) {
            result *= 0.9;
        } else if (result <= 30) {
            result *= 0.8;
        }
        result *= 0.7;
        return result;
    }

    public int addIndexesToDataset() {

        String basePath = "dataset/players/";
        String[] keys = {"total_points", "gw_index"};

        LOG.info("Started adding indexes to dataset.");
        LOG.info("Started counting indexes.");
        List<String> result;
        try (Stream<Path> walk = Files.walk(Paths.get(basePath), 1)) {
            result = walk
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new CustomException("Cannot read files from directory " + basePath + ".");
        }
        double maxPointsPerMatch = Double.MIN_VALUE;
        double maxPointsPerMatchLast6 = Double.MIN_VALUE;
        double minPointsPerMatch = Double.MAX_VALUE;
        double minPointsPerMatchLast6 = Double.MAX_VALUE;
        double maxCostPerPoint = Double.MIN_VALUE;
        double maxCostPerPointLast6 = Double.MIN_VALUE;
        double minCostPerPoint = Double.MAX_VALUE;
        double minCostPerPointLast6 = Double.MAX_VALUE;

        for (final String playerFile : result) {
            Path path = Paths.get(playerFile);
            BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(playerFile));
                String header = br.readLine();
                Integer[] indexes = DatasetUtils.getIndexes(header, keys);
                String line;
                String playerName = path.getFileName().toString().substring(0, path.getFileName().toString().lastIndexOf("."));

                double gws = 0.0;
                double totalPoints = 0.0;
                List<Integer> points = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    String filteredLine = DatasetUtils.filterLine(line, indexes);
                    String[] split = filteredLine.split(",");
                    if (split[0].equals("?")) {
                        break;
                    }
                    gws = Double.parseDouble(split[1]);
                    totalPoints += Integer.parseInt(split[0]);
                    points.add(Integer.parseInt(split[0]));
                }
                List<Integer> tail = points.subList(Math.max(points.size() - 6, 0), points.size());
                double last6 = 0.0;
                for (final Integer x : tail) {
                    last6 += x;
                }
                int prize = playerService.getPlayerPrize(playerName) / 10;
                double costPerPoint = totalPoints > 0 ? Math.round(prize / totalPoints * 10000.0) / 10000.0 : 0.0;
                double costPerPointLast6 = last6 > 0 ? Math.round(prize / last6 * 10000.0) / 10000.0 : 0.0;
                double pointsPerMatch = gws > 0 ? Math.round(totalPoints / gws * 100.0) / 100.0 : 0.0;
                double pointsPerMatchLast6 = Math.round(last6 / 6.0 + 100.0) / 100.0;
                if (costPerPoint < minCostPerPoint) {
                    minCostPerPoint = costPerPoint;
                }
                if (costPerPoint > maxCostPerPoint) {
                    maxCostPerPoint = costPerPoint;
                }
                if (costPerPointLast6 < minCostPerPointLast6) {
                    minCostPerPointLast6 = costPerPointLast6;
                }
                if (costPerPointLast6 > maxCostPerPointLast6) {
                    maxCostPerPointLast6 = costPerPointLast6;
                }
                if (pointsPerMatch < minPointsPerMatch) {
                    minPointsPerMatch = pointsPerMatch;
                }
                if (pointsPerMatch > maxPointsPerMatch) {
                    maxPointsPerMatch = pointsPerMatch;
                }
                if (pointsPerMatchLast6 < minPointsPerMatchLast6) {
                    minPointsPerMatchLast6 = pointsPerMatchLast6;
                }
                if (pointsPerMatchLast6 > maxPointsPerMatchLast6) {
                    maxPointsPerMatchLast6 = pointsPerMatchLast6;
                }

            } catch (FileNotFoundException e) {
                throw new CustomException("File " + playerFile + " does not exist, please generate file first.");
            } catch (IOException e) {
                throw new CustomException("Cannot read from file " + playerFile + ".");
            }
        }

        LOG.info("Finished counting indexes.");
        String filePath = "players/stats/stats.csv";
        try {
            Files.deleteIfExists(Paths.get("dataset/" + filePath));
        } catch (IOException e) {
            throw new CustomException("Couldn't delete file for writing data.", e);
        }

        for (final String playerFile : result) {
            Path path = Paths.get(playerFile);
            StringBuilder sb = new StringBuilder();
            BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(playerFile));
                String header = br.readLine();
                Integer[] indexes = DatasetUtils.getIndexes(header, keys);
                String line;
                String playerName = path.getFileName().toString().substring(0, path.getFileName().toString().lastIndexOf("."));

                double gws = 0.0;
                double totalPoints = 0.0;
                String newHeader = "player_name,total_points,gws,cost,position,points_per_match,points_last_6,points_per_match_last_6,cost_per_point,cost_per_point_last_6,cost_point_index,cost_point_index_last_6";
                List<Integer> points = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    String filteredLine = DatasetUtils.filterLine(line, indexes);
                    String[] split = filteredLine.split(",");
                    if (split[0].equals("?")) {
                        break;
                    }
                    gws = Double.parseDouble(split[1]);
                    totalPoints += Integer.parseInt(split[0]);
                    points.add(Integer.parseInt(split[0]));
                }
                List<Integer> tail = points.subList(Math.max(points.size() - 6, 0), points.size());
                double last6 = 0.0;
                for (final Integer x : tail) {
                    last6 += x;
                }
                int prize = playerService.getPlayerPrize(playerName) / 10;
                int position = playerService.getPlayerPosition(playerName);
                if (gws == 0) {
                    sb.append(0).append(",").append(0).append(",").append(prize).append(",").append(position).append(",");
                    sb.append(0).append(",").append(0).append(",").append(0).append(",");
                    sb.append(0).append(",").append(0).append(",").append(0).append(",").append(0);
                } else {
                    double costPerPoint = totalPoints > 0 ? Math.round(prize / totalPoints * 10000.0) / 10000.0 : 0.0;
                    double costPerPointLast6 = last6 > 0 ? Math.round(prize / last6 * 10000.0) / 10000.0 : 0.0;
                    double pointsPerMatch = Math.round(totalPoints / gws * 100.0) / 100.0;
                    double pointsPerMatchLast6 = Math.round(last6 / 6.0 + 100.0) / 100.0;
                    double normalizedCostPerPoint = (costPerPoint - minCostPerPoint) / (maxCostPerPoint - minCostPerPoint);
                    double normalizedCostPerPointLast6 = (costPerPointLast6 - minCostPerPointLast6) / (maxCostPerPointLast6 - minCostPerPointLast6);
                    double normalizedPointsPerMatch = (pointsPerMatch - minPointsPerMatch) / (maxPointsPerMatch - minPointsPerMatch);
                    double normalizedPointsPerMatchLast6 = (pointsPerMatchLast6 - minPointsPerMatchLast6) / (maxPointsPerMatchLast6 - minPointsPerMatchLast6);
                    Double cpi = Math.round(normalizedCostPerPoint + normalizedPointsPerMatch * 10000.0) / 10000.0;
                    Double cpiLast6 = Math.round(normalizedCostPerPointLast6 + normalizedPointsPerMatchLast6 * 10000.0) / 10000.0;

                    sb.append(playerName).append(",");
                    sb.append(totalPoints).append(",");
                    sb.append(gws).append(",");
                    sb.append(prize).append(",");
                    sb.append(position).append(",");
                    sb.append(pointsPerMatch).append(",").append(last6).append(",").append(pointsPerMatchLast6).append(",");
                    sb.append(costPerPoint).append(",").append(costPerPointLast6).append(",");
                    sb.append(cpi).append(",").append(cpiLast6);
                }
                sb.append("\n");
                fileService.appendDataToDataset(newHeader, sb.toString(), filePath);

            } catch (FileNotFoundException e) {
                throw new CustomException("File " + playerFile + " does not exist, please generate file first.");
            } catch (IOException e) {
                throw new CustomException("Cannot read from file " + playerFile + ".");
            }
        }

        LOG.info("Finished writing indexes.");
        return 0;
    }

    public List<Fixture> getRemainingFixtures() {
        String path = "data/2019-20/fixtures.csv";
        File file = new File(path);

        List<Fixture> fixtures = new ArrayList<>();
        BufferedReader br;

        String[] keys = {"event", "finished", "team_h", "team_a", "team_h_difficulty", "team_a_difficulty" };

        try {
            br = new BufferedReader(new FileReader(file));
            String header = br.readLine();
            Integer[] indexes = DatasetUtils.getIndexes(header, keys);
            String line;

            while ((line = br.readLine()) != null) {

                String filteredLine = DatasetUtils.filterLine(line, indexes);
                String[] split = filteredLine.split(",");
                if (!Objects.equals(split[0], "") && Objects.equals(split[1], "False")) {
                    final Fixture fixture = new Fixture(
                            Double.parseDouble(split[0]),
                            Boolean.parseBoolean(split[1]),
                            Integer.parseInt(split[2]),
                            Integer.parseInt(split[3]),
                            Integer.parseInt(split[4]),
                            Integer.parseInt(split[5])
                    );
                    fixtures.add(fixture);
                }
            }

        } catch (FileNotFoundException e) {
            throw new CustomException("File " + path + " does not exist, please generate fixtures file first.");
        } catch (IOException e) {
            throw new CustomException("Cannot read from file " + path + ".");
        }
        return fixtures;
    }

    private List<Fixture> filterFixtures(Integer teamId, List<Fixture> remainingFixtures) {
        List<Fixture> fixtureList = new ArrayList<>();

        for (Fixture fixture : remainingFixtures) {
            if (fixture.getHomeTeam() == teamId || fixture.getAwayTeam() == teamId) {
                fixtureList.add(fixture);
            }
        }
        return fixtureList;
    }

}