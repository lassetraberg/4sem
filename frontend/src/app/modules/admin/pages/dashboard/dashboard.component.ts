import { Component, OnInit } from "@angular/core";

@Component({
  selector: "app-dashboard",
  templateUrl: "./dashboard.component.html",
  styleUrls: ["./dashboard.component.scss"]
})
export class DashboardComponent implements OnInit {
  pages = [
    {
      path: "/admin/users",
      text:
        "See all accounts, and information about them. Unlock accounts, if they have been locked.",
      title: "User Administration"
    },
    {
      path: "/admin/data",
      text: "See data records for all vehicles, sorted by device id.",
      title: "Vehicle data"
    }
  ];
  constructor() {}

  ngOnInit() {}
}
