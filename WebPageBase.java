import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebPageBase{
	

	public static WebDriver driver;

	public static void Setup() throws IOException {
		System.setProperty("webdriver.chrome.driver", "C:/chromedriver/chromedriver.exe");

		ChromeOptions options = new ChromeOptions();
		options.addArguments("disable-infobars");
		options.addArguments("--incognito");
		options.addArguments("--headless", "--window-size=1366,768");
		
		driver = new ChromeDriver(options);
		driver.get("https://www.samsung.com/br/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	public static void Quit() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

	public WebDriver getDriver() {
		return driver;
	}
}