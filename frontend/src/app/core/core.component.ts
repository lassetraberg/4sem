import { Component, OnInit } from '@angular/core';
import { TitleService } from '../shared/services/title.service';
import { ScreenService } from '../shared/services/screen.service';
import { RouterOutlet } from '@angular/router';
import { routerAnimation } from 'src/animations/router.animation';
import { ThemeService } from '../shared/services/theme.service';
import { NavigationService, NavigationNode } from '../shared/services/navigation.service';

@Component({
  selector: 'app-core',
  templateUrl: './core.component.html',
  styleUrls: ['./core.component.scss'],
  animations: [
    routerAnimation
  ]
})
export class CoreComponent implements OnInit {

  public headerNodes: NavigationNode[];

  constructor(private titleService: TitleService, private screenService: ScreenService, private themeService: ThemeService, private navigationService: NavigationService) { }

  ngOnInit() {
    this.navigationService.navigationViews.subscribe(views => {
      this.headerNodes = views['Header'] || [];
    })
    this.titleService.init();
    this.screenService.init();
    this.themeService.init();
  }



  prepareRoute(outlet: RouterOutlet) {
    return outlet && outlet.activatedRouteData && outlet.activatedRouteData['animation'];
  }

}
