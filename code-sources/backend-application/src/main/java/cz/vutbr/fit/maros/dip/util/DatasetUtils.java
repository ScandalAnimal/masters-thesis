package cz.vutbr.fit.maros.dip.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class DatasetUtils {

    public static String getYearLabel(Integer year) {

        switch (year) {
            case 1:
                return "2016-17";
            case 2:
                return "2017-18";
            case 3:
                return "2018-19";
            case 4:
                return "2019-20";
            default:
                return "";
        }
    }

    public static String getFilePath(Integer year) {
        String dir2016 = "data/2016-17/players/";
        String dir2017 = "data/2017-18/players/";
        String dir2018 = "data/2018-19/players/";
        String dir2019 = "data/2019-20/players/";

        if (year == 1) {
            return dir2016;
        } else if (year == 2) {
            return dir2017;
        } else if (year == 3) {
            return dir2018;
        } else if (year == 4) {
            return dir2019;
        }
        return "";
    }

    public static String getNewName(String oldName) {
        String newName = oldName;
        long occurrences = oldName.chars().filter(ch -> ch == '_').count();
        if (occurrences > 1) {
            int sepPos = oldName.lastIndexOf("_");
            if (sepPos != -1) {
                newName = oldName.substring(0, sepPos);
            }
        }
        return newName;
    }

    public static Integer[] getIndexes(String header, String[] keys) {

        String[] split = header.split(",");
        Integer[] indexes = new Integer[keys.length];

        List<String> keyList = Arrays.asList(keys);
        for (int i = 0; i < split.length; i++) {
            final String s = split[i];
            int pos = keyList.indexOf(s);
            if (pos >= 0) {
                indexes[pos] = i;
            }
        }
        return indexes;
    }

    public static List<String> getCurrentSeasonPlayers() {
        String path = getFilePath(4);
        File file = new File(path);
        String[] directories = file.list((current, name) -> new File(current, name).isDirectory());

        List<String> players = new ArrayList<>();
        if (directories != null) {
            for (final String directory : directories) {
                String newName = getNewName(directory);
                players.add(newName);
            }
        }
        return players;
    }

    public static String filterLine(String line, Integer[] indexes) {

        StringJoiner joiner = new StringJoiner(",");

        String[] split = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        for (final Integer index : indexes) {
            joiner.add(split[index]);
        }
        return joiner.toString();
    }
}
