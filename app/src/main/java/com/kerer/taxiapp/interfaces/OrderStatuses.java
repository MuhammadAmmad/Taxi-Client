package com.kerer.taxiapp.interfaces;

/**
 * statuses on order
 * STATUS_CREATED - order created by client
 * STATUS_GETED - some driver geted order
 * STATUS_BY_ORIGIN - driver by origin addres
 * STATUS_BY_DESTINATION - driver by destination addres
 * STATUS_PAYED - client payed order/order finished
 * STATUS_CANCELED - driver or client cancel order
 */

public interface OrderStatuses {
    int STATUS_CREATED = 0;
    int STATUS_GETED = 1;
    int STATUS_BY_ORIGIN = 2;
    int STATUS_BY_DESTINATION = 3;
    int STATUS_PAYED = 4;
    int STATUS_CANCELED = 5;
}
