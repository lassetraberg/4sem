import { Injectable, HostListener } from "@angular/core";
import { Observable, of as observableOf, Subject, BehaviorSubject } from "rxjs";

@Injectable()
export class ScreenService {

    public readonly mobile: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(null); 
    
    public readonly scrollPosition: Subject<number> = new Subject<number>();
    public readonly windowWidth: Subject<number> = new Subject<number>();
    private lastScrollPosition: number;

    constructor() {
        window.addEventListener("resize", (event) => {
            this.windowWidth.next(window.innerWidth);
            if(window.innerWidth <= 769){
                this.mobile.next(true);
            } else{
                this.mobile.next(false);
            }
        });

        window.addEventListener("scroll", (event) => {
            this.lastScrollPosition = window.scrollY;
            this.scrollPosition.next(this.lastScrollPosition);
        });
    }

    init() {
        window.dispatchEvent(new Event('resize'));
    }

    public isMobile(): boolean{
        return this.mobile.value;
    }
}