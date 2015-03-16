package com.spt.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by : Panupong Chantaklang
 * Created Date : 16/03/2015
 */
public class PhantomWebDriver {
    private static Logger LOGGER = LoggerFactory.getLogger(PhantomWebDriver.class);
    private WebDriver driver;

    public PhantomWebDriver(){

    }

    public PhantomWebDriver(WebDriver driver){
        this.driver = driver;
    }

    public void clickByXPath(String xpath){
        waitForElementPresentByXPath(xpath);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.xpath(xpath)));
    }

    public void clickByCSS(String css){
        waitForElementPresentByCSS(css);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.cssSelector(css)));
    }

    public void clickById(String elementId){
        waitForElementPresentById(elementId);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id(elementId)));

    }

    public void sendKeysById(String elementId,String values){
        waitForElementPresentById(elementId);
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', '" + values + "');", driver.findElement(By.id(elementId)));
    }

    public String getValueById(String elementId){
        waitForElementPresentById(elementId);
        return ((JavascriptExecutor) driver).executeScript("return arguments[0].value;", driver.findElement(By.id(elementId))).toString();
    }

    public String getValueByXPath(String xpath){
        waitForElementPresentByXPath(xpath);
        return ((JavascriptExecutor) driver).executeScript("return arguments[0].value;", driver.findElement(By.xpath(xpath))).toString();
    }


    public Boolean waitForElementPresentById(String elementId){
        Integer counter = 0;
        Boolean isPresent = Boolean.FALSE;

        isPresent = Boolean.FALSE;
        while(!isPresent&&counter<120){
            try {
                if (driver.findElement(By.id(elementId)).isEnabled()) {
                    isPresent = Boolean.TRUE;
                }
            }catch(Exception e){
                counter++;
                LOGGER.info("-= {} not found =-", elementId);
            }
        }

        return isPresent;
    }

    public Boolean waitForElementPresentByCSS(String css){
        Integer counter = 0;
        Boolean isPresent = Boolean.FALSE;

        isPresent = Boolean.FALSE;
        while(!isPresent&&counter<120){
            try {
                if (driver.findElement(By.cssSelector(css)).isEnabled()) {
                    isPresent = Boolean.TRUE;
                }
            }catch(Exception e){
                counter++;
                LOGGER.info("-= {} not found =-", css);
            }
        }
        return isPresent;
    }

    public Boolean waitForElementPresentByXPath(String xpath){
        Integer counter = 0;
        Boolean isPresent = Boolean.FALSE;

        isPresent = Boolean.FALSE;
        while(!isPresent&&counter<120){
            try {
                if (driver.findElement(By.xpath(xpath)).isEnabled()) {
                    isPresent = Boolean.TRUE;
                }
            }catch(Exception e){
                counter++;
                LOGGER.info("-= {} not found =-", xpath);
            }
        }

        return isPresent;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
}
