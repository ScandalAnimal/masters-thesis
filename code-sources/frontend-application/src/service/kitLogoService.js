import logo1 from '../assets/kits/1.png';
import logo10 from '../assets/kits/10.png';
import logo11 from '../assets/kits/11.png';
import logo12 from '../assets/kits/12.png';
import logo13 from '../assets/kits/13.png';
import logo14 from '../assets/kits/14.png';
import logo15 from '../assets/kits/15.png';
import logo16 from '../assets/kits/16.png';
import logo17 from '../assets/kits/17.png';
import logo18 from '../assets/kits/18.png';
import logo19 from '../assets/kits/19.png';
import logo2 from '../assets/kits/2.png';
import logo20 from '../assets/kits/20.png';
import logo3 from '../assets/kits/3.png';
import logo4 from '../assets/kits/4.png';
import logo5 from '../assets/kits/5.png';
import logo6 from '../assets/kits/6.png';
import logo7 from '../assets/kits/7.png';
import logo8 from '../assets/kits/8.png';
import logo9 from '../assets/kits/9.png';

const kitLogoService = {
  getTeamKit(id) {
    switch (id) {
      case 1:
        return logo1;
      case 2:
        return logo2;
      case 3:
        return logo3;
      case 4:
        return logo4;
      case 5:
        return logo5;
      case 6:
        return logo6;
      case 7:
        return logo7;
      case 8:
        return logo8;
      case 9:
        return logo9;
      case 10:
        return logo10;
      case 11:
        return logo11;
      case 12:
        return logo12;
      case 13:
        return logo13;
      case 14:
        return logo14;
      case 15:
        return logo15;
      case 16:
        return logo16;
      case 17:
        return logo17;
      case 18:
        return logo18;
      case 19:
        return logo19;
      case 20:
        return logo20;
      default:
        return null;
    }
  },
};

export default kitLogoService;
