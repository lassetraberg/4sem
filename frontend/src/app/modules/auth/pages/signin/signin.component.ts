import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/shared/services/auth.service';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators, FormControl} from '@angular/forms';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.scss']
})
export class SigninComponent implements OnInit {

  form: FormGroup;
  user = { username: '', password: ''};

  constructor(private auth: AuthService, private fb:FormBuilder, private router: Router) { }

  ngOnInit(): void {
    this.form = new FormGroup({
      'username': new FormControl(this.user.username, [
        Validators.required
      ]),
      'password': new FormControl(this.user.password, [
        Validators.required
      ])
    });

  }
  
  get username() { return this.form.get('username'); }
  get password() { return this.form.get('password'); }

  public async submit() {
    const result = await this.auth.login(this.form.controls.username.value, this.form.controls.password.value).toPromise();
    // Redirect
  }

}
