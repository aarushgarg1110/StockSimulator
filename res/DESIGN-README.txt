text description of this design:

--------------------------------------------------------------------------------------------------
Design Changes for Assignment 6:

1. We noticed that the command classes were too coupled with view. So we made a new public method
   called prompter that handles the displaying of prompts and taking inputs. This way, I can call
   it for text line based controllers and not call it with gui controllers. And so, we modified
   the controller implementations to call the prompter method before execute.

2. We have added a new constructor that does not require a scanner, only the parameters needed for
   each command as the GUI will provide all of those through its text boxes and whatnot. Its
   scanner is set to null as not needed, and null checks have been added. These constructors were
   added to only the commands needed by the view as of right now.

------------------------------------------------------------- |
Assignment 6 additions:                                       |
The design for this application utilizes the MVC architecture |
------------------------------------------------------------- |
Controller
---------
The Features interface:
    This represents the callbacks interface. These callbacks are the features
    of the view, and what the listeners of the view must be able to execute and provide. Supports
    good design through callbacks as provided in lecture notes.

The IGUIController interface:
    GUIControllers have a public setView method to set any GUIView as its
    view. As it is a public method that should be enforced, an interface was made for all
    GUIControllers.

The GUIController class:
    This is the implementation of IGUIController. It implements both of the above interfaces and
    takes care of setting up its view and the callback functions defined.


NOTE***:
    We acknowledge that for this assignment, there were two ways to go about it: Have
    specialized controllers for each view, or possible have one controller that can handle
    all text based and graphical UIs. We chose to go with the former design as both have
    their own pros and cons, but the former proved to be more favorable. Having specialized
    controllers has the advantage of high cohesiveness. Each controller has a specific and singular
    purpose, either to manage the text based view, or graphical view. Additionally, this allows
    users of previous versions to keep their software running with no problems as that controller
    has not been altered. We wanted to promote interface segregation as at this point in time,
    the GUI has less methods than the text-based one. So if we had one controller, it would have
    the callbacks, plus old functionality for methods not required in this assignment. In the
    future, when all features are equal and available, a unified controller will be preferred.



View
----
The GUIView interface:
    We made a new GUIView interface that extends IView. This interface was created because views
    that deal with GUIs have an additional addFeatures method allowing Listeners to add themselves
    to this view. That method must be enforced for all GUI view classes.

The GraphicsView2 class:
    This is the implementation of GUIView for this assignment. We basically broke the panels of the
    GUI down into separate classes for higher cohesiveness and better organization. This class puts
    them all together when initialized. It also transmits the results messages it gets to one of
    its own panels. It has two popups to welcome the user and show the features available. After
    that, it sends the listener to the other panel classes as those panel classes hold the buttons.

The ...Panel classes: Purpose described above

The AbstractPanel class:
    Simply abstracts common functionality from panels that may appear on a view. Chose this over
    an interface as not all panels may have text fields, or need to handle errors, etc. With an
    abstract class, can reuse common functionality without enforcing it on every panel. It has a
    nested GhostText field that is common for multiple panels with text. It shows the user the
    expected input as is common on the internet.

The MockGUIView class:
    This class was used for mock testing to ensure that it shows the right messages we want for
    this specific implementation. Also was the workaround for not having our GUI pop up every time
    we ran a test.

---------------------------------------------------------------------------------------------------




---------------------------------------------------------------------------------------------------
Design changes/enhancements to existing code for Assignment 5: (mostly minor technical changes)

    1. Changed return type of queryStock() from Stock to Investment to avoid return of
       concrete class

    2. add sellInvestment method to SimplePortfolio,

       the previous implementation only focused on
       buying which is why this was overlooked, but it only makes sense for any sort of portfolio
       to be able to remove objects if one can add objects to it.
       SimplePortfolio now has a simple sellInvestment method that is the opposite of
       addInvestment.

    3. changed addInvestment to take in a double quantity, not int quantity for 2 reasons:

       a. The controller already uses Integer.parseInt to ensure that the user types in an int and
       not a double. That invalid input is already accounted for at time of input, so no need to
       double enforce that constraint.
       b. By removing this extra constraint, the programmer now has more flexibility when it comes
       to any new or existing functionality that involves adding shares. The programmer can add
       partial shares as needed for any computations as all that matters is that the user cannot
       buy partial shares. (Ex. rebalancing allows programmer to buy partial shares)

    4. modified PortfolioManager to abstract duplicate logic in addPortfolio and create portfolio
       methods, resulting in less duplicate code

       Now able to override this protected method in the extended BetterPortfolioManager class to
       utilize PortfolioV2 objects and reduce duplication across logic in methods there as well

    6. In InvestmentController, abstracted duplicate code between my two IController
       implementations using AbstractInvestmentController class that extends IController.

    7. In AbstractCommands, added a helper method to enforce a more user-friendly and less
       error-prone in the form of having the user specify dates.

       Adjusted all existing commands class that required a date input to use this version instead.
       Added $ to units in output
       allow user to type lowercase and uppercase letters for ticker symbols

    8. ValueProviders interface: unify the getEndOfDayVal and getValOfPortfolio methods under one
       interface as they serve the exact same purpose.

---------------------------------------------------------------------------------------------------
------------------------------------------------------------- |
Assignment 5 additions:                                       |
The design for this application utilizes the MVC architecture |
------------------------------------------------------------- |

Model
-----
The Transaction class:
    As we are now passing in dates for most methods, saving files, loading files, rebalancing, and
    checking if sales are valid based on dates of future transactions, it is important to store
    that information. The transaction information consists of the stock involved in a transaction,
    the amount of shares of that stock involved, the date on which that transaction was conducted,
    and whether the user bought or sold for that transaction. This information is very useful to
    parse through when needing to reference the dates for each transaction, and allows
    BetterPortfolio to not rely on the private fields of the SimplePortfolio delegate.
    Additionally, saving a portfolio means it must hold of its relevant information. This class is
    perfect for only including in the file what is needed. For example, there is no need to store
    heaps of historical data in the file, as we can recover that again later quickly. The types
    enum was left public so other classes in both the model and controller packages can reference
    it. We thought the enum was very basic as it exposes no information that should be private, it
    simply represents a buy or sale, nothing more.

The PortfolioV2 interface:
    This interface specifies the operations for a newer implementation of the Portfolio interface
    that supports more functionality. It extends Portfolio interface so that objects of PortfolioV2
    are backwards compatible in places where objects of type Portfolio are needed. Additionally,
    the existing addInvestment and sellInvestment methods had to be overloaded to include a date
    parameter as the user must specify when they would like to do those actions. Since this
    implementation requires a date to be specified, we wanted to render the previous two method
    signature unusable. We accomplished that by making the extended methods default to give it a
    method body, and throwing unsupported operation exceptions for the old signatures, as that is
    the case. Now only the new signatures for those two methods will work on these PortfolioV2
    objects.

The BetterPortfolio class:
    This class represents the new and improved portfolio that has more functionality than the old
    SimplePortfolio. This class implements PortfolioV2 interface, which extends Portfolio
    interface, thus making BetterPortfolio backwards compatible with Portfolio objects. As
    mentioned in the SimplePortfolio class, this one uses SimplePortfolio as a delegate for
    composition, as every future portfolio is at its very skeleton a simple portfolio, just with
    more restrictions and functionality. It also logs transactions as mentioned before in a list
    and holds a map of <String, Investment> which stores the investment objects added so far,
    making it not necessary for the user to specify the source of the stocks they would like to
    reference once again or multiple times. From here on, methods are defined to implement the
    functionality that is specific to PortfolioV2 objects. The queryStock() method is simply
    extended and not typed again using delegate as that one method does not require any portfolio
    instance at all.

The IModelV2 interface:
    This interface is similar to the previous IModel interface, but has more functionality. Once
    again IModel(V2) is the interface for our main "model" class that is fed into the main program.
    So this interface extends IModel, further reinforcing backwards compatibility when putting
    IModelV2 objects where IModel objects previously were.

The BetterPortfolioManager class:
    Similar to the previous PortfolioManager class, this is the new and improved BetterPortfolio-
    Manager. Design is pretty straightforward similar to PortfolioManager, however it holds
    PortfolioV2 objects, not Portfolio objects as these new functions are for the new
    implementations. We override the executeOnPortfolio() method that was inherited to deal with
    PortfolioV2 objects instead, abstracting duplicate logic in code. One design choice I must note
    refers to the loadPortfolio method. As mentioned before, a saved XML file only contains the
    essential information needed to reduce complexity of file, so it stores ticker names, quantity,
    dates, and type. However, when loading a portfolio, we don't want to ask the user to re-specify
    the source of information for each and every stock in their portfolio as that would be tedious,
    and the user may not remember all those original sources if there are too many stocks in the
    portfolio. To remedy this, we warn the user and make a reasonable assumption to generate stock
    objects using the AlphaVantage API for all of them. It is a reasonable assumption because even
    when a user previously used manual file entry for sources, it is reasonably expected for that
    historical data to be almost as accurate and as aligned as the historical data from an API.
    This method does require internet connection, as otherwise, it would not be user-friendly to
    manually load in 20 files for 20 stocks once again.

The LineChartGenerator class:
    This is a package-private class responsible for the logic for making a performance bar chart.
    It was made into its own class due to the extensive logic. It mainly utilizes the JDK
    ChronoUnit package and our own way of keeping track of scaling to produce a string
    representation of the bar chart.

The ValueProviders interface:
    As mentioned above in the changes, this interface serves the purpose of unifying the methods
    of getting the value of a portfolio or stock at the end of the day. Instead of considering them
    to both be separate methods, we unite them under 1 method in 1 interface as they do the exact
    same thing.


---------------------------------------------------------------------------------------------------
Controller
----------

The StocksCommandV2 interface:
    This interface extends StocksCommands for greater code flexibility and code reusability.
    Emphasizes separation of concerns as this interface is meant to represent commands that
    work with the IModelV2 interface specifically. Calls execute on IModelV2 objects, while
    saying that the old execute method which could take in old IModels are unsupported, if the
    user chooses to use this implementation.

The AbstractCommandsV2 class:
    Simply use adapter pattern to extend functionality from the original AbstractCommands to
    make reuse of existing code, but implement StocksCommandsV2 as that is the target object
    of the following commands for IModelV2 objects that extend this abstract class.

The AbstractInvestmentController class:
    Utilized this class to extract common logic between the two IController interfaces. Both
    implementations have common fields of model and view and readable. Additionally, we reuse
    some existing commands such as the ones regarding stocks, so through abstraction, both classes
    can come equipped with those identical commands rather than instantiating once again.
    Abstracts common duplicate code in the start method. Abstracts the code for command design
    pattern regarding StocksCommands as BetterInvestmentController is supposed to refer to that
    functionality if the user-requested command is not a V2 command.

The BetterInvestmentController class:
    Newer and better implementation of old InvestmentController as it supports more functionality
    regarding V2 objects. Stores more commands.


Just like before, every other class beginning with Handle is a command class that executes a
command. created new Handle... command classes for the new functionalities needed in the program.

Decided to make new Controller implementation instead of altering existing one
so that old users of previous function still have a functional program to use.




---------------------------------------------------------------------------------------------------
View
----

The TextViewV2 class:
    This class represents an IView implementation for the BetterPortfolioManager model class and
    BetterInvestmentController controller class. This class extends the existing TextView as all
    methods but one are exactly the same. Overrode showMenu() to display more options for the user,
    use super call to TextView showCustomMessage() method to utilize existing code and not needing
    to have this class have its own private final Appendable field.

---------------------------------------------------------------------------------------------------

------------------------------------------------------------- |
Assignment 4:                                                 |
The design for this application utilizes the MVC architecture |
------------------------------------------------------------- |

Model
------
The Investment interface:
    We chose to use an Investment interface to allow this project to be open to extension.
    We kept in mind that later on, it may be feasible to perform similar operations with other
    forms of investments such as bonds, ETFs, Mutual Index Funds, Cryptocurrency, foreign currency,
    etc. So, someone can create classes for those under a common investment interface as they would
    likely all have similar methods.

The Stock class:
    Going off of the above point, This stock class represents the objective of this assignment
    which was centered around stocks. We chose to represent a stock through its ticker symbol
    and all of its historical data. The historical data is stored as a List of StockData objects.
    One StockData object represents the high, low, open, and close prices and volume for a certain
    stock on a specific date. The List will have data for all timestamps that can be retrieved. We
    utilized abstraction in the methods by creating a helper method which abstracted the shared
    logic of identifying which timestamps/dates needed to be analyzed.
    The StockData objects are nested inside this class, which we will explain in the next section.

The StockData class:
    We chose to use a nested static class as the only class which needs stock data information is
    the Stock class itself. There is no reason for this object to exist other than for the Stock
    objects. This class represents one object holding all the prices data at one point in time.
    As mentioned above, the Stock object holds a list of these objects.

The StockDataSource interface:
    This interface represents the operation of fetching data for stock objects. As data can be
    obtained from a variety of sources (files, API, manual, etc), we used an interface as all of
    these sources will consist of the operation of retrieving data. Additionally, using an
    interface means that in the future, we can easily add more sources that are unified with the
    ones we currently have. So essentially, through this interface, our program supports multiple
    sources.

The AbstractDataSource class:
    This class is abstract as it represents the shared logic in fetching data from any source.
    From any source, the pattern follows: check if the data is already stored locally (CacheManager
    which we'll talk about shortly), scan the data source, and then construct StockData objects
    from that data source. The only public method is fetchData() as the other steps are just
    helpers in this overall process. From this class, each extended class will utilize fetchNewData
    for the specific and unique steps.

The APIStockDataSource and FileStockDataSource classes:
    APISDS class represents getting data from the AlphaVantage API. FileSDS class represents
    getting data from a csv file containing the same information as produced by AlphaVantage.
    As stated above, user has multiple ways of getting data.

    We started with just the APISDS, but then implemented FileSDS to support offline functionality.
    A user simply needs to input a locally downloaded file to access all the functions.

The CacheManager class:
    This class is something unique that we added. It represents a local cache of all previously
    queried stock data. When talking about APIs, there are only so many requests that can be made
    before a limit is hit. Why waste usages contributing to that limit on data that was already
    retrieved? This way, we improve efficiency as locally stored data is already available to
    produce, and address the issue of overloading an API. This class will store fetched data from
    any source, files, API, or any other future source implemented.

    For this class, we utilized the singleton pattern. The singleton pattern only allows one
    instance of an object to exist at a time, so that pattern is perfect here. We only want one
    local cache. All queries should be sent to one centralized location for easy and fast recovery.
    Whenever a user wants to fetch any data, all requests will first be checked against the local
    cache to see if extra work is needed at all. Additionally, this class supports the feature
    of offline usage.

The Portfolio interface:
    This interface represents the operations for any portfolio a user may construct. When thinking
    about the future, many forms of portfolios can exist, so it is best to have an interface
    representing common functionality between all those implementations.

The SimplePortfolio class:
    For example, one instance can be a simple portfolio that only holds stock objects. This class
    represents that and supports operations of adding stocks, and getting value in regard to
    stocks added. It is a simple portfolio. However, when regarding our Investment interface, and
    how we specified that in the future, different forms of investments can be made possible, we
    wanted to allow the ability to create portfolios representing that. However, we do not want
    users to override or mess up the working code for a simple portfolio. So it is a final class
    that is meant to be used for composition, not inheritance. For example, if a user wants a
    portfolio of bonds and stocks, they can make a new ComplexPortfolio class that uses a
    SimplePortfolio delegate object. In their methods, whenever they want to address adding stocks
    or getting value of stocks, they can delegate. Then they can override the addInvestment and
    getValueOf methods to additionally include logic for adding and getting the value of bonds.

    Users can add new functionality to portfolios through composition, if they want to hold more
    than just stocks in the future.

The PortfolioManager class:
    This class is the main "Model" class. This model is what will be used for the application
    It stores a map of portfolios associated with a name. Each portfolio has a map of stocks.
    It is very easy to delegate to the Stock class for the features requiring analysis of a
    specific stock.

The IModel:
    This is the interface for classes representing models. Future model classes with new
    functionalities can implement an interface that extends this IModel interface.
------------------------------------------------------------------------------------------

Controller
-----------
The InvestmentController class:
    Similar to the PortfolioManager class, this is the main controller class for this program.
    This controller follows the extensible command design pattern by creating a Map of known
    commands. These commands are all delegated to classes that are unified under a common
    interface. With this design, adding new functionality or a new command is as simple as
    making a new class and adding that class's command to the map of known commands. Additionally,
    the code is much easier to read without switch statements.

The AbstractCommands class:
    Relating to above, each command was delegated to a different class. However, many of these
    commands shared common functionality such as prompting the user for a stock, a source, or
    certain dates. So the whole action of prompting a common message and needing to scan that input
    was abstracted to AbstractCommands.

The StocksCommands interface:
    This is the interface that unifies all the commands. It just has one method called execute
    as a command just needs to execute its function. It takes in model and view for that method as
    Each command not only takes in an input to send to the model, but also displays custom
    messages to the user based on their action.

All the remaining classes in the Controller package are the commands. They each display a
custom prompt and then call specific methods on the model.

More functionality can be added by simply implementing the StocksCommands interface to a new class
for each new command.
------------------------------------------------

View
-----

The IView interface:
    This interface represents operations for any view classes that should display some sort of
    output to the user. This interface has a barebones set of operations that every view class
    must have, such as the welcome message, goodbye message, and a menu. Then the fourth method
    is taking in a custom message, which is used for the commands which display specific prompts
    and results.

The TextView class:
    This is the main view class for this application as of right now. It is a text based view that
    is interactive with the user's actions.
---------------------------------------------------------------------------------------------------

Features:

-Typos do not cause the program to crash and exit the program, they are handled appropriately
 letting the user know they entered something invalid.

-No long unwieldy inputs needed by user

-No fractional amount of shares allowed to be bought, only whole shares as reinforced by int
 parameter in the method that handles adding stocks.

-Program not dependent on internet connection

-Lowers number of API queries made through local cache

