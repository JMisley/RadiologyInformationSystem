package com.risjavafx.pages.referrals.billing;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BillingUtils {
    public enum InsuranceCompanies {
        CVS(.25),
        JNJ(.5),
        UNH(.75),
        MCK(.95);

        private final double percent;

        InsuranceCompanies(double percent) {
            this.percent = percent;
        }

        public double getPercent() {
            return percent;
        }
    }


    public static class BillingData {
        SimpleStringProperty modalityData;
        SimpleStringProperty dateTimeData;
        SimpleIntegerProperty priceData;

        public BillingData(String modality, String dateTime, int price) {
            this.modalityData = new SimpleStringProperty(modality);
            this.dateTimeData = new SimpleStringProperty(dateTime);
            this.priceData = new SimpleIntegerProperty(price);
        }

        public String getModalityData() {
            return modalityData.get();
        }

        public void setModalityData(String modality) {
            this.modalityData.set(modality);
        }

        public String getDateTimeData() {
            return dateTimeData.get();
        }

        public void setDateTimeData(String dateTime) {
            this.dateTimeData.set(dateTime);
        }

        public int getPriceData() {
            return priceData.get();
        }

        public void setPriceData(int price) {
            this.priceData.set(price);
        }
    }
}
