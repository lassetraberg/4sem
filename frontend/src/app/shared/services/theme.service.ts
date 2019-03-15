import { Injectable } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router';
import { filter, map, mergeMap } from 'rxjs/operators';

const body = document.getElementsByTagName('body')[0];
const bodyStyles = document.body.style;

@Injectable()
export class ThemeService {

    // Current and previous theme.
    bodyClassOld: string;
    bodyClass: string = "default";

    // Theme variables declared in CSS.
    bg: string = "--bg";
    lighterShade: string = "--lightershade";
    lightShade: string = "--lightshade";
    shade: string = "--shade";
    darkShade: string = "--darkshade";
    textColor: string = "--text-color";
    acc: string = "--acc";

    /**
     * Colors for different themes.
     */
    // (Default) Light theme.
    L_bg: string = "#fff";
    L_lighterShade: string = "#FAFAFA";
    L_lightShade: string = "#F5F5F5";
    L_shade: string = "#D9DBDF";
    L_darkShade: string = "#6E6E6E"
    L_textColor: string = "#202020";
    L_acc: string = "#1a73e8";

    // Dark theme.
    D_bg: string = "#1E1E1E"
    D_lighterShade: string = "#2C2C2C";
    D_lightShade: string = "#2C2C2C";
    D_shade: string = "#2C2C2C";
    D_darkShade: string = "#191919"
    D_textColor: string = "#FFFFFF";
    D_acc: string = "#1a73e8";

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
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
                if (data.theme) {
                    this.bodyClass = data.theme;
                    if (this.bodyClass == "dark") {
                        this.goDark();
                    } else {
                        this.goDefault();
                    }
                    return data.theme;
                } else {
                    this.bodyClass = "default";
                    this.goDefault();
                    return "default";
                }
            }))
            .subscribe((pathString) => {
                body.classList.remove(this.bodyClassOld);
                body.classList.add(this.bodyClass);
                this.bodyClassOld = this.bodyClass;
            });
    }

    static ucFirst(string) {
        if (!string) { return string; }
        return string.charAt(0).toUpperCase() + string.slice(1);
    }










    private goDefault() {
        bodyStyles.setProperty(this.bg, this.L_bg);
        bodyStyles.setProperty(this.lighterShade, this.L_lighterShade);
        bodyStyles.setProperty(this.lightShade, this.L_lightShade);
        bodyStyles.setProperty(this.shade, this.L_shade);
        bodyStyles.setProperty(this.darkShade, this.L_darkShade);
        bodyStyles.setProperty(this.textColor, this.L_textColor);
        bodyStyles.setProperty(this.acc, this.L_acc);
    }

    private goDark() {
        bodyStyles.setProperty(this.bg, this.D_bg);
        bodyStyles.setProperty(this.lighterShade, this.D_lighterShade);
        bodyStyles.setProperty(this.lightShade, this.D_lightShade);
        bodyStyles.setProperty(this.shade, this.D_shade);
        bodyStyles.setProperty(this.darkShade, this.D_darkShade);
        bodyStyles.setProperty(this.textColor, this.D_textColor);
        bodyStyles.setProperty(this.acc, this.D_acc);
    }

}