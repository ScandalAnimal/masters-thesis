package cz.vutbr.fit.maros.dip.service.impl;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.vutbr.fit.maros.dip.constants.ApiConstants;
import cz.vutbr.fit.maros.dip.exception.CustomException;
import cz.vutbr.fit.maros.dip.model.BetterPlayers;
import cz.vutbr.fit.maros.dip.model.OptimizeRequest;
import cz.vutbr.fit.maros.dip.model.OptimizedSquad;
import cz.vutbr.fit.maros.dip.model.OptimizedSquads;
import cz.vutbr.fit.maros.dip.model.Player;
import cz.vutbr.fit.maros.dip.model.PlayerDetailData;
import cz.vutbr.fit.maros.dip.model.PlayerId;
import cz.vutbr.fit.maros.dip.model.PlayerInjuryData;
import cz.vutbr.fit.maros.dip.model.PlayerProjection;
import cz.vutbr.fit.maros.dip.model.PlayerStats;
import cz.vutbr.fit.maros.dip.model.PlayerTeam;
import cz.vutbr.fit.maros.dip.model.PredictedPointsStats;
import cz.vutbr.fit.maros.dip.model.TeamPlayer;
import cz.vutbr.fit.maros.dip.model.Technique;
import cz.vutbr.fit.maros.dip.service.PlayerService;
import cz.vutbr.fit.maros.dip.util.DatasetUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private static final Logger LOG = LoggerFactory.getLogger(PlayerServiceImpl.class);

    public List<PlayerTeam> getAllPlayersTeams() {

        List<PlayerTeam> players = new ArrayList<>();
        String fileName = ApiConstants.BASE_URL + "players_raw.csv";

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String header = br.readLine();
            String[] keys = header.split(",");
            String line;
            while ((line = br.readLine()) != null) {

                String[] values = line.split(",");
                JSONObject jsonObject = new JSONObject();
                String[] selectedKeys = { "first_name", "second_name", "team" };
                List<String> keyList = Arrays.asList(selectedKeys);
                List<Object> valueList = new ArrayList<>();
                List<String> builtKeysList = new ArrayList<>();

                for (int i = 0; i < keys.length; i++) {
                    if (keyList.contains(keys[i])) {
                        valueList.add(values[i]);
                        builtKeysList.add(keys[i]);
                    }
                }

                for (int i = 0; i < builtKeysList.size(); i++) {
                    jsonObject.put(builtKeysList.get(i), valueList.get(i));
                }

                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create();
                PlayerTeam player = gson.fromJson(jsonObject.toString(), PlayerTeam.class);
                players.add(player);
            }

        } catch (IOException e) {
            throw new CustomException("Cannot read from file " + fileName + ".");
        }
        return players;
    }

    public List<Player> getAllPlayers() {

        LOG.info("Started getting all players.");
        List<Player> players = new ArrayList<>();
        String fileName = ApiConstants.BASE_URL + "players_raw.csv";

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String header = br.readLine();
            String[] keys = header.split(",");
            String line;
            while ((line = br.readLine()) != null) {

                String[] values = line.split(",");
                JSONObject jsonObject = new JSONObject();
                String[] selectedKeys = { "first_name", "second_name", "goals_scored", "assists", "total_points", "minutes", "goals_conceded",
                    "creativity", "influence", "threat", "bonus", "bps", "ict_index", "clean_sheets", "red_cards", "yellow_cards",
                    "selected_by_percent", "now_cost", "team", "team_code", "element_type" };
                List<String> keyList = Arrays.asList(selectedKeys);
                List<Object> valueList = new ArrayList<>();
                List<String> builtKeysList = new ArrayList<>();

                for (int i = 0; i < keys.length; i++) {
                    if (keyList.contains(keys[i])) {
                        valueList.add(values[i]);
                        builtKeysList.add(keys[i]);
                    }
                }

                for (int i = 0; i < builtKeysList.size(); i++) {
                    jsonObject.put(builtKeysList.get(i), valueList.get(i));
                }

                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create();
                Player player = gson.fromJson(jsonObject.toString(), Player.class);
                players.add(player);
            }

        } catch (IOException e) {
            throw new CustomException("Cannot read from file " + fileName + ".");
        }
        LOG.info("Finished getting all players.");
        return players;
    }

    public List<PlayerId> getAllPlayersIds() {

        List<PlayerId> players = new ArrayList<>();
        String fileName = ApiConstants.BASE_URL + "player_idlist.csv";

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String header = br.readLine();
            String[] keys = header.split(",");
            String line;
            while ((line = br.readLine()) != null) {

                String[] values = line.split(",");
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < keys.length; i++) {
                    jsonObject.put(keys[i], values[i]);
                }

                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create();
                PlayerId player = gson.fromJson(jsonObject.toString(), PlayerId.class);
                players.add(player);
            }

        } catch (IOException e) {
            throw new CustomException("Cannot read from file " + fileName + ".");
        }
        return players;
    }

    public List<PlayerProjection> getAllPlayersProjections(int id) {

        List<PlayerProjection> players = new ArrayList<>();
        String fileName = ApiConstants.DATASET_URL + "players/predictions/" +  id + "gw.csv";

        LOG.info("Started getting all players predictions.");
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String header = br.readLine();
            String[] keys = header.split(",");
            String line;
            while ((line = br.readLine()) != null) {

                String[] values = line.split(",");
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < keys.length; i++) {
                    if (keys[i].equals("predicted_points")) {
                        int rounded = 0;
                        if (!values[i].equals("?")) {
                            rounded = (int) Math.round(Double.parseDouble(values[i]));
                        }

                        jsonObject.put(keys[i], rounded);
                    } else {
                        jsonObject.put(keys[i], values[i]);
                    }
                }

                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create();
                PlayerProjection player = gson.fromJson(jsonObject.toString(), PlayerProjection.class);
                players.add(player);
            }

        } catch (IOException e) {
            throw new CustomException("Cannot read from file " + fileName + ".");
        }
        LOG.info("Finished geting all players predictions.");
        return players;
    }

    public List<PlayerDetailData> getAllPlayerData(String id) {

        LOG.info("Started getting player data for id: " + id);
        String[] keys = {"total_points", "bps", "minutes", "own_goals", "goals_scored", "assists",
            "yellow_cards", "red_cards", "goals_conceded", "saves", "clean_sheets", "opponent_team" };

        List<PlayerDetailData> players = new ArrayList<>();

        List<String> currentSeasonPlayers = DatasetUtils.getCurrentSeasonPlayers();
        List<PlayerId> ids = getAllPlayersIds();
        String playerName = "";
        for (final PlayerId playerId : ids) {
            if (playerId.getId().equals(Long.parseLong(id))) {
                playerName = playerId.getFirstName() + "_" + playerId.getSecondName();
            }
        }

        for (int year = 1; year < 5; year++) {
            String path = DatasetUtils.getFilePath(year);
            File file = new File(path);
            String[] directories = file.list((current, name) -> new File(current, name).isDirectory());

            if (directories != null) {
                for (final String directory : directories) {
                    int index = 1;
                    String newName = DatasetUtils.getNewName(directory);
                    if (playerName.equals(newName)) {
                        if (currentSeasonPlayers.contains(newName)) {
                            BufferedReader br;
                            try {
                                br = new BufferedReader(new FileReader(path + directory + "/gw.csv"));
                                String header = br.readLine();
                                Integer[] indexes = DatasetUtils.getIndexes(header, keys);
                                String filteredHeader = "season,gw_index,player_name," + DatasetUtils.filterLine(header, indexes);
                                String[] filteredKeysArray = filteredHeader.split(",");

                                String line;

                                while ((line = br.readLine()) != null) {

                                    String filteredLine = DatasetUtils.getYearLabel(year) + "," + index + "," + newName + "," + DatasetUtils
                                            .filterLine(line, indexes);
                                    index++;
                                    String[] filteredLineArray = filteredLine.split(",");

                                    JSONObject jsonObject = new JSONObject();
                                    for (int i = 0; i < filteredKeysArray.length; i++) {
                                        jsonObject.put(filteredKeysArray[i], filteredLineArray[i]);
                                    }
                                    Gson gson = new GsonBuilder()
                                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                            .create();
                                    PlayerDetailData player = gson.fromJson(jsonObject.toString(), PlayerDetailData.class);
                                    players.add(player);
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
        }
        LOG.info("Finished getting all player data for id: " + id);
        return players;
    }

    public List<PlayerInjuryData> getAllPlayerInjuries() {

        LOG.info("Started getting all player injuries.");
        List<PlayerInjuryData> players = new ArrayList<>();
        String fileName = ApiConstants.BASE_URL + "players_raw.csv";

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String header = br.readLine();
            String[] keys = header.split(",");
            String line;
            while ((line = br.readLine()) != null) {

                String[] values = line.split(",");
                JSONObject jsonObject = new JSONObject();

                String[] selectedKeys = { "first_name", "second_name", "chance_of_playing_next_round", "chance_of_playing_this_round",
                    "news", "news_added", "red_cards", "yellow_cards", "team_code", "web_name"};
                List<String> keyList = Arrays.asList(selectedKeys);
                List<Object> valueList = new ArrayList<>();
                List<String> builtKeysList = new ArrayList<>();

                for (int i = 0; i < keys.length; i++) {
                    if (keyList.contains(keys[i])) {
                        valueList.add(values[i]);
                        builtKeysList.add(keys[i]);
                    }
                }
                for (int i = 0; i < builtKeysList.size(); i++) {
                    if (builtKeysList.get(i).equals("news")) {
                        if (!valueList.get(i).equals("")) {
                            for (int j = 0; j < builtKeysList.size(); j++) {
                                jsonObject.put(builtKeysList.get(j), valueList.get(j));
                            }

                            Gson gson = new GsonBuilder()
                                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                    .create();
                            PlayerInjuryData player = gson.fromJson(jsonObject.toString(), PlayerInjuryData.class);
                            players.add(player);
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new CustomException("Cannot read from file " + fileName + ".");
        }
        LOG.info("Finished getting all players injuries.");
        return players;
    }

    public Integer getPlayerPrize(String playerName) {

        String fileName = ApiConstants.BASE_URL + "cleaned_players.csv";
        String[] keys = {"first_name", "second_name", "now_cost"};
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String header = br.readLine();
            Integer[] indexes = DatasetUtils.getIndexes(header, keys);
            String line;

            while ((line = br.readLine()) != null) {
                String filteredLine = DatasetUtils.filterLine(line, indexes);
                String[] split = filteredLine.split(",");
                String newName = split[0] + "_" + split[1];
                if (newName.equals(playerName)) {
                    return Integer.parseInt(split[2]);
                }
            }

        } catch (IOException e) {
            throw new CustomException("Cannot read from file " + fileName + ".");
        }
        return 0;
    }

    public Integer getPlayerPosition(String playerName) {

        LOG.info("Started getting position for player: " + playerName);
        String fileName = ApiConstants.BASE_URL + "players_raw.csv";
        String[] keys = {"first_name", "second_name", "element_type"};
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String header = br.readLine();
            Integer[] indexes = DatasetUtils.getIndexes(header, keys);
            String line;

            while ((line = br.readLine()) != null) {
                String filteredLine = DatasetUtils.filterLine(line, indexes);
                String[] split = filteredLine.split(",");
                String newName = split[0] + "_" + split[1];
                if (newName.equals(playerName)) {
                    LOG.info("Finished getting position for player: " + playerName);
                    return Integer.parseInt(split[2]);
                }
            }

        } catch (IOException e) {
            throw new CustomException("Cannot read from file " + fileName + ".");
        }
        LOG.info("Finished getting position for player: " + playerName);
        return 0;
    }

    public OptimizedSquads getOptimizedSquads(OptimizeRequest optimizeRequest) {
        LOG.info("Started getting optimized squads");
        OptimizedSquads optimizedSquads = new OptimizedSquads();

        Long gameWeeks = optimizeRequest.getGameWeeks();
        Long transfers = optimizeRequest.getTransfers();
        Long tips = optimizeRequest.getTips();
        List<PlayerStats> playerStats = getPlayerStats(gameWeeks);
        List<PlayerStats> currentTeam = getCurrentTeam(optimizeRequest, playerStats);

        if (transfers.equals(0L)) {
            List<OptimizedSquad> squads = new ArrayList<>();
            List<PlayerStats> sorted = currentTeam.stream().sorted(Comparator.comparing(a -> getValueByTechnique(a, optimizeRequest.getTechnique()))).collect(
                    Collectors.toList());
            squads.add(createSquadFromArray(sorted));
            optimizedSquads.setSquads(squads);
            LOG.info("Finished getting optimized squads");
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
            optimizedSquads.setSquads(createSquadsFromList(bestOptions, currentTeam, optimizeRequest.getTechnique()));

            LOG.info("Finished getting optimized squads");
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
            optimizedSquads.setSquads(createSquadsFromList(bestOptions, currentTeam, optimizeRequest.getTechnique()));

            LOG.info("Finished getting optimized squads");
            return optimizedSquads;
        }

        LOG.info("Finished getting optimized squads");

        return null;
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
        List<PlayerId> playerIds = getAllPlayersIds();

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
            bestOptions.add(workingOptions.get(bestDiffIndex));
            workingOptions.remove(bestDiffIndex);
        }

        return bestOptions;
    }

    private boolean isCorrectNumberOfPlayersFromTeams(List<PlayerStats> currentTeam, BetterPlayers options) {

        List<PlayerTeam> playerTeams = getAllPlayersTeams();
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
        // TODO make into methods
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

    private List<PlayerStats> getCurrentTeam(OptimizeRequest optimizeRequest, List<PlayerStats> playerStats) {
        List<String> teamPlayers = Arrays.stream(optimizeRequest.getTeam()).map(TeamPlayer::getPlayerName).collect(Collectors.toList());
        return playerStats.stream().filter(player -> teamPlayers.contains(player.getPlayerName())).collect(Collectors.toList());
    }

    private List<PlayerStats> getPlayerStats(Long gameWeeks) {

        List<PredictedPointsStats> predictedPointsStats = getPredictedPointsStats(gameWeeks);
        String[] keys = {"player_name","total_points","cost", "position", "cost_point_index", "cost_point_index_last_6"};
        List<PlayerStats> playerStats = new ArrayList<>();

        String basePath = "dataset/players/stats/stats.csv";
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

    private List<PredictedPointsStats> getPredictedPointsStats(Long gameWeeks) {
        String[] predictedPointsKeys = {"player_name","predicted_points"};
        List<PredictedPointsStats> predictedPointsStats = new ArrayList<>();

        String basePath = "dataset/players/predictions/" + gameWeeks + "gw.csv";
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

}
