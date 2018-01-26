package com.danacosoftware.purchasetracker;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Daniel Phillips on 8/13/2016.
 */
public class Sorter implements Comparator<ItemObject> {

    String type;
    int order;
    Sorter(int order, String type) {
        this.order = order;
        this.type = type;
    }
    String [] Sizes = {"Preemie",
            "Newborn",
            "0-3 Months",
            "3 Months",
            "3-6 Months",
            "6 Months",
            "6-9 Months",
            "9 Months",
            "9-12 Months",
            "12 Months",
            "12-18 Months",
            "18 Months",
            "18-24 Months",
            "24 Months",
            "2T",
            "3T","4T","5T","6T","7T","8T","Other"};


    @Override
    public int compare(ItemObject lhs, ItemObject rhs) {
        if(type.equals("Brand")){
            if(lhs.getBrand().compareTo(rhs.getBrand())==0){
                return 0;
            }else if(lhs.getBrand().compareTo(rhs.getBrand()) < 0){
                return order;
            }else{
                return (order * -1);
            }
        }else if(type.equals("Size")){
            int FirstValue = java.util.Arrays.asList(Sizes).indexOf(lhs.getSize());
            int SecondValue = java.util.Arrays.asList(Sizes).indexOf(rhs.getSize());
            if((FirstValue-SecondValue)==0){
                return 0;
            }else if((FirstValue-SecondValue) >  0){
                return order;
            }else{
                return (order * -1);
            }
        }else  if(type.equals("Store")) {
            if (lhs.getStore().compareTo(rhs.getStore()) == 0) {
                return 0;
            } else if (lhs.getStore().compareTo(rhs.getStore()) > 0) {
                return order;
            } else {
                return (order * -1);
            }
        }else  if(type.equals("Barcode")) {
            if (lhs.getBarcode().compareTo(rhs.getBarcode()) == 0) {
                return 0;
            } else if (lhs.getBarcode().compareTo(rhs.getBarcode()) > 0) {
                return order;
            } else {
                return (order * -1);
            }
        }else  if(type.equals("Price")) {
            try {
                float FirstPrice = Float.valueOf(lhs.getPrice());
                float SecondPrice = Float.valueOf(rhs.getPrice());
                if (FirstPrice == SecondPrice) {
                    return 0;
                } else if (FirstPrice > SecondPrice) {
                    return order;
                } else {
                    return (order * -1);
                }
            }catch (java.lang.NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
            }else if(type.equals("Pieces")) {
            try {
                float FirstPrice = Float.valueOf(lhs.getPieces());
                float SecondPrice = Float.valueOf(rhs.getPieces());
                if (FirstPrice == SecondPrice) {
                    return 0;
                } else if (FirstPrice > SecondPrice) {
                    return order;
                } else {
                    return (order * -1);
                }
            }catch (java.lang.NumberFormatException e){
                e.printStackTrace();
                return 0;
            }}else  if(type.equals("Gender")) {
            if (lhs.getGender().compareTo(rhs.getGender()) == 0) {
                return 0;
            } else if (lhs.getGender().compareTo(rhs.getGender()) < 0) {
                return order;
            } else {
                return (order * -1);
            }}else  if(type.equals("Order Added")) {
            if (lhs.getID() == (rhs.getID())) {
                return 0;
            } else if (lhs.getID() > rhs.getID()) {
                return order;
            } else {
                return (order * -1);
            }
    }
        return 0;
    }
}
