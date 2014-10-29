/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.manounou.domain;

import com.google.appengine.repackaged.org.joda.time.DateTime;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

/**
 *
 * @author sgl
 */
@PersistenceCapable(embeddedOnly = "true")
public class DailyLog {
    

    @Persistent
    private DateTime startTimestamp;
    @Persistent
    private DateTime endTimestamp;
    @Persistent
    private String comment;
    /**
     * Prise d'un repas
     */
    @Persistent
    private Boolean meal;
    /**
     * Prise d'un goûter
     */
    @Persistent
    private Boolean snack;

    protected DailyLog() {
    }

    public DailyLog(DateTime startTimestamp, DateTime endTimestamp) {
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
    }
    
    
}
