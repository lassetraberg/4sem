import { Component, OnInit, Input } from '@angular/core';
import { fromLonLat } from 'ol/proj';
import { toLonLat } from 'ol/proj';
import 'ol/ol.css';
import {Map, View, Feature} from 'ol';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import {Point} from 'ol/geom'; 
import {Vector} from 'ol/source';
import {Vector as VectorLayer} from 'ol/layer';
import {Style, Icon} from 'ol/style';

import { CONTENT_URL_PREFIX } from 'src/app/shared/services/document.service';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {

  pointerPath: string = CONTENT_URL_PREFIX + 'img/map/location.png';
  map: Map;
  view: View;
  point: Feature;
  lat: number;
  lon: number;


  constructor() { 
    this.lat = 50;
    this.lon = 50;
  }

  ngOnInit() {
    const osmLayer = new TileLayer({
      source: new OSM()
    });

    this.view = new View({
      center: this.toWebMerc([this.lon, this.lat]),
      zoom: 12
    });

    this.point = new Feature({
      name: "Point",
      id: "LocationPoint",
      geometry: new Point(
        this.toWebMerc([this.lon, this.lat])
      )
    });
    var pointStyle = new Style({
      image: new Icon({
        anchor: [0.5, 30],
        anchorXUnits: "fraction",
        anchorYUnits: "pixels",
        opacity: 1,
        src: this.pointerPath
      })
    })
    this.point.setStyle(pointStyle);

    var vectorSource = new Vector({
      title: "New vector",
      features: [this.point]
    });

    var vectorLayer = new VectorLayer({
      source: vectorSource
    })

    this.map = new Map({
      target: 'map',
      layers: [
        osmLayer,
        vectorLayer
      ],
      controls : [

      ],
      view: this.view
    }); 

  }

  /**
   * Converts normal coordinates into the projection format our map understands.
   * @param coord an array of coordinates [lon, lat].
   */
  private toWebMerc(coord){
    return fromLonLat(coord, "EPSG:3857");
  }

  /**
   * Converts web merc coordinates into the lon/lat format.
   * @param coord an array of coordinates [lon, lat].
   */
  private fromWebMerc(coord){
    return toLonLat(coord, "EPSG:3857");
  }

  /**
   * Centers the map to the current position.
   * @param coord an array of coordinates [lon, lat].
   */
  private centerLocation(coord){
    this.map.getView().setCenter(this.toWebMerc([coord.lon, coord.lat]));
    this.point.getGeometry().setCoordinates(this.toWebMerc([coord.lon, coord.lat]));
  }

  /**
   * Update the map component with new coordinates.
   * @param coord an array of coordinates [lon, lat].
   */
  public updateMap(coord): void{
    this.centerLocation(coord);
    this.lon = coord.lon;
    this.lat = coord.lat;
  }



}
