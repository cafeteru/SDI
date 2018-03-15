package com.unioiv.test.util;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestUtil {
	private boolean acceptNextAlert = true;
	private WebDriver driver;

	public TestUtil(WebDriver driver) {
		this.driver = driver;
	}

	public void waitSeconds(WebDriver driver, int segundos) {
		synchronized (driver) {
			try {
				driver.wait(segundos * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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

	public List<WebElement> EsperaCargaPagina(String criterio, String text,
			int timeout) {
		String busqueda;
		if (criterio.equals("id"))
			busqueda = "//*[contains(@id,'" + text + "')]";
		else if (criterio.equals("class"))
			busqueda = "//*[contains(@class,'" + text + "')]";
		else if (criterio.equals("text"))
			busqueda = "//*[contains(text(),'" + text + "')]";
		else if (criterio.equals("free"))
			busqueda = text;
		else
			busqueda = "//*[contains(" + criterio + ",'" + text + "')]";

		return EsperaCargaPaginaxpath(busqueda, timeout);
	}

	public List<WebElement> EsperaCargaPaginaxpath(String xpath, int timeout) {
		WebElement resultado = (new WebDriverWait(driver, timeout)).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		assertTrue(resultado != null);
		List<WebElement> elementos = driver.findElements(By.xpath(xpath));
		return elementos;
	}

	public void textoPresentePagina(String texto) {
		List<WebElement> list = driver.findElements(
				By.xpath("//*[contains(text(),'" + texto + "')]"));
		assertTrue("Texto " + texto + " no localizado!", list.size() > 0);
	}
}
