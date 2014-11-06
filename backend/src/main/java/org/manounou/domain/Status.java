/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.manounou.domain;

/**
 * @author sgl
 */
public enum Status {

    EMPLOYER_VALIDATION, // Validation par l'employeur
    EMPLOYEE_VALIDATION, // Validation por l'employé
    REJECTED, // rejet
    TO_CHANGE, // A modifier
    ON_EDIT     // Etat initial
}
