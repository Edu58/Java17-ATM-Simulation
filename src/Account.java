import java.util.ArrayList;

public class Account {
    private final String name;
    private final String uuid;
    private final User holder;
    private final ArrayList<Transaction> transactions;

    /**
     * Creates an account object
     *
     * @param name    the name of the account
     * @param holder  the holder/user to whom the account belongs to
     * @param thebank the bank to which the account belongs to
     */
    public Account(String name, User holder, Bank thebank) {
        this.name = name;
        this.holder = holder;

        // get new account UUID
        this.uuid = thebank.getNewAccountUUID();

        this.transactions = new ArrayList<Transaction>();
    }

    /**
     * Return the account's balance
     *
     * @return the account balance
     */
    public double getBalance() {
        double balance = 0;

        for (Transaction t : this.transactions) {
            balance += t.getAmount();
        }
        return balance;
    }

    /**
     * return an accounts uuid
     *
     * @return the account uuid
     */
    public String getAccountUUID() {
        return this.uuid;
    }

    /**
     * Returns account summary including the account uuid, balance and name
     *
     * @return account summary
     */

    /**
     * Adds a transaction to the transaction ArrayList
     *
     * @param amount amount transacted
     * @param memo   transaction memo
     */
    public void addTransaction(double amount, String memo) {
        Transaction newTransaction = new Transaction(amount, this, memo);
        this.transactions.add(newTransaction);
    }

    public String getSummary() {
        //get the account balance
        double balance = this.getBalance();

        // format summary line depending on if the account has overdraft
        return String.format("%s : KSH %.02f : %s \n", this.uuid, balance, this.name);
    }

    public void printTransactionHistory() {
        System.out.printf("\nTransaction history for account %s\n", this.uuid);

        for (int t = this.transactions.size() - 1; t >= 0; t--) {
            System.out.printf(this.transactions.get(t).getSummaryLine());
        }
    }
}
