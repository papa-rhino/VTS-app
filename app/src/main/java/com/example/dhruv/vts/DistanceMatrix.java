package com.example.dhruv.vts;

import java.util.ArrayList;

/**
 * Created by Dhruv Sharma on 30-01-2018.
 */

public class DistanceMatrix {

    ArrayList<String>destination_addresses;
    ArrayList<String>origin_addresses;
    ArrayList<Elements> rows;
    String status;

    public ArrayList<String> getDestination_addresses() {
        return destination_addresses;
    }

    public void setDestination_addresses(ArrayList<String> destination_addresses) {
        this.destination_addresses = destination_addresses;
    }

    public ArrayList<String> getOrigin_addresses() {
        return origin_addresses;
    }

    public void setOrigin_addresses(ArrayList<String> origin_addresses) {
        this.origin_addresses = origin_addresses;
    }

    public ArrayList<Elements> getRows() {
        return rows;
    }

    public void setRows(ArrayList<Elements> rows) {
        this.rows = rows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
