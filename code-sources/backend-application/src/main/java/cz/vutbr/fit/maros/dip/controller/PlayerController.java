package cz.vutbr.fit.maros.dip.controller;

import com.google.gson.Gson;
import cz.vutbr.fit.maros.dip.constants.ApiConstants;
import cz.vutbr.fit.maros.dip.model.OptimizeRequest;
import cz.vutbr.fit.maros.dip.model.OptimizedSquads;
import cz.vutbr.fit.maros.dip.model.Player;
import cz.vutbr.fit.maros.dip.model.PlayerDetailData;
import cz.vutbr.fit.maros.dip.model.PlayerId;
import cz.vutbr.fit.maros.dip.model.PlayerInjuryData;
import cz.vutbr.fit.maros.dip.model.PlayerProjection;
import cz.vutbr.fit.maros.dip.results.ResponseWrapper;
import cz.vutbr.fit.maros.dip.service.impl.PlayerServiceImpl;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerServiceImpl playerService;

    public PlayerController(PlayerServiceImpl playerService) {
        this.playerService = playerService;
    }

    @GetMapping()
    public ResponseWrapper<List<Player>> getAllPlayers() {
        return new ResponseWrapper<>(playerService.getAllPlayers(), HttpStatus.OK);
    }

    @GetMapping(value = "/ids")
    public ResponseWrapper<List<PlayerId>> getAllPlayerIds() {
        return new ResponseWrapper<>(playerService.getAllPlayersIds(), HttpStatus.OK);
    }

    @GetMapping(value = "/projected-points")
    public ResponseWrapper<List<PlayerProjection>> getAllPlayerProjections() {
        return new ResponseWrapper<>(playerService.getAllPlayersProjections(1), HttpStatus.OK);
    }

    @GetMapping(value = "/projected-points/{id}")
    public ResponseWrapper<List<PlayerProjection>> getAllPlayerProjections(@Valid @Pattern(regexp = ApiConstants.REGEX_FOR_NUMBERS, message = ApiConstants.MESSAGE_FOR_REGEX_NUMBER_MISMATCH) @PathVariable(value = "id")
            String id) {
        return new ResponseWrapper<>(playerService.getAllPlayersProjections(Integer.parseInt(id)), HttpStatus.OK);
    }

    @GetMapping(value = "/detail/{id}")
    public ResponseWrapper<List<PlayerDetailData>> getAllPlayerDetailData(@PathVariable(value = "id") String id) {
        return new ResponseWrapper<>(playerService.getAllPlayerData(id), HttpStatus.OK);
    }

    @GetMapping(value = "/injuries")
    public ResponseWrapper<List<PlayerInjuryData>> getAllPlayerInjuries() {
        return new ResponseWrapper<>(playerService.getAllPlayerInjuries(), HttpStatus.OK);
    }

    @GetMapping(value = "/mock-optimize")
    public ResponseWrapper<OptimizedSquads> mockOptimize() {
        String json = "{'team':[{'id':93,'sellingPrice':47,'nowCost':49,'purchasePrice':46,'playerName':'Nick_Pope'},{'id':293,'sellingPrice':46,'nowCost':47,'purchasePrice':46,'playerName':'Jack_O\\'Connell'},{'id':164,'sellingPrice':49,'nowCost':50,'purchasePrice':49,'playerName':'Çaglar_Söyüncü'},{'id':159,'sellingPrice':63,'nowCost':63,'purchasePrice':64,'playerName':'Ricardo Domingos_Barbosa Pereira'},{'id':181,'sellingPrice':69,'nowCost':70,'purchasePrice':69,'playerName':'Andrew_Robertson'},{'id':219,'sellingPrice':73,'nowCost':73,'purchasePrice':75,'playerName':'David_Silva'},{'id':463,'sellingPrice':61,'nowCost':61,'purchasePrice':65,'playerName':'Mason_Mount'},{'id':199,'sellingPrice':54,'nowCost':54,'purchasePrice':54,'playerName':'Georginio_Wijnaldum'},{'id':147,'sellingPrice':64,'nowCost':64,'purchasePrice':64,'playerName':'Dominic_Calvert-Lewin'},{'id':187,'sellingPrice':94,'nowCost':95,'purchasePrice':93,'playerName':'Roberto_Firmino'},{'id':166,'sellingPrice':96,'nowCost':96,'purchasePrice':96,'playerName':'Jamie_Vardy'},{'id':494,'sellingPrice':46,'nowCost':46,'purchasePrice':46,'playerName':'Aaron_Ramsdale'},{'id':150,'sellingPrice':81,'nowCost':83,'purchasePrice':80,'playerName':'Richarlison_de Andrade'},{'id':218,'sellingPrice':78,'nowCost':78,'purchasePrice':78,'playerName':'Bernardo Mota_Veiga de Carvalho e Silva'},{'id':297,'sellingPrice':48,'nowCost':48,'purchasePrice':49,'playerName':'John_Lundstram'}],'transfers':0,'technique':'max','gameWeeks':1}";
        Gson gson = new Gson();

        OptimizeRequest optimizeRequest = gson.fromJson(json, OptimizeRequest.class);
        OptimizedSquads optimizedSquads = playerService.getOptimizedSquads(optimizeRequest);
        if (optimizedSquads == null) {
            return new ResponseWrapper<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseWrapper<>(optimizedSquads, HttpStatus.OK);
    }

    @PostMapping(value = "/optimize")
    public ResponseWrapper<OptimizedSquads> optimize(@RequestBody String json) {
        Gson gson = new Gson();
        OptimizeRequest optimizeRequest = gson.fromJson(json, OptimizeRequest.class);
        OptimizedSquads optimizedSquads = playerService.getOptimizedSquads(optimizeRequest);
        if (optimizedSquads == null) {
            return new ResponseWrapper<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseWrapper<>(optimizedSquads, HttpStatus.OK);
    }
}
