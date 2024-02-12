package movierental;

import java.util.ArrayList;
import java.util.List;

public class Customer {

    private String name;
    private List<Rental> rentals = new ArrayList<>();

    public Customer(String name) {
        this.name = name;
    }

    public void addRental(Rental rental) {
        rentals.add(rental);
    }

    public String getName() {
        return name;
    }

    public String statement() {
        double totalAmount = 0;
        int frequentRenterPoints = 0;
        StringBuilder result = new StringBuilder(addHeaderLines());

        for (Rental rental : rentals) {
            double thisAmount = determineAmountsForEachLine(rental);
            frequentRenterPoints += addFrequentRenterPoints(rental);
            // show figures for this rental
            result.append(showFiguresForThisRental(rental, thisAmount));
            totalAmount += thisAmount;
        }

        // add footer lines
        result.append(addFooterLines(totalAmount, frequentRenterPoints));

        return result.toString();
    }

    private String addHeaderLines() {
        return "Rental Record for " + getName() + "\n";
    }

    private static String addFooterLines(double totalAmount, int frequentRenterPoints) {
        return "Amount owed is " + totalAmount + "\n" +
                "You earned " + frequentRenterPoints + " frequent renter points";
    }

    private static String showFiguresForThisRental(Rental rental, double thisAmount) {
        return "\t" + rental.getMovie().getTitle() + "\t" + thisAmount + "\n";
    }

    private static int addFrequentRenterPoints(Rental rental) {
        return 1 + addBonusForATwoDaysNewReleaseRental(rental);
    }

    private static int addBonusForATwoDaysNewReleaseRental(Rental rental) {
        return (rental.getMovie().getPriceCode() == Movie.NEW_RELEASE && rental.getDaysRented() > 1) ? 1 : 0;
    }

    private static double determineAmountsForEachLine(Rental rental) {
        double thisAmount = 0;

        // determine amounts for each line
        switch (rental.getMovie().getPriceCode()) {
            case Movie.REGULAR:
                thisAmount += 2;
                if (rental.getDaysRented() > 2)
                    thisAmount += (rental.getDaysRented() - 2) * 1.5;
                break;
            case Movie.NEW_RELEASE:
                thisAmount += rental.getDaysRented() * 3;
                break;
            case Movie.CHILDRENS:
                thisAmount += 1.5;
                if (rental.getDaysRented() > 3)
                    thisAmount += (rental.getDaysRented() - 3) * 1.5;
                break;
        }
        return thisAmount;
    }
}
