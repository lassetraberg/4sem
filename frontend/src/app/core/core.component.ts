import { Component, OnInit } from '@angular/core';
import { TitleService } from '../shared/services/title.service';
import { ScreenService } from '../shared/services/screen.service';

@Component({
  selector: 'app-core',
  templateUrl: './core.component.html',
  styleUrls: ['./core.component.scss']
})
export class CoreComponent implements OnInit {

  constructor(private titleService: TitleService, private screenService: ScreenService) { }

  ngOnInit() {
    this.titleService.init();
    this.screenService.init();
  }

}
