package com.flightpalfx;

public class Tickets {
  public String id, airline, origin, destination, date, departureTime, arrivesTime, classType;
  public int price;

  public Tickets(String id, String airline, String origin, String destination, String date, String departureTime, String arrivesTime, String classType, int price) {
    this.id = id;
    this.airline = airline;
    this.origin = origin;
    this.destination = destination;
    this.date = date;
    this.departureTime = departureTime;
    this.arrivesTime = arrivesTime;
    this.classType = classType;
    this.price = price;
  }
}
