package com.model;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Item
{

    public String name;

    public String code;

    public int quantity;

    public String price;

    public String[] tArray;

    public String fileLocation = "Resources\\stock.txt";
    public String separator = "\\,";
    public final ArrayList<Item> stock = new ArrayList<>();

    //Method to get all info about item excluding the code and return as string. To display on the kiosk.
    public String allInfo()
    {
        String info = new String(name + " £" + price + " (" + quantity + " in stock)");
        return info;
    }
    public String[] getStock()
    {
        return tArray;
    }

    public String basketInfo()
    {
        String basketInfo = new String(name + " £" + price);
        return basketInfo;
    }
    //Gets specific item from stock using selected index of list
    public Item geItemFromStock(int inIndex)
    {
        //If the index  passed in is not within the scope of the range then return null
        if(inIndex >= stock.size())
        {
            return null;
        }

        //If selected index has an item location, return the item in that location
        return stock.get(inIndex);
    }

    //Getters and setters
    public String getName()
    {
        return this.name;
    }

    public void setName(String inName)
    {
        this.name = inName;
    }

    public Integer getQuantity()
    {
        return this.quantity;
    }

    public void setQuantity(int inQuantity)
    {
        this.quantity = inQuantity;
    }

    public String getCode()
    {
        return this.code;
    }

    public void setCode(String inCode)
    {
        this.code = inCode;
    }

    public String getPrice()
    {
        return this.price;
    }

    public void setPrice(String inPrice)
    {
        this.price = inPrice;
    }

    public void addNewItem(Item newItem)
    {
        stock.add(newItem);
    }
    public void deleteItem(Item delItem)
    {
        stock.remove(delItem);
    }


    public void loadFile()
    {
        try
        {
            File file = new File(fileLocation);

            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine())
            {
                //Gets next line and stores as string
                String itemLine = scanner.nextLine();

                tArray = itemLine.split(separator);

                Item tItem = new Item();

                tItem.setCode(tArray[0]);
                tItem.setName(tArray[1]);
                tItem.setQuantity(Integer.parseInt(tArray[2]));
                tItem.setPrice(tArray[3]);
                stock.add(tItem);

                getStock();

            }
            scanner.close();

            System.out.println("File loaded");
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not loaded");
            e.printStackTrace();
        }
    }
}
