import { Injectable, HostListener } from "@angular/core";
import { Observable, of as observableOf, Subject } from "rxjs";

@Injectable()
export class ScreenService {

    public readonly isMobile: Subject<boolean> = new Subject<boolean>(); 
    
    public readonly scrollPosition: Subject<number> = new Subject<number>();
    public readonly windowWidth: Subject<number> = new Subject<number>();
    private lastScrollPosition: number;

    constructor() {
        window.addEventListener("resize", (event) => {
            this.windowWidth.next(window.innerWidth);
            if(window.innerWidth <= 769){
                this.isMobile.next(true);
            } else{
                this.isMobile.next(false);
            }
        });

        window.addEventListener("scroll", (event) => {
            this.lastScrollPosition = window.scrollY;
            this.scrollPosition.next(this.lastScrollPosition);
        });
    }

    init() {
        
    }
}