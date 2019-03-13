import { Injectable } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router';

import { filter, map, mergeMap } from 'rxjs/operators';
import { Subject } from 'rxjs/internal/Subject';

const APP_TITLE = '';
const SEPARATOR = ' > ';
const body = document.getElementsByTagName('body')[0];

@Injectable()
export class TitleService {

    // Current and previous route.
    routeOld: string;
    route: string = "default";

    public titleUpdated: Subject<String> = new Subject<String>();

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private titleService: Title,
    ) { }

    init() {
        this.router.events.pipe(filter((event) => event instanceof NavigationEnd)).pipe(
            map(() => {
                let route = this.activatedRoute;
                while (route.firstChild) route = route.firstChild;
                return route;
            })).pipe(
            filter((route) => route.outlet === 'primary')).pipe(
            mergeMap((route) => route.data)).pipe(
            map((data) => {
                if (data.title) {
                    // If a route has a title set (data: {title: "Foo"}) we use it
                    this.titleUpdated.next(data.title);
                    this.route = data.title;
                    return data.title;
                } else {
                    // If not, we create an approximation
                    return this.router.url.split('/').reduce((acc, frag) => {
                        if (acc && frag) { acc += SEPARATOR; }
                        return acc + TitleService.ucFirst(frag);
                    });
                }
            }))
            .subscribe((pathString) => {
                this.titleService.setTitle(`${APP_TITLE} ${pathString}`);
                body.classList.remove(this.routeOld);
                body.classList.add(this.route);
                this.routeOld = this.route;
        });
    }

    static ucFirst(string) {
        if (!string) { return string; }
        return string.charAt(0).toUpperCase() + string.slice(1);
    }

}