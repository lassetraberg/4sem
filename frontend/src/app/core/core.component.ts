import { Component, OnInit } from '@angular/core';
import { TitleService } from '../shared/services/title.service';
import { ScreenService } from '../shared/services/screen.service';
import { RouterOutlet } from '@angular/router';
import { routerAnimation } from 'src/animations/router.animation';
import { ThemeService } from '../shared/services/theme.service';

@Component({
  selector: 'app-core',
  templateUrl: './core.component.html',
  styleUrls: ['./core.component.scss'],
  animations: [
    routerAnimation
  ]
})
export class CoreComponent implements OnInit {

  constructor(private titleService: TitleService, private screenService: ScreenService, private themeService: ThemeService) { }

  ngOnInit() {
    this.titleService.init();
    this.screenService.init();
    this.themeService.init();
  }



  prepareRoute(outlet: RouterOutlet) {
    return outlet && outlet.activatedRouteData && outlet.activatedRouteData['animation'];
  }

}
