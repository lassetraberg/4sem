import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/shared/services/auth.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup, FormBuilder, Validators, FormControl} from '@angular/forms';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {

  form: FormGroup;
  user = { username: '', password: '', confirm_password: '', role: ''};
  return: string = '';

  constructor(private auth: AuthService, private fb:FormBuilder, private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.queryParams
    .subscribe(params => this.return = params['return'] || '/');

    this.form = new FormGroup({
      'username': new FormControl(this.user.username, [
        Validators.required
      ]),
      'password': new FormControl(this.user.password, [
        Validators.required
      ]),
      'confirm_password': new FormControl(this.user.confirm_password, [
        Validators.required
      ]),
      'role': new FormControl(this.user.role, [
        Validators.required
      ])
    });
    this.form.controls['role'].setValue(this.auth.roles[0], {onlySelf: true});

  }

  get username() { return this.form.get('username'); }
  get password() { return this.form.get('password'); }
  get confirm_password() { return this.form.get('confirm_password'); }
  get role() { return this.form.get('role')}

  public async submit() {
    await this.auth.register(this.form.controls.username.value, this.form.controls.password.value).toPromise();
    this.router.navigate(['user/add-vehicle']);
  }

}
