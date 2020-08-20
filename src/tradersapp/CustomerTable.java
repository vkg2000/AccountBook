/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tradersapp;

/**
 *
 * @author offic
 */
public class CustomerTable {
    private int id;
    private String date;
    private int deposit;
    
    public CustomerTable (int id, String date ,int deposit ){
   
        this.id=id;
        this.date=date;
        this.deposit=deposit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }
       
    
}
