import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/shared/services/auth.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  constructor(private auth: AuthService) { }

  ngOnInit() {
  }

  public signOut(){
    this.auth.logout();
  }

}
