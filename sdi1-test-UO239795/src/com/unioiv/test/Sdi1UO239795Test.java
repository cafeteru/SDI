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

	private static String email;

	private static String PathFirefox = "../Firefox46.win/FirefoxPortable.exe";
	private static String URL = "http://localhost:8090/";
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
	 * Registro de Usuario con datos inválidos (repetición de contraseña
	 * invalida).
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
		driver.findElement(By.linkText("Usuario")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username"))
				.sendKeys("ivangonzalezmahagamage@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.textoPresentePagina("Usuario Autenticado como");
		test.textoPresentePagina("ivangonzalezmahagamage@gmail.com");
		test.textoPresentePagina("Logout");
		test.textoNoPresentePagina("Login");
	}

	/**
	 * Inicio de sesión con datos inválidos (usuario no existente en la
	 * aplicación).
	 */
	@Test
	public void Test02_2_InInVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Usuario")).click();
		test.waitChangeWeb();
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
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Usuario")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username"))
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
	 * Intento de acceso con URL desde un usuario no identificado al listado de
	 * usuarios desde un usuario en sesión. Debe producirse un acceso no
	 * permitido a vistas privadas.
	 */
	@Test
	public void Test03_2_LisUsrInVal() {
		driver.get(URL + "?lang=es");
		driver.get(URL + "user/list");
		test.waitChangeWeb();
		test.textoNoPresentePagina("Lista de usuarios");
		test.textoPresentePagina("Login");
	}

	/**
	 * Realizar una búsqueda valida en el listado de usuarios desde un usuario
	 * en sesión.
	 */
	@Test
	public void Test04_1_BusUsrVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Usuario")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username"))
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
		test.textoPresentePagina(email);
	}

	/**
	 * Intento de acceso con URL a la búsqueda de usuarios desde un usuario no
	 * identificado. Debe producirse un acceso no permitido a vistas privadas.
	 */
	@Test
	public void Test04_2_BusUsrInVal() {
		driver.get(URL + "?lang=es");
		driver.get(URL + "user/list");
		test.waitChangeWeb();
		test.textoNoPresentePagina("Lista de usuarios");
		test.textoNoPresentePagina("Buscar usuario");
		test.textoPresentePagina("Login");
	}

	/**
	 * Enviar una invitación de amistad a un usuario de forma valida.
	 */
	@Test
	public void Test05_1_InvVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Usuario")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username"))
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
	 * invitado la invitación previamente. No debería dejarnos enviar la
	 * invitación, se podría ocultar el botón de enviar invitación o notificar
	 * que ya había sido enviada previamente.
	 */
	@Test
	public void Test05_2_InvInVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Usuario")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username"))
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
	 * Listar las invitaciones recibidas por un usuario, realizar la
	 * comprobación con una lista que al menos tenga una invitación recibida.
	 */
	@Test
	public void Test06_1_LisInvVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Usuario")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(email);
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
		driver.findElement(By.linkText("Usuario")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(email);
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
	 * Listar los amigos de un usuario, realizar la comprobación con una lista
	 * que al menos tenga un amigo.
	 */
	@Test
	public void Test08_1_ListAmiVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Usuario")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Amigos")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Iván");
		test.textoPresentePagina("González Mahagamage");
	}

	/**
	 * Crear una publicación con datos válidos.
	 */
	@Test
	public void Test09_1_PubVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Usuario")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Publicaciones")).click();
		driver.findElement(By.linkText("Añadir publicación")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("title")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("Prueba Post");
		driver.findElement(By.name("text")).click();
		driver.findElement(By.name("text")).clear();
		driver.findElement(By.name("text"))
				.sendKeys("Prueba de contenido de post");
		driver.findElement(By.xpath("//input[@value='Enviar']")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Lista de publicaciones");
		test.textoPresentePagina("Prueba Post");
		test.textoPresentePagina("Prueba de contenido de post");
	}

	/**
	 * Acceso al listado de publicaciones desde un usuario en sesión.
	 */
	@Test
	public void Test10_1_LisPubVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Usuario")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Publicaciones")).click();
		driver.findElement(By.linkText("Listar mis publicaciones")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Lista de publicaciones de");
		test.textoPresentePagina("Prueba Post");
		test.textoPresentePagina("Prueba de contenido de post");
	}

	/**
	 * Listar las publicaciones de un usuario amigo
	 */
	@Test
	public void Test11_1_LisPubAmiVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Usuario")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Amigos")).click();
		test.waitChangeWeb();
		driver.findElement(By.xpath("//input[@value='Ver publilcaciones']"))
				.click();
		test.waitChangeWeb();
		test.textoPresentePagina("Lista de publicaciones de");
		test.textoPresentePagina("Iván González Mahagamage");
	}

	/**
	 * Utilizando un acceso vía URL tratar de listar las publicaciones de un
	 * usuario que no sea amigo del usuario identificado en sesión.
	 */
	@Test
	public void Test11_2_LisPubAmiVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Usuario")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.get(URL + "post/friends/3");
		test.textoPresentePagina("Esta es una zona privada la web");
		test.textoPresentePagina("Usuario Autenticado como");
		test.textoPresentePagina(email);

	}

	/**
	 * Crear una publicación con datos válidos y una foto adjunta.
	 */
	@Test
	public void Test12_1_PubFot1Val() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Usuario")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Publicaciones")).click();
		driver.findElement(By.linkText("Añadir publicación")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("title")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("Prueba Post con foto");
		driver.findElement(By.name("text")).click();
		driver.findElement(By.name("text")).clear();
		String aux = System.getProperty("user.dir") + "\\imagenDePrueba.jpg";
		aux = aux.replace("\\", "/");
		aux = "file:///" + aux;
		driver.findElement(By.name("text")).sendKeys(aux);
		driver.findElement(By.name("imgn")).sendKeys(aux);
		driver.findElement(By.xpath("//input[@value='Enviar']")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Lista de publicaciones");
		test.textoPresentePagina("Prueba Post con foto");
		test.textoPresentePagina(aux);
	}

	/**
	 * Crear una publicación con datos válidos y sin una foto adjunta.
	 */
	@Test
	public void Test12_2_PubFot2Val() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Usuario")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Publicaciones")).click();
		driver.findElement(By.linkText("Añadir publicación")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("title")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("Prueba Post sin foto");
		driver.findElement(By.name("text")).click();
		driver.findElement(By.name("text")).clear();
		driver.findElement(By.name("text"))
				.sendKeys("Prueba de contenido de post sin foto");
		driver.findElement(By.xpath("//input[@value='Enviar']")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Lista de publicaciones");
		test.textoPresentePagina("Prueba Post sin foto");
		test.textoPresentePagina("Prueba de contenido de post sin foto");
	}

	/**
	 * Inicio de sesión como administrador con datos válidos.
	 */
	@Test
	public void Test13_1_AdInVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Administrador")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Login de administrador");
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
		test.textoNoPresentePagina("Login de administrador");
	}

	/**
	 * Inicio de sesión como administrador con datos inválidos (usar los datos
	 * de un usuario que no tenga perfil administrador).
	 */
	@Test
	public void Test13_2_AdInInVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Administrador")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("igm1990@hotmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Login de administrador");
		driver.findElement(By.xpath("(//button[@type='button'])[2]")).click();
	}

	/**
	 * Desde un usuario identificado en sesión como administrador listar a todos
	 * los usuarios de la aplicación.
	 */
	@Test
	public void Test14_1_AdLisUsrVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Administrador")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Login de administrador");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email"))
				.sendKeys("ivangonzalezmahagamage@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Administrar usuarios")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Administrador: Listado de usuarios");
	}

	/**
	 * Desde un usuario identificado en sesión como administrador eliminar un
	 * usuario existente en la aplicación.
	 */
	@Test
	public void Test15_1_AdBorUsrVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Administrador")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Login de administrador");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email"))
				.sendKeys("ivangonzalezmahagamage@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Administrar usuarios")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Administrador: Listado de usuarios");
		driver.findElement(By.name("searchText")).click();
		driver.findElement(By.name("searchText")).clear();
		driver.findElement(By.name("searchText")).sendKeys(email);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.xpath("//input[@value='Eliminar']"));
	}

	/**
	 * Intento de acceso vía URL al borrado de un usuario existente en la
	 * aplicación. Debe utilizarse un usuario identificado en sesión pero que no
	 * tenga perfil de administrador.
	 */
	@Test
	public void Test15_2_AdBorUsrInVal() {
		driver.get(URL + "?lang=es");
		driver.findElement(By.linkText("Identifícate")).click();
		driver.findElement(By.linkText("Usuario")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys("igm1990@hotmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.get(URL + "admin/list");
		test.textoPresentePagina("Access is denied");
	}
}
