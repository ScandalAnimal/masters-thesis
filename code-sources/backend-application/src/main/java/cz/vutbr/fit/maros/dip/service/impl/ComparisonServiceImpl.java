package cz.vutbr.fit.maros.dip.service.impl;

import cz.vutbr.fit.maros.dip.constants.ApiConstants;
import cz.vutbr.fit.maros.dip.exception.CustomException;
import cz.vutbr.fit.maros.dip.model.BetterPlayers;
import cz.vutbr.fit.maros.dip.model.Fixture;
import cz.vutbr.fit.maros.dip.model.OptimizeRequest;
import cz.vutbr.fit.maros.dip.model.OptimizedSquad;
import cz.vutbr.fit.maros.dip.model.OptimizedSquads;
import cz.vutbr.fit.maros.dip.model.PlayerId;
import cz.vutbr.fit.maros.dip.model.PlayerStats;
import cz.vutbr.fit.maros.dip.model.PlayerTeam;
import cz.vutbr.fit.maros.dip.model.PredictedPointsStats;
import cz.vutbr.fit.maros.dip.model.TeamPlayer;
import cz.vutbr.fit.maros.dip.model.Technique;
import cz.vutbr.fit.maros.dip.service.ComparisonService;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ComparisonServiceImpl implements ComparisonService {

    private static final Logger LOG = LoggerFactory.getLogger(ComparisonServiceImpl.class);

    private final DatasetService datasetService;
    private final TeamService teamService;
    private final FileService fileService;
    private final PlayerService playerService;

    public ComparisonServiceImpl(DatasetService datasetService, TeamService teamService, FileService fileService,
            PlayerService playerService) {
        this.datasetService = datasetService;
        this.teamService = teamService;
        this.fileService = fileService;
        this.playerService = playerService;
    }

    public int createDataForComparison() {

        int gwCount = 20;
        double minGw = 10.0;
//        Map<String, List<String>> futureData = initializeDatasetForEachGameWeek(minGw, gwCount);
//        for (double i = minGw; i < gwCount; i++) {
//            divideDatasets("compare-dataset/players/" + (int) i + "/");
//        }
//        for (double i = minGw; i < gwCount; i++) {
//            makeAllPredictions("compare-dataset/players/" + (int) i + "/", futureData, (int) i);
//        }
//
//        for (double i = minGw; i < gwCount; i++) {
//            addIndexesToDataset("compare-dataset/players/" + (int) i + "/", (int) i);
//        }


//        List<Long> teamIds = Arrays.asList(93L,304L,181L,252L,381L,219L,218L,392L,150L,460L,187L,319L,185L,463L,11L);
//        OptimizedSquad week9Team = new OptimizedSquad();
//        week9Team.setTeam(teamIds);
//        week9Team.setCaptain(460L);
//        week9Team.setViceCaptain(93L);

        List<Long> teamIds = Arrays.asList(93L,181L,103L,65L,297L,215L,287L,463L,470L,460L,67L,48L,208L,191L,210L);
        OptimizedSquad week9Team = new OptimizedSquad();
        week9Team.setTeam(teamIds);
        week9Team.setCaptain(103L);
        week9Team.setViceCaptain(210L);

        List<Integer> week9TotalPoints = countTotalPointsForSquad(week9Team, 9);
        System.out.println("ALL: week 9 points: [squad] " + week9TotalPoints.get(0) + ", [bench] " + week9TotalPoints.get(1));

        List<Long> newTeamIds = teamIds;
        System.out.println("ONE TRANSFER");
        for (int i = (int) minGw; i < gwCount; i++) {
            newTeamIds = countPointsForAllWeeks(newTeamIds, "max", i, 1, false);
        }
        newTeamIds = teamIds;
        for (int i = (int) minGw; i < gwCount; i++) {
            newTeamIds = countPointsForAllWeeks(newTeamIds, "total", i, 1, false);
        }
        newTeamIds = teamIds;
        for (int i = (int) minGw; i < gwCount; i++) {
            newTeamIds = countPointsForAllWeeks(newTeamIds, "form", i, 1, false);
        }
        System.out.println("TWO TRANSFERS every other week");
        for (int i = (int) minGw; i < gwCount; i++) {
            newTeamIds = countPointsForAllWeeks(newTeamIds, "max", i, 2, false);
        }
        newTeamIds = teamIds;
        for (int i = (int) minGw; i < gwCount; i++) {
            newTeamIds = countPointsForAllWeeks(newTeamIds, "total", i, 2, false);
        }
        newTeamIds = teamIds;
        for (int i = (int) minGw; i < gwCount; i++) {
            newTeamIds = countPointsForAllWeeks(newTeamIds, "form", i, 2, false);
        }
        System.out.println("TWO TRANSFERS every other week - reversed");
        for (int i = (int) minGw; i < gwCount; i++) {
            newTeamIds = countPointsForAllWeeks(newTeamIds, "max", i, 2, true);
        }
        newTeamIds = teamIds;
        for (int i = (int) minGw; i < gwCount; i++) {
            newTeamIds = countPointsForAllWeeks(newTeamIds, "total", i, 2, true);
        }
        newTeamIds = teamIds;
        for (int i = (int) minGw; i < gwCount; i++) {
            newTeamIds = countPointsForAllWeeks(newTeamIds, "form", i, 2, true);
        }

        return 0;
    }

    private List<Long> countPointsForAllWeeks(List<Long> teamIds, String technique, Integer gw, Integer transfers, boolean reversed) {
        TeamPlayer[] weekTeam = new TeamPlayer[15];
        for (int j = 0; j < 15; j++) {
            Long playerId = teamIds.get(j);
            String playerName = getPlayerNameById(playerId);
            Integer prize = playerService.getPlayerPrize(playerName);
            TeamPlayer teamPlayer = new TeamPlayer();
            teamPlayer.setId(playerId);
            teamPlayer.setNowCost((long) prize);
            teamPlayer.setPlayerName(playerName);
            weekTeam[j] = teamPlayer;
        }
        long t = 0L;
        if (transfers == 1) {
            t = 1L;
        }
        if (transfers == 2 && gw % 2 == 0) {
            t = 2L;
        }
        if (reversed) {
            if (t == 2L) {
                t = 0L;
            } else if (t == 0L) {
                t = 2L;
            }
        }

        OptimizeRequest squadWeek = new OptimizeRequest();
        squadWeek.setTechnique(technique);
        squadWeek.setTeam(weekTeam);
        squadWeek.setTips(1L);
        squadWeek.setGameWeeks(1L);
        squadWeek.setTransfers(t);
        OptimizedSquads optimizedSquads = getOptimizedSquads(squadWeek, gw);
        if (optimizedSquads.getSquads() == null) {
            return teamIds;
        }
        OptimizedSquad newSquad = optimizedSquads.getSquads().get(0);

        List<Long> newTeam = newSquad.getTeam();
        List<Integer> totalPoints = countTotalPointsForSquad(newSquad, gw);
        System.out.println(technique.toUpperCase() + ": week " + gw + " points: [squad] " + totalPoints.get(0) + ", [bench] " + totalPoints.get(1));
        return newTeam;
    }

    private List<Integer> countTotalPointsForSquad(OptimizedSquad squad, Integer gw) {

        List<Long> team = squad.getTeam();
        List<Long> playingSquad = team.subList(0, 11);
        List<Long> bench = team.subList(11, 15);

        List<String> playingSquadNames = new ArrayList<>();
        List<String> benchNames = new ArrayList<>();
        String[] keys = {"total_points"};
        Map<String, Integer> pointMap = new HashMap<>();
        Map<String, Integer> benchPointMap = new HashMap<>();
        String captainName = "";
        String viceCaptainName = "";

        List<String> currentSeasonPlayers = DatasetUtils.getCurrentSeasonPlayers();
        List<PlayerId> ids = playerService.getAllPlayersIds();
        for (final PlayerId playerId : ids) {
            if (playingSquad.contains(playerId.getId())) {
                playingSquadNames.add(playerId.getFirstName() + "_" + playerId.getSecondName());
            }
            if (bench.contains(playerId.getId())) {
                benchNames.add(playerId.getFirstName() + "_" + playerId.getSecondName());
            }
            if (playerId.getId().equals(squad.getCaptain())) {
                captainName = playerId.getFirstName() + "_" + playerId.getSecondName();
            }
            if (playerId.getId().equals(squad.getViceCaptain())) {
                viceCaptainName = playerId.getFirstName() + "_" + playerId.getSecondName();
            }
        }
        String path = DatasetUtils.getFilePath(4);
        File file = new File(path);
        String[] directories = file.list((current, name) -> new File(current, name).isDirectory());

        if (directories != null) {
            for (final String directory : directories) {
                int index = 1;
                String newName = DatasetUtils.getNewName(directory);
                if (playingSquadNames.contains(newName) || benchNames.contains(newName)) {
                    if (currentSeasonPlayers.contains(newName)) {
                        BufferedReader br;
                        try {
                            br = new BufferedReader(new FileReader(path + directory + "/gw.csv"));
                            String header = br.readLine();
                            Integer[] indexes = DatasetUtils.getIndexes(header, keys);
                            String line;

                            while ((line = br.readLine()) != null) {

                                if (index == gw) {
                                    String filteredLine = DatasetUtils.filterLine(line, indexes);
                                    String[] filteredLineArray = filteredLine.split(",");
                                    if (playingSquadNames.contains(newName)) {
                                        pointMap.put(newName, Integer.valueOf(filteredLineArray[0]));
                                    }
                                    if (benchNames.contains(newName)) {
                                        benchPointMap.put(newName, Integer.valueOf(filteredLineArray[0]));
                                    }
                                }
                                index++;
                            }

                        } catch (FileNotFoundException e) {
                            throw new CustomException("File " + path + directory + " does not exist, please generate gw file first.");
                        } catch (IOException e) {
                            throw new CustomException("Cannot read from file " + path + directory + ".");
                        }
                    }
                }
            }
        }
        int pointsResult = 0;
        int benchResult = 0;
        boolean capPlayed = false;
        for (Map.Entry<String, Integer> entry : pointMap.entrySet()) {
            String key = entry.getKey();
            Integer v = entry.getValue();
            pointsResult += v;
            if (captainName.equals(key)) {
                pointsResult += v;
                capPlayed = true;
            }
        }
        for (Map.Entry<String, Integer> entry : benchPointMap.entrySet()) {
            String name = entry.getKey();
            Integer value = entry.getValue();
            benchResult += value;
            if (!capPlayed && viceCaptainName.equals(name)) {
                benchResult += value;
            }
        }

        List<Integer> results = new ArrayList<>();
        results.add(pointsResult);
        results.add(benchResult);

        return results;
    }

    private String getPlayerNameById(Long id) {
        List<PlayerId> playerIds = playerService.getAllPlayersIds();

        return playerIds
                .stream()
                .filter(player -> player.getId().equals(id))
                .map(player -> player.getFirstName() + "_" + player.getSecondName())
                .findFirst()
                .orElse(null);
    }

    private Map<String, List<String>> initializeDatasetForEachGameWeek(double minGw, int gameWeekCount) {

        String[] keys = {"total_points", "bps", "creativity", "threat", "influence", "ict_index", "minutes", "value",
            "goals_scored", "assists", "yellow_cards", "red_cards", "goals_conceded", "saves", "was_home", "opponent_team" };

        String url = "players/";
        Map<String, List<String>> futurePlayerData = new HashMap<>();
        List<String> currentSeasonPlayers = DatasetUtils.getCurrentSeasonPlayers();

        try {
            Path path = Paths.get(ApiConstants.COMPARE_DATASET_URL);
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new CustomException("Couldn't delete file for writing data.", e);
        }
        LOG.info("Start of initializing dataset for comparison.");
        for (int year = 1; year < 5; year++) {
            LOG.info("Initializing dataset: year " + year + ".");
            String path = DatasetUtils.getFilePath(year);
            File file = new File(path);
            String[] directories = file.list((current, name) -> new File(current, name).isDirectory());

            if (directories != null) {
                for (final String directory : directories) {
                    String newName = DatasetUtils.getNewName(directory);
                    if (currentSeasonPlayers.contains(newName)) {
                        int[] minutes = new int[38];
                        int lastYearIndex = 0;
                        List<String> futureData = new ArrayList<>();
                        System.out.println(newName);
                        StringBuilder sb = new StringBuilder();
                        BufferedReader br;
                        try {
                            br = new BufferedReader(new FileReader(path + directory + "/gw.csv"));
                            String header = br.readLine();
                            Integer[] indexes = DatasetUtils.getIndexes(header, keys);
                            String filteredHeader = "gw_index," + DatasetUtils.filterLine(header, indexes);
                            String line;

                            int index;
                            File existingFile = new File(ApiConstants.COMPARE_DATASET_URL + url + 10 + "/" + newName + ".csv");
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

                            int index4 = index;

                            while ((line = br.readLine()) != null) {

                                String filteredLine;
                                if (year == 4) {
                                    filteredLine = index4 + "," + DatasetUtils.filterLine(line, indexes);
                                } else {
                                    filteredLine = index + "," + DatasetUtils.filterLine(line, indexes);
                                }
                                sb.append(filteredLine).append("\n");
                                if (year == 4) {
                                    futureData.add(filteredLine);
                                    index4++;
                                }
                                if (year != 4) {
                                    minutes[lastYearIndex] = Integer.parseInt(filteredLine.split(",")[7]);
                                    index++;
                                }
                                lastYearIndex++;
                            }
                            futurePlayerData.put(newName, futureData);
                            System.out.println("Index: " + index4);
                            if (year == 4) {
                                System.out.print("Writing gameweek: ");
                                for (double gw = minGw; gw <= gameWeekCount; gw = gw + 1.0) {
                                    System.out.print((int) gw + ", ");
                                    StringBuilder compareSb = new StringBuilder();
                                    for (int i = 0; i < gw - 1; i++) {
                                        if (futureData.size() > i) {
                                            minutes[i] = Integer.parseInt(futureData.get(i).split(",")[7]);
                                            compareSb.append(futureData.get(i)).append("\n");
                                        }
                                    }
                                    index4 = index + ((int) gw - 1);
                                    String[] names = DatasetUtils.getNewName(directory).split("_");
                                    Integer teamId = teamService.getTeamByPlayerName(names[0], names[1]);
                                    List<Fixture> newFixtures = getRemainingFixtures(gw);
                                    List<Fixture> teamFixtures = filterFixtures(teamId, newFixtures);
                                    for (Fixture fixture : teamFixtures) {
                                        StringBuilder sb2 = new StringBuilder();
                                        String[] split = filteredHeader.split(",");
                                        sb2.append(index4).append(",");
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
                                                int minCount = countMinutes(minutes, (int) gw);
                                                if (minCount > 90) {
                                                    minCount = 90;
                                                }
                                                minutes[(int) gw] = minCount;
                                                String min = minCount + ",";
                                                sb2.append(min);
                                            } else if (!Objects.equals(s, "gw_index")) {
                                                sb2.append("?,");
                                            }
                                        }
                                        compareSb.append(sb2.toString());
                                        index4++;
                                    }
                                    fileService.appendDataToCompareDataset(filteredHeader, compareSb.toString(), url + (int) gw + "/" + newName + ".csv");
                                }
                                System.out.println();
                            }
                            if (year != 4) {
                                for (double gw = minGw; gw <= gameWeekCount; gw = gw + 1.0) {
                                    fileService.appendDataToCompareDataset(filteredHeader, sb.toString(), url + (int) gw + "/" + newName + ".csv");
                                }
                            }

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
        return futurePlayerData;
    }

    private void divideDatasets(String basePath) {
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

    private int countMinutes(int[] minutes, int index) {
        int count;
        if (index <= 6) {
            count = Arrays.stream(minutes).sum();
            if (count == 0) {
                return 90;
            }
            return count / index;
        } else {
            count = minutes[index - 1] + minutes[index - 2] + minutes[index - 3] + minutes[index - 4] + minutes[index - 5] + minutes[index - 6];
            return count / 6;
        }
    }

    public List<Fixture> getRemainingFixtures(double gameWeek) {
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
                if (!Objects.equals(split[0], "")) {
                    double event = Double.parseDouble(split[0]);
                    if (event >= gameWeek) {
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
            }

        } catch (FileNotFoundException e) {
            throw new CustomException("File " + path + " does not exist, please generate fixtures file first.");
        } catch (IOException e) {
            throw new CustomException("Cannot read from file " + path + ".");
        }
        return fixtures;
    }

    public void makeAllPredictions(String basePath, Map<String, List<String>> futurePlayerData, int gw) {

        final int PREDICTIONS = 1;
        final int MAX_LAG = 8;
        StringBuilder stringBuilder1 = new StringBuilder();
        String keys = "player_name,predicted_points,actual_points";

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
                List<String> nameRecord = futurePlayerData.get(playerName);
                String actual = "?";
                if (nameRecord.size() > gw) {
                    actual = nameRecord.get(gw).split(",")[1];
                }

                if (trainData.numInstances() <= MAX_LAG) {
                    stringBuilder1.append(playerName).append(",").append("?").append(",").append(actual).append("\n");
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
                                stringBuilder1.append(playerName).append(",").append(Math.floor(scaledPrediction * 100) / 100).append(",").append(actual).append("\n");
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

    private OptimizedSquads getOptimizedSquads(OptimizeRequest optimizeRequest, Integer gw) {
        OptimizedSquads optimizedSquads = new OptimizedSquads();

        Long gameWeeks = optimizeRequest.getGameWeeks();
        Long transfers = optimizeRequest.getTransfers();
        Long tips = optimizeRequest.getTips();
        List<PlayerStats> playerStats = getPlayerStats(gameWeeks, gw);
        List<PlayerStats> currentTeam = getCurrentTeam(optimizeRequest, playerStats);

        if (transfers.equals(0L)) {
            List<OptimizedSquad> squads = new ArrayList<>();
            List<PlayerStats> sorted = currentTeam.stream().sorted(Comparator.comparing(a -> getValueByTechnique(a, optimizeRequest.getTechnique()))).collect(
                    Collectors.toList());
            squads.add(createSquadFromArray(sorted));
            optimizedSquads.setSquads(squads);
            return optimizedSquads;
        }
        List<PlayerStats> allGK = filterBasedOnPosition(playerStats, 1);
        List<PlayerStats> allDF = filterBasedOnPosition(playerStats, 2);
        List<PlayerStats> allMD = filterBasedOnPosition(playerStats, 3);
        List<PlayerStats> allFW = filterBasedOnPosition(playerStats, 4);

        List<PlayerStats> currentGK = filterBasedOnPosition(currentTeam, 1);
        List<PlayerStats> currentDF = filterBasedOnPosition(currentTeam, 2);
        List<PlayerStats> currentMD = filterBasedOnPosition(currentTeam, 3);
        List<PlayerStats> currentFW = filterBasedOnPosition(currentTeam, 4);

        Double originalBudget = countOriginalBudget(optimizeRequest.getTeam());

        List<PlayerStats> sortedGK = sortByTechnique(currentGK, optimizeRequest.getTechnique());
        List<PlayerStats> sortedDF = sortByTechnique(currentDF, optimizeRequest.getTechnique());
        List<PlayerStats> sortedMD = sortByTechnique(currentMD, optimizeRequest.getTechnique());
        List<PlayerStats> sortedFW = sortByTechnique(currentFW, optimizeRequest.getTechnique());

        List<BetterPlayers> options = new ArrayList<>();

        if (transfers.equals(1L)) {
            options.addAll(findBetterPlayersOneTransfer(sortedGK, allGK, optimizeRequest, originalBudget));
            options.addAll(findBetterPlayersOneTransfer(sortedDF, allDF, optimizeRequest, originalBudget));
            options.addAll(findBetterPlayersOneTransfer(sortedMD, allMD, optimizeRequest, originalBudget));
            options.addAll(findBetterPlayersOneTransfer(sortedFW, allFW, optimizeRequest, originalBudget));

            List<BetterPlayers> bestOptions = findBestOptions(options, optimizeRequest.getTechnique(), currentTeam, tips);
            if (bestOptions.size() > 0) {
                optimizedSquads.setSquads(createSquadsFromList(bestOptions, currentTeam, optimizeRequest.getTechnique()));
            } else {
                optimizedSquads.setSquads(null);
            }
            return optimizedSquads;
        }

        if (transfers.equals(2L)) {
            options.addAll(findBetterPlayersTwoTransfers(sortedGK, sortedGK, allGK, allGK, optimizeRequest, originalBudget, true));
            options.addAll(findBetterPlayersTwoTransfers(sortedDF, sortedDF, allDF, allDF, optimizeRequest, originalBudget, true));
            options.addAll(findBetterPlayersTwoTransfers(sortedMD, sortedMD, allMD, allMD, optimizeRequest, originalBudget, true));
            options.addAll(findBetterPlayersTwoTransfers(sortedFW, sortedFW, allFW, allFW, optimizeRequest, originalBudget, true));
            options.addAll(findBetterPlayersTwoTransfers(sortedGK, sortedDF, allGK, allDF, optimizeRequest, originalBudget, false));
            options.addAll(findBetterPlayersTwoTransfers(sortedGK, sortedMD, allGK, allMD, optimizeRequest, originalBudget, false));
            options.addAll(findBetterPlayersTwoTransfers(sortedGK, sortedFW, allGK, allFW, optimizeRequest, originalBudget, false));
            options.addAll(findBetterPlayersTwoTransfers(sortedDF, sortedMD, allDF, allMD, optimizeRequest, originalBudget, false));
            options.addAll(findBetterPlayersTwoTransfers(sortedDF, sortedFW, allDF, allFW, optimizeRequest, originalBudget, false));
            options.addAll(findBetterPlayersTwoTransfers(sortedMD, sortedFW, allMD, allFW, optimizeRequest, originalBudget, false));

            List<BetterPlayers> bestOptions = findBestOptions(options, optimizeRequest.getTechnique(), currentTeam, tips);
            if (bestOptions.size() > 0) {
                optimizedSquads.setSquads(createSquadsFromList(bestOptions, currentTeam, optimizeRequest.getTechnique()));
            } else {
                optimizedSquads.setSquads(null);
            }
            return optimizedSquads;
        }

        return optimizedSquads;
    }

    private List<OptimizedSquad> createSquadsFromList(List<BetterPlayers> bestOptions, List<PlayerStats> currentTeam, String technique) {
        List<OptimizedSquad> squads = new ArrayList<>();
        for (final BetterPlayers bestOption : bestOptions) {
            List<PlayerStats> builtTeam = new ArrayList<>();
            for (final PlayerStats player : currentTeam) {
                if (!bestOption.getToRemove().contains(player)) {
                    builtTeam.add(player);
                }
            }
            builtTeam.addAll(bestOption.getToAdd());
            List<PlayerStats> sorted = builtTeam.stream().sorted(Comparator.comparing(a -> getValueByTechnique(a, technique))).collect(
                    Collectors.toList());
            squads.add(createSquadFromArray(sorted));
        }
        return squads;
    }

    private OptimizedSquad createSquadFromArray(List<PlayerStats> list) {
        String captainName = list.get(list.size() - 1).getPlayerName();
        String viceCaptainName = list.get(list.size() - 2).getPlayerName();
        Long captain = getPlayerIdByName(captainName);
        Long viceCaptain = getPlayerIdByName(viceCaptainName);
        OptimizedSquad squad = new OptimizedSquad();
        List<Long> teamIds = createTeam(list);
        squad.setTeam(teamIds);
        squad.setCaptain(captain);
        squad.setViceCaptain(viceCaptain);
        return squad;
    }

    private List<Long> createTeam(List<PlayerStats> team) {
        List<PlayerStats> createdTeam = new ArrayList<>();

        Collections.reverse(team);

        int[] values = {2,5,5,3};
        int[] counts = {0,0,0,0};

        for (int i = 0; i < 12; i++) {
            if (i == 0) {
                List<PlayerStats> pool = team.stream().filter(player -> player.getPosition() == 1).collect(Collectors.toList());
                createdTeam.add(pool.get(0));
                team.remove(pool.get(0));
                counts[0]++;
            } else if (i < 11) {
                List<PlayerStats> pool = team.stream().filter(player -> player.getPosition() != 1).collect(Collectors.toList());
                int index = 0;
                boolean found = false;
                do {
                    PlayerStats selected = pool.get(index);
                    int pos = selected.getPosition();
                    if (counts[pos - 1] < values[pos - 1]) {
                        createdTeam.add(selected);
                        team.remove(selected);
                        found = true;
                        counts[pos - 1]++;
                    }
                } while (!found);

            } else {
                createdTeam.addAll(team);
            }
        }
        return createdTeam.stream().map(player -> getPlayerIdByName(player.getPlayerName())).collect(Collectors.toList());
    }

    private Long getPlayerIdByName(String name) {
        List<PlayerId> playerIds = playerService.getAllPlayersIds();

        return playerIds.stream().filter(player -> {
            String playerName = player.getFirstName() + "_" + player.getSecondName();
            return playerName.equals(name);
        }).map(PlayerId::getId).findFirst().orElse(null);
    }

    private List<BetterPlayers> findBestOptions(List<BetterPlayers> options, String technique, List<PlayerStats> currentTeam, Long tips) {
        List<BetterPlayers> workingOptions = new ArrayList<>(options);
        List<BetterPlayers> bestOptions = new ArrayList<>();

        for (int i = 0; i < tips; i++) {

            double bestDiff = 0.0;
            int bestDiffIndex = 0;

            for (int i1 = 0; i1 < workingOptions.size(); i1++) {
                boolean isCorrectNumberOfPlayersFromTeams = isCorrectNumberOfPlayersFromTeams(currentTeam, workingOptions.get(i1));
                if (isCorrectNumberOfPlayersFromTeams) {
                    final BetterPlayers option = workingOptions.get(i1);
                    Double before = 0.0;
                    List<PlayerStats> toRemove = option.getToRemove();
                    for (final PlayerStats playerStats : toRemove) {
                        before += getValueByTechnique(playerStats, technique);
                    }
                    Double after = 0.0;
                    List<PlayerStats> toAdd = option.getToAdd();
                    for (final PlayerStats playerStats : toAdd) {
                        after += getValueByTechnique(playerStats, technique);
                    }

                    double difference = after - before;
                    if (difference > bestDiff) {
                        bestDiff = difference;
                        bestDiffIndex = i1;
                    }
                }
            }
            if (workingOptions.size() > 0) {
                bestOptions.add(workingOptions.get(bestDiffIndex));
                workingOptions.remove(bestDiffIndex);
            }
        }

        return bestOptions;
    }

    private boolean isCorrectNumberOfPlayersFromTeams(List<PlayerStats> currentTeam, BetterPlayers options) {

        List<PlayerTeam> playerTeams = playerService.getAllPlayersTeams();
        List<PlayerStats> builtTeam = new ArrayList<>();
        for (final PlayerStats playerStats : currentTeam) {
            if (!options.getToRemove().contains(playerStats)) {
                builtTeam.add(playerStats);
            }
        }
        builtTeam.addAll(options.getToAdd());

        Map<String, Integer> countMap = new HashMap<>();
        for (PlayerStats item: builtTeam) {
            String team = findTeam(playerTeams, item.getPlayerName());
            if (countMap.containsKey(team)) {
                countMap.put(team, countMap.get(team) + 1);
            } else {
                countMap.put(team, 1);
            }
        }

        for (Map.Entry<String,Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > 3) {
                return false;
            }
        }

        return true;
    }

    private String findTeam(List<PlayerTeam> teams, String playerName) {
        return teams.stream().filter(playerTeam -> {
            String name = playerTeam.getFirstName() + "_" + playerTeam.getSecondName();
            return name.equals(playerName);
        }).map(PlayerTeam::getTeam).findFirst().orElse("");
    }

    private List<BetterPlayers> findBetterPlayersTwoTransfers(
            List<PlayerStats> currentTeamPos1, List<PlayerStats> currentTeamPos2, List<PlayerStats> otherPlayersPos1, List<PlayerStats> otherPlayersPos2,
            OptimizeRequest optimizeRequest, Double originalBudget, boolean same) {

        Long tips = optimizeRequest.getTips();
        List<PlayerStats> toRemove = new ArrayList<>();
        toRemove.add(currentTeamPos1.get(0));
        if (same) {
            toRemove.add(currentTeamPos1.get(1));
        } else {
            toRemove.add(currentTeamPos2.get(0));
        }
        double selectedPlayersSellingPrice = 0.0;
        double selectedPlayersValue = 0.0;
        for (final PlayerStats s : toRemove) {
            String selectedPlayerName = s.getPlayerName();
            for (final TeamPlayer teamPlayer : optimizeRequest.getTeam()) {
                if (teamPlayer.getPlayerName().equals(selectedPlayerName)) {
                    Long price = teamPlayer.getSellingPrice() == null ? teamPlayer.getNowCost() : teamPlayer.getSellingPrice();
                    selectedPlayersSellingPrice += price / 10.0;
                }
            }
            selectedPlayersValue += getValueByTechnique(s, optimizeRequest.getTechnique());
        }

        double recalculatedBudget = Math.round((originalBudget + selectedPlayersSellingPrice) * 10.0) / 10.0;

        List<List<PlayerStats>> betterPlayers = new ArrayList<>();
        for (final PlayerStats level1player : otherPlayersPos1) {
            if (!currentTeamPos1.contains(level1player)) {
                Double level1value = getValueByTechnique(level1player, optimizeRequest.getTechnique());
                if (level1value > 0.0) {
                    Double level1cost = level1player.getCost();
                    for (final PlayerStats level2player : otherPlayersPos2) {
                        if (!currentTeamPos2.contains(level2player)) {
                            boolean duplicity = false;
                            for (final List<PlayerStats> betterPlayer : betterPlayers) {
                                if (betterPlayer.contains(level1player) && betterPlayer.contains(level2player)) {
                                    duplicity = true;
                                    break;
                                }
                            }
                            if (!duplicity) {
                                if (!level1player.equals(level2player)) {
                                    Double level2value = getValueByTechnique(level2player, optimizeRequest.getTechnique());
                                    if (level2value > 0.0) {
                                        Double level2cost = level2player.getCost();
                                        if (recalculatedBudget >= (level1cost + level2cost)) {
                                            if (compareValuesByTechnique(level1value + level2value, selectedPlayersValue, optimizeRequest.getTechnique())) {
                                                List<PlayerStats> list = new ArrayList<>();
                                                list.add(level1player);
                                                list.add(level2player);
                                                betterPlayers.add(list);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        List<List<PlayerStats>> sortedBetterPlayers = sortListByTechnique(betterPlayers, optimizeRequest.getTechnique());

        List<BetterPlayers> options = new ArrayList<>();
        if (sortedBetterPlayers.size() > 0) {
            List<List<PlayerStats>> best = sortedBetterPlayers
                    .subList(Math.max(sortedBetterPlayers.size() - Integer.parseInt(String.valueOf(tips)), 0), sortedBetterPlayers.size());

            for (int i = 0; i < tips; i++) {
                List<PlayerStats> list = new ArrayList<>(best.get(i));
                options.add(new BetterPlayers(toRemove, list));
            }
        }
        return options;

    }

    private List<List<PlayerStats>> sortListByTechnique(List<List<PlayerStats>> list, String technique) {
        List<List<PlayerStats>> tmpList = new ArrayList<>(list);
        if (Technique.MAX.label.equals(technique)) {
            tmpList = tmpList.stream().sorted((o1,o2) -> {
                double o1value = 0.0;
                for (final PlayerStats playerStats : o1) {
                    o1value += playerStats.getPredictedPoints();
                }
                double o2value = 0.0;
                for (final PlayerStats playerStats : o2) {
                    o2value += playerStats.getPredictedPoints();
                }

                return Double.compare(o1value, o2value);
            }).collect(Collectors.toList());
        } else if (Technique.TOTAL.label.equals(technique)) {
            tmpList = tmpList.stream().sorted((o1,o2) -> {
                double o1value = 0.0;
                for (final PlayerStats playerStats : o1) {
                    o1value += playerStats.getCostPointIndex();
                }
                double o2value = 0.0;
                for (final PlayerStats playerStats : o2) {
                    o2value += playerStats.getCostPointIndex();
                }

                return Double.compare(o1value, o2value);
            }).collect(Collectors.toList());
        } else if (Technique.FORM.label.equals(technique)) {
            tmpList = tmpList.stream().sorted((o1,o2) -> {
                double o1value = 0.0;
                for (final PlayerStats playerStats : o1) {
                    o1value += playerStats.getCostPointIndexLast6();
                }
                double o2value = 0.0;
                for (final PlayerStats playerStats : o2) {
                    o2value += playerStats.getCostPointIndexLast6();
                }

                return Double.compare(o1value, o2value);
            }).collect(Collectors.toList());
        }
        return tmpList;
    }

    private List<BetterPlayers> findBetterPlayersOneTransfer(List<PlayerStats> currentTeam, List<PlayerStats> otherPlayers, OptimizeRequest optimizeRequest, Double originalBudget) {
        Long tips = optimizeRequest.getTips();
        PlayerStats selected = currentTeam.get(0);
        List<PlayerStats> toRemove = new ArrayList<>();
        toRemove.add(selected);

        String selectedPlayerName = selected.getPlayerName();
        double selectedPlayerSellingPrice = 0.0;
        for (final TeamPlayer teamPlayer : optimizeRequest.getTeam()) {
            if (teamPlayer.getPlayerName().equals(selectedPlayerName)) {
                Long price = teamPlayer.getSellingPrice() == null ? teamPlayer.getNowCost() : teamPlayer.getSellingPrice();
                selectedPlayerSellingPrice = price / 10.0;
            }
        }
        Double selectedPlayerValue = getValueByTechnique(selected, optimizeRequest.getTechnique());
        double recalculatedBudget = Math.round((originalBudget + selectedPlayerSellingPrice) * 10.0) / 10.0;

        List<PlayerStats> betterPlayers = otherPlayers.stream()
                .filter(player -> !currentTeam.contains(player))
                .filter(player -> {
                    Double value = getValueByTechnique(player, optimizeRequest.getTechnique());
                    return value > 0.0;
                })
                .filter(player -> recalculatedBudget >= player.getCost())
                .filter(player -> {
                    Double value = getValueByTechnique(player, optimizeRequest.getTechnique());
                    return compareValuesByTechnique(value, selectedPlayerValue, optimizeRequest.getTechnique());
                })
                .collect(Collectors.toList());
        List<PlayerStats> sortedBetterPlayers = sortByTechnique(betterPlayers, optimizeRequest.getTechnique());

        List<BetterPlayers> options = new ArrayList<>();
        if (sortedBetterPlayers.size() > 0) {
            List<PlayerStats> best = sortedBetterPlayers
                    .subList(Math.max(sortedBetterPlayers.size() - Integer.parseInt(String.valueOf(tips)), 0), sortedBetterPlayers.size());

            for (int i = 0; i < tips; i++) {
                List<PlayerStats> list = new ArrayList<>();
                list.add(best.get(i));
                options.add(new BetterPlayers(toRemove, list));
            }
        }
        return options;
    }

    private boolean compareValuesByTechnique(Double value, Double selectedPlayerValue, String technique) {
        if (Technique.MAX.label.equals(technique)) {
            return value > selectedPlayerValue;
        } else if (Technique.TOTAL.label.equals(technique)) {
            return value < selectedPlayerValue;
        } else if (Technique.FORM.label.equals(technique)) {
            return value < selectedPlayerValue;
        }
        return false;
    }

    private Double getValueByTechnique(PlayerStats player, String technique) {
        if (Technique.MAX.label.equals(technique)) {
            return player.getPredictedPoints();
        } else if (Technique.TOTAL.label.equals(technique)) {
            return player.getCostPointIndex();
        } else if (Technique.FORM.label.equals(technique)) {
            return player.getCostPointIndexLast6();
        }
        return 0.0;
    }

    private Double countOriginalBudget(TeamPlayer[] team) {
        double cost = 0.0;
        for (final TeamPlayer teamPlayer : team) {
            Long price = teamPlayer.getPurchasePrice() == null ? teamPlayer.getNowCost() : teamPlayer.getPurchasePrice();
            cost += price / 10.0;
        }
        return Math.round((100.0 - cost) * 10.0) / 10.0;
    }

    private List<PlayerStats> sortByTechnique(List<PlayerStats> list, String technique) {
        List<PlayerStats> tmpList = new ArrayList<>(list);
        if (Technique.MAX.label.equals(technique)) {
            tmpList.sort(Comparator.comparing(PlayerStats::getPredictedPoints));
        } else if (Technique.TOTAL.label.equals(technique)) {
            tmpList.sort(Comparator.comparing(PlayerStats::getCostPointIndex));
        } else if (Technique.FORM.label.equals(technique)) {
            tmpList.sort(Comparator.comparing(PlayerStats::getCostPointIndexLast6));
        }
        return tmpList;
    }

    private List<PlayerStats> getCurrentTeam(OptimizeRequest optimizeRequest, List<PlayerStats> playerStats) {
        List<String> teamPlayers = Arrays.stream(optimizeRequest.getTeam()).map(TeamPlayer::getPlayerName).collect(Collectors.toList());
        return playerStats.stream().filter(player -> teamPlayers.contains(player.getPlayerName())).collect(Collectors.toList());
    }

    private List<PlayerStats> getPlayerStats(Long gameWeeks, Integer gw) {

        List<PredictedPointsStats> predictedPointsStats = getPredictedPointsStats(gameWeeks, gw);
        String[] keys = {"player_name","total_points","cost", "position", "cost_point_index", "cost_point_index_last_6"};
        List<PlayerStats> playerStats = new ArrayList<>();

        String basePath = "compare-dataset/players/" + gw + "/stats/stats.csv";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(basePath));
            String header = br.readLine();
            Integer[] indexes = DatasetUtils.getIndexes(header, keys);
            String line;
            while ((line = br.readLine()) != null) {
                String filteredLine = DatasetUtils.filterLine(line, indexes);
                String[] split = filteredLine.split(",");
                String name = split[0];
                Double points = predictedPointsStats.stream().filter(stat -> stat.getPlayerName().equals(name)).findFirst().map(
                        PredictedPointsStats::getPredictedPoints).orElse(0.0);
                playerStats.add(new PlayerStats(split[0], Double.parseDouble(split[1]), Double.parseDouble(split[2]), Integer.parseInt(split[3]), Double.parseDouble(split[4]), Double.parseDouble(split[5]), points));
            }

        } catch (FileNotFoundException e) {
            throw new CustomException("File " + basePath + " does not exist, please generate file first.");
        } catch (IOException e) {
            throw new CustomException("Cannot read from file " + basePath + ".");
        }
        return playerStats;
    }

    private List<PredictedPointsStats> getPredictedPointsStats(Long gameWeeks, Integer gw) {
        String[] predictedPointsKeys = {"player_name","predicted_points"};
        List<PredictedPointsStats> predictedPointsStats = new ArrayList<>();

        String basePath = "compare-dataset/players/" + gw + "/predictions/" + gameWeeks + "gw.csv";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(basePath));
            String header = br.readLine();
            Integer[] indexes = DatasetUtils.getIndexes(header, predictedPointsKeys);
            String line;
            while ((line = br.readLine()) != null) {
                String filteredLine = DatasetUtils.filterLine(line, indexes);
                String[] split = filteredLine.split(",");
                Double points = split[1].equals("?") ? 0.0 : Double.parseDouble(split[1]);
                predictedPointsStats.add(new PredictedPointsStats(split[0], points));
            }

        } catch (FileNotFoundException e) {
            throw new CustomException("File " + basePath + " does not exist, please generate file first.");
        } catch (IOException e) {
            throw new CustomException("Cannot read from file " + basePath + ".");
        }
        return predictedPointsStats;
    }

    private List<PlayerStats> filterBasedOnPosition(List<PlayerStats> stats, Integer position) {
        return stats.stream().filter(player -> player.getPosition().equals(position)).collect(Collectors.toList());
    }

    private void addIndexesToDataset(String basePath, int gw) {

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
        String filePath = "players/" + gw + "/stats/stats.csv";
        try {
            Files.deleteIfExists(Paths.get("compare-dataset/" + filePath));
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
                fileService.appendDataToCompareDataset(newHeader, sb.toString(), filePath);

            } catch (FileNotFoundException e) {
                throw new CustomException("File " + playerFile + " does not exist, please generate file first.");
            } catch (IOException e) {
                throw new CustomException("Cannot read from file " + playerFile + ".");
            }
        }

        LOG.info("Finished writing indexes.");
    }

}