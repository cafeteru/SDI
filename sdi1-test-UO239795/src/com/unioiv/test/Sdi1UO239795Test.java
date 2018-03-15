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

import com.unioiv.test.util.Random;
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
		String email = Random.email();
		driver.get("http://localhost:8090/");
		driver.findElement(By.linkText("Registrarse")).click();
		test.waitSeconds(driver, 2);
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
		test.textoPresentePagina("Usuario Autenticado como");
		test.textoPresentePagina("ivangonzalezmahagamage@gmail.com");
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
		test.textoPresentePagina("Login");
	}

	/**
	 * Acceso al listado de usuarios desde un usuario en sesión
	 */
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
		test.textoPresentePagina("Usuario Autenticado como");
		driver.findElement(By.linkText("Usuarios")).click();
		test.textoPresentePagina("Lista de usuarios");
	}

	/**
	 * Intento de acceso con URL desde un usuario no identificado al listado de
	 * usuarios desde un usuario en sesión. Debe producirse un acceso no
	 * permitido a vistas privadas.
	 */
	@Test
	public void Test03_2_LisUsrInVal() {

	}

	/**
	 * Realizar una búsqueda valida en el listado de usuarios desde un usuario
	 * en sesión.
	 */
	@Test
	public void Test04_1_BusUsrVal() {

	}

	/**
	 * Intento de acceso con URL a la búsqueda de usuarios desde un usuario no
	 * identificado. Debe producirse un acceso no permitido a vistas privadas.
	 */
	@Test
	public void Test04_2_BusUsrInVal() {

	}

	/**
	 * Enviar una invitación de amistad a un usuario de forma valida.
	 */
	@Test
	public void Test05_1_InvVal() {

	}

	/**
	 * Enviar una invitación de amistad a un usuario al que ya le habíamos
	 * invitado la invitación previamente. No debería dejarnos enviar la
	 * invitación, se podría ocultar el botón de enviar invitación o notificar
	 * que ya había sido enviada previamente.
	 */
	@Test
	public void Test05_2_InvInVal() {

	}

	/**
	 * Listar las invitaciones recibidas por un usuario, realizar la
	 * comprobación con una lista que al menos tenga una invitación recibida.
	 */
	@Test
	public void Test06_1_LisInvVal() {

	}

	/**
	 * Aceptar una invitación recibida.
	 */
	@Test
	public void Test07_1_AcepInvVal() {

	}

	/**
	 * Listar los amigos de un usuario, realizar la comprobación con una lista
	 * que al menos tenga un amigo.
	 */
	@Test
	public void Test08_1_ListAmiVal() {

	}

	/**
	 * Crear una publicación con datos válidos.
	 */
	@Test
	public void Test09_1_PubVal() {

	}

	/**
	 * Acceso al listado de publicaciones desde un usuario en sesión.
	 */
	@Test
	public void Test10_1_LisPubVal() {

	}

	/**
	 * Listar las publicaciones de un usuario amigo
	 */
	@Test
	public void Test11_1_LisPubAmiVal() {

	}

	/**
	 * Utilizando un acceso vía URL tratar de listar las publicaciones de un
	 * usuario que no sea amigo del usuario identificado en sesión.
	 */
	@Test
	public void Test11_2_LisPubAmiVal() {

	}

	/**
	 * Crear una publicación con datos válidos y una foto adjunta.
	 */
	@Test
	public void Test12_1_PubFot1Val() {

	}

	/**
	 * Crear una publicación con datos válidos y sin una foto adjunta.
	 */
	@Test
	public void Test12_2_PubFot2Val() {

	}

	/**
	 * Inicio de sesión como administrador con datos válidos.
	 */
	@Test
	public void Test13_1_AdInVal() {

	}

	/**
	 * Inicio de sesión como administrador con datos inválidos (usar los datos
	 * de un usuario que no tenga perfil administrador).
	 */
	@Test
	public void Test13_2_AdInInVal() {

	}

	/**
	 * Desde un usuario identificado en sesión como administrador listar a todos
	 * los usuarios de la aplicación.
	 */
	@Test
	public void Test14_1_AdLisUsrVal() {

	}

	/**
	 * Desde un usuario identificado en sesión como administrador eliminar un
	 * usuario existente en la aplicación.
	 */
	@Test
	public void Test15_1_AdBorUsrVal() {

	}

	/**
	 * Intento de acceso vía URL al borrado de un usuario existente en la
	 * aplicación. Debe utilizarse un usuario identificado en sesión pero que no
	 * tenga perfil de administrador.
	 */
	@Test
	public void Test15_2_AdBorUsrInVal() {

	}
}
