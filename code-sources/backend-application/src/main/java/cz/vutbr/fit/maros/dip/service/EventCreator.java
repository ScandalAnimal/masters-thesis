package cz.vutbr.fit.maros.dip.service;

import cz.vutbr.fit.maros.dip.constants.ApiConstants;
import cz.vutbr.fit.maros.dip.exception.CustomException;
import cz.vutbr.fit.maros.dip.util.StringUtils;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@SuppressWarnings("CheckStyle")
@Component
public class EventCreator {

    private static final Logger LOG = LoggerFactory.getLogger(EventCreator.class);
    private final FileService fileService;

    public EventCreator(FileService fileService) {
        this.fileService = fileService;
    }

    //    @Scheduled(cron = "0 0 12 * * ?")
    @SuppressWarnings("checkstyle:CommentsIndentation")
//    @Scheduled(fixedRate = 1000000000)
    public void getFPLData() {

        String apiUrl = "https://fantasy.premierleague.com/api/bootstrap-static/";
        Connection.Response response;
        try {
            response = Jsoup.connect(apiUrl)
                    .ignoreContentType(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:73.0) Gecko/20100101 Firefox/73.0")
                    .timeout(10000)
                    .execute();

            int statusCode = response.statusCode();
            if (HttpStatus.OK.value() == statusCode) {
                Document doc = Jsoup.connect(apiUrl).ignoreContentType(true).get();
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(doc.text());
                fileService.writeRawObjectToFile(json);

                JSONArray players = (JSONArray) json.get("elements");
                parseArrayToCsv(players, "players.csv");

                JSONArray events = (JSONArray) json.get("events");
                Long gameWeekNumber = getGameWeekNumber(events);
                System.out.println("Gameweek: " + gameWeekNumber);

                getCleanedPlayerStatValues(players);

                getFixtures();

                JSONArray teams = (JSONArray) json.get("teams");
                parseArrayToCsv(teams, "teams.csv");

                Map<Long, String> playerIds = getPlayerIdList(players);
                System.out.println(playerIds);

                int numberOfPlayers = players.size();
                System.out.println("Players: " + numberOfPlayers);

                // TODO commented out for testing
//                extractPlayerData(playerIds, gameWeekNumber);

//                mergeGws(gameWeekNumber);

            } else {
                throw new CustomException("Connection to FPL Api failed. Couldn't download data. Error code: " + statusCode + ".");
            }
        } catch (IOException e) {
            throw new CustomException("Connection to FPL Api failed. Couldn't download data.");
        } catch (ParseException e) {
            throw new CustomException("Invalid format of fetched data from the FPL Api. Couldn't download data.");
        }

    }

    private Long getGameWeekNumber(JSONArray json) {
        for (final Object o : json) {
            JSONObject event = (JSONObject) o;
            if ((Boolean) event.get("is_current")) {
                return (Long) event.get("id");
            }
        }
        return 0L;
    }

    private String getCleanedPlayerStatKeys(JSONObject player, List<String> keyList) {
        StringBuilder resultKeys = new StringBuilder();
        List<String> keyStatList = new ArrayList<>();

        Iterator keyIterator = player.keySet().iterator();
        Iterator valueIterator = player.values().iterator();
        while (keyIterator.hasNext()) {
            String key = (String) keyIterator.next();
            if (keyList.contains(key)) {
                if (valueIterator.hasNext()) {
                    keyStatList.add(key);
                }
            } else {
                if (valueIterator.hasNext()) {
                    valueIterator.next();
                }
            }
        }
        resultKeys.append(StringUtils.stringifyJSONObject(keyStatList.toString()));
        return resultKeys.toString();
    }

    private void getCleanedPlayerStatValues(JSONArray json) {
        String[] keys = { "first_name", "second_name", "goals_scored", "assists", "total_points", "minutes", "goals_conceded",
                "creativity", "influence", "threat", "bonus", "bps", "ict_index", "clean_sheets", "red_cards", "yellow_cards",
                "selected_by_percent", "now_cost" };
        List<String> keyList = Arrays.asList(keys);
        List<Object> valueList = new ArrayList<>();

        StringBuilder resultValues = new StringBuilder();

        JSONObject firstPlayer = (JSONObject) json.get(0);
        String keyStats = getCleanedPlayerStatKeys(firstPlayer, keyList);

        for (Object o : json) {
            JSONObject player = (JSONObject) o;

            Iterator keyIterator = player.keySet().iterator();
            Iterator valueIterator = player.values().iterator();
            while (keyIterator.hasNext()) {
                String key = (String) keyIterator.next();
                if (keyList.contains(key)) {
                    if (valueIterator.hasNext()) {
                        valueList.add(valueIterator.next());
                    }
                } else {
                    if (valueIterator.hasNext()) {
                        valueIterator.next();
                    }
                }
            }
            resultValues.append(StringUtils.stringifyJSONObject(valueList.toString()));
            valueList.clear();
        }

        fileService.writeDataToCsv(keyStats, resultValues.toString(), "cleaned_players.csv");
    }

    private void getFixtures() {

        String apiUrl = "https://fantasy.premierleague.com/api/fixtures/";
        Connection.Response response;
        try {
            response = Jsoup.connect(apiUrl)
                    .ignoreContentType(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:73.0) Gecko/20100101 Firefox/73.0")
                    .timeout(10000)
                    .execute();

            int statusCode = response.statusCode();
            if (HttpStatus.OK.value() == statusCode) {
                Document doc = Jsoup.connect(apiUrl).ignoreContentType(true).get();
                JSONParser parser = new JSONParser();
                JSONArray json = (JSONArray) parser.parse(doc.text());

                parseArrayToCsv(json, "fixtures.csv");

            } else {
                throw new CustomException("Connection to FPL Api failed. Couldn't download data. Error code: " + statusCode + ".");
            }
        } catch (IOException e) {
            throw new CustomException("Connection to FPL Api failed. Couldn't download data.");
        } catch (ParseException e) {
            throw new CustomException("Invalid format of fetched data from the FPL Api. Couldn't download data.");
        }

    }

    private void parseArrayToCsv(JSONArray array, String fileName) {
        JSONObject first = (JSONObject) array.get(0);
        String keys = StringUtils.stringifyJSONObject(first.keySet().toString());

        StringBuilder result = new StringBuilder();

        for (Object o : array) {
            JSONObject elem = (JSONObject) o;
            result.append(StringUtils.stringifyJSONObject(elem.values().toString()));
        }

        fileService.writeDataToCsv(keys, result.toString(), fileName);
    }

    private Map<Long, String> getPlayerIdList(JSONArray json) {
        Map<Long, String> playerIds = new HashMap<>();
        String[] keys = { "first_name", "second_name", "id" };
        List<String> keyList = Arrays.asList(keys);
        List<Object> valueList = new ArrayList<>();

        StringBuilder resultValues = new StringBuilder();

        JSONObject firstPlayer = (JSONObject) json.get(0);
        String keyStats = getCleanedPlayerStatKeys(firstPlayer, keyList);

        for (Object o : json) {
            JSONObject player = (JSONObject) o;

            Iterator keyIterator = player.keySet().iterator();
            Iterator valueIterator = player.values().iterator();
            String firstName = "";
            String secondName = "";
            Long id = 0L;
            while (keyIterator.hasNext()) {
                String key = (String) keyIterator.next();
                if (keyList.contains(key)) {
                    if ("first_name".equals(key)) {
                        if (valueIterator.hasNext()) {
                            firstName = (String) valueIterator.next();
                            valueList.add(firstName);
                        }
                    }
                    else if ("second_name".equals(key)) {
                        if (valueIterator.hasNext()) {
                            secondName = (String) valueIterator.next();
                            valueList.add(secondName);
                        }
                    }
                    else if ("id".equals(key)) {
                        if (valueIterator.hasNext()) {
                            id = (Long) valueIterator.next();
                            valueList.add(id);
                        }
                    }
                } else {
                    if (valueIterator.hasNext()) {
                        valueIterator.next();
                    }
                }
            }
            playerIds.put(id, firstName + "_" + secondName);
            resultValues.append(StringUtils.stringifyJSONObject(valueList.toString()));
            valueList.clear();
        }

        fileService.writeDataToCsv(keyStats, resultValues.toString(), "player_idlist.csv");
        return playerIds;
    }

    private void extractPlayerData(Map<Long, String> playerIds, Long gameWeek) {

        StringBuilder playersInGameWeek = new StringBuilder();
        String playersInGameWeekKeys = "";
        String gwFileName = "gws/gw" + gameWeek.toString() + ".csv";
        for (Map.Entry<Long,String> entry : playerIds.entrySet()) {
            Long id = entry.getKey();
            String name = entry.getValue();
            JSONObject playerData = getPlayerData(id);
            JSONArray historyPast = (JSONArray) playerData.get("history_past");
//            if (historyPast.size() > 0) {
//                String fileName = "players/" + name + "_" + id + "/history.csv";
//                parseArrayToCsv(historyPast, fileName);
//            }
            JSONArray history = (JSONArray) playerData.get("history");
            if (history.size() > 0) {
                String fileName = "players/" + name + "_" + id + "/gw.csv";

                JSONObject first = (JSONObject) history.get(0);
                String keys = StringUtils.stringifyJSONObject(first.keySet().toString());
                playersInGameWeekKeys = "name, " + (StringUtils.stringifyJSONObject(first.keySet().toString()));

                StringBuilder result = new StringBuilder();

                for (Object o : history) {
                    JSONObject elem = (JSONObject) o;
                    if (elem.get("round").equals(gameWeek)) {
                        String nameWithId = name + ", ";
                        playersInGameWeek.append(nameWithId);
                        playersInGameWeek.append(StringUtils.stringifyJSONObject(elem.values().toString()));
                    }
                    result.append(StringUtils.stringifyJSONObject(elem.values().toString()));
                }

//                fileService.writeDataToCsv(keys, result.toString(), fileName);
            }
        }
        fileService.writeDataToCsv(playersInGameWeekKeys, playersInGameWeek.toString(), gwFileName);
    }

    private JSONObject getPlayerData(Long id) {

        String apiUrl = "https://fantasy.premierleague.com/api/element-summary/" + id + "/";
        Connection.Response response;
        try {
            response = Jsoup.connect(apiUrl)
                    .ignoreContentType(true)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:73.0) Gecko/20100101 Firefox/73.0")
                    .timeout(10000)
                    .execute();

            int statusCode = response.statusCode();
            if (HttpStatus.OK.value() == statusCode) {
                Document doc = Jsoup.connect(apiUrl).ignoreContentType(true).get();
                JSONParser parser = new JSONParser();
                return (JSONObject) parser.parse(doc.text());

            } else {
                throw new CustomException("Connection to FPL Api failed. Couldn't download data. Error code: " + statusCode + ". Url: " + apiUrl + ".");
            }
        } catch (IOException e) {
            throw new CustomException("Connection to FPL Api failed. Couldn't download data. Url: " + apiUrl + ".");
        } catch (ParseException e) {
            throw new CustomException("Invalid format of fetched data from the FPL Api. Couldn't download data.");
        }

    }

    private void mergeGws(Long gameWeek) {
        String gwFileName = ApiConstants.BASE_URL + "gws/gw" + gameWeek.toString() + ".csv";
        String mergedFileName = "gws/merged_gw.csv";
        String newGw = ", " + gameWeek.toString();

        StringBuilder sb = new StringBuilder();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(gwFileName));
            String header = br.readLine() + ", GW";
            String line;
            while ((line = br.readLine()) != null) {

                line += newGw;
                sb.append(line).append("\n");
            }
            fileService.appendDataToCsv(header, sb.toString(), mergedFileName, gameWeek == 1L);

        } catch (FileNotFoundException e) {
            throw new CustomException("File " + gwFileName + " does not exist, please generate gw file first.");
        } catch (IOException e) {
            throw new CustomException("Cannot read from file " + gwFileName + ".");
        }
    }

}
