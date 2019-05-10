import { Component, OnInit } from "@angular/core";
import { AuthService } from "src/app/shared/services/auth.service";
import { Router, ActivatedRoute } from "@angular/router";
import {
  FormGroup,
  FormBuilder,
  Validators,
  FormControl
} from "@angular/forms";
import { MatSnackBar } from "@angular/material";

@Component({
  selector: "app-signin",
  templateUrl: "./signin.component.html",
  styleUrls: ["./signin.component.scss"]
})
export class SigninComponent implements OnInit {
  form: FormGroup;
  user = { username: "", password: "" };
  return: string = "";

  constructor(
    private auth: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(
      params => (this.return = params["return"] || "/")
    );

    this.form = new FormGroup({
      username: new FormControl(this.user.username, [Validators.required]),
      password: new FormControl(this.user.password, [Validators.required])
    });
  }

  get username() {
    return this.form.get("username");
  }
  get password() {
    return this.form.get("password");
  }

  public async submit() {
    try {
      await this.auth
        .login(
          this.form.controls.username.value,
          this.form.controls.password.value
        )
        .toPromise();
    } catch (err) {
      this.snackBar.open(err.error.errors.login[0], null, { duration: 3000 });
    }
    if (this.auth.isAuthenticated()) {
      this.router.navigateByUrl(this.return);
    }
  }
}
