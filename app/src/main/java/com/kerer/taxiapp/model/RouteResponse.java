package com.kerer.taxiapp.model;

import java.util.List;

/**
 * Created by ivan on 08.01.17.
 */

public class RouteResponse {
    public List<Route> routes;

    public String getPoints() {
        return this.routes.get(0).overview_polyline.points;
    }

    class Route {
        OverviewPolyline overview_polyline;
    }

    class OverviewPolyline {
        String points;
    }
}
