package com.uniovi.test.util;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TestUtil {
	private boolean acceptNextAlert = true;
	private WebDriver driver;
	private int time = 4;

	public TestUtil(WebDriver driver) {
		this.driver = driver;
	}

	public void waitSeconds(int segundos) {
		synchronized (driver) {
			try {
				driver.wait(segundos * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void waitChangeWeb() {
		waitSeconds(time);
	}

	public String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}

	public void textoPresentePagina(String texto) {
		List<WebElement> list = driver.findElements(
				By.xpath("//*[contains(text(),'" + texto + "')]"));
		assertTrue("Texto " + texto + " no localizado!", list.size() > 0);
	}

	public void textoNoPresentePagina(String texto) {
		List<WebElement> list = driver.findElements(
				By.xpath("//*[contains(text(),'" + texto + "')]"));
		assertTrue("Texto " + texto + " aun presente !", list.size() == 0);
	}
}
