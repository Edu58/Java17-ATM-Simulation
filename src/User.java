import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {
    private final String firstName;
    private final String lastName;
    private final String uuid;
    private final ArrayList<Account> accounts;
    private byte[] pinHash;

    /**
     * Create a user/customer
     *
     * @param firstName the user's first name
     * @param lastName  the user's first name
     * @param pin       the user's PIN
     * @param theBank   the Bank object that the user is customer of
     */
    public User(String firstName, String lastName, String pin, Bank theBank) {
        this.firstName = firstName;
        this.lastName = lastName;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        this.uuid = theBank.getNewUserUUID();
        this.accounts = new ArrayList<Account>();

        System.out.printf("New user %s, %s with ID %s created.\n", lastName, firstName, this.uuid);
    }

    /**
     * Return the user's first name
     *
     * @return the user's first name
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Returns the account balance
     *
     * @param fromAcctIdx the account index
     * @return the account balance
     */
    public double getAccountBalance(int fromAcctIdx) {
        return this.accounts.get(fromAcctIdx).getBalance();
    }

    /**
     * Returns the number of accounts teh user has
     *
     * @return number of accounts
     */
    public Integer numAccounts() {
        return this.accounts.size();
    }

    /**
     * Add an account to the User's ArrayList
     *
     * @param account the account Object
     */
    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    /**
     * Returns the accounts uuid
     *
     * @param toAcctIdx the account index
     * @return the account uuid
     */
    public String getAccountUUID(int toAcctIdx) {
        return this.accounts.get(toAcctIdx).getAccountUUID();
    }

    /**
     * Return the user's uuid
     *
     * @return the user's uuid
     */
    public String getUUID() {
        return this.uuid;
    }

    /**
     * Check if the user's PIN is valid
     *
     * @param PIN teh user's PIN
     * @return whether the PIN is valid or not
     */
    public boolean validatePIN(String PIN) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(PIN.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        return false;
    }

    /**
     * Adds a transaction to the
     *
     * @param acctIdx index of account
     * @param amount  amount to transfer
     * @param memo    a transaction memo
     */
    public void addAccountTransaction(int acctIdx, double amount, String memo) {
        this.accounts.get(acctIdx).addTransaction(amount, memo);
    }

    /**
     * Prints the summary of the user's account
     */
    public void printAccountsSummary() {
        System.out.printf("\n\n%s's accounts summary \n", this.firstName);
        for (int a = 0; a < this.accounts.size(); a++) {
            System.out.printf("%d) %s\n", a + 1, this.accounts.get(a).getSummary());
        }
    }

    /**
     * prints the transaction history of the user's account
     *
     * @param theAcctIdx the user account index
     */
    public void printAccountTransactionHistory(int theAcctIdx) {
        this.accounts.get(theAcctIdx).printTransactionHistory();
    }
}