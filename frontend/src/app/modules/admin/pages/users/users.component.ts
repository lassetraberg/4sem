import { Component, OnInit, ViewChild } from "@angular/core";
import { DataService } from "src/app/shared/services/data.service";
import { User } from "src/app/shared/models/user";
import { MatTableDataSource } from "@angular/material/table";
import { DataSource } from "@angular/cdk/table";
import { MatPaginator } from "@angular/material";

@Component({
  selector: "app-users",
  templateUrl: "./users.component.html",
  styleUrls: ["./users.component.scss"]
})
export class UsersComponent implements OnInit {
  users: Array<User> = [];

  dataSource: MatTableDataSource<User>;
  tableColumns: string[] = [
    "id",
    "username",
    "created",
    "lastLoginAttempt",
    "role",
    "locked"
  ];
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private data: DataService) {}

  ngOnInit() {
    this.getUsers();
  }

  formatDate(date: string): string {
    const d = new Date(date);
    return d.toLocaleString();
  }

  unlockAccount(userId: number) {
    this.data
      .unlockAccount(userId)
      .toPromise()
      .then(res => {
        this.getUsers();
      });
  }

  getUsers() {
    this.data
      .getUsers()
      .toPromise()
      .then(users => {
        this.users = users;
        this.setupDataSource();
      });
  }

  private setupDataSource() {
    this.dataSource = new MatTableDataSource<User>(this.users);
    this.dataSource.paginator = this.paginator;
  }
}
