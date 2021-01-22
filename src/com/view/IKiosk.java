package com.view;

import com.model.Item;
import com.controller.menuController;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class IKiosk extends JFrame
{

    public JPanel mainPanel;
    private JTextField txtItemCode;
    private JButton btnScan;
    private JLabel lblCheckout;
    private JButton btnAdmin;
    private JButton btnCheckout;
    private JLabel lblTotal;
    private JLabel lblBasket;
    private JList lstStock;
    public JList lstBasket;
    public JOptionPane popup;
    public Float total = 0.0f;

    File text = new File("Resources\\Stock.txt");

    //Creating the array of stock items
    private ArrayList<Item> Stock = new ArrayList<>();

    public IKiosk(JFrame kiosk, JFrame next)
    {
        //Running function to display stock on kiosk page
        //showStock();
        lstBasket.setModel(new DefaultListModel());



        btnScan.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                getCode();
            }
        });
        btnAdmin.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                login(kiosk, lstStock);
            }
        });
        btnCheckout.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                menuController.startCheckout(lstBasket, total ,kiosk ,next);
            }
        });
    }

    //Function to display stock in the list on the kiosk menu
//    public void showStock()
//    {
//        //Creating new item
//        Item newItem = new Item();
//
//        //Loading data from file
//        newItem.loadFile();
//
//        //Creating temporary array
//        Item[] tArray = new Item[0];
//
//        //Adding the stock to the array
//        tArray = newItem.stock.toArray(tArray);
//
//        ///Creating model of list to add to
//        DefaultListModel lstModel = new DefaultListModel();
//
//        //
//        for (int i = 0; i < tArray.length; i++)
//        {
//
//            lstModel.addElement(tArray[i].getCode() +" , " + tArray[i].getName() +" , " + tArray[i].getPrice() + " , " + tArray[i].getQuantity());
//        }
//        lstStock.setModel(lstModel);
//    }
    public void getCode()
    {
        //Create a list model for the basket
        DefaultListModel lstModelBasket = (DefaultListModel) lstBasket.getModel();
        //Defining the item attribute seperator value
        String separator = ",";
        //Defining the variable to store the correct item in
        String matchCode;

        //Using buffered reader so it takes input a character at a time.
        try
        {
            //Creating new reader instance to read file
            BufferedReader bReader = new BufferedReader(new FileReader(text));
            //Creating variable to store the current line in
            String cLine = null;
            //While the current line is not empty, keep reading in data
            while ((cLine = bReader.readLine()) != null)
            {
                //Stores the item code in the string array
                String[] attribute = cLine.split(separator);

                //If the code from the first item matches the inputted code
                if(attribute[0].equals(txtItemCode.getText()))
                {
                    //Storing whole item in the matchCode variable
                    matchCode = cLine;

                    //Adding the item to the basket
                    lstModelBasket.addElement(matchCode);

                    //Need to update the basket total once item is scanned
                    //pass object into update total. not the price.
                    updateTotal(attribute);

                }
            }
            bReader.close();
        }
        //Error handling
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void updateTotal(String[] item)
    {
        //Define temp price variable
        String tPrice;
        //Store the price of the item in the variable
        tPrice = item[3];

        //Re storing the string without the "£" so that it can be converted to a float
        tPrice = tPrice.replace("£", "");

        //Adding the total together
        total = total + Float.parseFloat(tPrice);
        //Setting the label to the new price in the correct format
        lblTotal.setText("£" + String.format("%.2f", total));
    }

    public void login(JFrame kiosk, JList stockList)
    {
        JFrame popUp = new JFrame();
        popUp.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JTextField txtUser = new JTextField();
        //Storing password as string in normal text as isnt recognised for some reason as a password variable.
        JTextField txtPassword = new JPasswordField();

        //Defining the login credentials
        String username = "admin";

        String password = "admin123";

        Object[] message =
                {
                        "Username:", txtUser,
                        "Password:", txtPassword,
                };

        int op = popup.showConfirmDialog(popUp, message, "Admin Login", JOptionPane.OK_CANCEL_OPTION);


        if(op == popup.OK_OPTION)
        {
            if(txtUser.getText().equals(username) && txtPassword.getText().equals(password))
            {
                popup.showMessageDialog(popUp, "Admin Login Correct.", "Admin Login", JOptionPane.INFORMATION_MESSAGE);
                menuController.startAdmin(kiosk, lstStock);
            }
            else
            {
                popup.showMessageDialog(popUp, "Admin Login Failed", "Admin Login", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else
        {
            popup.showMessageDialog(popUp, "Admin Login Failed.", "Admin Login", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
