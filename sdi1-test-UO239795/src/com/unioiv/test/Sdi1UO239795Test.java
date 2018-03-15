package com.unioiv.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.unioiv.test.util.TestUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Sdi1UO239795Test {

	private static String PathFirefox = "C:\\Users\\igm1990\\Desktop\\SDI\\"
			+ "Firefox46.win\\FirefoxPortable.exe";
	private static String URL = "http://localhost:8090";
	private static WebDriver driver = getDriver(PathFirefox);

	public static WebDriver getDriver(String PathFirefox) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	private TestUtil test = new TestUtil(driver);

	// Antes de cada prueba se navega al URL home de la aplicaciónn
	@Before
	public void setUp() {
		driver.navigate().to(URL);
	}

	// Después de cada prueba se borran las cookies del navegador
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}

	// Antes de la primera prueba
	@BeforeClass
	static public void begin() {
	}

	// Al finalizar la última prueba
	@AfterClass
	static public void end() { // Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}

	/**
	 * Registro de Usuario con datos válidos.
	 */
	@Test
	public void Test01_1_RegVal() {
		driver.get("http://localhost:8090/");
		driver.findElement(By.linkText("Registrarse")).click();
		test.waitSeconds(driver, 2);
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("sdfsds4@fgf.es");
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("Test");
		driver.findElement(By.name("surName")).clear();
		driver.findElement(By.name("surName")).sendKeys("Selenium");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.name("passwordConfirm")).clear();
		driver.findElement(By.name("passwordConfirm")).sendKeys("123456");
		driver.findElement(By.xpath("//input[@value='Registrar']")).click();
	}

	/**
	 * Registro de Usuario con datos inválidos (repetición de contraseña
	 * invalida).
	 */
	@Test
	public void Test01_2_RegInval() {
		driver.get("http://localhost:8090/");
		driver.findElement(By.linkText("Registrarse")).click();
		test.waitSeconds(driver, 2);
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("sdfs8s4@fgf.es");
		driver.findElement(By.name("name")).click();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("Test");
		driver.findElement(By.name("surName")).clear();
		driver.findElement(By.name("surName")).sendKeys("Selenium");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.name("passwordConfirm")).clear();
		driver.findElement(By.name("passwordConfirm")).sendKeys("1234567");
		driver.findElement(By.xpath("//input[@value='Registrar']")).click();
		assertEquals("Error: las contraseñas no coinciden",
				test.closeAlertAndGetItsText());
	}

	/**
	 * Inicio de sesión con datos válidos.
	 */
	@Test
	public void Test02_1_Inval() {
		driver.get("http://localhost:8090/");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Usuario")).click();
		test.waitSeconds(driver, 2);
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username"))
				.sendKeys("ivangonzalezmahagamage@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	/**
	 * Inicio de sesión con datos inválidos (usuario no existente en la
	 * aplicación).
	 */
	@Test
	public void Test02_2_InInVal() {
		driver.get("http://localhost:8090/");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Usuario")).click();
		test.waitSeconds(driver, 2);
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("noExisto@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	@Test
	public void Test03_1_LisUsrVal() {
		driver.get("http://localhost:8090/");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Usuario")).click();
		test.waitSeconds(driver, 2);
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username"))
				.sendKeys("ivangonzalezmahagamage@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitSeconds(driver, 2);
		driver.findElement(By.linkText("Usuarios")).click();
	}

}
