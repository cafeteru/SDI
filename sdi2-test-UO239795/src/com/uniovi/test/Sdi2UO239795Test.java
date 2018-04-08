package com.uniovi.test;

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

import com.uniovi.test.util.Random;
import com.uniovi.test.util.TestUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Sdi2UO239795Test {

	private static String email;

	private static String PathFirefox = "../Firefox46.win/FirefoxPortable.exe";
	private static String URL = "http://localhost/";
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
	// Cerramos el navegador al finalizar las pruebas
	@AfterClass
	static public void end() { 
		driver.quit();
	}

	/**
	 * Registro de Usuario con datos válidos.
	 */
	@Test
	public void Test01_1_RegVal() {
		email = Random.email();
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Registrarse")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys(email);
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
		test.textoPresentePagina("Usuario Autenticado como");
		test.textoPresentePagina(email);
	}

	/**
	 * Registro de Usuario con datos inválidos 
	 * (repetición de contraseña invalida).
	 */
	@Test
	public void Test01_2_RegInval() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Registrarse")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys(Random.email());
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
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email"))
				.sendKeys("ivangonzalezmahagamage@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.textoPresentePagina("Usuario Autenticado como");
		test.textoPresentePagina("ivangonzalezmahagamage@gmail.com");
		test.textoPresentePagina("Logout");
		test.textoNoPresentePagina("Idéntificate");
	}

	/**
	 * Inicio de sesión con datos inválidos 
	 * (usuario no existente en la aplicación).
	 */
	@Test
	public void Test02_2_InInVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("noExisto@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.textoPresentePagina("Idéntificate");
	}

	/**
	 * Acceso al listado de usuarios desde un usuario en sesión
	 */
	@Test
	public void Test03_1_LisUsrVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		
		test.waitChangeWeb();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email"))
				.sendKeys("ivangonzalezmahagamage@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Usuario Autenticado como");
		driver.findElement(By.linkText("Usuarios")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Buscar usuario");
		test.textoPresentePagina("Lista de usuarios");
	}

	/**
	 * Intento de acceso con URL desde un usuario no identificado al 
	 * listado de usuarios desde un usuario en sesión. 
	 * Debe producirse un acceso no permitido a
	 * vistas privadas.
	 */
	@Test
	public void Test03_2_LisUsrInVal() {
		driver.get(URL + "?lang=es");
		driver.get(URL + "list");
		test.waitChangeWeb();
		test.textoNoPresentePagina("Lista de usuarios");
		test.textoPresentePagina("Idéntificate");
	}

	/**
	 * Realizar una búsqueda valida en el listado de usuarios desde 
	 * un usuario en sesión.
	 */
	@Test
	public void Test04_1_BusUsrVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email"))
				.sendKeys("ivangonzalezmahagamage@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Usuario Autenticado como");
		driver.findElement(By.linkText("Usuarios")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("searchText")).click();
		driver.findElement(By.name("searchText")).clear();
		driver.findElement(By.name("searchText")).sendKeys(email);
	    driver.findElement(By.xpath("//form[@action='/list']")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		test.textoPresentePagina(email);
	}

	/**
	 * Intento de acceso con URL a la búsqueda de usuarios desde un usuario no
	 * identificado. Debe producirse un acceso no permitido a vistas privadas.
	 */
	@Test
	public void Test04_2_BusUsrInVal() {
		driver.get(URL + "?lang=es");
		driver.get(URL + "list");
		test.waitChangeWeb();
		test.textoNoPresentePagina("Lista de usuarios");
		test.textoNoPresentePagina("Buscar usuario");
		test.textoPresentePagina("Idéntificate");
	}

	/**
	 * Enviar una invitación de amistad a un usuario de forma valida.
	 */
	@Test
	public void Test05_1_InvVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email"))
				.sendKeys("ivangonzalezmahagamage@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Usuario Autenticado como");
		driver.findElement(By.linkText("Usuarios")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("searchText")).click();
		driver.findElement(By.name("searchText")).clear();
		driver.findElement(By.name("searchText")).sendKeys(email);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("searchText")).sendKeys(email);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.xpath("//input[@value='Enviar solicitud']"))
				.click();
		test.textoNoPresentePagina(email);
	}

	/**
	 * Enviar una invitación de amistad a un usuario al que ya le habíamos 
	 * invitado la invitación previamente. No debería dejarnos enviar 
	 * la invitación, se podría ocultar el botón de enviar invitación o
	 * notificar que ya había sido enviada previamente.
	 */
	@Test
	public void Test05_2_InvInVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email"))
				.sendKeys("ivangonzalezmahagamage@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Usuario Autenticado como");
		driver.findElement(By.linkText("Usuarios")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("searchText")).click();
		driver.findElement(By.name("searchText")).clear();
		driver.findElement(By.name("searchText")).sendKeys(email);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("searchText")).sendKeys(email);
		test.textoPresentePagina(email);
		test.textoPresentePagina("Solicitud enviada");
	}

	/**
	 * Listar las invitaciones recibidas por un usuario, realizar 
	 * la comprobación con una lista que al menos tenga una 
	 * invitación recibida.
	 */
	@Test
	public void Test06_1_LisInvVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Peticiones")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Iván");
		test.textoPresentePagina("González Mahagamage");
	}

	/**
	 * Aceptar una invitación recibida.
	 */
	@Test
	public void Test07_1_AcepInvVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Peticiones")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Iván");
		test.textoPresentePagina("González Mahagamage");
		driver.findElement(By.xpath("//input[@value='Aceptar invitación']"))
				.click();
		test.waitChangeWeb();
		test.textoNoPresentePagina("Aceptar Petición");
		test.textoNoPresentePagina("Bloquear");
	}

	/**
	 * Listar los amigos de un usuario, realizar la comprobación con una 
	 * lista que al menos tenga un amigo.
	 */
	@Test
	public void Test08_1_ListAmiVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Amigos")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Iván");
		test.textoPresentePagina("González Mahagamage");
	}
}
