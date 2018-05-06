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

	public void login(String url, String email, String password) {
		driver.get(url);
		waitChangeWeb();
		driver.findElement(By.id("email")).click();
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("email")).sendKeys(email);
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("boton-login")).click();
		waitChangeWeb();
	}

	public void searchText(String text) {
		driver.findElement(By.id("searchText")).click();
		driver.findElement(By.id("searchText")).clear();
		driver.findElement(By.id("searchText")).sendKeys(text);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		waitChangeWeb();
	}

	public void sendEmail() {
		String message = Random.email();
		driver.findElement(By.id("message")).click();
		driver.findElement(By.id("message")).clear();
		driver.findElement(By.id("message")).sendKeys(message);
		driver.findElement(By.id("send")).click();
		waitChangeWeb();
	}
}
