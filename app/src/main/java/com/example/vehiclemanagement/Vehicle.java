package com.example.vehiclemanagement;

public class Vehicle {
    public int id;
    public String name;
    public String brand;
    public String type;
    public int dayStored;
    public long price;
    public byte[] image;

//    public Vehicle(int id, String name, int dayStored, double price, byte[] image) {
//        this.id = id;
//        this.name = name;
//        this.dayStored = dayStored;
//        this.price = price;
//        this.image = image;
//    }

    public Vehicle(int id, String name, String brand, String type, int dayStored, long price, byte[] image) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.type = type;
        this.dayStored = dayStored;
        this.price = doCalPriceByDay(dayStored);
        this.image = image;
    }

    public long doCalPriceByDay(int day) {
        float first_price = 10000f;
        float total = 0.0f;

        while (day > 0) {
            total += 7 * Math.max(7000, first_price);
            day -= 7;
            first_price -= 1000;
            if (day > 0 && day < 7) {
                total += day * Math.max(7000, first_price);
                break;
            }

        }

        if (dayStored <= 7) {
            total = dayStored * 10000f;
        }
        return (long) total;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getType() {
        return type;
    }

    public int getDayStored() {
        return dayStored;
    }

    public long getPrice() {
        return price;
    }

    public byte[] getImage() {
        return image;
    }
}
