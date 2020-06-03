package cz.vutbr.fit.maros.dip.service;

import cz.vutbr.fit.maros.dip.model.OptimizeRequest;
import cz.vutbr.fit.maros.dip.model.OptimizedSquads;
import cz.vutbr.fit.maros.dip.model.Player;
import cz.vutbr.fit.maros.dip.model.PlayerDetailData;
import cz.vutbr.fit.maros.dip.model.PlayerId;
import cz.vutbr.fit.maros.dip.model.PlayerInjuryData;
import cz.vutbr.fit.maros.dip.model.PlayerProjection;
import cz.vutbr.fit.maros.dip.model.PlayerTeam;
import java.util.List;

public interface PlayerService {

    List<Player> getAllPlayers();

    List<PlayerId> getAllPlayersIds();

    List<PlayerProjection> getAllPlayersProjections(int id);

    List<PlayerDetailData> getAllPlayerData(String playerName);

    List<PlayerInjuryData> getAllPlayerInjuries();

    Integer getPlayerPrize(String playerName);

    Integer getPlayerPosition(String playerName);

    OptimizedSquads getOptimizedSquads(OptimizeRequest optimizeRequest);

    List<PlayerTeam> getAllPlayersTeams();

}

