package com.danacosoftware.purchasetracker;

import android.graphics.Bitmap;
import android.widget.Button;

import android.widget.ImageView;


/**
 * Created by Daniel Phillips on 8/6/2016.
 */
public class ItemObject {

    int ID;
    String Barcode;
    String Type;
    String Colour;
    String Pattern;
    String Gender;
    String Brand;
    String Size;
    String Price;
    String Store;
    String Description;
    String Pieces;
    String Accessories;
    String Picture;

    ItemObject(){

    }

    ItemObject(int ID,
                 String Barcode,
                 String Type,
                 String Colour,
                 String Pattern,
                 String Gender,
                 String Brand,
                 String Size,
                 String Price,
                 String Store,
                 String Description,
                 String Pieces,
                 String Accessories,
                 String Picture){

        this.ID = ID;
        this.Barcode = Barcode;
        this.Type = Type;
        this.Colour = Colour;
        this.Pattern = Pattern;
        this.Gender = Gender;
        this.Brand = Brand;
        this.Size = Size;
        this.Price = Price;
        this.Store = Store;
        this.Description = Description;
        this.Pieces = Pieces;
        this.Accessories = Accessories;
        this.Picture = Picture;
    }
    ItemObject(String Barcode,
               String Type,
               String Colour,
               String Pattern,
               String Gender,
               String Brand,
               String Size,
               String Price,
               String Store,
               String Description,
               String Pieces,
               String Accessories,
               String Picture){


        this.Barcode = Barcode;
        this.Type = Type;
        this.Colour = Colour;
        this.Pattern = Pattern;
        this.Gender = Gender;
        this.Brand = Brand;
        this.Size = Size;
        this.Price = Price;
        this.Store = Store;
        this.Description = Description;
        this.Pieces = Pieces;
        this.Accessories = Accessories;
        this.Picture = Picture;
    }

    public int getID() {
        return this.ID;
    }
    public String getBarcode(){
        return this.Barcode;
    }
    public String getType(){
        return this.Type;
    }
    public String getColour(){
        return this.Colour;
    }
    public String getPattern(){
        return this.Pattern;
    }
    public String getGender(){
        return this.Gender;
    }
    public String getBrand(){
        return this.Brand;
    }
    public String getSize(){
        return this.Size;
    }
    public String getPrice(){
        return this.Price;
    }
    public String getStore(){
        return this.Store;
    }
    public String getDescription(){
        return this.Description;
    }
    public String getPieces(){
        return this.Pieces;
    }
    public String getAccessories(){
        return this.Accessories;
    }
    public String getPicture(){
        return this.Picture;
    }
    public void setID(int ID){
        this.ID = ID;
    }
    public void setBarcode(String Barcode){
        this.Barcode = Barcode;
    }
    public void setType(String Type ){
         this.Type = Type;
    }
    public void setColour(String Colour ){
         this.Colour = Colour;
    }
    public void setPattern(String Pattern){
         this.Pattern = Pattern;
    }
    public void setGender(String Gender ){
         this.Gender = Gender;
    }
    public void setBrand(String Brand ){
         this.Brand = Brand;
    }
    public void setSize(String Size ){
         this.Size = Size;
    }
    public void setPrice(String Price ){
         this.Price = Price;
    }
    public void setStore(String Store ){
         this.Store = Store;
    }
    public void setDescription(String Description ){
         this.Description = Description;
    }
    public void setPieces(String Pieces){
         this.Pieces = Pieces;
    }
    public void setAccessories(String Accessories ){
         this.Accessories = Accessories;
    }
    public void setPicture(String Picture ){
         this.Picture = Picture;
    }
}
