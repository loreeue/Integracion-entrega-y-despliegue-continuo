package es.codeurjc.ais.nitflex.e2e.selenium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import es.codeurjc.ais.nitflex.Application;

@SpringBootTest(
		classes = Application.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SeleniumTest {
	@LocalServerPort
	int port;
	WebDriver driver;
	WebDriverWait wait;
	@Test
	@DisabledOnOs({OS.LINUX, OS.MAC})
	public void systemWindowsTest() throws InterruptedException {
		chromeTest();
		firefoxTest();
		edgeTest();
	}

	@Test
	@DisabledOnOs({OS.WINDOWS, OS.MAC})
	public void systemLinuxTest() throws InterruptedException {
		chromeTest();
		firefoxTest();
	}

	@Test
	@DisabledOnOs({OS.LINUX, OS.WINDOWS})
	public void systemMacOSTest() throws InterruptedException {
		chromeTest();
		firefoxTest();
		safariTest();
	}

	public void chromeTest() throws InterruptedException {
		setupChrome();
		createFilmTest();
		teardown();

		setupChrome();
		removeFilmTest();
		teardown();

		setupChrome();
		invalidYearTest();
		teardown();

		setupChrome();
		cancelEditTest();
		teardown();
	}

	public void firefoxTest() throws InterruptedException {
		setupFirefox();
		createFilmTest();
		teardown();

		setupFirefox();
		removeFilmTest();
		teardown();

		setupFirefox();
		invalidYearTest();
		teardown();

		setupFirefox();
		cancelEditTest();
		teardown();
	}

	public void edgeTest() throws InterruptedException {
		setupEdge();
		createFilmTest();
		teardown();

		setupEdge();
		removeFilmTest();
		teardown();

		setupEdge();
		invalidYearTest();
		teardown();

		setupEdge();
		cancelEditTest();
		teardown();
	}

	public void safariTest() throws InterruptedException {
		setupSafari();
		createFilmTest();
		teardown();

		setupSafari();
		removeFilmTest();
		teardown();

		setupSafari();
		invalidYearTest();
		teardown();

		setupSafari();
		cancelEditTest();
		teardown();
	}

	public void setupChrome() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		driver = new ChromeDriver(options);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	public void setupFirefox() {
		FirefoxOptions options = new FirefoxOptions();
		options.addArguments("--headless");
		driver = new FirefoxDriver(options);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	public void setupEdge() {
		EdgeOptions options = new EdgeOptions();
		options.addArguments("--headless");
		driver = new EdgeDriver(options);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	public void setupSafari() {
		driver = new SafariDriver();
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	public void teardown() {
		if (driver != null) {
			driver.quit();
		}
	}

	private boolean findMovie(List<WebElement> titleList, List<WebElement> yearList, String expectedTitle, String expectedYear) {
		for (int i = 0; i < titleList.size(); i++) {
			if (titleList.get(i).getText().equals(expectedTitle) && yearList.get(i).getText().equals(expectedYear)) {
				return true;
			}
		}
		return false;
	}

	public void createFilmTest() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/");

		driver.findElement(By.id("create-film")).click(); //Click "New Film"

		// Fill in the fields of a new film
		this.wait.until(ExpectedConditions.presenceOfElementLocated(By.name("title")));
		driver.findElement(By.name("title")).sendKeys("Hush");
		driver.findElement(By.name("releaseYear")).sendKeys("2016");
		driver.findElement(By.name("url")).sendKeys("https://media.themoviedb.org/t/p/w300_and_h450_bestv2/wYHfD8izsrr12KNxh1Y4T1OnrTh.jpg");
		driver.findElement(By.name("synopsis")).sendKeys("Maddie, una escritora sorda, vive aislada en el bosque, hasta que un intruso enmascarado aparece en la ventana. Pero sus limites no haran de ella una presa facil.");

		driver.findElement(By.id("Save")).click(); //Click "Save"

		this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("film-title")));
		String titleNuevo = driver.findElement(By.id("film-title")).getText();
		String synopsisNuevo = driver.findElement(By.id("film-synopsis")).getText();
		assertThat(titleNuevo).isEqualTo("Hush");
		assertThat(synopsisNuevo).contains("Maddie, una escritora sorda, vive aislada en el bosque, hasta que un intruso enmascarado aparece en la ventana. Pero sus limites no haran de ella una presa facil.");

		this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("all-films")));
		driver.findElement(By.id("all-films")).click(); //Click "All Films"

		this.wait.until(ExpectedConditions.presenceOfElementLocated(By.className("film-title")));
		List<WebElement> elements1 = driver.findElements(By.className("film-title"));
		List<WebElement> elements2 = driver.findElements(By.className("year"));

		// We make sure that the movie was created
		assertTrue(findMovie(elements1,elements2,"Hush","2016"));
	}

	public void removeFilmTest() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/");

		driver.findElement(By.id("create-film")).click(); //Click "New Film"

		// Fill in the fields of a new film
		this.wait.until(ExpectedConditions.presenceOfElementLocated(By.name("title")));
		driver.findElement(By.name("title")).sendKeys("Cars");
		driver.findElement(By.name("releaseYear")).sendKeys("2006");
		driver.findElement(By.name("url")).sendKeys("https://image.tmdb.org/t/p/w1280/nqii8TqllZRainaNhQkJLNoSbMv.jpg");
		driver.findElement(By.name("synopsis")).sendKeys("El aspirante a campeon de carreras Rayo McQueen parece que esta a punto de conseguir el exito. Su actitud arrogante se desvanece cuando llega a una pequeña comunidad olvidada que le enseña las cosas importantes de la vida que habia olvidado.");

		driver.findElement(By.id("Save")).click(); //Click "Save"

		this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("film-title")));
		driver.findElement(By.id("remove-film")).click(); //Click "Remove"

		this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("message")));
		String mensaje = driver.findElement(By.id("message")).getText();
		assertThat(mensaje).contains("Film 'Cars' deleted");

		this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("all-films")));
		driver.findElement(By.id("all-films")).click(); //Click "All Films"

		this.wait.until(ExpectedConditions.presenceOfElementLocated(By.className("film-title")));
		List<WebElement> elements1 = driver.findElements(By.className("film-title"));
		List<WebElement> elements2 = driver.findElements(By.className("year"));

		assertFalse(findMovie(elements1,elements2,"Cars","2006"));
	}

	public void cancelEditTest() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/");

		driver.findElement(By.id("create-film")).click(); //Click "New Film"

		//Fill in the fields of a new film
		this.wait.until(ExpectedConditions.presenceOfElementLocated(By.name("title")));
		driver.findElement(By.name("title")).sendKeys("101 dalmatas");
		driver.findElement(By.name("releaseYear")).sendKeys("1961");
		driver.findElement(By.name("url")).sendKeys("https://www.tebeosfera.com/T3content/img/T3_series/a/s/101_dalmatas.jpeg");
		driver.findElement(By.name("synopsis")).sendKeys("Los dalmatas Pongo y Perdita son una feliz pareja canina que vive rodeada de sus cachorros y con sus amos, Roger y Anita. "
				+ "pero su felicidad esta amenazada por Cruella De Ville, una perfida mujer que adora los abrigos de pieles.");

		driver.findElement(By.id("Save")).click(); //Click "Save"

		this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("film-title")));
		String urlActual = driver.getCurrentUrl();
		driver.findElement(By.id("edit-film")).click(); //Click "Edit"

		this.wait.until(ExpectedConditions.presenceOfElementLocated(By.name("title")));
		driver.findElement(By.id("Cancel")).click(); //Click "Cancel"

		this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("film-title")));
		String urlPosterior = driver.getCurrentUrl();

		String titleNuevo = driver.findElement(By.id("film-title")).getText();
		String synopsisNuevo = driver.findElement(By.id("film-synopsis")).getText();

		assertThat(titleNuevo).isEqualTo("101 dalmatas");
		assertThat(synopsisNuevo).contains("Los dalmatas Pongo y Perdita son una feliz pareja canina que vive rodeada de sus cachorros y con sus amos, Roger y Anita. "
				+ "pero su felicidad esta amenazada por Cruella De Ville, una perfida mujer que adora los abrigos de pieles.");
		assertThat(urlActual).contains(urlPosterior);
	}

	public void invalidYearTest() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/");

		driver.findElement(By.id("create-film")).click(); //Click "New Film"

		// Fill in the fields of a new film
		this.wait.until(ExpectedConditions.presenceOfElementLocated(By.name("title")));
		driver.findElement(By.name("title")).sendKeys("Hush");
		driver.findElement(By.name("releaseYear")).sendKeys("1800");
		driver.findElement(By.name("url")).sendKeys("https://media.themoviedb.org/t/p/w300_and_h450_bestv2/wYHfD8izsrr12KNxh1Y4T1OnrTh.jpg");
		driver.findElement(By.name("synopsis")).sendKeys("Maddie, una escritora sorda, vive aislada en el bosque, hasta que un intruso enmascarado aparece en la ventana. Pero sus limites no haran de ella una presa facil.");

		driver.findElement(By.id("Save")).click(); //Click "Save"

		this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("message")));
		String message = driver.findElement(By.id("message")).getText();

		this.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("all-films")));
		driver.findElement(By.id("all-films")).click(); //Click "All Films"

		List<WebElement> elements1 = driver.findElements(By.className("film-title"));
		List<WebElement> elements2 = driver.findElements(By.className("year"));

		// We make sure that the movie wasn´t created
		assertFalse(findMovie(elements1,elements2,"Hush","1800"));
		assertThat(message).contains("The year is invalid");
	}
}