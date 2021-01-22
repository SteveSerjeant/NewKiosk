package com.controller;

import javax.swing.*;
import com.view.*;
import com.model.*;

public class menuController{

    public JFrame oldFrame;

    public static void startKiosk(JFrame oldFrame)
    {

        JFrame kiosk = new JFrame("Kiosk");
        kiosk.setContentPane(new IKiosk(kiosk,oldFrame).mainPanel);
        kiosk.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        kiosk.setSize(500, 300);
        kiosk.setVisible(true);
        //Will give error each launch as there is no previous panel but doesn't effect functionality.
        //oldFrame.setVisible(false);
        Item data = new Item();
        data.loadFile();
    }
    public static void startAdmin(JFrame kiosk, JList lstStock)
    {
        JFrame adminFrame = new JFrame("Admin");
        adminFrame.setContentPane(new IAdmin(adminFrame, lstStock).mainPanel);
        adminFrame.setSize(700,700);
        adminFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        adminFrame.setVisible(true);
        kiosk.setVisible(false);
    }
    public static void startCheckout(JList cartList, Float total, JFrame kiosk, JFrame checkout)
    {
        checkout = new JFrame("Checkout");
        checkout.setContentPane(new ICheckout(cartList, total,kiosk,checkout).mainPanel);
        checkout.setSize(500,500);
        checkout.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        checkout.setVisible(true);
        kiosk.setVisible(false);
    }


    //Function uses switch/case statement to switch between pages with the name of the page being the case.
//    public static void pageSetup(String inName) throws IOException
//    {
//        switch (inName)
//        {
//            case "IKiosk":
//                kioskView = new IKiosk();
//                break;
//        }
//    }

//    //Function to actually change the page using the switch statement

//    public static void pageChange(JFrame currentPage, JFrame nextFrame){
//        //Hiding the current page
//        currentPage.setVisible(false);

//        //Setting position of next page to same as current one
//        nextFrame.setLocation(currentPage.getLocation());

//        //Making next page visible
//        nextFrame.setVisible(true);

//        //Getting rid of the current page
//        currentPage.dispose();
//    }

}
