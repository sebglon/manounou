/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.manounou.domain;

import java.util.Date;
import javax.jdo.annotations.EmbeddedOnly;

/**
 *
 * @author sgl
 */
@EmbeddedOnly
public class StatusHistory {
    private Date date;
    private Status status;
    private String comment;   
}
