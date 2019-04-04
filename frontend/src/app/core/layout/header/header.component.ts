import { Component, OnInit, Input } from '@angular/core';
import { AuthService } from 'src/app/shared/services/auth.service';
import { Router } from '@angular/router';
import { TitleService } from 'src/app/shared/services/title.service';
import { ScreenService } from 'src/app/shared/services/screen.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  title: String = "";

  constructor(private auth: AuthService, private router: Router, private titleService: TitleService, private screen: ScreenService) { }

  ngOnInit() {
    this.titleService.titleUpdated.subscribe((title) => this.title = title);
  }
  
  public signout(){
    this.auth.logout();
    this.router.navigateByUrl("/");
  }

  get username(): string {
    return localStorage.getItem('username');
  }

}
