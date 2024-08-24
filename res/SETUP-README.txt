***No additional files are REQUIRED to run this program if you would like to use the API or any
AlphaVantage API csv files stored locally on your computer. However, if you would like to use
offline data in our program without ever calling an API, we have provided some files for you***

---------------------------------------------------------------------------------------------------
To access them with your jar file:
  1. From the res folder of this submission, copy the jar file and all files in the res/Stocks
     folder into a new common folder somewhere on your laptop.
  2. In that new folder you created, you should have the jar file and various csv files labeled
     with their ticker symbols.
  3. Once you run the jar file from this new folder on your terminal (powershell/shell), you should
     be able to feed in the files when prompted by just typing GOOG or AAPL for example.
---------------------------------------------------------------------------------------------------

1. From your terminal (powershell/shell), navigate to the folder at which your jar and data files
   are stored.

2a. Once you are in that directory, type "java -jar Assignment6.jar"

3a. After you have typed that, the program should be running as you will see the GUI.

2b. Once you are in that directory, type "java -jar Assignment6.jar -text"

3b. After you have typed that, the program should be running as you will
   be prompted with the welcome message and a menu of options

---------------------------------------------------------------------------------------------------

List of stocks supported:

-All stocks available on the AlphaVantage API
 (https://www.alphavantage.co/query?function=LISTING_STATUS&apikey=demo)

-All stocks for which the user has a local csv file following the same format as
 csv file from AlphaVantage API for stock information

-Stocks whose files we have provided in our res/Stocks folder
-AAPL, AMZN, DAL, GOOG, MSFT, NVDA, SHEL, SONY, SP, TESLA

Date Restrictions:
No data for dates that are not available from the AlphaVantage API for each stock
unless the user has their own local file of dates, at which point the restriction
depends on their information