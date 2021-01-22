package com.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import com.controller.menuController;
import com.model.Item;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ICheckout
{
    public JPanel mainPanel;
    private JButton btnCash;
    private JButton btnCard;
    private JButton btnPrint;
    private JButton btnBack;
    private JLabel lblCheckout;
    public JLabel lblTotalTxt;
    private JLabel lblPaymentType;
    private JList lstCheckoutBasket;
    public JScrollPane scrollPane1;
    private JPanel panel1;
    private JLabel lblTotal;
    //Creating a temporary list
    public JList tlstCheckoutBasket;
    public JOptionPane checkoutPopup;
    Float tCash = 0.00f;
    Float change = 0.00f;
    String receipt;

    File text = new File("Resources\\Stock.txt");
    public String separator = ",";

    public ICheckout(JList cartList, Float tTotal, JFrame kiosk, JFrame checkout)
    {
        lstCheckoutBasket.setModel(cartList.getModel());


        tlstCheckoutBasket = cartList;

        //Setting total
        lblTotalTxt.setText("£" + String.format("%.2f", tTotal));

        //Adding back button function
        btnBack.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                menuController.startKiosk(checkout);
            }
        });

        //Functionality for the print receipt button
        btnPrint.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                printReceipt();

            }
        });

        //Functionality for the card payment option
        btnCard.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                cardPayment();
            }
        });

        //Functionality for the cash payment option
        btnCash.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                cashPayment();
            }
        });
    }

    //Card paymnt
    public void cardPayment()
    {
        //Defining values and setting formats for the datetime on receipt
        Float tTotal = 0.00f;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        //Create popup for cash input
        JFrame cardPopup = new JFrame();
        cardPopup.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //Creating pane message and title
        int yesNo = checkoutPopup.showConfirmDialog(cardPopup, "Card verification", "Security", JOptionPane.INFORMATION_MESSAGE);
        if(yesNo==JOptionPane.YES_OPTION)
        {
            checkoutPopup.showMessageDialog(cardPopup, "Card Verified!", "Security" , JOptionPane.INFORMATION_MESSAGE);
            receipt = "----------Tescos---------- \n" +
                    "Date and Time:  " + dtf.format(now) + "\n" +
                    "Basket:  " + lstCheckoutBasket.getModel().toString() + "\n" +
                    "Total:  " + lblTotalTxt.getText() + "\n" +
                    "Card payment verified" + "\n" +
                    "Have a nice day :)";
            updateStock();
        }
        else
        {
            checkoutPopup.showMessageDialog(cardPopup, "Card not verified. Please try again");
        }


    }

    //Cash payment
    public void cashPayment()
    {
        //Defining values and setting formats for the datetime on receipt
        Float tTotal = 0.00f;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        //Create popup for cash input
        JFrame cashPopup = new JFrame();
        cashPopup.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //Storing the cash inputted in the temporary cash variable
        tCash = Float.parseFloat(checkoutPopup.showInputDialog(cashPopup, "Please enter Cash: "));

        //Storing the temporary total from the label in the string variable
        String tSTotal = lblTotalTxt.getText();

        //Need to just get the number from the total string (same method as updateTotal on kiosk)
        tSTotal = tSTotal.replace("£", "");
        tTotal = Float.parseFloat(tSTotal);

        //If statement to check they entered right amount
        if (tCash >= tTotal)
        {
            //Calculate change
            change = tCash - tTotal;
            //Storing receipt with relevant info to be used later
            receipt = "----------Tescos---------- \n" +
                    "Date and Time:  " + dtf.format(now) + "\n" +
                    "Basket:  " + lstCheckoutBasket.getModel().toString() + "\n" +
                    "Total:  " + lblTotalTxt.getText() + "\n" +
                    "Amount paid:  £" + tCash + "\n" +
                    "Change:  " + (String.format("£" + "%.2f",change)) + "\n" +
                    "Have a nice day :)";
            updateStock();
        }

        else
        {
            checkoutPopup.showMessageDialog(cashPopup, "Not enough money. Please enter more");
        }
    }
    public void printReceipt()
    {
        JFrame popFrame = new JFrame();
        popFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        checkoutPopup.showMessageDialog(popFrame, receipt, "Receipt", JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateStock()
    {

        //Creating item to use
        Item itemArray = new Item();

        //Loading in the data
        itemArray.loadFile();

        //Creting model of basket with items in
        DefaultListModel lstMPurchase = (DefaultListModel) lstCheckoutBasket.getModel();

        //Get number of items in basket
        int basketSize = lstMPurchase.size();

        //For each item in basket
        for (int i = 0; i < basketSize; i++)
        {

            //Gets the item each time incrementing
            String getAttribute = (String) lstMPurchase.getElementAt(i);


            String[] tArray = getAttribute.split(separator);

            //Gets the code of each item to find match
            String code = tArray[0];

            //For size of the current stock
            for (int j = 0; j < itemArray.stock.size(); j++)
            {
                //Get code of each item
                Item currentItem = itemArray.stock.get(j);

                //Store code of each item
                String tCode = currentItem.getCode();

                //Check for a match
                if (tCode.equals(code))
                {
                    //If there is a match, decrease the quantity of the item by one
                    itemArray.stock.get(j).setQuantity(currentItem.getQuantity() - 1);

                    updateStock(itemArray);
                }
            }

        }
    }

    public void updateStock(Item tempItem)
    {
        //Creating a temporary stock array to duplicate the items to store
        ArrayList<Item> tStock = new ArrayList<>();

        //Setting the temp stock as the current stock of item passed in
        tStock = tempItem.stock;

        try
        {
            //Opening filewriter connection so I can write to file (update stock)
            FileWriter  fileWriter= new FileWriter(text);

            //For each item in the stock
            for(int i = 0; i < tStock.size(); i++)
            {
                //Initialising variables
                String tInfo = "";
                String tCode;
                String tName;
                //String tQuantity;
                String tPrice;

                //If i is greater than 0 (Every iteration except the first) insert a return into the file
                if(i >= 1)
                {
                    tInfo = "\n";
                }

                //Getting code of item
                tCode = tStock.get(i).getCode();
                //Adding code to first section of string. Also adds the separator value (,)
                tInfo += tCode + ",";

                //Getting name of item
                tName = tStock.get(i).getName();
                //Adding name to second section of string. Also adds the separator value (,)
                tInfo += tName + ",";

                //Getting quantity of item
                String tQuantity = tStock.get(i).getQuantity().toString();
                //Adding quantity to third section of string. Also adds the separator value (,)
                tInfo += tQuantity + ",";

                //Getting price of item
                tPrice = tStock.get(i).getPrice();
                //Adding price to final section of string. Also adds the separator value (,)
                tInfo += tPrice;

                fileWriter.write(tInfo);
            }
            //Closing writer
            fileWriter.close();
        }
        //Error handling
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
