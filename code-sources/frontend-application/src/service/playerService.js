import { POSITIONS } from '../constants';

const playerService = {
  getPlayerName(player) {
    switch (player.id) {
      case 106:
        return 'Luiz';
      case 469:
        return 'Ceballos';
      case 504:
        return 'Martinelli';
      case 36:
        return 'Jota';
      case 452:
        return 'Ezri Konsa';
      case 465:
        return 'Trézéguet';
      case 492:
        return 'Groeneveld';
      case 51:
        return 'Izquierdo';
      case 118:
        return 'Jorginho';
      case 462:
        return 'Kenedy';
      case 422:
        return 'Gomes';
      case 629:
        return 'Virgínia';
      case 159:
        return 'Pereira';
      case 197:
        return 'Fabinho';
      case 209:
        return 'Danilo';
      case 211:
        return 'Jesus';
      case 218:
        return 'Bernardo Silva';
      case 221:
        return 'Fernandinho';
      case 440:
        return 'Angeliño';
      case 443:
        return 'Rodri';
      case 229:
        return 'Dalot';
      case 244:
        return 'Fred';
      case 618:
        return 'Fernandes';
      case 261:
        return 'Joselu';
      case 466:
        return 'Joelinton';
      case 328:
        return 'Romeu';
      case 605:
        return 'Fernandes';
      case 357:
        return 'Femenía';
      case 365:
        return 'Success';
      case 386:
        return 'Hernández';
      case 387:
        return 'Silva';
      case 594:
        return 'Cardoso';
      case 595:
        return 'Rosa';
      case 402:
        return 'Jonny';
      case 406:
        return 'Vinagre';
      case 411:
        return 'Patrício';
      case 414:
        return 'Neves';
      case 415:
        return 'Moutinho';
      case 473:
        return 'Vallejo';
      case 528:
        return 'Neto';
      case 609:
        return 'Jordao';
      case 619:
        return 'Podence';
      case 5:
      case 486:
      case 428:
      case 450:
      case 390:
      case 470:
      case 39:
      case 107:
      case 111:
      case 113:
      case 114:
      case 150:
      case 152:
      case 189:
      case 526:
      case 212:
      case 345:
      case 592:
        return player.first_name;
      default:
        return player.second_name;
    }
  },
  getPlayerDetailName(player) {
    switch (player.id) {
      case 106:
        return 'David Luiz';
      default:
        return player.first_name + ' ' + player.second_name;
    }
  },
  getPositionLabel(player) {
    switch (player.element_type) {
      case POSITIONS.GK:
        return 'sort.positions.gk';
      case POSITIONS.DF:
        return 'sort.positions.df';
      case POSITIONS.MF:
        return 'sort.positions.md';
      case POSITIONS.FW:
        return 'sort.positions.fw';
      default:
        return null;
    }
  },
  isPlayerUnavailable(injuries, firstName, lastName) {
    const match = injuries.find(
      item => item.first_name === firstName && item.second_name === lastName
    );
    return match !== undefined;
  },
  getInjuryStatus(injuries, firstName, lastName) {
    const match = injuries.find(
      item => item.first_name === firstName && item.second_name === lastName
    );
    return match !== undefined ? match.news : null;
  },
};

export default playerService;
