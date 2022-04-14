package com.risjavafx.pages.referrals.billing;

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
