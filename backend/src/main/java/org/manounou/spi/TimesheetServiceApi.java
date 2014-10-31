/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.manounou.spi;

import org.manounou.domain.PMF;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.google.appengine.repackaged.org.joda.time.MonthDay;
import java.io.IOException;
import javax.jdo.PersistenceManager;
import org.manounou.Constants;
import org.manounou.domain.Status;
import static org.manounou.domain.Status.*;
import org.manounou.domain.Timesheet;

/**
 *
 * @author sgl
 */
@Api(name = "timesheetApi", version = "v1", description = "An API to manage timesheet",
        scopes = {Constants.EMAIL_SCOPE},
        clientIds = {Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID},
        audiences = {Constants.ANDROID_AUDIENCE})
public class TimesheetServiceApi {

    @ApiMethod(name = "get")
    public Timesheet getTimeSheet(@Named("contract") String contract,
            @Named("monthDay") MonthDay monthDay, User user) throws OAuthRequestException, IOException {
        // @TODO Add user control;
        if (user == null) {
            throw new OAuthRequestException("Accès non autorisé");
        }
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Timesheet result = null;
        try {
            result = pm.getObjectById(Timesheet.class, Timesheet.createKey(contract, monthDay));

            if (result == null) {
                result = new Timesheet(contract, monthDay);
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
            @Named("monthDay") MonthDay monthDay, User user) throws NotFoundException, OAuthRequestException, IOException {
        if (contract == null || monthDay == null) {
            throw new IllegalArgumentException("Contract and MontDay can not be null");
        }
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Timesheet timesheet = pm.getObjectById(Timesheet.class, Timesheet.createKey(contract, monthDay));
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
            @Named("monthDay") MonthDay monthDay,
            User user) throws NotFoundException, OAuthRequestException, IOException {
        if (contract == null || monthDay == null) {
            throw new IllegalArgumentException("Contract and MontDay can not be null");
        }
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            Timesheet timesheet = pm.getObjectById(Timesheet.class, Timesheet.createKey(contract, monthDay));
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
