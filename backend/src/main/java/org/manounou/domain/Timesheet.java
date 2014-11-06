/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.manounou.domain;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.org.joda.time.MonthDay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author sgl
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Timesheet {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String encodedKey;

    @Persistent
    private String contract;

    @Persistent
    private MonthDay monthDay;

    @Persistent(embeddedElement = "true", defaultFetchGroup = "true")
    private List<DailyLog> dailyLogs = new ArrayList<>();

    @Persistent(embeddedElement = "true")
    private List<StatusHistory> statusHistory = new ArrayList<>();

    @Persistent(embedded = "true")
    private Status status;

    protected Timesheet() {
    }

    public Timesheet(String contrat, MonthDay monthDay) {
        this.contract = contrat;
        this.monthDay = monthDay;
        this.encodedKey = createKey(contrat, monthDay);
    }

    public static final String createKey(String contrat, MonthDay monthDay) {
        return KeyFactory.createKeyString(Timesheet.class.getSimpleName(), contrat + monthDay.toString("mm/yyyy"));
    }

    public String getEncodedKey() {
        return encodedKey;
    }

    public MonthDay getMonthDay() {
        return monthDay;
    }

    public String getContract() {
        return contract;
    }

    public List<DailyLog> getDailyLogs() {
        return dailyLogs;
    }

    public void setDailyLogs(List<DailyLog> dailyLogs) {
        this.dailyLogs = dailyLogs;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<StatusHistory> getStatusHistory() {
        return Collections.unmodifiableList(statusHistory);
    }

    protected void setStatusHistory(List<StatusHistory> statusHistory) {
        this.statusHistory = statusHistory;
    }

    public void addStatusHistory(StatusHistory history) {
        statusHistory.add(history);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.encodedKey);
        hash = 29 * hash + Objects.hashCode(this.contract);
        hash = 29 * hash + Objects.hashCode(this.monthDay);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Timesheet other = (Timesheet) obj;
        if (!Objects.equals(this.encodedKey, other.encodedKey)) {
            return false;
        }
        if (!Objects.equals(this.contract, other.contract)) {
            return false;
        }
        if (!Objects.equals(this.monthDay, other.monthDay)) {
            return false;
        }
        return true;
    }

}
