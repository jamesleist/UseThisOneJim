package com.example.listview;

/**
 * See builderpattern example project for how to do builders
 * they are essential when constructing complicated objects and
 * with many optional fields
 */
public class BikeData {
    private final String Company, Model, Description, Location, Date, Picture, Link;
    private final double Price;
    //TODO make all BikeData fields final

    @Override
    public String toString() {
        return "Company: " + Company + "\nModel: " + Model + "\nPrice: $" + Price +
                "\nLocation: " + Location + "\nDate Listed: " + Date +
                "\nDescription: " + Description + "\nLink: " + Link;
    }

    private BikeData(Builder b) {
        this.Company = b.Company;
        this.Model = b.Model;
        this.Price = b.Price;
        this.Description = b.Description;
        this.Location = b.Location;
        this.Date = b.Date;
        this.Picture = b.Picture;
        this.Link = b.Link;
    }

    public String getCompany() {
        return Company;
    }

    public String getModel() {
        return Model;
    }

    public String getDescription() {
        return Description;
    }

    public String getLocation() {
        return Location;
    }

    public String getDate() {
        return Date;
    }

    public String getPicture() {
        return Picture;
    }

    public String getLink() {
        return Link;
    }

    public double getPrice() {
        return Price;
    }

    /**
     * @author lynn builder pattern, see page 11 Effective Java UserData mydata
     *         = new
     *         UserData.Builder(first,last).addProject(proj1).addProject(proj2
     *         ).build()
     */
    public static class Builder {
        final String Company;
        final String Model;
        final Double Price;
        String Description;
        String Location;
        String Date;
        String Picture;
        String Link;

        // Model and price required
        Builder(String Company, String Model, Double Price) {
            this.Company = Company;
            this.Model = Model;
            this.Price = Price;
        }

        // the following are setters
        // notice it returns this bulder
        // makes it suitable for chaining
        Builder setDescription(String Description) {
            this.Description = Description;
            return this;
        }

        Builder setLocation(String Location) {
            this.Location = Location;
            return this;
        }

        Builder setDate(String Date) {
            this.Date = Date;
            return this;
        }

        Builder setPicture(String Picture) {
            this.Picture = Picture;
            return this;
        }

        Builder setLink(String Link) {
            this.Link = Link;
            return this;
        }

        // use this to actually construct Bikedata
        // without fear of partial construction
        public BikeData build() {
            return new BikeData(this);
        }
    }
}
