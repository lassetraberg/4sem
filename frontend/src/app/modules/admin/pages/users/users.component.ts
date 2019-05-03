import { Component, OnInit } from "@angular/core";
import { DataService } from "src/app/shared/services/data.service";
import { User } from "src/app/shared/models/user";

@Component({
  selector: "app-users",
  templateUrl: "./users.component.html",
  styleUrls: ["./users.component.scss"]
})
export class UsersComponent implements OnInit {
  users: Array<User> = [];

  constructor(private data: DataService) {}

  ngOnInit() {
    this.getUsers();
  }

  formatDate(date: string): string {
    const d = new Date(date);
    return d.toLocaleString();
  }

  unlockAccount(userId: number) {
    this.data.unlockAccount(userId).subscribe(res => {
      this.getUsers();
    });
  }

  getUsers() {
    this.data.getUsers().subscribe(users => {
      this.users = users;
    });
  }
}
