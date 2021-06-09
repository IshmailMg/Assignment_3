/**
 * RunSolution.java
 * Assignment 3 - Worker Class
 * @author Ishmail T Mgwena - 215088417 
 * Date: 08 June 2021
 */
package za.ac.cput.assignment3;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class RunSolution {

    ObjectInputStream readFile;
    FileWriter writerFile;
    BufferedWriter buffWriter;
    ArrayList<Customer> customerArrayList = new ArrayList<Customer>();
    ArrayList<Supplier> supplierArrayList = new ArrayList<Supplier>();

    public void openFileRead() {
        try {
            readFile = new ObjectInputStream(new FileInputStream("stakeholder.ser"));
            System.out.println("*** ser file created and opened for reading  ***");
        } catch (IOException ioe) {
            System.out.println("error opening ser file: " + ioe.getMessage());
            System.exit(1);
        }
    }

    public void readFile() {
        try {
            while (true) {
                Object line = readFile.readObject();
                String cus = "Customer";
                String sup = "Supplier";
                String name = line.getClass().getSimpleName();
                if (name.equals(cus)) {
                    customerArrayList.add((Customer) line);
                } else if (name.equals(sup)) {
                    supplierArrayList.add((Supplier) line);
                } else {
                    System.out.println("It didn't work");
                }
            }
        } catch (EOFException eofe) {
            System.out.println("End of file reached");
        } catch (ClassNotFoundException ioe) {
            System.out.println("Class error reading ser file: " + ioe);
        } catch (IOException ioe) {
            System.out.println("Error reading ser file: " + ioe);
        }

        sortCustomer();
        sortSuppliers();
    }

    public void readCloseFile() {
        try {
            readFile.close();
        } catch (IOException ioe) {
            System.out.println("error closing ser file: " + ioe.getMessage());
            System.exit(1);
        }
    }

    public void sortCustomer() {
        String[] sortID = new String[customerArrayList.size()];
        ArrayList<Customer> sortA = new ArrayList<Customer>();
        int count = customerArrayList.size();
        for (int i = 0; i < count; i++) {
            sortID[i] = customerArrayList.get(i).getStHolderId();
        }
        Arrays.sort(sortID);

        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                if (sortID[i].equals(customerArrayList.get(j).getStHolderId())) {
                    sortA.add(customerArrayList.get(j));
                }
            }
        }
        customerArrayList.clear();
        customerArrayList = sortA;
    }

    public int getAge(String dob) {
        String[] seperation = dob.split("-");

        LocalDate birth = LocalDate.of(Integer.parseInt(seperation[0]), Integer.parseInt(seperation[1]), Integer.parseInt(seperation[2]));
        LocalDate current = LocalDate.now();
        Period difference = Period.between(birth, current);
        int age = difference.getYears();
        return age;
    }

    public String formatDob(Customer dob) {
        LocalDate dateOfBirthToFormat = LocalDate.parse(dob.getDateOfBirth());
        DateTimeFormatter changeFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return dateOfBirthToFormat.format(changeFormat);
    }

    public String rent() {
        int count = customerArrayList.size();
        int canRent = 0;
        int notRent = 0;
        for (int i = 0; i < count; i++) {
            if (customerArrayList.get(i).getCanRent()) {
                canRent++;
            } else {
                notRent++;
            }
        }
        String line = "Number of customers who can rent : " + '\t' + canRent + '\n' + "Number of customers who cannot rent : " + '\t' + notRent;
        return line;
    }

    public void displayCustomersTextFile() {
        try {
            writerFile = new FileWriter("customerOutFile.txt");
            buffWriter = new BufferedWriter(writerFile);
            buffWriter.write(String.format("%s\n", "===========================Customers================================"));

            buffWriter.write(String.format("%-15s %-15s %-15s %-15s %-15s\n", "ID", "Name", "Surname", "Date of Birth", "Age"));
            buffWriter.write(String.format("%s\n", "===================================================================="));
            for (int i = 0; i < customerArrayList.size(); i++) {
                buffWriter.write(String.format("%-15s %-15s %-15s %-15s %-15s \n", customerArrayList.get(i).getStHolderId(), customerArrayList.get(i).getFirstName(), customerArrayList.get(i).getSurName(), formatDob(customerArrayList.get(i)), getAge(customerArrayList.get(i).getDateOfBirth())));
            }
            buffWriter.write(String.format("%s\n", " "));
            buffWriter.write(String.format("%s\n", " "));
            buffWriter.write(String.format("%s\n", rent()));
        } catch (IOException fnfe) {
            System.out.println(fnfe);
            System.exit(1);
        }
        try {
            buffWriter.close();
        } catch (IOException ioe) {
            System.out.println("error closing text file: " + ioe.getMessage());
            System.exit(1);
        }
    }

    public void sortSuppliers() {
        String[] sortID = new String[supplierArrayList.size()];
        ArrayList<Supplier> sortA = new ArrayList<Supplier>();
        int count = supplierArrayList.size();
        for (int i = 0; i < count; i++) {
            sortID[i] = supplierArrayList.get(i).getName();
        }
        Arrays.sort(sortID);

        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                if (sortID[i].equals(supplierArrayList.get(j).getName())) {
                    sortA.add(supplierArrayList.get(j));
                }
            }
        }
        supplierArrayList.clear();
        supplierArrayList = sortA;
    }

    public void displaySupplierTextFile() {
        try {
            writerFile = new FileWriter("supplierOutFile.txt");
            buffWriter = new BufferedWriter(writerFile);
            buffWriter.write(String.format("%s\n", "============================ SUPPLIERS ===================================="));

            buffWriter.write(String.format("%-15s %-15s \t %-15s %-15s \n", "ID", "Name", "Prod Type", "Description"));
            buffWriter.write("===========================================================================\n");
            for (int i = 0; i < supplierArrayList.size(); i++) {
                buffWriter.write(String.format("%-15s %-15s \t %-15s %-15s \n", supplierArrayList.get(i).getStHolderId(), supplierArrayList.get(i).getName(), supplierArrayList.get(i).getProductType(), supplierArrayList.get(i).getProductDescription()));
            }
            System.out.println("Supplier Text file created and information is displayed.");

        } catch (IOException fnfe) {
            System.out.println(fnfe);
            System.exit(1);
        }
        try {
            buffWriter.close();
        } catch (IOException ioe) {
            System.out.println("error closing text file: " + ioe.getMessage());
            System.exit(1);
        }
    }

    public static void main(String args[]) {
        RunSolution obj = new RunSolution();
        obj.openFileRead();
        obj.readFile();
        obj.readCloseFile();
        obj.displayCustomersTextFile();
        obj.displaySupplierTextFile();

    }

}//End of RunSolution
