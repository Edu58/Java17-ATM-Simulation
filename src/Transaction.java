import java.util.Date;

public class Transaction {
    private final double amount;
    private final Date timestamp;
    private final Account account;
    private String memo;

    /**
     * Make a transaction
     *
     * @param amount    the amount to transact
     * @param inAccount the account to credit the amount
     */
    public Transaction(double amount, Account inAccount) {
        this.amount = amount;
        this.account = inAccount;
        this.timestamp = new Date();
        this.memo = "";
    }

    /**
     * Make a transaction with a memo
     *
     * @param amount    the amount to transact
     * @param memo      the transaction memo/description
     * @param inAccount the account to credit the amount
     */
    public Transaction(double amount, Account inAccount, String memo) {
        // call the two-arg constructor first
        this(amount, inAccount);
        this.memo = memo;
    }

    /**
     * Returns the transacted amount
     *
     * @return the amount
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * Return a transaction summary
     *
     * @return transaction summary
     */
    public String getSummaryLine() {
        if (this.amount >= 0) {
            return String.format("%s : KSH %.02f : %s \n", this.timestamp.toString(), this.amount, this.memo);
        } else {
            return String.format("%s : KSH (%.02f) : %s \n", this.timestamp.toString(), this.amount, this.memo);
        }
    }
}
