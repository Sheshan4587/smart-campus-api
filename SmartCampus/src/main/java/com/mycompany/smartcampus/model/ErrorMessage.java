/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.smartcampus.model;

/**
 *
 * @author sheshan
 */
public class ErrorMessage {

    private String errorMessage;
    private int errorCode;
    private String documentation;

    // No args constructor (Required for JSON Serialization)
    public ErrorMessage() {
    }

    // All args constructor (Matches what your Exception Mappers are calling)
    public ErrorMessage(String errorMessage, int errorCode, String documentation) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.documentation = documentation;
    }

    // Getter and setter for errorMessage
    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    // Getter and setter for the errorCode
    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    // Getter and setter for the documentation 
    public String getDocumentation() {
        return this.documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }
}
