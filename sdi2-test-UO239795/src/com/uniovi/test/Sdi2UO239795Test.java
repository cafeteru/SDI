package com.uniovi.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.uniovi.test.util.Random;
import com.uniovi.test.util.TestUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Sdi2UO239795Test {

	private static String email;

	private static String PathFirefox = "../Firefox46.win/FirefoxPortable.exe";
	private static String URL = "http://localhost:8081/";
	private static String URLLogin = "http://localhost:8081/login";
	private static String URLjQuery = "http://localhost:8081/main.html?w=login";
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
		driver.get(URL);
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
	 * Registro de Usuario con datos inválidos (repetición de contraseña
	 * invalida).
	 */
	@Test
	public void Test01_2_RegInval() {
		driver.get(URL);
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
		test.login(URLLogin, "ivangonzalezmahagamage@gmail.com", "123456");
		test.textoPresentePagina("Usuario Autenticado como");
		test.textoPresentePagina("ivangonzalezmahagamage@gmail.com");
		test.textoPresentePagina("Logout");
		test.textoNoPresentePagina("Idéntificate");
	}

	/**
	 * Inicio de sesión con datos inválidos (usuario no existente en la
	 * aplicación).
	 */
	@Test
	public void Test02_2_InInVal() {
		test.login(URLLogin, "noExisto@gmail.com", "123456");
		test.textoPresentePagina("Idéntificate");
		test.textoNoPresentePagina("Usuario Autenticado como");
		test.textoNoPresentePagina("noExisto@gmail.com");
		test.textoNoPresentePagina("Logout");
	}

	/**
	 * Acceso al listado de usuarios desde un usuario en sesión
	 */
	@Test
	public void Test03_1_LisUsrVal() {
		test.login(URLLogin, "ivangonzalezmahagamage@gmail.com", "123456");
		test.textoPresentePagina("Usuario Autenticado como");
		driver.findElement(By.linkText("Usuarios")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Buscar usuario");
		test.textoPresentePagina("Lista de usuarios");
	}

	/**
	 * Intento de acceso con URL desde un usuario no identificado al listado de
	 * usuarios desde un usuario en sesión. Debe producirse un acceso no
	 * permitido a vistas privadas.
	 */
	@Test
	public void Test03_2_LisUsrInVal() {
		driver.get(URL + "list");
		test.waitChangeWeb();
		test.textoNoPresentePagina("Lista de usuarios");
		test.textoPresentePagina("Idéntificate");
	}

	/**
	 * Realizar una búsqueda valida en el listado de usuarios desde un usuario
	 * en sesión.
	 */
	@Test
	public void Test04_1_BusUsrVal() {
		test.login(URLLogin, "ivangonzalezmahagamage@gmail.com", "123456");
		test.textoPresentePagina("Usuario Autenticado como");
		driver.findElement(By.linkText("Usuarios")).click();
		test.waitChangeWeb();
		test.searchText(email);
		test.textoPresentePagina(email);
	}

	/**
	 * Intento de acceso con URL a la búsqueda de usuarios desde un usuario no
	 * identificado. Debe producirse un acceso no permitido a vistas privadas.
	 */
	@Test
	public void Test04_2_BusUsrInVal() {
		driver.get(URL);
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
		test.login(URLLogin, "ivangonzalezmahagamage@gmail.com", "123456");
		test.textoPresentePagina("Usuario Autenticado como");
		driver.findElement(By.linkText("Usuarios")).click();
		test.waitChangeWeb();
		test.searchText(email);
		driver.findElement(By.xpath("//input[@value='Enviar solicitud']"))
				.click();
		test.waitChangeWeb();
		test.textoPresentePagina("Petición enviada correctamente");
	}

	/**
	 * Enviar una invitación de amistad a un usuario al que ya le habíamos
	 * invitado la invitación previamente. No debería dejarnos enviar la
	 * invitación, se podría ocultar el botón de enviar invitación o notificar
	 * que ya había sido enviada previamente.
	 */
	@Test
	public void Test05_2_InvInVal() {
		test.login(URLLogin, "ivangonzalezmahagamage@gmail.com", "123456");
		test.textoPresentePagina("Usuario Autenticado como");
		driver.findElement(By.linkText("Usuarios")).click();
		test.waitChangeWeb();
		test.searchText(email);
		test.textoPresentePagina(email);
		test.textoPresentePagina("Solicitud enviada");
	}

	/**
	 * Listar las invitaciones recibidas por un usuario, realizar la
	 * comprobación con una lista que al menos tenga una invitación recibida.
	 */
	@Test
	public void Test06_1_LisInvVal() {
		test.login(URLLogin, email, "123456");
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
		test.login(URLLogin, email, "123456");
		test.textoPresentePagina("Iván");
		test.textoPresentePagina("González Mahagamage");
		driver.findElement(By.linkText("Peticiones")).click();
		test.waitChangeWeb();
		driver.findElement(By.xpath("//input[@value='Aceptar invitación']"))
				.click();
		test.waitChangeWeb();
		test.textoNoPresentePagina("Aceptar Petición");
		test.textoNoPresentePagina("Bloquear");
	}

	/**
	 * Listar los amigos de un usuario, realizar la comprobación con una lista
	 * que al menos tenga un amigo.
	 */
	@Test
	public void Test08_1_ListAmiVal() {
		test.login(URLLogin, email, "123456");
		driver.findElement(By.linkText("Amigos")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Iván");
		test.textoPresentePagina("González Mahagamage");
	}

	/**
	 * Inicio de sesión con datos válidos.
	 */
	@Test
	public void TestC1_1_CInVal() {
		test.login(URLjQuery, "ivangonzalezmahagamage@gmail.com", "123456");
		test.textoPresentePagina("ivangonzalezmahagamage@gmail.com");
		test.textoPresentePagina("Usuario Autenticado como");
		test.textoPresentePagina("Amistades");
		test.textoNoPresentePagina("Idéntificate");
		test.textoNoPresentePagina("Login");
	}

	/**
	 * Inicio de sesión con datos inválidos (usuario no existente en la
	 * aplicación).
	 */
	@Test
	public void TestC1_2_CInInVal() {
		test.login(URLjQuery, "noExisto@gmail.com", "123456");
		test.textoNoPresentePagina("noExisto@gmail.com");
		test.textoNoPresentePagina("Usuario Autenticado como");
		test.textoNoPresentePagina("Amistades");
		test.textoPresentePagina("Idéntificate");
		test.textoPresentePagina("Login");
	}

	/**
	 * Acceder a la lista de amigos de un usuario, que al menos tenga tres
	 * amigos.
	 */
	@Test
	public void TestC2_1_CListAmiVal() {
		test.login(URLjQuery, "ivangonzalezmahagamage@gmail.com", "123456");
		List<WebElement> filas = driver.findElements(By.xpath("//tr"));
		assertTrue(filas.size() > 3);
		test.textoPresentePagina("ivangonzalezmahagamage@gmail.com");
		test.textoPresentePagina("Usuario Autenticado como");
		test.textoPresentePagina("Amistades");
	}

	/**
	 * Acceder a la lista de amigos de un usuario, y realizar un filtrado para
	 * encontrar a un amigo concreto, el nombre a buscar debe coincidir con el
	 * de un amigo.
	 */
	@Test
	public void TestC2_2_CListAmiFil() {
		test.login(URLjQuery, "ivangonzalezmahagamage@gmail.com", "123456");
		test.textoPresentePagina("ivangonzalezmahagamage@gmail.com");
		test.textoPresentePagina("Usuario Autenticado como");
		test.textoPresentePagina("Amistades");
		test.searchText("igm1990");
		List<WebElement> filas = driver.findElements(By.xpath("//tr"));
		assertTrue(filas.size() > 1);
		test.textoPresentePagina("Iván");
		test.textoPresentePagina("González Mahagamage");
		test.textoPresentePagina("igm1990@hotmail.com");
	}

	/**
	 * Acceder a la lista de mensajes de un amigo “chat”, la lista debe contener
	 * al menos tres mensajes.
	 */
	@Test
	public void TestC3_1_CListMenVal() {
		test.login(URLjQuery, "ivangonzalezmahagamage@gmail.com", "123456");
		test.textoPresentePagina("ivangonzalezmahagamage@gmail.com");
		test.textoPresentePagina("Usuario Autenticado como");
		test.textoPresentePagina("Amistades");
		test.searchText("igm1990");
		test.waitChangeWeb();
		driver.findElement(By.xpath("//tbody[@id='bodyTable']/tr/td")).click();
		test.waitChangeWeb();
		List<WebElement> numMensages = driver
				.findElements(By.className("containerChat"));
		assertTrue(numMensages.size() > 3);
	}

	/**
	 * Acceder a la lista de mensajes de un amigo “chat” y crear un nuevo
	 * mensaje, validar que el mensaje aparece en la lista de mensajes.
	 */
	@Test
	public void TestC4_1_CCrearMenVal() {
		test.login(URLjQuery, "ivangonzalezmahagamage@gmail.com", "123456");
		test.searchText(email);
		driver.findElement(By.xpath("//tbody[@id='bodyTable']/tr/td")).click();
		test.waitChangeWeb();
		int numMessagesBefore = driver
				.findElements(By.className("containerChat")).size();
		test.sendEmail();
		int numMessagesAfter = driver
				.findElements(By.className("containerChat")).size();
		assertTrue(numMessagesBefore < numMessagesAfter);
		test.textoPresentePagina(email);
	}

	/**
	 * Identificarse en la aplicación y enviar un mensaje a un amigo, validar
	 * que el mensaje enviado aparece en el chat. Identificarse después con el
	 * usuario que recibido el mensaje y validar que tiene un mensaje sin leer,
	 * entrar en el chat y comprobar que el mensaje pasa a tener el estado
	 * leído.
	 */
	@Test
	public void TestC5_1_CMenLeidoVal() {
		test.login(URLjQuery, "ivangonzalezmahagamage@gmail.com", "123456");
		test.searchText(email);
		test.waitChangeWeb();
		driver.findElement(By.xpath("//tbody[@id='bodyTable']/tr/td")).click();
		test.waitChangeWeb();
		int numMessagesBefore = driver
				.findElements(By.className("containerChat")).size();
		test.sendEmail();
		int numMessagesAfter = driver
				.findElements(By.className("containerChat")).size();
		assertTrue(numMessagesBefore < numMessagesAfter);
		int numImgs = driver.findElements(By.tagName("img")).size();
		assertFalse(numMessagesAfter == numImgs);
		test.textoPresentePagina(email);
		test.login(URLjQuery, email, "123456");
		test.textoPresentePagina(email);
		test.textoPresentePagina("Usuario Autenticado como");
		test.textoPresentePagina("Amistades");
		test.searchText("ivangonzalezmahagamage");
		test.waitChangeWeb();
		List<WebElement> list = driver.findElements(By.xpath(
				"//tbody[@id='bodyTable']/tr/td[4][contains(text(),'2')]"));
		assertTrue(list.size() == 1);
		driver.findElement(By.linkText("Iván")).click();
		test.waitChangeWeb();
		test.waitChangeWeb();
		numMessagesAfter = driver.findElements(By.className("containerChat"))
				.size();
		numImgs = driver.findElements(By.tagName("img")).size();
		assertTrue(numMessagesAfter == numImgs);
	}

	/**
	 * Identificarse en la aplicación y enviar tres mensajes a un amigo, validar
	 * que los mensajes enviados aparecen en el chat. Identificarse después con
	 * el usuario que recibido el mensaje y validar que el número de mensajes
	 * sin leer aparece en la propia lista de amigos.
	 */
	@Test
	public void TestC6_1_CListaMenNoLeidoVal() {
		test.login(URLjQuery, "ivangonzalezmahagamage@gmail.com", "123456");
		test.searchText(email);
		driver.findElement(By.xpath("//tbody[@id='bodyTable']/tr/td")).click();
		test.waitChangeWeb();
		int numMessagesBefore = driver
				.findElements(By.className("containerChat")).size();
		for (int i = 0; i < 3; i++) {
			test.sendEmail();
		}
		int numMessagesAfter = driver
				.findElements(By.className("containerChat")).size();
		assertEquals(3, numMessagesAfter - numMessagesBefore);
		test.login(URLjQuery, email, "123456");
		test.textoPresentePagina(email);
		test.textoPresentePagina("Usuario Autenticado como");
		test.textoPresentePagina("Amistades");
		test.searchText("ivangonzalezmahagamage");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		List<WebElement> list = driver.findElements(By.xpath(
				"//tbody[@id='bodyTable']/tr/td[4][contains(text(),'3')]"));
		assertTrue(list.size() == 1);
	}

	/**
	 * Identificarse con un usuario A que al menos tenga 3 amigos, ir al chat
	 * del ultimo amigo de la lista y enviarle un mensaje, volver a la lista de
	 * amigos y comprobar que el usuario al que se le ha enviado el mensaje esta
	 * en primera posición. Identificarse con el usuario B y enviarle un mensaje
	 * al usuario A. Volver a identificarse con el usuario A y ver que el
	 * usuario que acaba de mandarle el mensaje es el primero en su lista de
	 * amigos.
	 */
	@Test
	public void TestC7_1_COrdenMenVall() {
		test.login(URLjQuery, "ivangonzalezmahagamage@gmail.com", "123456");
		test.searchText(email);
		driver.findElement(By.xpath("//tbody[@id='bodyTable']/tr/td")).click();
		test.waitChangeWeb();
		int numMessagesBefore = driver
				.findElements(By.className("containerChat")).size();
		test.sendEmail();
		int numMessagesAfter = driver
				.findElements(By.className("containerChat")).size();
		assertEquals(1, numMessagesAfter - numMessagesBefore);
		driver.findElement(By.linkText("Amigos")).click();
		test.waitChangeWeb();
		List<WebElement> list = driver.findElements(
				By.xpath("//tbody[@id='bodyTable']/tr/td[3][contains(text(),'"
						+ email + "')]"));
		assertTrue(list.size() == 1);
		test.login(URLjQuery, email, "123456");
		test.textoPresentePagina(email);
		test.textoPresentePagina("Usuario Autenticado como");
		test.textoPresentePagina("Amistades");
		list = driver.findElements(
				By.xpath("//tbody[@id='bodyTable']/tr/td[3][contains(text(),"
						+ "'ivangonzalezmahagamage@gmail.com')]"));
		assertTrue(list.size() == 1);
		test.searchText("ivangonzalezmahagamage");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.xpath("//tbody[@id='bodyTable']/tr/td")).click();
		test.waitChangeWeb();
	}

}
