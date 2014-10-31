package org.manounou.domain;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public final class PMF {

    private static final PersistenceManagerFactory pmfInstance
            = JDOHelper.getPersistenceManagerFactory("MaNounouPMF");

    private PMF() {
    }

    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}
