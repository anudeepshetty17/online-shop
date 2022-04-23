package com.anudeep.onlineshop.populate;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Saily
 */

public class PopulateDB_ecommerce  {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/ecommerce", "ecommerce", "ecommerce");

        boolean succeeds = false;
        String[] tbname = {"CART_ITEMS","ORDER_DETAILS", "ORDERS","USERS", "PRODUCTS", "CATEGORY", "USER_SIGNUP_OTP"};
        
        for(String i: tbname) {
            try{
               
               
                    //conn.createStatement().execute("DROP TABLE IF EXISTS " +i);
                    System.out.println("DROP TABLE IF EXISTS " +i);// System.out.println("DROP TABLE IF EXISTS " +i);
                    continue;                
            }
            catch(Exception sqle) {
            	sqle.printStackTrace();
                continue;
                
            }
        }

       // System.out.println(sql); System.out.println();
        
        succeeds = false;
        while (!succeeds) {
            String sql = "CREATE TABLE USERS("
                    + "ID BIGINT NOT NULL,"
                    + "ACTIVE BOOLEAN,"
                    + "EMAIL VARCHAR(100),"
                    + "FIRST_NAME VARCHAR(100),"
                    + "LAST_NAME VARCHAR(100),"
                    + "ADDRESS1 VARCHAR(100),"
                    + "ADDRESS2 VARCHAR(100),"
                    + "CITY VARCHAR(100),"
                    + "STATE VARCHAR(100),"
                    + "ZIP VARCHAR(10),"
                    + "COUNTRY VARCHAR(100),"
                    + "PHONE VARCHAR(100),"
                    + "ROLE VARCHAR(100),"
                    + "PASSWORD VARCHAR(500),"
                    + "CONSTRAINT USER_ID_PK PRIMARY KEY(ID))";

            try {

                //conn.createStatement().execute(sql);
                System.out.println(sql);// System.out.println("Created USERS");
                succeeds = true;

            } catch (Exception sqle) {
                
                ////conn.createStatement().execute("DROP TABLE ORDER_DETAILS");
                ////conn.createStatement().execute("DROP TABLE CART");
                //conn.createStatement().execute("DROP TABLE USERS");
                sqle.printStackTrace();
                

            }
        }
        
        succeeds = false;

        while (!succeeds) {
            String sql = "CREATE TABLE PRODUCTS("
                    + "PRODUCT_ID VARCHAR(100) NOT NULL,"
                    + "CATEGORY_TYPE INT,"
                    + "CREATE_TIME TIMESTAMP,"
                    + "PRODUCT_DESCRIPTION VARCHAR(1000),"
                    + "PRODUCT_ICON VARCHAR(1000),"
                    + "PRODUCT_NAME VARCHAR(255),"
                    + "PRODUCT_PRICE DOUBLE,"
                    + "PRODUCT_STATUS INT,"
                    + "PRODUCT_STOCK INT,"
                    + "UPDATE_TIME TIMESTAMP,"
                    + "CONSTRAINT PRODUCT_ID_PK PRIMARY KEY(PRODUCT_ID))";

            try {

                //conn.createStatement().execute(sql);
                System.out.println(sql);// System.out.println("Created PRODUCTS");
                succeeds = true;

            } catch (Exception sqle) {

                sqle.printStackTrace();
                //conn.createStatement().execute("DROP TABLE PRODUCTS");
                
            }
        }
        succeeds = false;

        while (!succeeds) {
            String sql = "CREATE TABLE USER_SIGNUP_OTP("
                    + "EMAILID VARCHAR(200) PRIMARY KEY,"
                    + "OTP INT)";
            try {

                //conn.createStatement().execute(sql);
                System.out.println(sql);// System.out.println("Created USER_SIGNUP_OTP");
                succeeds = true;

            } catch (Exception sqle) {

                sqle.printStackTrace();
                //conn.createStatement().execute("DROP TABLE USER_SIGNUP_OTP");
                
            }
        }
        
        succeeds = false;

        while (!succeeds) {
            String sql = "CREATE TABLE CATEGORY("
                    + "CATEGORY_ID BIGINT PRIMARY KEY,"
                    + "CATEGORY_NAME VARCHAR(255),"
                    + "CATEGORY_TYPE INT,"
                    + "CREATE_TIME TIMESTAMP,"
                    + "UPDATE_TIME TIMESTAMP)";

            try {

                //conn.createStatement().execute(sql);
                System.out.println(sql);// System.out.println("Created CATEGORY");
                succeeds = true;

            } catch (Exception sqle) {

                sqle.printStackTrace();
                //conn.createStatement().execute("DROP TABLE CATEGORY");

            }
        }
        
        succeeds = false;

        while (!succeeds) {
            String sql = "CREATE TABLE ORDERS("
                    + "ORDER_ID BIGINT PRIMARY KEY,"
                    + "CREATE_TIME TIMESTAMP,"
                    + "USER_ID BIGINT,"
                    + "ORDER_AMOUNT DOUBLE,"
                    + "ORDER_STATUS INT,"
                    + "UPDATE_TIME TIMESTAMP,"
                    + "NUM_OF_ITEMS INT,"
                    + "CONSTRAINT FK_ORDER_USER FOREIGN KEY (USER_ID) REFERENCES USERS(ID))";

            try {

                //conn.createStatement().execute(sql);
                System.out.println(sql);// System.out.println("Created ORDERS");
                succeeds = true;

            } catch (Exception sqle) {
                sqle.printStackTrace();
                //conn.createStatement().execute("DROP TABLE ORDER_DETAILS");
                //conn.createStatement().execute("DROP TABLE CART");
                //conn.createStatement().execute("DROP TABLE ORDERS");

            }
        }
        
        succeeds = false;
               

        while (!succeeds) {
            String sql = "CREATE TABLE ORDER_DETAILS("
            		 + "ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                     + "PRODUCT_ID VARCHAR(100),"
                     + "ORDER_ID BIGINT,"
                     + "USER_ID BIGINT,"
                     + "QUANTITY INT,"
                     + "CONSTRAINT ORDER_DETAILS_ID PRIMARY KEY(ID),"
                     + "CONSTRAINT FK_ORDER_DETAILS_ORDER FOREIGN KEY (ORDER_ID) REFERENCES ORDERS(ORDER_ID),"
                     + "CONSTRAINT FK_ORDER_DETAILS_USER FOREIGN KEY (USER_ID) REFERENCES USERS(ID))";

            try {

                //conn.createStatement().execute(sql);
                System.out.println(sql);// System.out.println("Created ORDER_DETAILS");
                succeeds = true;

            } catch (Exception sqle) {
                sqle.printStackTrace();
                //conn.createStatement().execute("DROP TABLE ORDER_DETAILS");

            }
        }
        succeeds = false;
        while (!succeeds) {
            String sql = "CREATE TABLE CART_ITEMS ("
                    + "ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "PRODUCT_ID VARCHAR(100),"
                    + "USER_ID BIGINT,"
                    + "QUANTITY INT,"
                    + "CONSTRAINT CART_ID PRIMARY KEY(ID),"
                    + "CONSTRAINT FK_CART_PRODUCT FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS(PRODUCT_ID),"
                    + "CONSTRAINT FK_CART_USER FOREIGN KEY (USER_ID) REFERENCES USERS(ID))";


            try {

                //conn.createStatement().execute(sql);
                System.out.println(sql);// System.out.println("Created CART_ITEMS");
                succeeds = true;

            } catch (Exception sqle) {
            	sqle.printStackTrace();
            	////conn.createStatement().execute("DROP TABLE ORDER_DETAILS");
                //conn.createStatement().execute("DROP TABLE CART_ITEMS");

            }
        }
        
        String insert = "INSERT INTO products "
                + "(PRODUCT_ID,CATEGORY_TYPE,CREATE_TIME,PRODUCT_DESCRIPTION,PRODUCT_ICON,"
                + "PRODUCT_NAME,PRODUCT_PRICE,PRODUCT_STATUS,PRODUCT_STOCK,UPDATE_TIME)"
                + "VALUES "
                + " ('5366019376', 1, '2019-05-03 08:19:42', '13-inch Retina Display, 8GB RAM, 256GB SSD Storage - Space Gray', 'https://m.media-amazon.com/images/I/71k3fJh5EwL._AC_SL1500_.jpg', 'Apple MacBook Air', 0.00, 0, 58, null), "

                + " ('4675032447', 1, '2019-05-07 22:47:08', '15-inch Intel Core i3-1005G1-8GB Memory - 256GB SSD - Platinum Grey', 'https://images-na.ssl-images-amazon.com/images/I/61BydH95RYL._AC_SL1500_.jpg', 'Lenovo IdeaPad 3', 417.88, 0, 8, null), "

                + " ('0071279252', 1, '2019-11-27 14:39:27', '14-inch Intel Dual Core Celeron N3350 Processor, 4GB RAM, 32GB eMMC Storage', 'https://images-na.ssl-images-amazon.com/images/I/810R8jfOCqL._AC_SL1500_.jpg', 'ASUS Chromebook', 264.50, 0, 34, null), "

                + " ('3219180626', 1, '2019-08-04 19:26:49','24-inch AMD Ryzen 3 4300U Mobile Processor, 8GB DDR4, 256GB M.2 Name SSD', 'https://images-na.ssl-images-amazon.com/images/I/81eRAX3sB6L._AC_SL1500_.jpg', 'Lenovo IdeaCentre AIO 3', 547.85, 0, 15, null), "

                + " ('1573021212', 1, '2019-05-11 21:44:06', '34 inch 21:9 Curved UltraWide WQHD IPS Monitor', 'https://images-na.ssl-images-amazon.com/images/I/81WBbFOEHwL._AC_SL1500_.jpg', 'LG 34WN80C-B', 679.99, 0, 50, null), "

                + " ('2765463962', 1, '2019-11-11 13:54:03', '4K streaming device with Alexa Voice Remote', 'https://images-na.ssl-images-amazon.com/images/I/51CgKGfMelL._AC_SL1000_.jpg', 'Fire TV Stick', 29.99, 0, 11, null), "

                + " ('1341734595', 1, '2019-05-12 18:24:58', 'Wifi Router Replacement', 'https://images-na.ssl-images-amazon.com/images/I/715jmtsmLkL._AC_SL1500_.jpg', 'Google Wifi ', 99.00, 0, 57, null), "

                + " ('9679805974', 1, '2019-01-05 08:38:17', '3.5 Inch SATA 6Gb/s 7200 RPM 256MB Cache 3.5-Inch', 'https://images-na.ssl-images-amazon.com/images/I/71Czt9ypIbL._AC_SL1500_.jpg', 'Seagate BarraCuda 2TB Internal Hard Drive', 51.99, 0, 27, null), "

                + " ('8851622106', 1, '2019-09-28 08:00:06', '8-inch HD display, 32 GB, Blue Kid-Proof Case', 'https://images-na.ssl-images-amazon.com/images/I/61lgxCi9b7L._AC_SL1000_.jpg', 'Fire HD 8 Kids Edition', 79.99, 0, 24, null), "

                + " ('3840441268', 1, '2019-05-23 15:30:23', 'Heartrate Red/Vermillion Red', 'https://images-na.ssl-images-amazon.com/images/I/71TAKO9xqXL._AC_SL1500_.jpg', 'Presidio Pro iPhone XR Case', 29.96, 0, 35, null), "

                + " ('0729129462', 2, '2019-03-24 20:28:47', '1 Count', 'https://images-na.ssl-images-amazon.com/images/I/71785Jrt80L._SL1483_.jpg', 'Yellow Onion', 0.55, 0, 48, null), "

                + " ('0742510639', 2, '2019-07-01 21:11:20', '1 lb', 'https://images-na.ssl-images-amazon.com/images/I/8143FLAy6GL._SL1500_.jpg', 'Strawberries', 4.49, 0, 52, null), "

                + " ('8104779347', 2, '2019-12-12 20:28:25', '1 Count', 'https://images-na.ssl-images-amazon.com/images/I/81UzOaQ6VyL._SL1500_.jpg', 'Lemon', 0.79, 0, 15, null), "

                + " ('7586238284', 2, '2018-03-03 21:49:52', '16 oz Bag', 'https://images-na.ssl-images-amazon.com/images/I/81DwDGOIGYL._SL1500_.jpg', 'Whole Trade French Green Beans', 4.04, 0, 28, null), "

                + " ('3499975125', 2, '2018-06-27 19:04:08', 'Peanut Butter Chocolate Chip, 6 Bars, 0.89oz Each', 'https://images-na.ssl-images-amazon.com/images/I/91-H0tTgLiL._SL1500_.jpg', 'Annies Organic Chewy Granola Bars', 3.99, 0, 12, null), "

                + " ('4505099566', 2, '2017-05-04 19:42:09', '24 Count of 240 Sheets Per Roll, Pack of 2', 'https://images-na.ssl-images-amazon.com/images/I/91j2xvxU-VL._AC_SL1500_.jpg', 'Seventh Generation White Toilet Paper', 27.58, 0, 25, null), "

                + " ('5074573703', 2, '2018-02-15 23:24:10', 'Chocolate Peanut Butter Cup, 16 oz', 'https://images-na.ssl-images-amazon.com/images/I/81%2B7NAu44wL._SL1500_.jpg', 'Talenti Gelato', 5.290, 0, 34, null), "

                + " ('8630781086', 2, '2019-02-19 23:42:01', '90 Count', 'https://images-na.ssl-images-amazon.com/images/I/81mg0otLalL._AC_SL1500_.jpg', 'Glad Drawstring Trash Bags', 12.36, 0, 44, null), "

                + " ('6015568026', 2, '2017-05-24 21:41:30', '33.8 Fl Oz, Pack of 12', 'https://images-na.ssl-images-amazon.com/images/I/71pg2bYQX8L._SL1500_.jpg', 'Essentia Water', 19.89, 0, 19, null), "

                + " ('0766989101', 2, '2018-09-01 20:41:26', '15 oz', 'https://images-na.ssl-images-amazon.com/images/I/81MPv9uJ%2BoL._SL1500_.jpg', 'Kraft Mayo Real Mayonnaise', 2.78, 0, 21, null), "

                + " ('3317302733', 3, '2019-09-22 16:01:39', '5 Gallon Portrait Glass LED Aquarium', 'https://images-na.ssl-images-amazon.com/images/I/91BTPllOzdL._AC_SL1500_.jpg', 'MarineLand Aquarium Kit', 50.39, 0, 23, null), "

                + " ('8819701637', 3, '2019-10-27 20:00:26', 'Basking Ledges for Fish, Reptiles, Amphibians, and Small Animals', 'https://images-na.ssl-images-amazon.com/images/I/718xlASJ9eL._AC_SL1500_.jpg', 'Penn-Plax Reptology Shale Step Ledge', 20.01, 0, 11, null), "

                + " ('3593281853', 3, '2019-12-13 19:14:48', 'Aqua Blue Quick Catch Mesh Wire Net Safe for All Fish – 4 Inches', 'https://images-na.ssl-images-amazon.com/images/I/615nXSppkqL._AC_SL1500_.jpg', 'Penn Plax Aquarium Fish Net', 1.99, 0, 43, null), "

                + " ('9389821178', 3, '2019-10-12 17:27:41', 'Chicken Vegetable Hanging Feeder 2Pcs', 'https://images-na.ssl-images-amazon.com/images/I/61FjKlTc-nL._AC_SL1200_.jpg', 'Vehomy Chicken Xylophone Toy', 13.99, 0, 27, null), "

                + " ('8866880912', 3, '2019-08-27 20:02:28', '6-Dose, 5-9 Pounds', 'https://images-na.ssl-images-amazon.com/images/I/81g59cD1kPL._AC_SL1500_.jpg', 'Advantage II Flea Prevention', 47.18, 0, 50, null), "

                + " ('4583526670', 3, '2019-11-16 00:20:01', 'Dog and Cat', 'https://images-na.ssl-images-amazon.com/images/I/61X%2BD0I-T8L._AC_SL1000_.jpg', 'Best Pet Supplies Pet Tent', 22.99, 0, 30, null), "

                + " ('5460025153', 3, '2019-08-09 19:52:11', 'Chicken & Steak', 'https://images-na.ssl-images-amazon.com/images/I/91Z1i3zWcDL._AC_SL1500_.jpg', 'Pedigree Adult Dry Dog Food', 20.95, 0, 36, null), "

                + " ('2217689821', 3, '2019-09-27 19:35:49', 'Dog Treats', 'https://images-na.ssl-images-amazon.com/images/I/811yqazMe6L._AC_SL1500_.jpg', 'Milk-Bone Dog Treats', 7.19, 0, 10, null), "

                + " ('9146479133', 3, '2019-09-25 22:35:55', 'Cats and Dogs', 'https://images-na.ssl-images-amazon.com/images/I/51uDvpICZ6L._AC_SL1200_.jpg', 'PetSafe Food Dispenser', 89.95, 0, 18, null), "

                + " ('1922455118', 3, '2019-10-29 22:30:35', 'Guinea Pig,Rabbit,Chinchilla and Other Small Animals', 'https://images-na.ssl-images-amazon.com/images/I/81sV1XF-GvL._AC_SL1500_.jpg', 'Romalon Indoor Hay Feeder/Rack', 10.77, 0, 20, null), "

                + " ('0875720180', 4, '2019-08-01 23:54:12', '0.17 Fl Oz, 1 Count', 'https://images-na.ssl-images-amazon.com/images/I/61PPci2flAL._SL1500_.jpg', 'Maybelline SuperStay Matte Ink Liquid Lipstick', 7.98, 0, 54, null), "

                + " ('7653919300', 4, '2018-12-21 22:59:31', 'Hot Air Brush', 'https://images-na.ssl-images-amazon.com/images/I/71aXzv34N%2BL._SL1500_.jpg', 'REVLON Hair Dryer And Volumizer', 39.99, 0, 7, null), "

                + " ('9195967637', 4, '2018-10-17 00:17:39', 'Tropical Mango, 18oz', 'https://images-na.ssl-images-amazon.com/images/I/61OzSj4jB3L._SX679_.jpg', 'Tree Hut Shea Sugar Scrub', 6.48, 0, 26, null), "

                + " ('2445171235', 4, '2018-10-06 17:31:35', 'Makeup Blender, Set of 4', 'https://images-na.ssl-images-amazon.com/images/I/81FQhO1UGML._SL1500_.jpg', 'Real Techniques Beauty Sponge', 9.00, 0, 35, null), "

                + " ('3359057997', 4, '2019-11-26 21:17:44', '16 Pcs Professional Eye Brush Set', 'https://images-na.ssl-images-amazon.com/images/I/71amjJDMVIL._SL1500_.jpg', 'BESTOPE Eye Makeup Brushes', 9.99, 0, 38, null), "

                + " ('4560759885', 4, '2019-08-25 17:26:39', 'Beeswax Bounty Assorted Gift Set', 'https://images-na.ssl-images-amazon.com/images/I/71MUSyr%2BEZL._SL1500_.jpg', 'Burt’s Bees', 9.57, 0, 17, null), "

                + " ('8149679252', 4, '2019-10-15 17:04:03', 'Finishing Powder, Fair Light, 0.7 oz.', 'https://images-na.ssl-images-amazon.com/images/I/81nUSk6fVUL._SL1500_.jpg', 'Maybelline New York Fit Me Loose', 5.33, 0, 23, null), "

                + " ('4138946757', 4, '2019-01-14 19:54:55', '0.85 Ounces, Pack of 3', 'https://images-na.ssl-images-amazon.com/images/I/81I1c0NHOzL._SL1500_.jpg', 'Bio-Oil Skincare Oil', 19.47, 0, 47, null), "

                + " ('3101598222', 4, '2019-10-27 20:23:04', '25 Count, 2 Pack', 'https://images-na.ssl-images-amazon.com/images/I/81z8tQe35hL._SL1500_.jpg', 'Neutrogena Makeup Remover Wipes', 8.97, 0, 13, null), "

                + " ('5973175208', 4, '2019-02-09 00:25:35', 'Body Cream with Hyaluronic Acid, Niacinamide, and Ceramides,19 Ounce', 'https://images-na.ssl-images-amazon.com/images/I/61S7BrCBj7L._SL1000_.jpg', 'CeraVe Moisturizing Cream', 18.39, 0, 38, null), "

                + " ('9380694761', 5, '2019-07-07 15:41:29', 'Girls & Boys Ages 3-12', 'https://images-na.ssl-images-amazon.com/images/I/81gVqxwc5kL._AC_SL1280_.jpg', 'Zen Laboratory DIY Slime Kit', 23.95, 0, 27, null), "

                + " ('1365270235', 5, '2019-12-14 07:43:37', 'STEM Toy for Boys and Girls Age 8 and Up', 'https://images-na.ssl-images-amazon.com/images/I/71KJQygS1OL._AC_SL1300_.jpg', 'ThinkFun Circuit Maze', 29.99, 0, 20, null), "

                + " ('1506653440', 5, '2019-09-03 17:22:09', 'Six double-sided, interUSER_ACTIVE pages feature animals from 12 categories such as the forest, the ocean and the shore', 'https://images-na.ssl-images-amazon.com/images/I/71SDfj4aXHL._AC_SL1500_.jpg', 'LeapFrog 100 Animals Book', 17.99, 0, 21, null), "

                + " ('4895468860', 5, '2019-11-18 22:40:28', 'Rechargeable Light Up Ball Drone Infrared Induction Helicopter', 'https://images-na.ssl-images-amazon.com/images/I/61Gc5SPf4kL._AC_SL1500_.jpg', 'Betheaces Flying Ball Toys', 12.99, 0, 37, null), "

                + " ('8536500000', 5, '2019-05-28 22:24:28', 'Infant to toddler musical walker and ride-on toy', 'https://images-na.ssl-images-amazon.com/images/I/618LzjKfb0L._AC_SL1500_.jpg', 'Fisher-Price Walk Bounce & Ride Pony', 39.99, 0, 55, null), "

                + " ('5479927664', 5, '2019-04-25 12:23:13', 'Every action activates unique patterns of lights, colors and sounds', 'https://images-na.ssl-images-amazon.com/images/I/91e0g-1TiFL._AC_SL1500_.jpg', 'LeapFrog My First Learning Tablet', 16.29, 0, 15, null), "

                + " ('5708331273', 5, '2019-05-03 15:44:37', 'Toy Keys for Toddlers and Babies', 'https://images-na.ssl-images-amazon.com/images/I/51tmj6qYbAL._AC_SL1000_.jpg', 'B. toys–FunKeys Toy', 10.53, 0, 16, null), "

                + " ('6098890645', 5, '2019-08-21 19:58:56', '11 pcs Stack Up Cup Toys for 10 Month+ Infant', 'https://images-na.ssl-images-amazon.com/images/I/51NsSdqJs1L._AC_SL1500_.jpg', 'GOODWAY Stacking & Nesting Cups', 12.49, 0, 29, null), "

                + " ('0446777953', 5, '2019-10-06 05:23:32', 'Animals Jigsaw Puzzle Sorting and Stacking Games', 'https://images-na.ssl-images-amazon.com/images/I/61XjvLcqD0L._AC_SL1000_.jpg', 'LIKEE Wooden Pattern Blocks', 18.39, 0, 17, null), "

                + " ('7523780730', 5, '2019-06-22 08:45:19', 'USER_ACTIVE Toy for Babies and Toddlers 9 Months and Up with 4 Balls', 'https://images-na.ssl-images-amazon.com/images/I/71JE-2xUiHL._AC_SL1500_.jpg', 'Playskool Chase n Go Ball Popper', 30.99, 0, 33, null), "

                + " ('7929634051', 6, '2017-10-23 22:26:07', 'Household Hand Tool Kit', 'https://images-na.ssl-images-amazon.com/images/I/91JQZ%2BjOi4L._AC_SL1500_.jpg', 'Cartman 148-Piece Tool Set', 27.99, 0, 8, null), "

                + " ('9340926421', 6, '2020-06-14 11:53:08', '23-Piece, 1/2 Drive Metric/SAE', 'https://images-na.ssl-images-amazon.com/images/I/61iCG02fNbL._AC_SL1000_.jpg', 'DEWALT Impact Socket Set', 44.09, 0, 18, null), "

                + " ('8225352571', 6, '2020-07-13 16:02:47', '8pcs', 'https://images-na.ssl-images-amazon.com/images/I/61v7Zho-LJL._AC_SL1000_.jpg', 'Klein Tools Screwdriver Set', 66.58, 0, 32, null), "

                + " ('1782952582', 6, '2019-05-11 07:13:43', '2-Pk', 'https://images-na.ssl-images-amazon.com/images/I/81ABj3m638L._AC_SL1500_.jpg', 'Radial Trailer Tire Rim', 319.95, 0, 22, null), "

                + " ('4470093517', 6, '2017-09-21 08:56:20', 'Vinyl, Rubber and Plastic Non-Greasy Dry-to-the-Touch', 'https://images-na.ssl-images-amazon.com/images/I/61JMaPElNhL._AC_SL1000_.jpg', 'Chemical Guys Tire Dressing', 8.97, 0, 33, null), "

                + " ('3540206684', 6, '2019-10-24 10:52:14', 'Cleaner for Cars, 18 Oz', 'https://images-na.ssl-images-amazon.com/images/I/71atCXHQ4nL._AC_SL1500_.jpg', 'Armor All Extreme Car Tire Foam Spray Bottle', 4.47, 0, 34, null), "

                + " ('7209270111', 6, '2017-12-18 19:54:05', 'Charcoal Gray', 'https://images-na.ssl-images-amazon.com/images/I/51Ntz25nijL._AC_SL1000_.jpg', 'BDK PolyPro Car Seat Covers', 23.75, 0, 21, null), "

                + " ('2850837619', 6, '2019-12-14 13:58:40', 'Weather Protection, Universal Trim to Fit', 'https://images-na.ssl-images-amazon.com/images/I/61oa6jhqVjL._AC_SL1000_.jpg', 'Motor Trend Rubber Floor Mats', 37.63, 0, 57, null), "

                + " ('8576848165', 6, '2018-09-05 16:18:01', '15-inch, Microfiber Leather Viscose, Breathable, Anti-Slip', 'https://images-na.ssl-images-amazon.com/images/I/81rnLmJqzOL._AC_SL1500_.jpg', 'KAFEEK Steering Wheel Cover', 12.23, 0, 50, null), "

                + " ('3237189120', 6, '2020-11-06 15:50:52', '2.5 Ton Low Profile', 'https://images-na.ssl-images-amazon.com/images/I/71SZAEhA5SL._AC_SL1500_.jpg', 'Liftmaster Jack', 64.99, 0, 20, null), "

                + " ('9370636475', 7, '2020-10-11 22:01:52', '20-Pound Hourglass', 'https://images-na.ssl-images-amazon.com/images/I/415Eu2YD0pL._AC_.jpg', 'Hourglass Dumbbell Set', 41.23, 0, 14, null), "

                + " ('2048221797', 7, '2017-09-30 14:52:09', '2 Pack', 'https://images-na.ssl-images-amazon.com/images/I/61hHtUlD0iL._AC_SL1105_.jpg', 'Emoly Jump Rope', 11.99, 0, 3, null), "

                + " ('2640839734', 7, '2019-02-07 19:32:43', 'Box of 3', 'https://images-na.ssl-images-amazon.com/images/I/81-hL6WRYYL._AC_SL1500_.jpg', 'Rawlings Official League Baseballs', 9.99, 0, 31, null), "

                + " ('8422437826', 7, '2019-04-24 13:41:00', '60-inch Adjustable Height Tempered Glass Backboard', 'https://images-na.ssl-images-amazon.com/images/I/61W%2BQWGQ0sL._AC_SL1500_.jpg', 'Silverback In-Ground Basketball Hoop', 637.49, 0, 21, null), "

                + " ('9071147075', 7, '2018-04-10 08:12:39', 'Basketball', 'https://images-na.ssl-images-amazon.com/images/I/91vdgs5FY4L._AC_SL1500_.jpg', 'Wilson Evolution Game Basketball', 64.99, 0, 49, null), "

                + " ('4651353189', 7, '2019-08-03 00:36:30', 'Size 3', 'https://images-na.ssl-images-amazon.com/images/I/61jU4o9laBL._AC_SL1200_.jpg', 'Wilson Traditional Soccer Ball', 15.19, 0, 41, null), "

                + " ('2847519495', 7, '2019-02-13 09:57:17', 'Complete Kit', 'https://images-na.ssl-images-amazon.com/images/I/811dERKLr0L._AC_SL1500_.jpg', 'Franklin Sports Ball Maintenance Kit', 11.99 , 0, 15, null), "

                + " ('0147737803', 7, '2019-01-14 18:17:52', 'Boxing Gloves', 'https://images-na.ssl-images-amazon.com/images/I/61i0i4-1aBL._AC_SL1000_.jpg', 'Everlast Pro Style Training Gloves', 59.00, 0, 9, null), "

                + " ('5229353302', 7, '2019-08-24 02:54:31', '100 lb, Black', 'https://images-na.ssl-images-amazon.com/images/I/81pCv8PO64L._AC_SL1500_.jpg', 'Everlast HydroStrike Water Bag', 123.89, 0, 28, null), "

                + " ('1384170136', 7, '2020-01-10 17:57:19', '24/7 Heart Rate, Black/Black, One Size', 'https://images-na.ssl-images-amazon.com/images/I/71wPLzgLNYL._AC_SL1500_.jpg', 'Fitbit Inspire 2', 69.00, 0, 51, null) ";
        try {

                //conn.createStatement().execute(insert);
                System.out.println(insert);// System.out.println("PRODUCTS POPULATED");
                succeeds = true;

            } catch (Exception sqle) {
                sqle.printStackTrace();

            }
        
          insert = "INSERT INTO category"
                + "(CATEGORY_ID,CATEGORY_NAME,CATEGORY_TYPE,CREATE_TIME,UPDATE_TIME) "
                + "VALUES "
                + "(2345440205, 'All', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),"
                + "(2313940205, 'Electronics', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),"
                + "(4223546480, 'Food & Grocery', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),"
                + "(3516176897, 'Pet Supplies', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),"
                + "(1713306480, 'Beauty & Health', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),"
                + "(0224122836, 'Toys, Kids & Baby', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),"
                + "(8493814043, 'Automotive & Industrial', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),"
                + "(5774641647, 'Sports', 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
          try {

                //conn.createStatement().execute(insert);
                System.out.println(insert);// System.out.println("PRODUCT_CATEGORY POPULATED");
                succeeds = true;

            } catch (Exception sqle) {
                sqle.printStackTrace();

            }
          
            insert = "Insert into users (id, active, email, first_name, last_name, address1, address2, city, state, zip, country, phone, role,password) "
                    + "values "
                    + "(2147483641, true, 'admin@shoppingapplication.com', 'Shopping App', 'Admin', '430 S Michigan Ave', '01', 'Chicago', 'Illinois', '60605','US','3123413500','ROLE_MANAGER', '$2a$10$sIsYzTgwro/mmDONrl/x3eb6rtauA0sp/8FTy74G4mxWjnk2RI89.'),"
                    + "(2147483642, true, 'andy@gmail.com', 'Andy', 'Brown', '111 S Michigan Ave', '425', 'Chicago', 'Illinois', '60601','US','123456890','ROLE_CUSTOMER', '$2a$10$sIsYzTgwro/mmDONrl/x3eb6rtauA0sp/8FTy74G4mxWjnk2RI89.')";  
          try {

                //conn.createStatement().execute(insert);
                System.out.println(insert);// System.out.println("USERS POPULATED");
                succeeds = true;

            } catch (Exception sqle) {
                sqle.printStackTrace();

            }
          

    }

    
}
