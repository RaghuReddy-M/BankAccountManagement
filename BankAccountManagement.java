package bankAccountManagement.com;

import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.io.Serializable;


@SuppressWarnings("serial")
class BankAccount implements Serializable {
    
	private String accountNumber;
    private String accountHolderName;
    private double balance;

    public BankAccount(String accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposit successful. New Balance: " + balance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawal successful. New Balance: " + balance);
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
    }

    @Override
    public String toString() {
        return "Account Number: " + accountNumber +
               ", Name: " + accountHolderName +
               ", Balance: " + balance;
    }
}

public class BankAccountManagement {
    private static final String FILE_NAME = "data/accounts.dat";
    private static HashMap<String, BankAccount> accounts = new HashMap<>();

    public static void main(String[] args) {
        loadAccounts();

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== BANK ACCOUNT MANAGEMENT =====");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. View All Accounts");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> createAccount(sc);
                case 2 -> depositMoney(sc);
                case 3 -> withdrawMoney(sc);
                case 4 -> checkBalance(sc);
                case 5 -> viewAllAccounts();
                case 6 -> {
                    saveAccounts();
                    System.out.println("Accounts saved. Exiting...");
                }
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 6);

        sc.close();
    }

    private static void createAccount(Scanner sc) {
        System.out.print("Enter Account Number: ");
        String accNum = sc.nextLine();
        if (accounts.containsKey(accNum)) {
            System.out.println("Account number already exists!");
            return;
        }
        System.out.print("Enter Account Holder Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Initial Balance: ");
        double balance = sc.nextDouble();

        BankAccount acc = new BankAccount(accNum, name, balance);
        accounts.put(accNum, acc);
        saveAccounts();
        System.out.println("Account created successfully.");
    }

    private static void depositMoney(Scanner sc) {
        System.out.print("Enter Account Number: ");
        String accNum = sc.nextLine();
        BankAccount acc = accounts.get(accNum);
        if (acc != null) {
            System.out.print("Enter Amount to Deposit: ");
            double amt = sc.nextDouble();
            acc.deposit(amt);
            saveAccounts();
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void withdrawMoney(Scanner sc) {
        System.out.print("Enter Account Number: ");
        String accNum = sc.nextLine();
        BankAccount acc = accounts.get(accNum);
        if (acc != null) {
            System.out.print("Enter Amount to Withdraw: ");
            double amt = sc.nextDouble();
            acc.withdraw(amt);
            saveAccounts();
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void checkBalance(Scanner sc) {
        System.out.print("Enter Account Number: ");
        String accNum = sc.nextLine();
        BankAccount acc = accounts.get(accNum);
        if (acc != null) {
            System.out.println("Balance: " + acc.getBalance());
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void viewAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("📂 No accounts found.");
        } else {
            accounts.values().forEach(System.out::println);
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadAccounts() {
        try {
            File dataFile = new File(FILE_NAME);
            if (!dataFile.exists()) {
                System.out.println("No existing account file found. Starting fresh.");
                return;
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                accounts = (HashMap<String, BankAccount>) ois.readObject();
                System.out.println("Accounts loaded from file.");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
        }
    }

    private static void saveAccounts() {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdir(); // Create 'data' folder if not present
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                oos.writeObject(accounts);
            }
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }
}

