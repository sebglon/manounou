/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.manounou.spi;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.google.appengine.repackaged.org.joda.time.MonthDay;
import com.google.appengine.repackaged.org.joda.time.format.DateTimeFormat;
import com.google.appengine.repackaged.org.joda.time.format.DateTimeFormatter;

import org.manounou.Constants;
import org.manounou.domain.PMF;
import org.manounou.domain.Status;
import org.manounou.domain.Timesheet;

import java.io.IOException;

import javax.jdo.PersistenceManager;

import static org.manounou.domain.Status.EMPLOYEE_VALIDATION;
import static org.manounou.domain.Status.EMPLOYER_VALIDATION;
import static org.manounou.domain.Status.REJECTED;

/**
 * @author sgl
 */
@Api(name = "timesheetApi", version = "v1", description = "An API to manage timesheet",
        namespace = @ApiNamespace(ownerDomain = "manounou.org", ownerName = "Ma nounou"),
        scopes = {Constants.EMAIL_SCOPE},
        clientIds = {Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID, Constants.ANDROID_CLIENT_ID},
        audiences = {Constants.ANDROID_AUDIENCE})
public class TimesheetServiceApi {

    DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/yyyy");

    @ApiMethod(name = "get")
    public Timesheet getTimeSheet(@Named("contract") String contract,
                                  @Named("monthDay") String monthDay, User user) throws OAuthRequestException, IOException {

        MonthDay date = MonthDay.parse(monthDay, fmt);
        // @TODO Add user control;
        if (user == null) {
            throw new OAuthRequestException("Accès non autorisé");
        }
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Timesheet result = null;
        try {
            result = pm.getObjectById(Timesheet.class, Timesheet.createKey(contract, date));

            if (result == null) {
                result = new Timesheet(contract, date);
            } else {
                result = pm.detachCopy(result);
            }
        } finally {
            pm.close();
        }
        return result;
    }

    @ApiMethod(name = "update")
    public Timesheet update(Timesheet timesheet, User user) throws NotFoundException, OAuthRequestException, IOException {
        // @TODO Add user control;
        if (timesheet == null) {
            throw new NotFoundException("Timesheet does not exist.");
        }

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            pm.makePersistent(timesheet);
        } finally {
            pm.close();
        }
        return timesheet;

    }

    @ApiMethod(name = "validate", httpMethod = "PUT")
    public Timesheet validate(@Named("contract") String contract,
                              @Named("monthDay") String monthDay, User user) throws NotFoundException, OAuthRequestException, IOException {
        if (contract == null || monthDay == null) {
            throw new IllegalArgumentException("Contract and MontDay can not be null");
        }
        MonthDay date = MonthDay.parse(monthDay, fmt);
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Timesheet timesheet = pm.getObjectById(Timesheet.class, Timesheet.createKey(contract, date));
            if (timesheet == null) {
                throw new NotFoundException("Timesheet does not exist.");
            } else {
                // @TODO Add user control
                switch (timesheet.getStatus()) {
                    case ON_EDIT:
                        timesheet.setStatus(EMPLOYEE_VALIDATION);
                        break;
                    case EMPLOYEE_VALIDATION:
                        timesheet.setStatus(EMPLOYER_VALIDATION);
                        break;
                    case TO_CHANGE:
                        timesheet.setStatus(EMPLOYEE_VALIDATION);
                    default:
                        timesheet.setStatus(Status.ON_EDIT);
                }
                return timesheet;
            }
        } finally {
            pm.close();
        }
    }

    @ApiMethod(name = "reject", httpMethod = "PUT")
    public Timesheet reject(@Named("contract") String contract,
                            @Named("monthDay") String monthDay,
                            User user) throws NotFoundException, OAuthRequestException, IOException {
        if (contract == null || monthDay == null) {
            throw new IllegalArgumentException("Contract and MontDay can not be null");
        }
        MonthDay date = MonthDay.parse(monthDay, fmt);
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Timesheet timesheet = pm.getObjectById(Timesheet.class, Timesheet.createKey(contract, date));
            if (timesheet == null) {
                throw new NotFoundException("Timesheet does not exist.");
            } else {
                // @TODO add user conntrol and User motification
                timesheet.setStatus(REJECTED);
                return timesheet;
            }
        } finally {
            pm.close();
        }
    }

}
