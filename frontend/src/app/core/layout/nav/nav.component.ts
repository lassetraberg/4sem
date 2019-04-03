import { Component, OnInit, Input } from '@angular/core';
import { NavigationNode } from 'src/app/shared/models/navigation';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.scss']
})
export class NavComponent implements OnInit {

  @Input() nodes: NavigationNode[];
  
  constructor() { }

  ngOnInit() {
  }

}
