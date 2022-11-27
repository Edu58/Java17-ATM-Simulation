import java.util.Scanner;

public class ATM {
    public static void main(String[] Args) {
        Scanner sc = new Scanner(System.in);

        // create a bank
        Bank bank = new Bank("Bank of EDD");

        // add a user
        User newUser = bank.addUser("Edwin", "KK", "4786");

        // Add checking account to bank
        Account checkingAccount = new Account("Checking", newUser, bank);
        newUser.addAccount(checkingAccount);
        bank.addAccount(checkingAccount);

        // user login
        User currUser;
        while (true) {
            // stay in login until successful login
            currUser = ATM.mainMenuPrompt(bank, sc);

            // stay in main menu until stopped
            ATM.printUserMenu(currUser, sc);
        }
    }

    /**
     * Loads the banks main menu
     *
     * @param bank the Bank object
     * @param sc   the Scanner
     * @return User Object
     */
    public static User mainMenuPrompt(Bank bank, Scanner sc) {
        String userID;
        String PIN;
        User authUser;

        //prompt for userID and PIN
        do {
            System.out.printf("\n\nWelcome to %s \n\n", bank.getName());

            System.out.print("Enter user ID: ");
            userID = sc.nextLine();

            System.out.print("Enter PIN: ");
            PIN = sc.nextLine();

            // Get user with the userID and PIN
            authUser = bank.userLogin(userID, PIN);

            if (authUser == null) {
                System.out.println("Incorrect UserID or PIN. Please try again!");
            }
        } while (authUser == null);

        return authUser;
    }

    /**
     * Displays the ATM main menu with choices from which teh user can choose from and get diffrent output for each
     *
     * @param theUser the user object
     * @param sc      a scanner instance
     */
    public static void printUserMenu(User theUser, Scanner sc) {
        // print a user's account summary
        theUser.printAccountsSummary();

        int choice;

        do {
            System.out.printf("Welcome %s, what would you like to do? \n", theUser.getFirstName());
            System.out.println("1) Show transaction history");
            System.out.println("2) Withdraw");
            System.out.println("3) Deposit");
            System.out.println("4) Transact");
            System.out.println("5) Quit");

            choice = sc.nextInt();

            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice!!!");
            }
        } while (choice < 1 || choice > 5);

        //process choice
        switch (choice) {
            case 1 -> ATM.showTransactionHistory(theUser, sc);
            case 2 -> ATM.withdraw(theUser, sc);
            case 3 -> ATM.deposit(theUser, sc);
            case 4 -> ATM.transact(theUser, sc);
        }

        //redisplay the menu unless the user quits
        if (choice != 5) {
            ATM.printUserMenu(theUser, sc);
        }
    }

    /**
     * Returns the transaction history of a users account
     *
     * @param theUser a user object
     * @param sc      a scanner instance
     */
    public static void showTransactionHistory(User theUser, Scanner sc) {
        int theAcct;

        do {
            System.out.printf("Enter the number of the account (1 - %d) you want to view the transaction history: ", theUser.numAccounts());

            theAcct = sc.nextInt() - 1;

            if (theAcct < 0 || theAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Try again.");
            }
        } while (theAcct < 0 || theAcct >= theUser.numAccounts());

        //print the transaction history
        theUser.printAccountTransactionHistory(theAcct);
    }

    /**
     * Lets a user move money from one account to another, deposit into their account or withdraw from their account
     *
     * @param theUser user object
     * @param sc      Scanner instance
     */
    private static void transact(User theUser, Scanner sc) {
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        // get account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account to transfer from: ", theUser.numAccounts());
            fromAcct = sc.nextInt() - 1;

            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Try again");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());

        acctBal = theUser.getAccountBalance(fromAcct);

        // get account to transfer to
        do {
            System.out.printf("Enter the number (1-%d) of the account to transfer to: ", theUser.numAccounts());
            toAcct = sc.nextInt() - 1;

            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Try again");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());

        //amount to transfer
        do {
            System.out.printf("Enter the amount to transfer (max KSH %.02f): ", acctBal);
            amount = sc.nextDouble();

            if (amount < 0) {
                System.out.println("Amount must be greater than zero");
            } else if (amount > acctBal) {
                System.out.printf("Amount must be less than KSH %.02f ", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        // do the transfer
        theUser.addAccountTransaction(fromAcct, -1 * amount, String.format("Transaction to %s ", theUser.getAccountUUID(toAcct)));
        theUser.addAccountTransaction(toAcct, amount, String.format("Transaction from %s ", theUser.getAccountUUID(fromAcct)));
    }

    /**
     * Withdraw money from account
     *
     * @param theUser a user Object
     * @param sc      a scanner instance
     */
    private static void withdraw(User theUser, Scanner sc) {
        int fromAcct;
        double amount;
        double acctBal;
        String memo;

        // get account to withdraw from
        do {
            System.out.printf("Enter the number (1-%d) of the account to withdraw from: ", theUser.numAccounts());
            fromAcct = sc.nextInt() - 1;

            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Try again");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());

        acctBal = theUser.getAccountBalance(fromAcct);

        //amount to withdraw
        do {
            System.out.printf("Enter the amount to withdraw (max KSH %.02f): ", acctBal);
            amount = sc.nextDouble();

            if (amount < 0) {
                System.out.println("Amount must be greater than zero");
            } else if (amount > acctBal) {
                System.out.printf("Amount must be less than KSH %.02f ", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        // gobble previous input
        sc.nextLine();

        //get a memo
        System.out.println("Enter a memo: ");
        memo = sc.nextLine();

        // do the withdrawal
        theUser.addAccountTransaction(fromAcct, -1 * amount, memo);
    }

    /**
     * Deposit money into account
     *
     * @param theUser a user object
     * @param sc      a scanner instance
     */
    private static void deposit(User theUser, Scanner sc) {
        int toAcct;
        double amount;
        double acctBal;
        String memo;

        // get account to deposit to
        do {
            System.out.printf("Enter the number (1-%d) of the account to deposit from: ", theUser.numAccounts());
            toAcct = sc.nextInt() - 1;

            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Invalid account. Try again");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());

        acctBal = theUser.getAccountBalance(toAcct);

        //amount to deposit
        do {
            System.out.printf("Enter the amount to deposit (max KSH %.02f): ", acctBal);
            amount = sc.nextDouble();

            if (amount < 0) {
                System.out.println("Amount must be greater than zero");
            }
        } while (amount < 0);

        // gobble previous input
        sc.nextLine();

        //get a memo
        System.out.println("Enter a memo: ");
        memo = sc.nextLine();

        // do the withdrawal
        theUser.addAccountTransaction(toAcct, amount, memo);
    }
}
