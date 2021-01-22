package com.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.controller.menuController;
import com.model.Item;
public class IAdmin {

    public JPanel panel1;
    public JList lstStock;
    private JButton btnExit;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnAdd;
    public JPanel mainPanel;
    public JOptionPane popUpOption;
    public JFrame popUp = new JFrame();

    public String separator = "\\,";
    File text = new File("Resources\\Stock.txt");

    public IAdmin(JFrame aFrame, JList itemList)
    {
        lstStock.setModel(new DefaultListModel());

        //No close on the popup
        popUp.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //Loads in the data from file
        loadData();

        btnAdd.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                addStock();
            }
        });

        btnEdit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                editStock();
            }
        });

        btnDelete.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                deleteStock();
            }
        });
        btnExit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //Opens kiosk
                menuController.startKiosk(aFrame);
            }
        });
    }
    //Function for editing stock
    public void editStock()
    {
        //Using same popup box method for text input as the addStock() function
        //Define variables for index and text input
        int index;
        JTextField txtCode = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtQuantity = new JTextField();
        JTextField txtPrice = new JTextField();

        //Get the selected item from lst
        index = lstStock.getSelectedIndex();

        //Temp item object
        Item tStock = new Item();

        //Loading in data
        tStock.loadFile();

        //Getting item using index and storing it in temp item created
        Item tItem = tStock.geItemFromStock(index);

        Object[] message =
                {
                        "Item code:  ", txtCode,
                        "Item Name:  ", txtName,
                        "Stock:  ", txtQuantity,
                        "Price:  (Please use £0.00 format  ", txtPrice

                };

        int yesNo = popUpOption.showConfirmDialog(popUp, message, "Edit Stock", JOptionPane.INFORMATION_MESSAGE);

        //If the option is yes
        if(yesNo == popUpOption.OK_OPTION)
        {
            //And if all fields are not empty
            if(txtCode != null && txtName != null && txtQuantity != null && txtPrice != null)
            {
                //Add a new item passing through data in text fields
                editItem(txtCode.getText(), txtName.getText(), txtQuantity.getText(), txtPrice.getText());

                //popup window to notify user edit was sucessful
                popUpOption.showMessageDialog(popUp, "Edit Successful", "Edit Stock", JOptionPane.INFORMATION_MESSAGE);

                //Load file
                loadData();

                //Console message for testing
                System.out.println("Product edited");
            }
            //Error handling
            else
            {
                popUpOption.showMessageDialog(popUp, "All information required!", "Attention", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        //Error handling
        else
        {
            popUpOption.showMessageDialog(popUp, "Product edit failed!" , "Attention", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void deleteStock()
    {
        //Using same popup box method for data as the addStock() function
        //Define variables for index and labels to display item being deleted
        int index;

        //Get the selected item from lst
        index = lstStock.getSelectedIndex();

        //Temp item object
        Item tStock = new Item();

        //Loading in data
        tStock.loadFile();

        //Getting item using index and storing it in temp item created
        Item tItem = tStock.geItemFromStock(index);

        //Storing each attribute as a string for JOption display
        String tCode = tItem.getCode();
        String tName = tItem.getName();
        String tQuantity = tItem.getQuantity().toString();
        String tPrice = tItem.getPrice();


        Object[] message =
                {
                        "Please confirm you want to delete this product from the stock database. This action cannot be undone! ",
                        "Item code: "+  tCode,
                        "Item Name: "+  tName,
                        "Stock: "+  tQuantity,
                        "Price: "+ tPrice
                };

        int yesNo = popUpOption.showConfirmDialog(popUp, message, "Delete Stock", JOptionPane.INFORMATION_MESSAGE);

        //If the option is yes
        if(yesNo == popUpOption.OK_OPTION)
        {
            //Function to delete item
            deleteItem();
            popUpOption.showMessageDialog(popUp, "Item successfully deleted", "Attention", JOptionPane.INFORMATION_MESSAGE);


            loadData();
        }
        //Error handling
        else
        {
            popUpOption.showMessageDialog(popUp, "Product deletion failed!" , "Attention", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public void deleteItem()
    {
        //New item
        Item tItem = new Item();

        //Loading stock
        tItem.loadFile();

        //Gets item to delete
        Item delItem = tItem.geItemFromStock(lstStock.getSelectedIndex());

        tItem.deleteItem(delItem);

        updateFile(tItem);
    }

    public void showAdmin()
    {
        //Opens the admin pane and closes the old one
        JFrame frame = new JFrame("Admin");
        frame.setContentPane(this.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
    private void loadData()
    {
        Item newItem = new Item();
        //Loading stock from file
        newItem.loadFile();
        //Creating array of stock items
        Item[] tArray = new Item[0];
        tArray = newItem.stock.toArray(tArray);

        DefaultListModel lstModel = new DefaultListModel();

        int length = tArray.length;

        for (int i = 0; i < length; i++)
        {

            lstModel.addElement(tArray[i].getCode() +"," + tArray[i].getName() + "," + tArray[i].getPrice() + "," + tArray[i].getQuantity());
        }
        lstStock.setModel(lstModel);
    }

    public void addStock()
    {
        //Adding variables for item attributes to be stored in
        JTextField txtCode = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtQuantity = new JTextField();
        JTextField txtPrice = new JTextField();

        Object[] message =
                {
                "Item code:  ", txtCode,
                "Item Name:  ", txtName,
                "Stock:  ", txtQuantity,
                "Price:  (Please use £0.00 format  ", txtPrice

        };

        int op = popUpOption.showConfirmDialog(popUp, message, "Add an item", JOptionPane.INFORMATION_MESSAGE);

        //If the option is yes
        if(op == popUpOption.OK_OPTION)
        {
            //And if all fields are not empty
            if(txtCode != null && txtName != null && txtQuantity != null && txtPrice != null)
            {
                //Add a new item passing through data in text fields
                addItem(txtCode.getText(), txtName.getText(), txtQuantity.getText(), txtPrice.getText());
                //Load file
                loadData();
                //Console message for testing
                System.out.println("Product added");
            }
            //Error handling
            else
            {
                popUpOption.showMessageDialog(popUp, "All information required!", "Attention", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        //Error handling
        else
        {
            popUpOption.showMessageDialog(popUp, "Product addtion failed!" , "Attention", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    //Function to add an object with the attributes passed into it.
    public void addItem(String inCode, String inName, String inQuantity, String inPrice)
    {
        //Creating new item object
        Item newItem = new Item();

        //Loading data
        newItem.loadFile();

        //Creating adding attributes to new item using setters
        newItem.setCode(inCode);
        newItem.setName(inName);
        newItem.setQuantity(Integer.parseInt(inQuantity));
        newItem.setPrice(inPrice);

        //Adding new item to stock
        newItem.addNewItem(newItem);

        //Saves the new item to the stock text file
        updateFile(newItem);
    }
    public void editItem( String inCode, String inName, String inQuant, String inPrice)
    {
        //Creating temporary item
        Item tItem = new Item();

        //Loading in data
        tItem.loadFile();

        //Getting the item selected
        Item editTItem = tItem.geItemFromStock(lstStock.getSelectedIndex());

        //Setting the attributes of item as ones passed in to editItem method
        editTItem.setCode(inCode);
        editTItem.setName(inName);
        editTItem.setQuantity(Integer.parseInt(inQuant));
        editTItem.setPrice(inPrice);

        //Updates the file with the newly edited item
        updateFile(tItem);
    }
    public void updateFile(Item tempItem)
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
