import { Injectable } from "@angular/core";
import { CONTENT_URL_PREFIX } from './document.service';
import { Observable, ConnectableObservable } from 'rxjs';
import { CurrentNodes, NavigationNode, NavigationViews } from '../models/navigation'
import { HttpClient } from '@angular/common/http';
import { publishLast, map } from 'rxjs/operators';
export { CurrentNodes, NavigationNode, NavigationViews } from '../models/navigation'

const navigationPath = CONTENT_URL_PREFIX + 'navigation.json';

@Injectable()
export class NavigationService{

    navigationViews: Observable<NavigationViews>;
    currentNodes: Observable<CurrentNodes>;

    constructor(private http: HttpClient){
        const navigationInfo = this.fetchNavigation();
        this.navigationViews = this.getNavigationViews(navigationInfo);

    }

    /**
     * Fetch navigation data from navigation.json.
     */
    private fetchNavigation(): Observable<NavigationViews>{
        const navigationInfo = this.http.get<NavigationViews>(navigationPath)
        .pipe(publishLast());
        (navigationInfo as ConnectableObservable<NavigationViews>).connect();
        return navigationInfo;
    }

    /**
     * 
     * @param navigationInfo 
     */
    private getNavigationViews(navigationInfo: Observable<NavigationViews>): Observable<NavigationViews>{
        const navigationViews = navigationInfo.pipe(
            map(response => {
                const views = Object.assign({}, response);
                Object.keys(views).forEach(key => {
                    if (key[0] === '_') { delete views[key]; }
                });
                return views as NavigationViews;
            }),
            publishLast(),
        );
        (navigationViews as ConnectableObservable<NavigationViews>).connect();
        return navigationViews;
    }

} 