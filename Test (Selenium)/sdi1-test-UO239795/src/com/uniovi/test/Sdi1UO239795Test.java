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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.uniovi.test.util.Random;
import com.uniovi.test.util.TestUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Sdi1UO239795Test {

	private static String email;

	
	private static String URL = "http://localhost:8090/";
	private static WebDriver driver = getDriver();

	public static WebDriver getDriver() {
		ChromeOptions options = new ChromeOptions(); options.addArguments("--headless", "--window-size=1920,1080", "--disable-gpu", "--no-sandbox");
		WebDriver driver = new ChromeDriver(options);
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
	public void Test01_1_RegValid() {
		email = Random.email();
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Signup")).click();
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
		driver.findElement(By.xpath("//input[@value='Register']")).click();
		test.textoPresentePagina("User authenticated as");
		test.textoPresentePagina(email);
	}

	/**
	 * Registro de Usuario con datos inválidos 
	 * (repetición de contraseña invalida).
	 */
	@Test
	public void Test01_2_RegInvalid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Signup")).click();
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
		driver.findElement(By.xpath("//input[@value='Register']")).click();
		assertEquals("These passwords do not match.",
				test.closeAlertAndGetItsText());
	}

	/**
	 * Inicio de sesión con datos válidos.
	 */
	@Test
	public void Test02_1_LoginValid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("User")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username"))
				.sendKeys("ivangonzalezmahagamage@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.textoPresentePagina("Admin: List of user");
		test.textoPresentePagina("Logout");
		test.textoNoPresentePagina("Login");
	}

	/**
	 * Inicio de sesión con datos inválidos 
	 * (usuario no existente en la aplicación).
	 */
	@Test
	public void Test02_2_LoginInvalid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("User")).click();
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
	public void Test03_1_ListUsersValid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("User")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username"))
				.sendKeys("ivangonzalezmahagamage@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("User authenticated as");
		driver.findElement(By.linkText("Users")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Search User");
		test.textoPresentePagina("List of users");
	}

	/**
	 * Intento de acceso con URL desde un usuario no identificado al 
	 * listado de usuarios desde un usuario en sesión. 
	 * Debe producirse un acceso no permitido a
	 * vistas privadas.
	 */
	@Test
	public void Test03_2_ListUsersInvalid() {
		driver.get(URL + "?lang=en");
		driver.get(URL + "user/list");
		test.waitChangeWeb();
		test.textoNoPresentePagina("List of users");
		test.textoPresentePagina("Login");
	}

	/**
	 * Realizar una búsqueda valida en el listado de usuarios desde 
	 * un usuario en sesión.
	 */
	@Test
	public void Test04_1_SearchUserValid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("User")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username"))
				.sendKeys("ivangonzalezmahagamage@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("User authenticated as");
		driver.findElement(By.linkText("Users")).click();
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
	public void Test04_2_SearchUserInvalid() {
		driver.get(URL + "?lang=en");
		driver.get(URL + "user/list");
		test.waitChangeWeb();
		test.textoNoPresentePagina("List of users");
		test.textoNoPresentePagina("Search User");
		test.textoPresentePagina("Login");
	}

	/**
	 * Enviar una invitación de amistad a un usuario de forma valida.
	 */
	@Test
	public void Test05_1_InviteValid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("User")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username"))
				.sendKeys("ivangonzalezmahagamage@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("User authenticated as");
		driver.findElement(By.linkText("Users")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("searchText")).click();
		driver.findElement(By.name("searchText")).clear();
		driver.findElement(By.name("searchText")).sendKeys(email);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.xpath("//input[@value='Send request']"))
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
	public void Test05_2_InviteInvalid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("User")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username"))
				.sendKeys("ivangonzalezmahagamage@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("User authenticated as");
		driver.findElement(By.linkText("Users")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("searchText")).click();
		driver.findElement(By.name("searchText")).clear();
		driver.findElement(By.name("searchText")).sendKeys(email);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("searchText")).sendKeys(email);
		test.textoPresentePagina(email);
		test.textoPresentePagina("Request sent");
	}

	/**
	 * Listar las invitaciones recibidas por un usuario, realizar 
	 * la comprobación con una lista que al menos tenga una 
	 * invitación recibida.
	 */
	@Test
	public void Test06_1_ListInvitesValid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("User")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Requests")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Mahagamage");
		test.textoPresentePagina("González Mahagamage");
	}

	/**
	 * Aceptar una invitación recibida.
	 */
	@Test
	public void Test07_1_AcceptInviteValid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("User")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Requests")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Mahagamage");
		test.textoPresentePagina("González Mahagamage");
		driver.findElement(By.xpath("//input[@value='Accept invitation']"))
				.click();
		test.waitChangeWeb();
		test.textoNoPresentePagina("Accept Request");
		test.textoNoPresentePagina("Block");
	}

	/**
	 * Listar los amigos de un usuario, realizar la comprobación con una 
	 * lista que al menos tenga un amigo.
	 */
	@Test
	public void Test08_1_ListFriendsValid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("User")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Friends")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Mahagamage");
		test.textoPresentePagina("González Mahagamage");
	}

	/**
	 * Crear una publicación con datos válidos.
	 */
	@Test
	public void Test09_1_PostValid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("User")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Publications")).click();
		driver.findElement(By.linkText("Add post")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("title")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("Prueba Post");
		driver.findElement(By.name("text")).click();
		driver.findElement(By.name("text")).clear();
		driver.findElement(By.name("text"))
				.sendKeys("Prueba de contenido de post");
		driver.findElement(By.xpath("//input[@value='Send']")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("List of posts");
		test.textoPresentePagina("Prueba Post");
		test.textoPresentePagina("Prueba de contenido de post");
	}

	/**
	 * Acceso al listado de publicaciones desde un usuario en sesión.
	 */
	@Test
	public void Test10_1_ListPostsValid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("User")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Publications")).click();
		driver.findElement(By.linkText("List my publications")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("List of posts");
		test.textoPresentePagina("Prueba Post");
		test.textoPresentePagina("Prueba de contenido de post");
	}

	/**
	 * Listar las publicaciones de un usuario amigo
	 */
	@Test
	public void Test11_1_ListFriendPostsValid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("User")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Friends")).click();
		test.waitChangeWeb();
		driver.findElement(By.xpath("//form[contains(@action, '/post/friends/')]/input"))
				.click();
		test.waitChangeWeb();
		test.textoPresentePagina("List of posts of");
		test.textoPresentePagina("Iván González Mahagamage");
	}

	/**
	 * Utilizando un acceso vía URL tratar de listar 
	 * las publicaciones de un usuario
	 * que no sea amigo del usuario identificado en sesión.
	 */
	@Test
	public void Test11_2_ListFriendPostsInvalid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("User")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.get(URL + "post/friends/3");
		test.textoPresentePagina("This is a private area the web");
		test.textoPresentePagina("User authenticated as");
		test.textoPresentePagina(email);

	}

	/**
	 * Crear una publicación con datos válidos y una foto adjunta.
	 */
	@Test
	public void Test12_1_PostPhoto1Valid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("User")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Publications")).click();
		driver.findElement(By.linkText("Add post")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("title")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("Prueba Post con foto");
		driver.findElement(By.name("text")).click();
		driver.findElement(By.name("text")).clear();
		String imagenDePrueba = "";
		imagenDePrueba += System.getProperty("user.dir");
		imagenDePrueba += "\\imagenDePrueba.jpg";
		
		driver.findElement(By.name("text")).sendKeys(imagenDePrueba);
		driver.findElement(By.name("imgn")).sendKeys(imagenDePrueba);
		driver.findElement(By.xpath("//input[@value='Send']")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("List of posts");
		test.textoPresentePagina("Prueba Post con foto");
		test.textoPresentePagina(imagenDePrueba);
	}

	/**
	 * Crear una publicación con datos válidos y sin una foto adjunta.
	 */
	@Test
	public void Test12_2_PostPhoto2Valid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("User")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username")).sendKeys(email);
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Publications")).click();
		driver.findElement(By.linkText("Add post")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("title")).click();
		driver.findElement(By.name("title")).clear();
		driver.findElement(By.name("title")).sendKeys("Prueba Post sin foto");
		driver.findElement(By.name("text")).click();
		driver.findElement(By.name("text")).clear();
		driver.findElement(By.name("text"))
				.sendKeys("Prueba de contenido de post sin foto");
		driver.findElement(By.xpath("//input[@value='Send']")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("List of posts");
		test.textoPresentePagina("Prueba Post sin foto");
		test.textoPresentePagina("Prueba de contenido de post sin foto");
	}

	/**
	 * Inicio de sesión como administrador con datos válidos.
	 */
	@Test
	public void Test13_1_AdminLoginValid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("Admin")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Admin Login");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email"))
				.sendKeys("ivangonzalezmahagamage@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.textoPresentePagina("Admin: List of user");
		test.textoPresentePagina("Logout");
		test.textoNoPresentePagina("Admin Login");
	}

	/**
	 * Inicio de sesión como administrador con datos inválidos (usar los 
	 * datos de un usuario que no tenga perfil administrador).
	 */
	@Test
	public void Test13_2_AdminLoginInvalid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("Admin")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email")).sendKeys("igm1990@hotmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Admin Login");
		driver.findElement(By.xpath("(//button[@type='button'])[2]")).click();
	}

	/**
	 * Desde un usuario identificado en sesión como administrador 
	 * listar a todos los usuarios de la aplicación.
	 */
	@Test
	public void Test14_1_AdminListUsersValid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("Admin")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Admin Login");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email"))
				.sendKeys("ivangonzalezmahagamage@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Admin users")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Admin: List of user");
	}

	/**
	 * Desde un usuario identificado en sesión como administrador eliminar un
	 * usuario existente en la aplicación.
	 */
	@Test
	public void Test15_1_AdminDeleteUserValid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("Admin")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Admin Login");
		driver.findElement(By.name("email")).click();
		driver.findElement(By.name("email")).clear();
		driver.findElement(By.name("email"))
				.sendKeys("ivangonzalezmahagamage@gmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.linkText("Admin users")).click();
		test.waitChangeWeb();
		test.textoPresentePagina("Admin: List of user");
		driver.findElement(By.name("searchText")).click();
		driver.findElement(By.name("searchText")).clear();
		driver.findElement(By.name("searchText")).sendKeys(email);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		test.waitChangeWeb();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.xpath("//input[@value='Delete']"));
	}

	/**
	 * Intento de acceso vía URL al borrado de un usuario existente en la
	 * aplicación. Debe utilizarse un usuario identificado en sesión 
	 * pero que no tenga perfil de administrador.
	 */
	@Test
	public void Test15_2_AdminDeleteUserInvalid() {
		driver.get(URL + "?lang=en");
		driver.findElement(By.linkText("Login")).click();
		driver.findElement(By.linkText("User")).click();
		test.waitChangeWeb();
		driver.findElement(By.name("username")).click();
		driver.findElement(By.name("username")).clear();
		driver.findElement(By.name("username"))
				.sendKeys("igm1990@hotmail.com");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.get(URL + "admin/list");
		test.textoPresentePagina("Forbidden");
	}
}










